package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

public class PurpleMagicSkill extends Skill {

    public PurpleMagicSkill(int skillUid, double manaCost, double reloadTime) {
        super(skillUid, manaCost, reloadTime);
    }

    @Override
    public void cast(double x, double y, int direction, IngameWindow ingame) {
        double xMultipiler = direction == PlayerModel.DIRECTION_LEFT ? -1
                : direction == PlayerModel.DIRECTION_RIGHT ? 1 : 0;
        
        double yMultipiler = direction == PlayerModel.DIRECTION_STAND ? 1
                : direction == PlayerModel.DIRECTION_BACK ? -1 : 0;
        
        for (int i = 1; i < 14; ++i)
            ingame.getEntities().add(new ParticleInstance(i % 2 == 0 ? Particles.MAGIC1 : Particles.MAGIC2, 
                    System.currentTimeMillis() - 80 + (80 * i), 
                    x + ((0.25 * i) * xMultipiler), 
                    y + ((0.25 * i) * yMultipiler), 
                    ingame.getScaleInPixel(), 
                    ingame.getWidth(), 
                    ingame.getHeight(),
                    0.5));
    }

    
    
}
