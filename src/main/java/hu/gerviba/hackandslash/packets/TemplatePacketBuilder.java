package hu.gerviba.hackandslash.packets;

import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.packets.ChatMessagePacket.MessageType;

public class TemplatePacketBuilder {

    public static byte[] buildChatMessage(MessageType type, String sender, String target, String message) {
        return String.format("{\"type\":\"%s\",\"sender\":\"%s\",\"target\":\"%s\",\"message\":\"%s\"}", 
                type.name(), sender, target, message).getBytes();
    }
    
    public static byte[] buildTelemetry(PlayerModel player) {
        return String.format("{\"x\":\"%f\",\"y\":\"%f\",\"direction\":\"%d\",\"walking\":%s}", 
                player.getX(), player.getY(), player.getDirection(), player.isWalking() ? "true" : "false")
                .getBytes();
    }

    public static byte[] buildJustConnected(int height, int width, int scale) {
        return String.format("{\"height\":%d,\"width\":%d,\"scale\":%d}", 
                height, width, scale).getBytes();
    }
    
}
