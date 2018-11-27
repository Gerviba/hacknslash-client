package hu.gerviba.hackandslash.client.skills;

import java.util.concurrent.ThreadLocalRandom;

import hu.gerviba.hackandslash.client.gui.ingame.IngameWindow;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;
import hu.gerviba.hackandslash.client.gui.ingame.particle.Particles;

public class ManaPotion extends Skill {

    public ManaPotion(int skillUid, int manaCost, double reloadTime) {
        super(skillUid, manaCost, reloadTime);
    }

    @Override
    public void cast(double x, double y, int direction, IngameWindow ingame) {
        ingame.getEntities().add(new ParticleInstance(Particles.MANA,
                System.currentTimeMillis() + (ThreadLocalRandom.current().nextInt(3) * 100),
                x,
                y + 0.1,
                ingame.getScaleInPixel(),
                ingame.getWidth(),
                ingame.getHeight(),
                0.75));
    }

}
