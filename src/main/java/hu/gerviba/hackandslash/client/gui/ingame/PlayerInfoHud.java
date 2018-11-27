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
import lombok.Getter;

/**
 * Player info and minimap GUI component
 * @author Gergely Szabó
 */
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
    @Getter
    private int manaCount;
    
    /**
     * Component root pane getter
     */
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
        
        return hudWarpper;
    }

    /**
     * Generates the base hud
     * @return Base HUD component
     */
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

    /**
     * Generates the minimap
     * @return The minimap component
     */
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

    /**
     * Generates player name field
     */
    private Text generatePlayerName() {
        Text playerName = new Text(HacknslashApplication.getInstance().getUser().getName());
        playerName.getStyleClass().add("player-name");
        return playerName;
    }
    
    /**
     * Append HP bar
     */
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
    
    /**
     * Append mana bar
     */
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

    /**
     * Append exp bar
     */
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
    
    /**
     * Render the map canvas based on the background of the current loaded map
     * @param background
     */
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
        playersGc.setFill(new Color(188.0/255, 58.0/255, 24.0/255, 1));
        playersGc.fillOval(64, 64, 4, 4);
    }
    
    /**
     * Move the minimap
     * @param x X component of the user's coordinate
     * @param y Y component of the user's coordinate
     */
    public void updateMapCanvas(double x, double y) {
        mapCanvas.setTranslateX((-x * scale) + 64 + (scale/2));
        mapCanvas.setTranslateY((-y * scale) + 64 + (scale/2));
    }

    /**
     * Update HP, mana, exp values
     * @param o The byte[] received from the server
     */
    public void update(byte[] o) {
        SelfInfoUpdatePacket packet;
        try {
            packet = HacknslashApplication.JSON_MAPPER.readValue(o, SelfInfoUpdatePacket.class);
            hp.setProgress(((float) packet.getHp()) / packet.getMaxHp()); 
            hpText.setText(packet.getHp() + "/" + packet.getMaxHp() + " ");
            mana.setProgress(((float) packet.getMana()) / packet.getMaxMana()); 
            manaText.setText(packet.getMana() + "/" + packet.getMaxMana() + " ");
            manaCount = packet.getMana();
            exp.setProgress(((float) packet.getExp()) / packet.getMaxExp()); 
            expText.setText(packet.getExp() + "/" + packet.getMaxExp() + " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
