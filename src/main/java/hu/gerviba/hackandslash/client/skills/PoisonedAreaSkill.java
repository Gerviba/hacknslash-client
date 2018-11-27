package hu.gerviba.hackandslash.client.skills;

import java.util.concurrent.ThreadLocalRandom;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

/**
 * Green gas cloud
 * @author Gergely Szabó
 */
public class PoisonedAreaSkill extends Skill {

    private final double[] X_COORDS, Y_COORDS;
    
    /**
     * Skill constructor
     * @param skillUid Unique ID of the skill
     * @param manaCost Mana cost of casing the skill
     * @param reloadTime Time to reload
     */
    public PoisonedAreaSkill(int skillUid, int manaCost, double reloadTime) {
        super(skillUid, manaCost, reloadTime);
        
        X_COORDS = new double[] {
                0.4,
                0.1,
                0.0,
                0.4,
                -0.4,
                -0.5,
                0.1,
                0.1,
                -0.1,
                -0.3
        };
        Y_COORDS = new double[] {
                0.0,
                0.1,
                -0.2,
                -0.4,
                0.3,
                -0.1,
                -0.4,
                0.3,
                0.2,
                -0.4
        };
        
    }

    /**
     * Cast the skill, register particle effects
     */
    @Override
    public void cast(double x, double y, int direction, IngameWindow ingame) {
        for (int i = 0; i < X_COORDS.length; ++i) {
            ingame.getEntities().add(new ParticleInstance(Particles.LONG_POISON,
                    System.currentTimeMillis() + 1000 + (ThreadLocalRandom.current().nextInt(20) * 10),
                    x + X_COORDS[i],
                    y + Y_COORDS[i],
                    ingame.getScaleInPixel(),
                    ingame.getWidth(),
                    ingame.getHeight(),
                    0));
        }
    }

}
