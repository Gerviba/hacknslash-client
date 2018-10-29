package hu.gerviba.hackandslash.client.gui.ingame.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemInstance {

    public static final ItemInstance NOTHING = new ItemInstance(ItemType.NOTHING, 0);
    
    private final ItemType type;
    private int count;
    
}
