package hu.gerviba.hackandslash.client.gui.ingame;

import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hackandslash.packets.MapLoadPacket.MapLayerInfo;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class PlayerInfoHud implements CustomComponent {

    AnchorPane hudWarpper;
    Canvas mapCanvas;
    Canvas players;
    int scale = 4;
    
    @Override
    public Pane toPane() {
        hudWarpper = new AnchorPane();
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
        
        StackPane map = new StackPane();
        map.setAlignment(Pos.TOP_LEFT);
        map.getStyleClass().add("map");
        map.setMinHeight(128);
        map.setMaxHeight(128);
        map.setMinWidth(128);
        map.setMaxWidth(128);
        map.setClip(new Circle(64, 64, 64));
        
        mapCanvas = new Canvas(1024, 1024);
        mapCanvas.getStyleClass().add("canvas");
        map.getChildren().add(mapCanvas);
        players = new Canvas(128, 128);
        map.getChildren().add(players);
        
        hud.add(map, 0, 0, 1, 5);
        
        Text playerName = new Text("Player");
        playerName.getStyleClass().add("player-name");
        hud.add(playerName, 1, 0);
        
        StackPane hpWrapper = new StackPane();
        hpWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        ProgressBar hp = new ProgressBar();
        hp.setProgress(0.80);
        hp.setPrefWidth(150);
        hp.setMaxHeight(4);
        hp.getStyleClass().add("hp");
        hpWrapper.getChildren().add(hp);
        Text hpText = new Text("80/100 ");
        hpWrapper.getChildren().add(hpText);
        StackPane.setAlignment(hpText, Pos.TOP_RIGHT);
        hpText.getStyleClass().add("text");
        hud.add(hpWrapper, 1, 1);
        
        StackPane manaWrapper = new StackPane();
        manaWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        ProgressBar mana = new ProgressBar();
        mana.setProgress(0.5);
        mana.setPrefWidth(150);
        mana.setMaxHeight(4);
        mana.getStyleClass().add("mana");
        manaWrapper.getChildren().add(mana);
        Text manaText = new Text("50/100 ");
        manaWrapper.getChildren().add(manaText);
        StackPane.setAlignment(manaText, Pos.TOP_RIGHT);
        manaText.getStyleClass().add("text");
        hud.add(manaWrapper, 1, 2);

        StackPane expWrapper = new StackPane();
        expWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        ProgressBar exp = new ProgressBar();
        exp.setProgress(0.1);
        exp.setPrefWidth(150);
        exp.setMaxHeight(4);
        exp.getStyleClass().add("exp");
        expWrapper.getChildren().add(exp);
        Text expText = new Text("10/100 ");
        expWrapper.getChildren().add(expText);
        StackPane.setAlignment(expText, Pos.TOP_RIGHT);
        expText.getStyleClass().add("text");
        hud.add(expWrapper, 1, 3);
        GridPane.setValignment(expWrapper, VPos.TOP);
        
        hud.add(new Text(), 1, 4);
        
        return hudWarpper;
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
        playersGc.setFill(new Color(1, 0, 0, 1));
        playersGc.fillOval(64, 64, 4, 4);
    }
    
    public void updateMapCanvas(double x, double y) {
        mapCanvas.setTranslateX((-x * scale) + 64 - (scale / 2));
        mapCanvas.setTranslateY((-y * scale) + 64 - (scale / 2));
    }
    
}
