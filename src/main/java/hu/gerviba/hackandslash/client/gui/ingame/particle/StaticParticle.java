package hu.gerviba.hackandslash.client.gui.ingame.particle;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class StaticParticle implements Particle {

    private Image image;
    private int fromX;
    private int fromY;
    private int particleSize;
    private int animationLength;
    private int selectedSprite;
    
    public StaticParticle(Image image, int fromX, int fromY, int particleSize, int sprites, int animationLength) {
        this.image = image;
        this.fromX = fromX;
        this.fromY = fromY;
        this.particleSize = particleSize;
        this.animationLength = animationLength;
        this.selectedSprite = ThreadLocalRandom.current().nextInt(sprites);
    }
        
    @Override
    public void render(GraphicsContext gc, double time, double x, double y) {
        gc.drawImage(image, 
                (fromX + selectedSprite) * particleSize,
                fromY * particleSize,
                particleSize, particleSize, 
                x, y,
                particleSize, particleSize);
    }

    @Override
    public int getHeight() {
        return particleSize;
    }

    @Override
    public int getWidth() {
        return particleSize;
    }

    @Override
    public long getAnimationLength() {
        return animationLength;
    }
    
}

