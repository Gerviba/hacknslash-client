package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;

public interface RenderableModel {

    public double getOrder();

    public void calc();

    public void draw(GraphicsContext midGc, GraphicsContext topGc, double time, double x, double y);
    
    public boolean isFinished();
    
}
