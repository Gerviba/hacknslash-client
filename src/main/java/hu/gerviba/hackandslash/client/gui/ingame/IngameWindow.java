package hu.gerviba.hackandslash.client.gui.ingame;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.CustomWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.packets.TelemetryPacket;
import hu.gerviba.hackandslash.packets.TelemetryPacket.PlayerModelStatus;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class IngameWindow extends CustomWindow {
    
    private static final long PLAYER_ENTITY_ID = -1L;

    private ObjectMapper mapper = new ObjectMapper();
    
    @Getter
    private ChatHud chatComponent;
    
    @Getter
    private Map<Long, PlayerModel> playerModel;
    
    public void updateTelemetry(byte[] o) {
        TelemetryPacket telemetry;
        try {
            telemetry = mapper.readValue(o, TelemetryPacket.class);
            
            if (telemetry.getPlayers() != null) {
                for (PlayerModelStatus pms : telemetry.getPlayers()) {
                    if (playerModel.containsKey(pms.getEntityId())) {
                        playerModel.get(pms.getEntityId()).update(pms);
                    } else {
                        PlayerModel player = new PlayerModel(pms.getEntityId(), 2, "player_no1");
                        player.update(pms);
                        player.setX(pms.getX());
                        player.setY(pms.getY());
                        playerModel.put(pms.getEntityId(), player);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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
     * Chat:
     * - Mode: Global
     * - Mode: Map
     * - Mode: Others
     * - Colored texts
     * - Send message
     * 
     * Player HUD:
     * - HP
     * - Mana
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
        playerModel = new ConcurrentHashMap<>();
        
        AnchorPane body = new AnchorPane();
        body.setMinHeight(600);
        StackPane ingame = new StackPane();
        body.getChildren().add(ingame);
        AnchorPane.setTopAnchor(ingame, 0.0);
        AnchorPane.setRightAnchor(ingame, 0.0);
        AnchorPane.setBottomAnchor(ingame, 0.0);
        AnchorPane.setLeftAnchor(ingame, 0.0);

        Image image = new Image("testbg-3.png");
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        iv1.setSmooth(false);
        iv1.setScaleY(10);
        ingame.getChildren().add(iv1);
        
        Canvas canvas = new Canvas(512, 512);
        ingame.getChildren().add(canvas);
     
        Set<String> input = new HashSet<>();
        
        initScene(body);

        scene.setOnKeyPressed(e -> {
            String code = e.getCode().toString();
            input.add(code);
        });
 
        scene.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
            input.remove(code);
        });
        
        try {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            playerModel.put(PLAYER_ENTITY_ID, new PlayerModel(PLAYER_ENTITY_ID, 2, "player_no1"));
            
            long startNanoTime = System.nanoTime();
            new AnimationTimer() {
                public void handle(long currentNanoTime) {
                    double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                    
                    PlayerModel me = playerModel.get(PLAYER_ENTITY_ID);
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
                    me.setX(dX);
                    me.setY(dY);
                    me.setDirection(direction);
                    me.setWalking(walking);
                    
                    gc.clearRect(0, 0, 512, 512);
                    for (PlayerModel pm : playerModel.values()) {
                        if (pm != null) {
                            pm.calc();
                            pm.draw(gc, t);
                        }
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatComponent = new ChatHud();
        ingame.getChildren().add(chatComponent.toPane());
        
        HacknslashApplication.getInstance().getConnection().startTelemetry();
    }

    private void initScene(Pane body) {
        scene = new Scene(body);
        scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        stage.setTitle("Hack'n'Slash");
        stage.setMinHeight(600);
        stage.setMinWidth(820);
    }

    public PlayerModel getMe() {
        return playerModel.get(PLAYER_ENTITY_ID);
    }
    
}
