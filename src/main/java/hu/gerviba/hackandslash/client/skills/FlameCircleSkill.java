package hu.gerviba.hackandslash.client.skills;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

public class FlameCircleSkill extends Skill {
    
    private final double[] X_COORDS, Y_COORDS;
    private final int FLAMES = 12;
    
    public FlameCircleSkill(int skillUid, int manaCost, double reloadTime) {
        super(skillUid, manaCost, reloadTime);
        
        final double r = 0.75;
        X_COORDS = new double[FLAMES];
        Y_COORDS = new double[FLAMES];
        
        for (int k = 0; k < FLAMES; ++k) {
            X_COORDS[k] = r * Math.cos((k * (Math.PI * 2)) / FLAMES);
            Y_COORDS[k] = r * Math.sin((k * (Math.PI * 2)) / FLAMES);
        }
    }

    @Override
    public void cast(double x, double y, int direction, IngameWindow ingame) {
        for (int i = 0; i < FLAMES; ++i) {
            if (!ingame.canMoveToVirtualCoords(x + X_COORDS[i], y + Y_COORDS[i]))
                continue;
            ingame.getEntities().add(new ParticleInstance(Particles.LONG_FLAME, 
                    System.currentTimeMillis(), 
                    x + X_COORDS[i], 
                    y + Y_COORDS[i], 
                    ingame.getScaleInPixel(), 
                    ingame.getWidth(), 
                    ingame.getHeight(),
                    0));
        }
    }

}
