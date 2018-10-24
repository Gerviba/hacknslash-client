package hu.gerviba.hackandslash.client.gui.ingame.model;

import hu.gerviba.hackandslash.client.gui.ingame.particle.Particle;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;

public class PermanentParticleInstance extends ParticleInstance {

    public PermanentParticleInstance(Particle particle, long time, double x, double y, 
            int scaleInPixel, int width, int height, double z) {
        super(particle, time, x, y, scaleInPixel, width, height, z);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}
