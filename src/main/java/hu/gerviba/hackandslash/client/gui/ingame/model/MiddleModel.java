package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Data;

@Data
public class MiddleModel implements RenderableModel {

    private final double dX;
    private final double dY;
    private final int renderWidth;
    private final int renderHeight;
    private final int scale;
    private final Image image;
    private final int textureX;
    private final int textureY;
    private final int canvasWidth;
    private final int canvasHeight;

    @Override
    public void calc() {
    }

    @Override
    public void draw(GraphicsContext midGc, GraphicsContext topGc, double time, double x, double y) {
        midGc.drawImage(image, 
                (scale * 32) * textureX, 
                (scale * 32) * textureY,
                renderWidth, renderHeight, 
                dX - x + (canvasWidth / 2), 
                dY - y + (canvasHeight / 2), 
                renderWidth, renderHeight);
    }

    @Override
    public double getY() {
        return dY;
    }



}
