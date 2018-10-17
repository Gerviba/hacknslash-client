package hu.gerviba.hackandslash.client.gui.ingame.model;

import javafx.scene.image.Image;
import lombok.Data;

@Data
public class PlayerModel {

    public static final int[] TEXTURE_STAND = new int[] {0, 1, 2};
    public static final int[] TEXTURE_LEFT = new int[] {3, 4, 5};
    public static final int[] TEXTURE_RIGHT = new int[] {6, 7, 8};
    public static final int[] TEXTURE_BACK = new int[] {9, 10, 11};
    
    private double x;
    private double y;
    
    private Image[] texture;
    
    public PlayerModel(String texture) {
        this.texture = new Image[16];
    }
    
}
