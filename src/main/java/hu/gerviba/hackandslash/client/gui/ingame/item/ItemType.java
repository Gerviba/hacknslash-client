package hu.gerviba.hackandslash.client.gui.ingame.item;

import lombok.Data;

@Data
public class ItemType {

    private final int id;
    private final int textureX;
    private final int textureY;
    private final int skillUid;
    private final String name;
    private final String type;
    private final String lore;
    
    private final int moneySell;
    private final int moneyBuy;
    
}
