package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.PlayerInfoHud;
import hu.gerviba.hackandslash.client.packets.TemplatePacketBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Skill {

    private final int skillUid;
    private final int manaCost;
    @Getter
    private final double reloadTime;
    private long lastUsed;
    
    public void send() {
        use();
        HacknslashApplication.getInstance().getConnection().appendTask(stomp -> 
                stomp.send("/app/skills", TemplatePacketBuilder.buildSkillPacket(skillUid)));
    }
    
    public abstract void cast(double x, double y, int direction,  IngameWindow ingame);
    
    public boolean canCast(PlayerInfoHud playerInfo) {
        return playerInfo.getManaCount() >= manaCost 
                && System.currentTimeMillis() > lastUsed + reloadTime;
    }
    
    protected final void use() {
        lastUsed = System.currentTimeMillis();
    }
    
}
