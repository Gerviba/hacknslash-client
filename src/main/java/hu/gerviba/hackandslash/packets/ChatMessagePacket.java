package hu.gerviba.hackandslash.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessagePacket {

    public static enum MessageType {
        CHAT,
        PARTY,
        CLAN,
        WHISPER,
        ANNOUNCEMENT,
        JOIN,
        LEAVE,
        ;
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
