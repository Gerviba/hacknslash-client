package hu.gerviba.hackandslash.client.gui.ingame.item;

import lombok.Data;

@Data
public class ItemType {

    public static ItemType NOTHING = new ItemType(-1, 0, 0, 0, "null", ItemCategory.THING, "", -1, -1);
    
    private final int id;
    private final int textureX;
    private final int textureY;
    private final int skillUid;
    private final String name;
    private final ItemCategory type;
    private final String lore;
    
    private final int moneySell;
    private final int moneyBuy;
    
}
