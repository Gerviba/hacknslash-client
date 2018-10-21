package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;

public interface Particle {

    public void render(GraphicsContext gc, double time, double x, double y);
    
}
