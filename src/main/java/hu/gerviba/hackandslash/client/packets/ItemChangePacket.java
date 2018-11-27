package hu.gerviba.hackandslash.client.packets;

import java.util.List;

import lombok.Data;

/**
 * Item change packet
 * @author Gergely Szabó
 */
@Data
public class ItemChangePacket {
    
    @Data
    public static class ChangePart {
        /**
         * -1 means empty slot<br>
         * <br>
         * 0-9: Reserved for Armors<br>
         * 0: Weapon<br>
         * 1: Ring<br>
         * 2: Helmet<br>
         * 3: Armor<br>
         * 4: Boots<br>
         * <br>
         * 10-21: Reserved for Skills bar<br>
         * 10-16: F1-F7<br>
         * <br>
         * 22-62: Slots<br>
         */
        private final int slot;
        private final int itemId;
        private final int count;
    }
    
    /**
     * Change method
     * @author Gergely Szabó
     */
    public enum ChangeMethod {
        OVERRIDE,
        ADD;
    }
    
    private final ChangeMethod method;
    private final List<ChangePart> changes;
    
    
}
