package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface of the renderable models
 * @author Gergely Szabó
 */
public interface RenderableModel {

    public default long getId() {
        return -1;
    }
    
    public double getOrder();

    public void calc();

    public void draw(GraphicsContext midGc, GraphicsContext topGc, double time, double x, double y);
    
    public boolean isFinished();
    
}
