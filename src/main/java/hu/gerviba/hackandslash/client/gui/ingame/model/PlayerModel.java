package hu.gerviba.hackandslash.client.gui.ingame.model;

import hu.gerviba.hackandslash.client.gui.ingame.item.Items;
import hu.gerviba.hackandslash.client.packets.TelemetryPacket;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.Setter;

/**
 * Player model
 * @author Gergely Szabó
 */
@Data
public class PlayerModel implements RenderableModel {

    public static final int[][] TEXTURE_STAND = new int[][] {
        new int[] {0, 0}, 
        new int[] {1, 0}, 
        new int[] {2, 0}, 
        new int[] {1, 0}};
    public static final int[][] TEXTURE_LEFT = new int[][] {
        new int[] {0, 1}, 
        new int[] {1, 1},
        new int[] {2, 1},
        new int[] {1, 1}};
    public static final int[][] TEXTURE_RIGHT = new int[][] {
        new int[] {0, 2}, 
        new int[] {1, 2}, 
        new int[] {2, 2},
        new int[] {1, 2}};
    public static final int[][] TEXTURE_BACK = new int[][] {
        new int[] {0, 3}, 
        new int[] {1, 3}, 
        new int[] {2, 3},
        new int[] {1, 3}};
    
    public static final int[][][] PLAYER_TEXTURE = new int[][][] {
        TEXTURE_STAND,
        TEXTURE_LEFT,
        TEXTURE_RIGHT,
        TEXTURE_BACK
    };
    
    public static int DIRECTION_STAND = 0;
    public static int DIRECTION_LEFT = 1;
    public static int DIRECTION_RIGHT = 2;
    public static int DIRECTION_BACK = 3;
    
    private static final Color BLACK_COLOR = new Color(0, 0, 0, 1);
    private static final Color TRANSPARENT_BLACK_COLOR = new Color(0, 0, 0, 0.5);
    private static final Color WHITE_COLOR = new Color(1, 1, 1, 1);
    private static final Color RED_COLOR = new Color(1, 0, 0, 1);
    
    private final long entityId;
    private final String name;
    
    private volatile double x;
    private volatile double y;
    private volatile int scale;
    private volatile int direction;
    private volatile boolean walking;
    private volatile float hp;

    private volatile String base = "null";
    private volatile String weapon = "null";
    private volatile String helmet = "null";
    private volatile String armor = "null";
    private volatile String boots = "null";
    
    private double targetX;
    private double targetY;
    private int canvasWidth;
    private int canvasHeight;
    
    @Setter
    private Image texture;
    
    public PlayerModel(long entityId, String name, int scale, Image texture, int width, int height) {
        this.entityId = entityId;
        this.scale = scale * 32;
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.name = name;
        this.texture = texture;
    }
    
    /**
     * Entity id of this component
     */
    @Override
    public long getId() {
        return entityId;
    }

    /**
     * Render method of player this model
     */
    @Override
    public void draw(GraphicsContext midGc, GraphicsContext topGc, double time, double dX, double dY) {
        renderModel(midGc, time, dX, dY);
        renderName(topGc, dX, dY);
        renderHPBar(topGc, dX, dY);
    }
    
    /**
     * Render the model with the action direction
     * @param gc GraphicContext of the canvas
     * @param time Current time in millis
     * @param dX X coordinate
     * @param dY Y coordinate
     */
    private void renderModel(GraphicsContext gc, double time, double dX, double dY) {
        int state = (int) (((long) (time * 6)) % 4);
        gc.drawImage(texture, 
                scale * PLAYER_TEXTURE[direction][(walking ? state : 1)][0], 
                scale * PLAYER_TEXTURE[direction][(walking ? state : 1)][1], 
                scale, scale, 
                this.x - dX  + (canvasWidth / 2) - (scale / 2), 
                this.y - dY  + (canvasHeight / 2) - scale, 
                scale, scale);
    }
    
    /**
     * Render the name of the player, above the model
     * @param gc GraphicContext of the canvas
     * @param dX X coordinate
     * @param dY Y coordinate
     */
    private void renderName(GraphicsContext gc, double dX, double dY) {
        gc.setStroke(BLACK_COLOR);
        gc.setFill(WHITE_COLOR);
        gc.strokeText(name, 
                this.x - dX  + (canvasWidth / 2) + (scale / 2) - (scale / 2), 
                this.y - dY  + (canvasHeight / 2) - 12 - scale);
        gc.fillText(name, 
                this.x - dX  + (canvasWidth / 2) + (scale / 2) - (scale / 2), 
                this.y - dY  + (canvasHeight / 2) - 12 - scale);
    }
    
    /**
     * Render the HP bar, above the model
     * @param gc GraphicContext of the canvas
     * @param dX X coordinate
     * @param dY Y coordinate
     */
    private void renderHPBar(GraphicsContext gc, double dX, double dY) {
        gc.setFill(TRANSPARENT_BLACK_COLOR);
        gc.fillRect(
                this.x - dX  + (canvasWidth / 2) + (scale * 0.125) - 1 - (scale / 2), 
                this.y - dY  + (canvasHeight / 2) - 10- scale,
                scale * 0.75 + 2, 6);
        
        gc.setFill(RED_COLOR);
        gc.fillRect(
                this.x - dX  + (canvasWidth / 2) + (scale * 0.125) - (scale / 2), 
                this.y - dY  + (canvasHeight / 2) - 9 - scale,
                scale * 0.75 * hp, 4);
    }
    
    /**
     * Update player status
     * @param ms PlayerModelStatus received from the telemetry packet
     */
    public void update(TelemetryPacket.PlayerModelStatus pms) {
        this.targetX = (long) pms.getX();
        if (Math.abs(this.targetX - this.x) > 10)
            this.x = this.targetX;
        this.targetY = (long) pms.getY();
        if (Math.abs(this.targetY - this.y) > 10)
            this.y = this.targetY;
        this.direction = pms.getDirection();
        this.walking = pms.isWalking();
        this.hp = pms.getHp();

        if (!this.weapon.equals(pms.getWeapon())
                || !this.helmet.equals(pms.getHelmet())
                || !this.armor.equals(pms.getArmor())
                || !this.boots.equals(pms.getBoots())
                || !this.base.equals(pms.getBase())) {
            
            this.weapon = pms.getWeapon();
            this.helmet = pms.getHelmet();
            this.armor = pms.getArmor();
            this.boots = pms.getBoots();
            this.base = pms.getBase();
            this.texture = Items.getImageByComponents(base, weapon, helmet, armor, boots);
        }
    }
    
    /**
     * Do the logic of the movement
     */
    @Override
    public void calc() {
        if (this.x != this.targetX || this.y != this.targetY) {
            double dX = Math.abs(this.targetX - this.x);
            double dY = Math.abs(this.targetY - this.y);
            double c = Math.sqrt(dX*dX + dY*dY);
            this.x += Math.ceil(Math.cos(dX / c)) * Math.signum(this.targetX - this.x);
            this.y += Math.ceil(Math.sin(dY / c)) * Math.signum(this.targetY - this.y);
        }
    }
    
    /**
     * Rendering order getter
     */
    @Override
    public double getOrder() {
        return y - scale - 2;
    }

    /**
     * Finished getter. Always return false. 
     * This method used in the custom entity garbage collector.
     */
    @Override
    public boolean isFinished() {
        return false;
    }
    
    /**
     * X coordinate setter
     * @param x X coordinate
     */
    public void setX(double x) {
        this.x = x;
        this.targetX = x;
    }

    /**
     * Y coordinate setter
     * @param y Y coordinate
     */
    public void setY(double y) {
        this.y = y;
        this.targetY = y;
    }
    
    /**
     * X coordinate getter
     * @param x General X coordinate
     */
    public double getGeneralX() {
        return this.x / scale;
    }
    
    /**
     * Y coordinate getter
     * @param y General Y coordinate
     */
    public double getGeneralY() {
        return this.y / scale;
    }
    
}
