package hu.gerviba.hackandslash.client.packets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.packets.ChatMessagePacket.MessageType;

public class TemplatePacketBuilderTest {

    @Test
    @DisplayName("Change item")
    void testChangeItem() throws Exception {
        assertEquals("{\"slotFrom\":12,\"slotTo\":76}", 
                new String(TemplatePacketBuilder.buildChangeItem(12, 76)));
    }

    @Test
    @DisplayName("Chat message")
    void testChatMessage() throws Exception {
        assertEquals("{\"type\":\"ANNOUNCEMENT\",\"sender\":\"This is me\",\"target\":"
                + "\"This is not\",\"message\":\"This is the message\"}", 
                new String(TemplatePacketBuilder.buildChatMessage(MessageType.ANNOUNCEMENT, 
                "This is me", "This is not", "This is the message")));
    }

    @Test
    @DisplayName("Just connected")
    void testJustConnected() throws Exception {
        assertEquals("{\"height\":712,\"width\":1200,\"scale\":2}", 
                new String(TemplatePacketBuilder.buildJustConnected(712, 1200, 2)));
    }

    @Test
    @DisplayName("Skill")
    void testJustSkillPacket() throws Exception {
        assertEquals("{\"skillUid\":102}", 
                new String(TemplatePacketBuilder.buildSkillPacket(102)));
    }   

    @Test
    @DisplayName("Telemetry")
    void testTelemetry() throws Exception {
        assertEquals("{\"x\":\"0.000000\",\"y\":\"0.000000\",\"direction\":\"0\",\"walking\":false}", 
                new String(TemplatePacketBuilder.buildTelemetry(new PlayerModel(102, "name", 2, null, 712, 1200))));
    }
}
