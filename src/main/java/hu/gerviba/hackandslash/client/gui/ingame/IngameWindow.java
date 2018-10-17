package hu.gerviba.hackandslash.client.gui.ingame;

import hu.gerviba.hackandslash.client.gui.CustomWindow;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class IngameWindow extends CustomWindow {

    @Getter
    private ChatHud chatComponent;
    
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
        
        chatComponent = new ChatHud();
        ingame.getChildren().add(chatComponent.toPane());
//        body.add(chat, 0, 0);
        
        initScene(body);
    }

    private void initScene(Pane body) {
        scene = new Scene(body);
        scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        stage.setTitle("Hack'n'Slash");
        stage.setMinHeight(600);
        stage.setMinWidth(820);
    }
    
}
