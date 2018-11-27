package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.PlayerInfoHud;
import hu.gerviba.hackandslash.client.packets.TemplatePacketBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Skills, magics and potions
 * @author Gergely Szabó
 */
@RequiredArgsConstructor
public abstract class Skill {

    private final int skillUid;
    private final int manaCost;
    @Getter
    private final double reloadTime;
    private long lastUsed;
    
    /**
     * Send the use packet
     */
    public void send() {
        use();
        HacknslashApplication.getInstance().getConnection().appendTask(stomp -> 
                stomp.send("/app/skills", TemplatePacketBuilder.buildSkillPacket(skillUid)));
    }

    /**
     * Cast the skill, register particle effects
     */
    public abstract void cast(double x, double y, int direction,  IngameWindow ingame);
    
    /**
     * Can the client cast this skill?
     * @param playerInfo 
     * @return true if it has enough mana and the skill is not loading
     */
    public boolean canCast(PlayerInfoHud playerInfo) {
        return playerInfo.getManaCount() >= manaCost 
                && System.currentTimeMillis() > lastUsed + reloadTime;
    }
    
    /**
     * Client uses this skill
     */
    protected final void use() {
        lastUsed = System.currentTimeMillis();
    }
    
}
