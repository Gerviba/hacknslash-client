package hu.gerviba.hackandslash.client;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import hu.gerviba.hackandslash.client.gui.ingame.model.PlayerModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ImageUtil {

    public static BufferedImage scale(BufferedImage before, int imageType, int scale) {
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w * scale, h * scale, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =  new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return scaleOp.filter(before, after);
    }
    
    public static Image loadImage(String textureName, int scale) {
        try {
            BufferedImage image = ImageIO.read(PlayerModel.class.getResource(textureName));
            BufferedImage rescaled = ImageUtil.scale(image, image.getType(), scale);
            return SwingFXUtils.toFXImage(rescaled, null);
        } catch (Exception e) {
            log.error("Failed to load image: " + textureName);
        }
        return null;
    }
    
    public static Image loadImage(String textureName) {
        return new Image(textureName);
    }

}
