package hu.gerviba.hackandslash.client.gui.ingame.particle;

import javafx.scene.canvas.GraphicsContext;

public interface Particle {

    public void render(GraphicsContext gc, ParticleInstance pi, double x, double y);
    
}
