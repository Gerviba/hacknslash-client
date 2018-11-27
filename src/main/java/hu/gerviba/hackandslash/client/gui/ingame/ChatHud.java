package hu.gerviba.hackandslash.client.gui.ingame;

import java.io.IOException;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hackandslash.client.packets.ChatMessagePacket;
import hu.gerviba.hackandslash.client.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hackandslash.client.packets.TemplatePacketBuilder;
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

/**
 * Chat GUI component
 * @author Gergely Szabó
 */
public class ChatHud implements CustomComponent {

    private AnchorPane chatWrapper;
    private TextField chatInput;
    private Button sendMessage;
    private GridPane title;
    private Text titleText;
    
    private Text[] texts = new Text[]{
            newText(), newText(), newText(), newText(), newText(),
            newText(), newText(), newText(), newText(), newText()
    };

    /**
     * Appends a new text line.
     * @return A new {@link Text} object with 'text' CSS class 
     */
    private Text newText() {
        Text text = new Text();
        text.getStyleClass().add("text");
        text.setWrappingWidth(494);
        return text;
    }
    
    /**
     * New message received handler
     * @param o A byte[] received from STOMP channel
     */
    public void appendMessage(byte[] o) {
        for (int i = 0; i < 9; ++i) {
            texts[i].setText(texts[i + 1].getText());
            texts[i].setId(texts[i + 1].getId());
        }
        ChatMessagePacket msg;
        try {
            msg = HacknslashApplication.JSON_MAPPER.readValue(o, ChatMessagePacket.class);
            texts[9].setText(" " + msg.getMessage() + " ");
            texts[9].setId("color-" + msg.getType().getColor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sends the message from the input text field if presents.
     */
    private void sendChatMessage() {
        HacknslashApplication.getInstance().getConnection().appendTask(stomp -> {
            try {
                if (chatInput.getText().length() > 0)
                    stomp.send("/app/chat", TemplatePacketBuilder.buildChatMessage(MessageType.CHAT,
                            HacknslashApplication.getInstance().getUser().getName(), 
                            null, chatInput.getText()));
                
                Platform.runLater(() -> {
                    chatInput.setDisable(true);
                    sendMessage.setDisable(true);
                    title.setVisible(false);
                    chatInput.setText("");
                    chatWrapper.getStyleClass().clear();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Getter to the root pane of the component
     */
    @Override
    public Pane toPane() {
        chatWrapper = new AnchorPane();
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
        
        title = new GridPane();
        title.minWidth(300);
        title.maxWidth(500);
        title.setVisible(false);
        titleText = new Text(" CHAT | Channel: global");
        titleText.getStyleClass().add("title-text");
        title.add(titleText, 0, 0);
        title.getStyleClass().add("title");
        chat.add(title, 0, 0, 2, 1);
        for (int i = 0; i < 10; ++i)
            chat.add(texts[i], 0, i + 1, 2, 1);
        
        chatInput = new TextField();
        chatInput.setDisable(true);
        chatInput.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                sendChatMessage();
            }
        });
        
        chat.add(chatInput, 0, 11, 1, 2);
        sendMessage = new Button(">");
        sendMessage.setDisable(true);
        sendMessage.setOnMouseClicked(event -> sendChatMessage());
        chat.add(sendMessage, 1, 11);
        return chatWrapper;
    }

    /**
     * Activates the chat component. 
     * The user will able to write message to the input text field.
     */
    public void allow() {
        chatInput.setDisable(false);
        sendMessage.setDisable(false);
        title.setVisible(true);
        chatWrapper.getStyleClass().add("allowed");
        Math.sin(1.0);
    }
    
}
