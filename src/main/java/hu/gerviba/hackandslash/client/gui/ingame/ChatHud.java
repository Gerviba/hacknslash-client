package hu.gerviba.hackandslash.client.gui.ingame;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hackandslash.packets.ChatMessagePacket;
import hu.gerviba.hackandslash.packets.TemplatePacketBuilder;
import hu.gerviba.hackandslash.packets.ChatMessagePacket.MessageType;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class ChatHud implements CustomComponent {

    private ObjectMapper mapper = new ObjectMapper();

    private Text[] texts = new Text[]{
            newText(), newText(), newText(), newText(), newText(),
            newText(), newText(), newText(), newText(), newText()
    };

    private Text newText() {
        Text text = new Text();
        text.getStyleClass().add("text");
        text.setWrappingWidth(494);
        return text;
    }
    
    public void appendMessage(byte[] o) {
        for (int i = 0; i < 9; ++i) {
            texts[i].setText(texts[i + 1].getText());
            texts[i].setId(texts[i + 1].getId());
        }
        ChatMessagePacket msg;
        try {
            msg = mapper.readValue(o, ChatMessagePacket.class);
            texts[9].setText(" " + msg.getMessage() + " ");
            texts[9].setId("color-" + msg.getType().getColor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Pane toPane() {
        AnchorPane chatWrapper = new AnchorPane();
        GridPane chat = new GridPane();
        chat.getStyleClass().add("chat");
        chat.setMinHeight(200);
        chat.setMinWidth(300);
        chat.setMaxWidth(500);
        chatWrapper.getChildren().add(chat);
        AnchorPane.setLeftAnchor(chat, 0.0);
        AnchorPane.setBottomAnchor(chat, 0.0);
        
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
        chatInput.setDisable(true);
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
        return chatWrapper;
    }
    
}
