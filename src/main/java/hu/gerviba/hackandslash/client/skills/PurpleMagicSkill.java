package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

public class PurpleMagicSkill extends Skill {

    public PurpleMagicSkill(double manaCost, double reloadTime) {
        super(manaCost, reloadTime);
    }

    @Override
    public void cast(PlayerModel pm, IngameWindow ingame) {
        use();
        
        double xMultipiler = pm.getDirection() == PlayerModel.DIRECTION_LEFT ? -1
                : pm.getDirection() == PlayerModel.DIRECTION_RIGHT ? 1 : 0;
        
        double yMultipiler = pm.getDirection() == PlayerModel.DIRECTION_STAND ? 1
                : pm.getDirection() == PlayerModel.DIRECTION_BACK ? -1 : 0;
        
        for (int i = 1; i < 14; ++i)
            ingame.getEntities().add(new ParticleInstance(i % 2 == 0 ? Particles.MAGIC1 : Particles.MAGIC2, 
                    System.currentTimeMillis() - 80 + (80 * i), 
                    pm.getGeneralX() + ((0.25 * i) * xMultipiler), 
                    pm.getGeneralY() + ((0.25 * i) * yMultipiler), 
                    ingame.getScaleInPixel(), 
                    ingame.getWidth(), 
                    ingame.getHeight(),
                    0.5));
    }

    
    
}
