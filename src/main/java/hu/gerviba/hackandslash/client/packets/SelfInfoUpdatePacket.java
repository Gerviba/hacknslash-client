package hu.gerviba.hackandslash.client.packets;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelfInfoUpdatePacket {
    
    private long entityId; 
    private int hp;
    private int maxHp;
    private int mana;
    private int maxMana;
    private int exp;
    private int maxExp;
    private int level;
    private int money;
    
}
