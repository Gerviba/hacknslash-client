package hu.gerviba.hackandslash.client.packets;

import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.packets.ChatMessagePacket.MessageType;

/**
 * Template packet builder
 * @author Gergely Szabó
 */
public class TemplatePacketBuilder {

    /**
     * Builds chat message packet
     * @param type Message type
     * @param sender Sender
     * @param target Target
     * @param message Message
     * @return byte[] representation of the packet
     */
    public static byte[] buildChatMessage(MessageType type, String sender, String target, String message) {
        return String.format("{\"type\":\"%s\",\"sender\":\"%s\",\"target\":\"%s\",\"message\":\"%s\"}", 
                type.name(), sender, target, message).getBytes();
    }

    /**
     * Builds telemetry packet
     * @param player Player
     * @return byte[] representation of the packet
     */
    public static byte[] buildTelemetry(PlayerModel player) {
        return String.format("{\"x\":\"%f\",\"y\":\"%f\",\"direction\":\"%d\",\"walking\":%s}", 
                player.getX(), player.getY(), player.getDirection(), player.isWalking() ? "true" : "false")
                .getBytes();
    }

    /**
     * Builds just connected packet
     * @param height Height of the window
     * @param width Width of the window
     * @param scale Resolution
     * @return byte[] representation of the packet
     */
    public static byte[] buildJustConnected(int height, int width, int scale) {
        return String.format("{\"height\":%d,\"width\":%d,\"scale\":%d}", 
                height, width, scale).getBytes();
    }

    /**
     * Builds skill packet
     * @param skillUid Requested skill uid
     * @return byte[] representation of the packet
     */
    public static byte[] buildSkillPacket(int skillUid) {
        return String.format("{\"skillUid\":%d}", skillUid).getBytes();
    }

    /**
     * Builds change item packet
     * @param from Slot id from
     * @param to Slot id to
     * @return byte[] representation of the packet
     */
    public static byte[] buildChangeItem(int from, int to) {
        return String.format("{\"slotFrom\":%d,\"slotTo\":%d}", from, to).getBytes();
    }
    
}
