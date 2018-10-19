package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Data;

@Data
public class MiddleModel implements RenderableEntity {

    private final double x;
    private final double y;

    @Override
    public void calc() {
    }

    @Override
    public void draw(GraphicsContext gc, double time, double x, double y) {
        // TODO Auto-generated method stub
        
    }


}
