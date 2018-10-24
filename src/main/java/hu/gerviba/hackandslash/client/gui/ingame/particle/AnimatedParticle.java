package hu.gerviba.hackandslash.client.gui.ingame.particle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AnimatedParticle implements Particle {

    private Image image;
    private int fromX;
    private int fromY;
    private int particleSize;
    private int sprites;
    private int animationLength;
    private int lifetime;
    
    @Override
    public void render(GraphicsContext gc, double time, double x, double y) {
        gc.drawImage(image, 
                (fromX + (((int)(time) / animationLength) % sprites)) * particleSize,
                fromY * particleSize,
                particleSize, particleSize, 
                (int) x, (int) y,
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
        return lifetime;
    }
    
}
