package hu.gerviba.hackandslash.client.gui.ingame.particle;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface of all the particles
 * @author Gergely Szabó
 */
public interface Particle {
    
    public void render(GraphicsContext gc, double time, double x, double y);

    public int getHeight();

    public int getWidth();

    public long getAnimationLength();
    
}
