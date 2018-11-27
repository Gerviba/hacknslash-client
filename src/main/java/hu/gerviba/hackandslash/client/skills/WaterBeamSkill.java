package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

public class WaterBeamSkill extends Skill {

    final double z = 0.5;
    
    public WaterBeamSkill(int skillUid, int manaCost, double reloadTime) {
        super(skillUid, manaCost, reloadTime);
    }

    @Override
    public void cast(double x, double y, int direction, IngameWindow ingame) {
        double xMultipiler = direction == PlayerModel.DIRECTION_LEFT ? -1
                : direction == PlayerModel.DIRECTION_RIGHT ? 1 : 0;
        
        double yMultipiler = direction == PlayerModel.DIRECTION_STAND ? 1
                : direction == PlayerModel.DIRECTION_BACK ? -1 : 0;
        
        for (int i = 1; i < 7; ++i) {
            if (!ingame.canMoveToVirtualCoords(
                    x + ((0.5 * i) * xMultipiler), 
                    y + ((0.5 * i) * yMultipiler)))
                break;
            ingame.getEntities().add(new ParticleInstance(Particles.WATER, 
                    System.currentTimeMillis() - 120 + (120 * i), 
                    x + ((0.5 * i) * xMultipiler), 
                    y + ((0.5 * i) * yMultipiler), 
                    ingame.getScaleInPixel(), 
                    ingame.getWidth(), 
                    ingame.getHeight(),
                    z));
        }
    }

    
    
}
