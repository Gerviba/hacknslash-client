package hu.gerviba.hackandslash.client.gui.ingame.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemInstance {

    private final ItemType type;
    private int count;
    
}
