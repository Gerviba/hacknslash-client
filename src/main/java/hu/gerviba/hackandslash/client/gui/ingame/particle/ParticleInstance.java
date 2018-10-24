package hu.gerviba.hackandslash.client.gui.ingame.particle;

import hu.gerviba.hackandslash.client.gui.ingame.model.RenderableModel;
import javafx.scene.canvas.GraphicsContext;
import lombok.Data;

@Data
public class ParticleInstance implements RenderableModel {

    private final Particle particle;
    private final long startTime;
    private final double x;
    private final double y;
    private final int scaleInPixel;
    private final int canvasWidth;
    private final int canvasHeight;
    private final double z;
    
    @Override
    public void calc() {
        
    }

    @Override
    public void draw(GraphicsContext midGc, GraphicsContext topGc, double time, double dX, double dY) {
        if (this.startTime >= System.currentTimeMillis())
            return;
        particle.render(midGc, System.currentTimeMillis() - this.startTime, 
                (this.x * scaleInPixel) - dX + (canvasWidth/2) - (particle.getWidth() / 2), 
                ((this.y - this.z) * scaleInPixel) - dY + (canvasHeight/2) - particle.getHeight());
    }
    
    public double getX() {
        return this.x * scaleInPixel;
    }

    public double getY() {
        return this.y * scaleInPixel;
    }

    @Override
    public double getOrder() {
        return getY() - scaleInPixel;
    }
    
    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() > (this.startTime + particle.getAnimationLength());
    }
    
}
