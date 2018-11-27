package hu.gerviba.hackandslash.client.gui.ingame.particle;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Static particle.
 * Select a random texture form the list of availables
 * @author Gergely Szabó
 */
public class StaticParticle implements Particle {

    private Image image;
    private int fromX;
    private int fromY;
    private int particleSize;
    private int animationLength;
    private int selectedSprite;
    
    /**
     * Static particle
     * @param image
     * @param fromX
     * @param fromY
     * @param particleSize
     * @param sprites
     * @param animationLength
     */
    public StaticParticle(Image image, int fromX, int fromY, int particleSize, int sprites, int animationLength) {
        this.image = image;
        this.fromX = fromX;
        this.fromY = fromY;
        this.particleSize = particleSize;
        this.animationLength = animationLength;
        this.selectedSprite = ThreadLocalRandom.current().nextInt(sprites);
    }

    /**
     * Render method of this particle
     */
    @Override
    public void render(GraphicsContext gc, double time, double x, double y) {
        gc.drawImage(image, 
                (fromX + selectedSprite) * particleSize,
                fromY * particleSize,
                particleSize, particleSize, 
                x, y,
                particleSize, particleSize);
    }

    /**
     * Height getter
     * @return particleSize
     */
    @Override
    public int getHeight() {
        return particleSize;
    }

    /**
     * Width getter
     * @return particleSize
     */
    @Override
    public int getWidth() {
        return particleSize;
    }

    /**
     * Animation length getter (in frames)
     */
    @Override
    public long getAnimationLength() {
        return animationLength;
    }
    
}

