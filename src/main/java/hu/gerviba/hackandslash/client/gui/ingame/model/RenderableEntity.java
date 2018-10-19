package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;

public interface RenderableEntity {

    public double getY();

    public void calc();

    public void draw(GraphicsContext gc, double time, double x, double y);

    
}
