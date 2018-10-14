package hu.gerviba.hackandslash.packets;

import hu.gerviba.hackandslash.packets.ChatMessagePacket.MessageType;

public class TemplatePacketBuilder {

    public static byte[] buildChatMessage(MessageType type, String sender, String target, String message) {
        return String.format("{\"type\":\"%s\",\"sender\":\"%s\",\"target\":\"%s\",\"message\":\"%s\"}", 
                type.name(), sender, target, message).getBytes();
    }
    
}
