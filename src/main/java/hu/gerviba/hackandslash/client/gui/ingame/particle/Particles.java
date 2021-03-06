package hu.gerviba.hackandslash.client.gui.ingame.particle;

import hu.gerviba.hackandslash.client.ImageUtil;
import javafx.scene.image.Image;

/**
 * Particle storage class
 * @author Gergely Szabó
 */
public class Particles {

    public static final Particle WATER;
    public static final Particle POISON;
    public static final Particle FLAME;
    public static final Particle MAGIC1;
    public static final Particle MAGIC2;
    public static final Particle SMOKE;
    public static final Particle LONG_FLAME;
    public static final Particle LONG_POISON;
    public static final Particle LONG_SMOKE;
    public static final Particle LIGHTNING;
    public static final Particle HEALTHS;
    public static final Particle MANA;
    
    /**
     * Initialize contained particle instances
     */
    static {
        Image texture = ImageUtil.loadImage("/assets/particles/magic.png");
        
        WATER = new AnimatedParticle(texture, 0, 2, 24, 5, 120, 480);
        POISON = new AnimatedParticle(texture, 0, 1, 24, 4, 150, 2000);
        FLAME = new AnimatedParticle(texture, 0, 4, 24, 4, 160, 1600);
        MAGIC1 = new AnimatedParticle(texture, 0, 5, 24, 4, 160, 480);
        MAGIC2 = new AnimatedParticle(texture, 0, 7, 24, 4, 160, 480);
        SMOKE = new AnimatedParticle(texture, 0, 6, 24, 8, 160, 1120); 
        LONG_FLAME = new AnimatedParticle(texture, 0, 8, 24, 12, 160, 1920);
        LONG_POISON = new AnimatedParticle(texture, 0, 9, 24, 16, 150, 2400);
        LONG_SMOKE = new AnimatedParticle(texture, 0, 10, 24, 16, 160, 2560);
        LIGHTNING = new AnimatedParticle(texture, 0, 11, 24, 16, 150, 2400);
        HEALTHS = new AnimatedParticle(texture, 5, 2, 24, 5, 150, 750);
        MANA = new AnimatedParticle(texture, 15, 2, 24, 1, 0, 0);
        
    }
    
}
