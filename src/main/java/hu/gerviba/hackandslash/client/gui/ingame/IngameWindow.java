package hu.gerviba.hackandslash.client.gui.ingame;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.ImageUtil;
import hu.gerviba.hackandslash.client.gui.CustomWindow;
import hu.gerviba.hackandslash.client.gui.ingame.item.Items;
import hu.gerviba.hackandslash.client.gui.ingame.model.MiddleModel;
import hu.gerviba.hackandslash.client.gui.ingame.model.MobModel;
import hu.gerviba.hackandslash.client.gui.ingame.model.PermanentParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.gui.ingame.model.RenderableModel;
import hu.gerviba.hackandslash.client.gui.ingame.model.StaticLayer;
import hu.gerviba.hackandslash.client.gui.ingame.model.StaticObjectModel;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;
import hu.gerviba.hackandslash.client.packets.MapLoadPacket;
import hu.gerviba.hackandslash.client.packets.MapLoadPacket.MapLayerInfo.BackgroundPart;
import hu.gerviba.hackandslash.client.packets.TelemetryPacket;
import hu.gerviba.hackandslash.client.packets.TelemetryPacket.MobStatus;
import hu.gerviba.hackandslash.client.packets.TelemetryPacket.PlayerModelStatus;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * The core ingame GUI window
 * @author Gergely Szabó
 */
@Slf4j
public class IngameWindow extends CustomWindow {
    
    private static final long PLAYER_ENTITY_ID = -1L;

    private ObjectMapper mapper = new ObjectMapper();
    
    @Getter
    private ChatHud chatComponent;
    
    @Getter
    private PlayerInfoHud playerInfoComponent;

    @Getter
    private SkillsHud skillsComponent;
    
    @Getter
    private InventoryHud inventoryComponent;
    
    @Getter
    private Map<Long, PlayerModel> playerModel;

    @Getter
    private List<RenderableModel> entities;
    
    private Canvas canvasBackground;
    private StaticLayer background = null;
    private Canvas canvasMiddle;
    private Canvas canvasForeground;
    private StaticLayer foreground = null;
    private Canvas playerNames;
    private List<int[]> walkable;
    
    @Getter
    private int height;
    @Getter
    private int width;
    @Getter
    private int scale;
    @Getter
    private int scaleInPixel;
    
    AnimationTimer animationTimer = null;
    
    ScheduledFuture<?> entityGrabageCollector = null;
    
    Set<String> input = new HashSet<>();

    /**
     * Load the default components and initialize the variables
     */
    @Override
    protected void init() {
        initVars();
        
        AnchorPane body = initBody();
        StackPane ingame = initIngame(body);
        initLayers(ingame);
        
        initScene(body);

        playerInfoComponent = new PlayerInfoHud();
        ingame.getChildren().add(playerInfoComponent.toPane());

        
        scene.setOnKeyPressed(e -> {
            String code = e.getCode().toString();
            input.add(code);
        });
 
        scene.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
            input.remove(code);
        });
        
        startAnimationLoop();
        startEntityGrabageCollector();

        skillsComponent = new SkillsHud(this);
        ingame.getChildren().add(skillsComponent.toPane());
        
        chatComponent = new ChatHud();
        ingame.getChildren().add(chatComponent.toPane());
        
        inventoryComponent = new InventoryHud(this);
        ingame.getChildren().add(inventoryComponent.toPane());

        StackPane highestLayer = new StackPane();
        highestLayer.setMaxWidth(0);
        highestLayer.setMaxHeight(0);
        ingame.getChildren().add(highestLayer);
        inventoryComponent.setHighestLayer(highestLayer);
        StackPane.setAlignment(highestLayer, Pos.TOP_LEFT);
        
        addParticles();
        
        HacknslashApplication.getInstance().getConnection().startTelemetry();
    }

    /**
     * Close handler to stop animation timer and remove unused entities.
     */
    @Override
    public void onClose() {
        if (animationTimer != null)
            animationTimer.stop();
        if (entityGrabageCollector != null && !entityGrabageCollector.isCancelled())
            entityGrabageCollector.cancel(false);
    }
    
    /**
     * Add particles for testing
     */
    @Deprecated
    private void addParticles() {
        entities.add(new PermanentParticleInstance(Particles.WATER, System.currentTimeMillis(), 
                3.5, 5.5, scaleInPixel, width, height, 0));
        entities.add(new PermanentParticleInstance(Particles.POISON, System.currentTimeMillis(), 
                4.5, 5.5, scaleInPixel, width, height, 0));
        entities.add(new PermanentParticleInstance(Particles.FLAME, System.currentTimeMillis(), 
                5.5, 5.5, scaleInPixel, width, height, 0));
        entities.add(new PermanentParticleInstance(Particles.MAGIC1, System.currentTimeMillis(), 
                6.5, 5.5, scaleInPixel, width, height, 0));
        entities.add(new PermanentParticleInstance(Particles.MAGIC2, System.currentTimeMillis(), 
                7.5, 5.5, scaleInPixel, width, height, 0));
        entities.add(new PermanentParticleInstance(Particles.SMOKE, System.currentTimeMillis(), 
                8.5, 5.5, scaleInPixel, width, height, 0));
    }
    
    /**
     * Starts the animation loop
     */
    private void startAnimationLoop() {
        GraphicsContext layerMiddle = canvasMiddle.getGraphicsContext2D();
        playerModel.put(PLAYER_ENTITY_ID, 
                new PlayerModel(PLAYER_ENTITY_ID, "null", scale, 
                        Items.getImageByComponents("player_no1", "null", "null", "null", "null"), 
                        width, height));
        
        long startNanoTime = System.nanoTime();
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                
                PlayerModel me = playerModel.get(PLAYER_ENTITY_ID);

                applyInput(me);

                if (background != null)
                    background.draw(me.getX(), me.getY());
                if (foreground != null)
                    foreground.draw(me.getX(), me.getY());
                playerInfoComponent.updateMapCanvas(
                        me.getX() / scaleInPixel, 
                        me.getY() / scaleInPixel);

                layerMiddle.clearRect(0, 0, width, height);
                GraphicsContext topLayer = playerNames.getGraphicsContext2D();
                topLayer.clearRect(0, 0, width, height);
                entities.stream()
                        .filter(pm -> pm != null)
                        .filter(pm -> !pm.isFinished())
                        .sorted((a, b) -> Double.compare(a.getOrder(), b.getOrder()))
                        .forEachOrdered(entity -> {
                            entity.calc();
                            entity.draw(layerMiddle, topLayer, t, me.getX(), me.getY());
                        });
            }

        };
        animationTimer.start();
    }

    /**
     * Starts the entity garbage collector.
     * Remove eg. finished particle animations.
     */
    private void startEntityGrabageCollector() {
        entityGrabageCollector = HacknslashApplication.ASYNC.scheduleAtFixedRate(
                () -> Platform.runLater(() -> entities.removeIf(x -> x.isFinished())), 
                5, 10, TimeUnit.SECONDS); 
    }
    
    /**
     * Initialize variables
     */
    private void initVars() {
        height = 800;
        width = 1280;
        scale = 2;
        scaleInPixel = scale * 32;
        playerModel = new ConcurrentHashMap<>();
        entities = Collections.synchronizedList(new LinkedList<>());
    }

    /**
     * Creates the ingame body component
     */
    private AnchorPane initBody() {
        AnchorPane body = new AnchorPane();
        body.setMinWidth(width);
        body.setMinHeight(height);
        return body;
    }

    /**
     * Creates the ingame component
     * @param body The parent of this component
     * @return
     */
    private StackPane initIngame(AnchorPane body) {
        StackPane ingame = new StackPane();
        ingame.setMinWidth(width);
        ingame.setMinHeight(height);
        body.getChildren().add(ingame);
        AnchorPane.setTopAnchor(ingame, 0.0);
        AnchorPane.setRightAnchor(ingame, 0.0);
        AnchorPane.setBottomAnchor(ingame, 0.0);
        AnchorPane.setLeftAnchor(ingame, 0.0);
        return ingame;
    }

    /**
     * Initialize the render layers
     * @param ingame Ingame component
     */
    private void initLayers(StackPane ingame) {
        canvasBackground = new Canvas(width, height);
        ingame.getChildren().add(canvasBackground);
        
        canvasMiddle = new Canvas(width, height);
        ingame.getChildren().add(canvasMiddle);
        
        canvasForeground = new Canvas(width, height);
        ingame.getChildren().add(canvasForeground);
        
        playerNames = new Canvas(width, height);
        ingame.getChildren().add(playerNames);
        playerNames.getGraphicsContext2D().setTextAlign(TextAlignment.CENTER);
        playerNames.getGraphicsContext2D().setFont(new Font("Roboto Mono Bold", 12));
    }

    /**
     * Handle keyboard input
     * @param me The player model of the local client user
     */
    private void applyInput(PlayerModel me) {
        double dX = me.getX();
        double dY = me.getY();
        int direction = me.getDirection();
        boolean walking = false;
        
        if (input.contains("LEFT")) {
            dX -= 1;
            direction = PlayerModel.DIRECTION_LEFT;
            walking = true;
        }
        if (input.contains("RIGHT")) {
            dX += 1;
            direction = PlayerModel.DIRECTION_RIGHT;
            walking = true;
        }
        if (input.contains("UP")) {
            dY -= 1;
            direction = PlayerModel.DIRECTION_BACK;
            walking = true;
        }
        if (input.contains("DOWN")) {
            dY += 1;
            direction = PlayerModel.DIRECTION_STAND;
            walking = true;
        }
        if (input.contains("T")) {
            chatComponent.allow();
        }
        if (input.contains("I")) {
            inventoryComponent.show();
        }
        if (input.contains("ESCAPE")) {
            inventoryComponent.close();
        }
        if (input.contains("F1")) {
            skillsComponent.handleSkill(0);
        }
        if (input.contains("F2")) {
            skillsComponent.handleSkill(1);
        }
        if (input.contains("F3")) {
            skillsComponent.handleSkill(2);
        }
        if (input.contains("F4")) {
            skillsComponent.handleSkill(3);
        }
        if (input.contains("F5")) {
            skillsComponent.handleSkill(4);
        }
        if (input.contains("F6")) {
            skillsComponent.handleSkill(5);
        }
        if (input.contains("F7")) {
            skillsComponent.handleSkill(6);
        }
        if (input.contains("F")) {
            // TODO: send use packet
        }
        
        if (canMoveTo(dX, dY)) {
            me.setX(dX);
            me.setY(dY);
        } else if (canMoveTo(me.getX(), dY)) {
            me.setY(dY);
        } else if (canMoveTo(dX, me.getY())) {
            me.setX(dX);
        }
            
        me.setDirection(direction);
        me.setWalking(walking);
    }

    /**
     * Function to determine if a player can move to a specific coordinate or not.
     * @param dX X coordinate (actual coordinates)
     * @param dY Y coordinate (actual coordinates)
     * @return true, if the movement is allowed
     */
    public boolean canMoveTo(double dX, double dY) {
        return walkable != null && walkable.stream().filter(c -> 
            c[0] == (int) ((dX + 14) / scaleInPixel) && 
            c[1] == (int) ((dY - 6) / scaleInPixel)
        ).findAny().isPresent() && walkable.stream().filter(c -> 
            c[0] == (int) ((dX - 14) / scaleInPixel) && 
            c[1] == (int) ((dY - 6) / scaleInPixel)
        ).findAny().isPresent();
    }
    
    /**
     * Initialize the JavaFX {@link Scene} of this window.
     * @param body The body component
     */
    private void initScene(Pane body) {
        scene = new Scene(body);
        scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        stage.setTitle("Hack'n'Slash");
        stage.setMinHeight(height);
        stage.setMinWidth(width);
    }

    /**
     * Getter for the local client representation.
     * @return
     */
    public PlayerModel getMe() {
        return playerModel.get(PLAYER_ENTITY_ID);
    }

    /**
     * Loads the specified map.
     * @param o The byte[] received from the server
     */
    public void loadMap(byte[] o) {
        try {
            MapLoadPacket packet = mapper.readValue(o, MapLoadPacket.class);
            Image texture = ImageUtil.loadImage("/assets/textures/" + packet.getTexture() + ".png", scale);
            
            background = new StaticLayer(canvasBackground.getGraphicsContext2D(), 
                    packet.getBackground(), texture, 64, width, height, 0, 0);
            walkable = flatMapOf(packet.getBackground().getParts());
            scene.getRoot().setStyle("-fx-background-color: #" + packet.getBackgroundColor());
            getMe().setX(packet.getSpawnX() * scaleInPixel);
            getMe().setY(packet.getSpawnY() * scaleInPixel);
            foreground = new StaticLayer(canvasForeground.getGraphicsContext2D(), 
                    packet.getForeground(), texture, 64, width, height, 0, 0);
            playerInfoComponent.renderMapCanvas(packet.getBackground());
            entities.addAll(packet.getMiddle().getParts().stream().flatMap(
                    part -> part.getPlaces().stream().map(place -> 
                        new MiddleModel(
                                scaleInPixel * place[0],
                                scaleInPixel * place[1],
                                scaleInPixel, scaleInPixel,
                                scale, texture,
                                part.getX(), part.getY(), 
                                width, height)
                    )).collect(Collectors.toList()));
            
            entities.addAll(packet.getObjects().stream()
                    .map(obj -> new StaticObjectModel(
                            scaleInPixel * (obj.getX() + 0.5),
                            scaleInPixel * (obj.getY() + 0.75),
                            scaleInPixel / 2, scaleInPixel / 2,
                            scale, ImageUtil.loadImage("/assets/objects/" + obj.getTexture() + ".png", 2),
                            width, height
                            ))
                    .collect(Collectors.toList()));
            log.info("Map loaded");
        } catch (Exception e) {
            log.error("Failed to load map", e);
        }
    }
    
    /**
     * Creates a flatten list of the coordinates passed
     * @param parts List of background parts
     * @return The flatten list
     */
    private List<int[]> flatMapOf(List<BackgroundPart> parts) {
        LinkedList<int[]> result = new LinkedList<>();
        for (BackgroundPart bg : parts)
            result.addAll(bg.getPlaces());
        return result;
    }

    /**
     * Updates player and mob coordinates. 
     * Also removes the killed or disconnected ones.
     * @param o The byte[] received form the server
     */
    public void updateTelemetry(byte[] o) {
        TelemetryPacket telemetry;
        try {
            telemetry = mapper.readValue(o, TelemetryPacket.class);
            
            if (telemetry.getPlayers() != null) {
                for (PlayerModelStatus pms : telemetry.getPlayers()) {
                    if (playerModel.containsKey(pms.getEntityId())) {
                        playerModel.get(pms.getEntityId()).update(pms);
                    } else {
                        PlayerModel player = new PlayerModel(pms.getEntityId(), pms.getName(), scale, 
                                Items.getImageByComponents(pms.getBase(), pms.getWeapon(), 
                                        pms.getHelmet(), pms.getArmor(), pms.getBoots()), 
                                width, height);
                        player.update(pms);
                        player.setX(pms.getX());
                        player.setY(pms.getY());
                        playerModel.put(pms.getEntityId(), player);
                        entities.add(player);
                    }
                }
                for (MobStatus ms : telemetry.getMobs()) {
                    Optional<RenderableModel> mobEntity = entities.stream()
                            .filter(e -> e.getId() == ms.getEntityId())
                            .findFirst();
                    if (mobEntity.isPresent()) {
                        ((MobModel)mobEntity.get()).update(ms);
                    } else {
                        MobModel mob = new MobModel(ms.getEntityId(), ms.getName(), scale, 
                                ms.getTexture(), width, height);
                        mob.update(ms);
                        mob.setX(mob.getX());
                        mob.setY(mob.getY());
                        entities.add(mob);
                    }
                }
                for (long eid : telemetry.getEntityRemove()) {
                    playerModel.remove(eid);
                    entities.removeIf(entity -> entity.getId() == eid);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Deprecated
    public boolean canMoveToVirtualCoords(double dX, double dY) {
        return true;
//        return walkable != null && walkable.stream().filter(c -> 
//            c[0] == (int) (dX) && c[1] == (int) (dY)
//        ).findAny().isPresent();
    }
    
}
