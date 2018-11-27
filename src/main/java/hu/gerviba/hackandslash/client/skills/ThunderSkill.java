package hu.gerviba.hackandslash.client.skills;

import java.util.concurrent.ThreadLocalRandom;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

/**
 * Clouds and lightnings
 * @author Gergely Szabó
 */
public class ThunderSkill extends Skill {

    private final double[] X_COORDS, Y_COORDS;
    
    /**
     * Skill constructor
     * @param skillUid Unique ID of the skill
     * @param manaCost Mana cost of casing the skill
     * @param reloadTime Time to reload
     */
    public ThunderSkill(int skillUid, int manaCost, double reloadTime) {
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
                -0.3,
                -0.4
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
                -0.4,
                -0.3
        };
        
    }
    
    /**
     * Cast the skill, register particle effects
     */
    @Override
    public void cast(double x, double y, int direction, IngameWindow ingame) {
        double xShift = direction == PlayerModel.DIRECTION_LEFT ? -1.5
                : direction == PlayerModel.DIRECTION_RIGHT ? 1.5 : 0;
        
        double yShift = direction == PlayerModel.DIRECTION_STAND ? 1.5
                : direction == PlayerModel.DIRECTION_BACK ? -1.5 : 0;
        
        for (int i = 0; i < X_COORDS.length; ++i)
            ingame.getEntities().add(new ParticleInstance(i % 2 == 0 ? Particles.LONG_SMOKE : Particles.LIGHTNING,
                    System.currentTimeMillis() + (ThreadLocalRandom.current().nextInt(3) * 100),
                    x + X_COORDS[i] + xShift,
                    y + Y_COORDS[i] + yShift,
                    ingame.getScaleInPixel(),
                    ingame.getWidth(),
                    ingame.getHeight(),
                    0));
    }

}
