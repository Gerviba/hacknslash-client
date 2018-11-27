package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

/**
 * A purple beam of magic
 * @author Gergely Szabó
 */
public class PurpleMagicSkill extends Skill {

    final double z = 0.5;
    
    /**
     * Skill constructor
     * @param skillUid Unique ID of the skill
     * @param manaCost Mana cost of casing the skill
     * @param reloadTime Time to reload
     */
    public PurpleMagicSkill(int skillUid, int manaCost, double reloadTime) {
        super(skillUid, manaCost, reloadTime);
    }

    /**
     * Cast the skill, register particle effects
     */
    @Override
    public void cast(double x, double y, int direction, IngameWindow ingame) {
        double xMultipiler = direction == PlayerModel.DIRECTION_LEFT ? -1
                : direction == PlayerModel.DIRECTION_RIGHT ? 1 : 0;
        
        double yMultipiler = direction == PlayerModel.DIRECTION_STAND ? 1
                : direction == PlayerModel.DIRECTION_BACK ? -1 : 0;
        
        for (int i = 1; i < 14; ++i) {
            ingame.getEntities().add(new ParticleInstance(i % 2 == 0 ? Particles.MAGIC1 : Particles.MAGIC2, 
                    System.currentTimeMillis() - 80 + (80 * i), 
                    x + ((0.25 * i) * xMultipiler), 
                    y + ((0.25 * i) * yMultipiler), 
                    ingame.getScaleInPixel(), 
                    ingame.getWidth(), 
                    ingame.getHeight(),
                    z));
        }
    }

    
    
}
