package hu.gerviba.hackandslash.client.packets;

import java.util.List;

import hu.gerviba.hackandslash.client.gui.ingame.item.ItemType;
import lombok.Data;

@Data
public class ItemChangePacket {
    
    @Data
    public static class ChangePart {
        /**
         * -1 means empty slot
         * 
         * 0-9: Reserved for Armors
         * 0: Weapon
         * 1: Ring
         * 2: Helmet
         * 3: Armor
         * 4: Boots
         * 
         * 10-21: Reserved for Skills bar
         * 10-16: F1-F7
         * 
         * 22-62: Slots
         */
        private final int slot;
        /**
         * @see {@link ItemType#getId()}
         */
        private final int itemId;
        private final int count;
    }
    
    public enum ChangeMethod {
        OVERRIDE,
        ADD;
    }
    
    private final ChangeMethod method;
    
    private final List<ChangePart> changes;
    
    
}
