package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;
import lombok.Data;

@Data
public class MiddleModel implements RenderableEntity {

    private final double x;
    private final double y;
    private final int width;
    private final int height;

    @Override
    public void calc() {
    }

    @Override
    public void draw(GraphicsContext gc, double time, double x, double y) {
        
    }


}
