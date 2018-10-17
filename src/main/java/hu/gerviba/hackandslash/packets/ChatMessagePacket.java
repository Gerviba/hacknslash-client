package hu.gerviba.hackandslash.packets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
        SERVER("purple")
        ;
        
        @Getter
        private String color;
    }

    @Getter
    @Setter
    private MessageType type;
    
    @Getter
    @Setter
    private String sender;

    @Getter
    @Setter
    private String target;
    
    @Getter
    @Setter
    private String message;

}
