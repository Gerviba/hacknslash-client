package hu.gerviba.hackandslash.client.gui.ingame;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.ImageUtil;
import hu.gerviba.hackandslash.client.gui.CustomWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.MiddleModel;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.gui.ingame.model.RenderableModel;
import hu.gerviba.hackandslash.client.gui.ingame.model.StaticLayer;
import hu.gerviba.hackandslash.client.packets.MapLoadPacket;
import hu.gerviba.hackandslash.client.packets.TelemetryPacket;
import hu.gerviba.hackandslash.client.packets.TelemetryPacket.PlayerModelStatus;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
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

@Slf4j
public class IngameWindow extends CustomWindow {
    
    private static final long PLAYER_ENTITY_ID = -1L;

    private ObjectMapper mapper = new ObjectMapper();
    
    @Getter
    private ChatHud chatComponent;
    
    @Getter
    private PlayerInfoHud playerInfoComponent;
    
    @Getter
    private Map<Long, PlayerModel> playerModel;

    @Getter
    private List<RenderableModel> entities = Collections.synchronizedList(new LinkedList<>());
    
    private Canvas canvasBackground;
    private StaticLayer background = null;
    private Canvas canvasMiddle;
    private Canvas canvasForeground;
    private StaticLayer foreground = null;
    private Canvas playerNames;
    
    private int height;
    private int width;
    private int scale;
    private int scaleInPixel;
    
    AnimationTimer animationTimer;
    
    Set<String> input = new HashSet<>();

    /**
     * Ingame:
     * - Window hint layer
     * - Window layer
     * - Texts layer
     * - Texts layer
     * - Foreground layer
     * - Fog layer
     * - Play Area layer
     * - Background layer
     * 
     * Player HUD:
     * - HP
     * - Mana
     * - Exp
     * - HP Potion count
     * 
     * Inventory:
     * - Helmet
     * - Armor
     * - Rings, etc
     * - Weapon
     * - Money
     * - Looted items (gems, bones, random shit)
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
        
        chatComponent = new ChatHud();
        ingame.getChildren().add(chatComponent.toPane());
        
        HacknslashApplication.getInstance().getConnection().startTelemetry();
    }

    private void startAnimationLoop() {
        try {
            GraphicsContext layerMiddle = canvasMiddle.getGraphicsContext2D();
            playerModel.put(PLAYER_ENTITY_ID, 
                    new PlayerModel(PLAYER_ENTITY_ID, "null", scale, "player_no1", width, height));
            
            long startNanoTime = System.nanoTime();
            animationTimer = new AnimationTimer() {
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
                            .sorted((a, b) -> Double.compare(a.getY(), b.getY()))
                            .forEachOrdered(entity -> {
                                entity.calc();
                                entity.draw(layerMiddle, topLayer, t, me.getX(), me.getY());
                            });
                }

            };
            animationTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVars() {
        height = 800;
        width = 1280;
        scale = 2;
        scaleInPixel = scale * 32;
        playerModel = new ConcurrentHashMap<>();
    }

    private AnchorPane initBody() {
        AnchorPane body = new AnchorPane();
        body.setMinWidth(width);
        body.setMinHeight(height);
        return body;
    }

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

    private void applyInput(PlayerModel me) {
        double dX = me.getX();
        double dY = me.getY();
        int direction = me.getDirection();
        boolean walking = false;
        
        if (input.contains("LEFT")) {
            dX -= 1;
            direction = 1;
            walking = true;
        }
        if (input.contains("RIGHT")) {
            dX += 1;
            direction = 2;
            walking = true;
        }
        if (input.contains("UP")) {
            dY -= 1;
            direction = 3;
            walking = true;
        }
        if (input.contains("DOWN")) {
            dY += 1;
            direction = 0;
            walking = true;
        }
        if (input.contains("T")) {
            chatComponent.allow();
        }
        
        me.setX(dX);
        me.setY(dY);
        me.setDirection(direction);
        me.setWalking(walking);
    }

    private void initScene(Pane body) {
        scene = new Scene(body);
        scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        stage.setTitle("Hack'n'Slash");
        stage.setMinHeight(height);
        stage.setMinWidth(width);
    }

    public PlayerModel getMe() {
        return playerModel.get(PLAYER_ENTITY_ID);
    }

    public void loadMap(byte[] o) {
        try {
            MapLoadPacket packet = mapper.readValue(o, MapLoadPacket.class);
            BufferedImage image = ImageIO.read(PlayerModel.class
                    .getResource("/assets/textures/" + packet.getTexture() + ".png"));
            BufferedImage rescaled = ImageUtil.scale(image, image.getType(), scale);
            Image texture = SwingFXUtils.toFXImage(rescaled, null);
            
            background = new StaticLayer(canvasBackground.getGraphicsContext2D(), 
                    packet.getBackground(), texture, 64, width, height, 0, 0);
            scene.getRoot().setStyle("-fx-background-color: #" + packet.getBackgroundColor());
            getMe().setX(packet.getSpawnX() * scaleInPixel);
            getMe().setY(packet.getSpawnY() * scaleInPixel);
            foreground = new StaticLayer(canvasForeground.getGraphicsContext2D(), 
                    packet.getForeground(), texture, 64, width, height, 0, 0);
            playerInfoComponent.renderMapCanvas(packet.getBackground());
            entities.addAll(packet.getMiddle().getParts().stream().flatMap(
                    part -> part.getPlaces().stream().map(place -> {
                        return new MiddleModel(
                                scaleInPixel * place[0],
                                scaleInPixel * place[1],
                                scaleInPixel, scaleInPixel,
                                scale, texture,
                                part.getX(), part.getY(), 
                                width, height);
                    })).collect(Collectors.toList()));
            
            log.info("Map loaded");
        } catch (Exception e) {
            log.error("Failed to load map", e);
        }
    }
    
    public void updateTelemetry(byte[] o) {
        TelemetryPacket telemetry;
        try {
            telemetry = mapper.readValue(o, TelemetryPacket.class);
            
            if (telemetry.getPlayers() != null) {
                for (PlayerModelStatus pms : telemetry.getPlayers()) {
                    if (playerModel.containsKey(pms.getEntityId())) {
                        playerModel.get(pms.getEntityId()).update(pms);
                    } else {
                        PlayerModel player = new PlayerModel(pms.getEntityId(), pms.getName(), 
                                scale, "player_no1", width, height);
                        player.update(pms);
                        player.setX(pms.getX());
                        player.setY(pms.getY());
                        playerModel.put(pms.getEntityId(), player);
                        entities.add(player);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
