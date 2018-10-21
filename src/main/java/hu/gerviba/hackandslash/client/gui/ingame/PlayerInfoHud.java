package hu.gerviba.hackandslash.client.gui.ingame;

import java.io.IOException;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hackandslash.client.packets.MapLoadPacket.MapLayerInfo;
import hu.gerviba.hackandslash.client.packets.SelfInfoUpdatePacket;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class PlayerInfoHud implements CustomComponent {

    private static final int MINIMAP_SIZE = 128;
    private AnchorPane hudWarpper;
    private Canvas mapCanvas;
    private Canvas players;
    private int scale = 4;
    
    private ProgressBar hp;
    private Text hpText;
    private ProgressBar mana;
    private Text manaText;
    private ProgressBar exp;
    private Text expText;
    
    @Override
    public Pane toPane() {
        hudWarpper = new AnchorPane();
        GridPane hud = createHud();
        
        hud.add(generateMinimap(), 0, 0, 1, 5);
        hud.add(generatePlayerName(), 1, 0);
        hud.add(generateHPBar(), 1, 1);
        hud.add(generateManaBar(), 1, 2);
        hud.add(generateExpBar(), 1, 3);
        hud.add(new Text(), 1, 4);
        
        HacknslashApplication.getInstance().getConnection().appendTask(stomp -> stomp.send("/app/self", "{}".getBytes()));
        
        return hudWarpper;
    }

    private GridPane createHud() {
        GridPane hud = new GridPane();
        hud.getStyleClass().add("hud");
        hud.setMinHeight(200);
        hud.setMinWidth(300);
        hud.setMaxWidth(300);
        hud.setHgap(10);
        hud.setVgap(8);
        hud.setAlignment(Pos.TOP_LEFT);
        hudWarpper.getChildren().add(hud);
        AnchorPane.setLeftAnchor(hud, 10.0);
        AnchorPane.setTopAnchor(hud, 10.0);
        return hud;
    }

    private StackPane generateMinimap() {
        StackPane map = new StackPane();
        map.setAlignment(Pos.TOP_LEFT);
        map.getStyleClass().add("map");
        map.setMinHeight(MINIMAP_SIZE);
        map.setMaxHeight(MINIMAP_SIZE);
        map.setMinWidth(MINIMAP_SIZE);
        map.setMaxWidth(MINIMAP_SIZE);
        map.setClip(new Circle(MINIMAP_SIZE / 2, MINIMAP_SIZE / 2, MINIMAP_SIZE / 2));
        
        mapCanvas = new Canvas(1024, 1024);
        mapCanvas.getStyleClass().add("canvas");
        map.getChildren().add(mapCanvas);
        players = new Canvas(MINIMAP_SIZE, MINIMAP_SIZE);
        map.getChildren().add(players);
        return map;
    }

    private Text generatePlayerName() {
        Text playerName = new Text(HacknslashApplication.getInstance().getUser().getName());
        playerName.getStyleClass().add("player-name");
        return playerName;
    }
    
    private StackPane generateHPBar() {
        StackPane hpWrapper = new StackPane();
        hpWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        hp = new ProgressBar();
        hp.setProgress(0);
        hp.setPrefWidth(150);
        hp.setMaxHeight(4);
        hp.getStyleClass().add("hp");
        hpWrapper.getChildren().add(hp);
        hpText = new Text("0/0 ");
        hpWrapper.getChildren().add(hpText);
        StackPane.setAlignment(hpText, Pos.TOP_RIGHT);
        hpText.getStyleClass().add("text");
        return hpWrapper;
    }
    
    private StackPane generateManaBar() {
        StackPane manaWrapper = new StackPane();
        manaWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        mana = new ProgressBar();
        mana.setProgress(0);
        mana.setPrefWidth(150);
        mana.setMaxHeight(4);
        mana.getStyleClass().add("mana");
        manaWrapper.getChildren().add(mana);
        manaText = new Text("0/0 ");
        manaWrapper.getChildren().add(manaText);
        StackPane.setAlignment(manaText, Pos.TOP_RIGHT);
        manaText.getStyleClass().add("text");
        return manaWrapper;
    }

    private StackPane generateExpBar() {
        StackPane expWrapper = new StackPane();
        expWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        exp = new ProgressBar();
        exp.setProgress(0);
        exp.setPrefWidth(150);
        exp.setMaxHeight(4);
        exp.getStyleClass().add("exp");
        expWrapper.getChildren().add(exp);
        expText = new Text("0/0 ");
        expWrapper.getChildren().add(expText);
        StackPane.setAlignment(expText, Pos.TOP_RIGHT);
        expText.getStyleClass().add("text");
        GridPane.setValignment(expWrapper, VPos.TOP);
        return expWrapper;
    }
    
    public void renderMapCanvas(MapLayerInfo background) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.setFill(new Color(1, 1, 1, 1));
        
        background.getParts()
                .stream()
                .filter(x -> !x.getName().startsWith("$"))
                .forEach(part -> {
                    part.getPlaces().forEach(place -> gc.fillRect(
                            scale * place[0], 
                            scale * place[1],
                            scale, scale));
                });
        
        GraphicsContext playersGc = players.getGraphicsContext2D();
        playersGc.setFill(new Color(0, 0, 0, 1));
        playersGc.fillOval(64, 64, 4, 4);
    }
    
    public void updateMapCanvas(double x, double y) {
        mapCanvas.setTranslateX((-x * scale) + 64 - (scale / 2));
        mapCanvas.setTranslateY((-y * scale) + 64 - (scale / 2));
    }

    public void update(byte[] o) {
        SelfInfoUpdatePacket packet;
        try {
            packet = HacknslashApplication.JSON_MAPPER.readValue(o, SelfInfoUpdatePacket.class);
            hp.setProgress(((float) packet.getHp()) / packet.getMaxHp()); 
            hpText.setText(packet.getHp() + "/" + packet.getMaxHp() + " ");
            mana.setProgress(((float) packet.getMana()) / packet.getMaxMana()); 
            manaText.setText(packet.getMana() + "/" + packet.getMaxMana() + " ");
            exp.setProgress(((float) packet.getExp()) / packet.getMaxExp()); 
            expText.setText(packet.getExp() + "/" + packet.getMaxExp() + " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
