package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Data;

/**
 * Static object model (such as: chest)
 * @author Gergely Szabó
 */
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
    
    /**
     * Do the logic of the movement
     */
    @Override
    public void calc() {
    }
    
    /**
     * Render method of this model
     */
    @Override
    public void draw(GraphicsContext midGc, GraphicsContext topGc, double time, double x, double y) {
        midGc.drawImage(image, 
                0, 0,
                renderWidth, renderHeight, 
                dX - x + (canvasWidth / 2) - (renderWidth / 2), 
                dY - y + (canvasHeight / 2), 
                renderWidth, renderHeight);
    }

    /**
     * Rendering order getter
     */
    @Override
    public double getOrder() {
        return dY - renderHeight;
    }

    /**
     * Finished getter. Always return false. 
     * This method used in the custom entity garbage collector.
     */
    @Override
    public boolean isFinished() {
        return false;
    }

}
