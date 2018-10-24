package hu.gerviba.hackandslash.client.skills;

import java.util.concurrent.ThreadLocalRandom;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

public class PoisonedAreaSkill extends Skill {

    private final double[] X_COORDS, Y_COORDS;

    public PoisonedAreaSkill(double manaCost, double reloadTime) {
        super(manaCost, reloadTime);
        
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
    
    @Override
    public void cast(PlayerModel pm, IngameWindow ingame) {
        use();
        
        for (int i = 0; i < X_COORDS.length; ++i)
            ingame.getEntities().add(new ParticleInstance(Particles.LONG_POISON,
                    System.currentTimeMillis() + 1000 + (ThreadLocalRandom.current().nextInt(20) * 10),
                    pm.getGeneralX() + X_COORDS[i],
                    pm.getGeneralY() + Y_COORDS[i],
                    ingame.getScaleInPixel(),
                    ingame.getWidth(),
                    ingame.getHeight(),
                    0));
    }

}
