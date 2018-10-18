package hu.gerviba.hackandslash.client.gui.ingame.model;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import hu.gerviba.hackandslash.packets.TelemetryPacket;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Data;

@Data
public class PlayerModel {

    public static final int[][] TEXTURE_STAND = new int[][] {new int[] {0, 0}, new int[] {1, 0}, new int[] {2, 0}, new int[] {1, 0}};
    public static final int[][] TEXTURE_LEFT = new int[][] {new int[] {0, 1}, new int[] {1, 1}, new int[] {2, 1}, new int[] {1, 1}};
    public static final int[][] TEXTURE_RIGHT = new int[][] {new int[] {0, 2}, new int[] {1, 2}, new int[] {2, 2}, new int[] {1, 2}};
    public static final int[][] TEXTURE_BACK = new int[][] {new int[] {0, 3}, new int[] {1, 3}, new int[] {2, 3}, new int[] {1, 3}};
    
    public static final int[][][] PLAYER_TEXTURE = new int[][][] {
        TEXTURE_STAND,
        TEXTURE_LEFT,
        TEXTURE_RIGHT,
        TEXTURE_BACK
    };
    
    private final long entityId;
    
    private volatile double x;
    private volatile double y;
    private volatile int scale;
    private volatile int direction;
    private volatile boolean walking = true;
    
    private double targetX;
    private double targetY;
    
    private Image texture;
    
    public PlayerModel(long entityId, int scale, String texture) throws IOException {
        this.entityId = entityId;
        this.scale = scale * 32;

        BufferedImage image = ImageIO.read(PlayerModel.class.getResource("/assets/characters/" + texture + ".png"));
        BufferedImage rescaled = scale(image, image.getType(), scale);
        
        this.texture = SwingFXUtils.toFXImage(rescaled, null);
    }
    
    public void draw(GraphicsContext gc, double time) {
        int state = (int) (((long) (time * 6)) % 4);
        gc.drawImage(texture, 
                scale * PLAYER_TEXTURE[direction][(walking ? state : 1)][0], 
                scale * PLAYER_TEXTURE[direction][(walking ? state : 1)][1], 
                scale, scale, x, y, scale, scale);
    }
    
    private static double EPSILON = 2;
    
    public void update(TelemetryPacket.PlayerModelStatus pms) {
        this.targetX = (long) pms.getX();
        this.targetY = (long) pms.getY();
        
//        if (Math.abs(this.targetX - this.x) < EPSILON)
//            this.x = this.targetX;
//        if (Math.abs(this.targetY - this.y) < EPSILON)
//            this.y = this.targetY;
                    
        this.direction = pms.getDirection();
        this.walking = pms.isWalking();
    }
    
    public void calc() {
        if (this.x != this.targetX || this.y != this.targetY) {
            double speed = 1.8;
            double dX = Math.abs(this.targetX - this.x);
            double dY = Math.abs(this.targetY - this.y);
            double c = Math.sqrt(dX*dX + dY*dY);
            this.x += Math.ceil(Math.cos(dX / c)) * Math.signum(this.targetX - this.x);
            this.y += Math.ceil(Math.sin(dY / c)) * Math.signum(this.targetY - this.y);
        }
    }
    
    public void setX(double x) {
        this.x = x;
        this.targetX = x;
    }
    
    public void setY(double y) {
        this.y = y;
        this.targetY = y;
    }
    
    public static BufferedImage scale(BufferedImage before, int imageType, int scale) {
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w * scale, h * scale, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =  new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return scaleOp.filter(before, after);
    }
    
}