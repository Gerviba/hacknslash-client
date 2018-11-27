package hu.gerviba.hackandslash.client.skills;

import java.util.concurrent.ThreadLocalRandom;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

/**
 * Increases health points
 * @author Gergely Szabó
 */
public class HealthPotion extends Skill {
    
    /**
     * Skill constructor
     * @param skillUid Unique ID of the skill
     * @param manaCost Mana cost of casing the skill
     * @param reloadTime Time to reload
     */
    public HealthPotion(int skillUid, int manaCost, double reloadTime) {
        super(skillUid, manaCost, reloadTime);
    }

    /**
     * Cast the skill, register particle effects
     */
    @Override
    public void cast(double x, double y, int direction, IngameWindow ingame) {
        ingame.getEntities().add(new ParticleInstance(Particles.HEALTHS,
                System.currentTimeMillis() + (ThreadLocalRandom.current().nextInt(3) * 100),
                x,
                y + 0.1,
                ingame.getScaleInPixel(),
                ingame.getWidth(),
                ingame.getHeight(),
                0.75));
    }
    
}
