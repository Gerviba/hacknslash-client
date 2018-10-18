package hu.gerviba.hackandslash.packets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessagePacket {

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
