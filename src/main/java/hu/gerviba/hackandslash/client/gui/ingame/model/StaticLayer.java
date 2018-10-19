package hu.gerviba.hackandslash.client.gui.ingame.model;

import hu.gerviba.hackandslash.packets.MapLoadPacket.MapLayerInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Data;

@Data
public class StaticLayer {

    private final GraphicsContext gc;
    private final MapLayerInfo layerInfo;
    private final Image image;
    private final int scale;
    private final int canvasSize;
    private final int dX;
    private final int dY;
    
    public void draw(double x, double y) {
        gc.clearRect(0, 0, canvasSize, canvasSize);
        
        layerInfo.getParts().forEach(part -> {
            part.getPlaces().forEach(place -> gc.drawImage(image, 
                    scale * part.getX(), 
                    scale * part.getY(),
                    scale, scale, 
                    place[0] * scale - (dX + x) + (canvasSize/2), 
                    place[1] * scale - (dY + y) + (canvasSize/2), 
                    scale, scale));
        });
    }
}
