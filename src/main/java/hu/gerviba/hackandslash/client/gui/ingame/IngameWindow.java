package hu.gerviba.hackandslash.client.gui.ingame;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.CustomWindow;
import hu.gerviba.hackandslash.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hackandslash.packets.TemplatePacketBuilder;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class IngameWindow extends CustomWindow {
    
    @Deprecated
    public static Text[] texts = new Text[]{
            newText(), newText(), newText(), newText(), newText(),
            newText(), newText(), newText(), newText(), newText()
    };

    public static Text newText() {
        Text text = new Text();
        text.getStyleClass().add("text");
        text.setWrappingWidth(494);
        return text;
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
        GridPane body = new GridPane();
        StackPane ingame = new StackPane();

        GridPane chat = new GridPane();
        chat.getStyleClass().add("chat");
        chat.setMinHeight(200);
        chat.setMinWidth(300);
        chat.setMaxWidth(500);
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.NEVER);
        col2.setHalignment(HPos.RIGHT);
        chat.getColumnConstraints().addAll(col1, col2);
        
        GridPane title = new GridPane();
        title.minWidth(300);
        title.maxWidth(500);
        Text titleText = new Text(" CHAT | Channel: global");
        titleText.getStyleClass().add("title-text");
        title.add(titleText, 0, 0);
        title.getStyleClass().add("title");
        chat.add(title, 0, 0, 2, 1);
        for (int i = 0; i < 10; ++i)
            chat.add(texts[i], 0, i + 1, 2, 1);
        
        TextField chatInput = new TextField();
        chatInput.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                HacknslashApplication.getInstance().getConnection().appendTask(stomp -> {
                    try {
                        stomp.send("/app/chat", TemplatePacketBuilder.buildChatMessage(MessageType.CHAT,
                                HacknslashApplication.getInstance().getUser().getName(), null, chatInput.getText()));
                        Platform.runLater(() -> chatInput.setText(""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        
        chat.add(chatInput, 0, 11, 1, 2);
        Button sendMessage = new Button(">");
        sendMessage.setOnMouseClicked(event -> {
            HacknslashApplication.getInstance().getConnection().appendTask(stomp -> {
                try {
                    stomp.send("/app/chat", TemplatePacketBuilder.buildChatMessage(MessageType.CHAT,
                            HacknslashApplication.getInstance().getUser().getName(), null, chatInput.getText()));
                    Platform.runLater(() -> chatInput.setText(""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        chat.add(sendMessage, 1, 11);
        
//        ingame.getChildren().add(chat);
        body.setAlignment(Pos.BOTTOM_LEFT);
        body.add(chat, 0, 0);
        
        initScene(body);
    }

    private void initScene(Pane body) {
        scene = new Scene(body);
        scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        stage.setTitle("Hack'n'Slash");
        stage.setMinHeight(400);
        stage.setMinWidth(820);
    }
    
}
