package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Data;

@Data
public class StaticObjectModel implements RenderableModel {

    private final double dX;
    private final double dY;
    private final int renderWidth;
    private final int renderHeight;
    private final int scale;
    private final Image image;
    private final int canvasWidth;
    private final int canvasHeight;
    
    @Override
    public void calc() {
    }

    @Override
    public void draw(GraphicsContext midGc, GraphicsContext topGc, double time, double x, double y) {
        midGc.drawImage(image, 
                0, 0,
                renderWidth, renderHeight, 
                dX - x + (canvasWidth / 2) - (renderWidth / 2), 
                dY - y + (canvasHeight / 2), 
                renderWidth, renderHeight);
    }

    @Override
    public double getOrder() {
        return dY - renderHeight;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
