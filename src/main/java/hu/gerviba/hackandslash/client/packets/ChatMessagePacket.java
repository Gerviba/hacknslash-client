package hu.gerviba.hackandslash.client.packets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Chat message packet pojo
 * @author Gergely Szabó
 */
@Data
@NoArgsConstructor
public class ChatMessagePacket {

    /**
     * Type (and color) of the message
     * @author Gergely Szabó
     */
    @AllArgsConstructor
    public static enum MessageType {
        CHAT("white"),
        PARTY("cyan"),
        CLAN("white"),
        WHISPER("silver"),
        ANNOUNCEMENT("yellow"),
        JOIN("blue"),
        LEAVE("gray"),
        ACTION("green"),
        WARNING("red"),
        SERVER("purple"),
        ;
        
        @Getter
        private String color;
    }

    private MessageType type;
    private String sender;
    private String target;
    private String message;

}
