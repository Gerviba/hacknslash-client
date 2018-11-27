package hu.gerviba.hackandslash.client.gui.ingame.model;

import hu.gerviba.hackandslash.client.gui.ingame.particle.Particle;
import hu.gerviba.hackandslash.client.gui.ingame.particle.ParticleInstance;

/**
 * Permanent particle model
 * @author Gergely Szabó
 *
 */
public class PermanentParticleInstance extends ParticleInstance {

    public PermanentParticleInstance(Particle particle, long time, double x, double y, 
            int scaleInPixel, int width, int height, double z) {
        super(particle, time, x, y, scaleInPixel, width, height, z);
    }
    
    /**
     * Finished getter. Always return false. 
     * This method used in the custom entity garbage collector.
     */
    @Override
    public boolean isFinished() {
        return false;
    }
    
}
