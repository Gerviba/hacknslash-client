package hu.gerviba.hackandslash.client;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ImageUtil {

    public static BufferedImage scale(BufferedImage before, int scale) {
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
            BufferedImage image = ImageIO.read(ImageUtil.class.getResource(textureName));
            BufferedImage rescaled = ImageUtil.scale(image, scale);
            return SwingFXUtils.toFXImage(rescaled, null);
        } catch (Exception e) {
            log.error("Failed to load image: " + textureName, e);
        }
        return null;
    }
    
    public static Image loadImage(String textureName) {
        return new Image(textureName);
    }

    public static Image loadImage(String base, String weapon, String helmet, String armor, String boots, int scale) {
        try {
            BufferedImage baseImg = ImageIO.read(ImageUtil.class.getResource(
                    "/assets/characters/" + base + "_base.png"));
            BufferedImage result = new BufferedImage(
                    baseImg.getWidth(),
                    baseImg.getHeight(), 
                    BufferedImage.TYPE_INT_ARGB);
            
            BufferedImage newLeyer;
            Graphics graphic = result.getGraphics();
            
            if (weapon != null && !weapon.equals("null")) {
                newLeyer = ImageIO.read(ImageUtil.class.getResource(
                        "/assets/characters/" + base + "_weaponbg_" + weapon + ".png"));
                graphic.drawImage(newLeyer, 0, 0, null);
            }
            
            graphic.drawImage(baseImg, 0, 0, null);
            
            if (helmet != null && !helmet.equals("null")) {
                newLeyer = ImageIO.read(ImageUtil.class.getResource(
                        "/assets/characters/" + base + "_helmet_" + helmet + ".png"));
                graphic.drawImage(newLeyer, 0, 0, null);
            }

            if (boots != null && !boots.equals("null")) {
                newLeyer = ImageIO.read(ImageUtil.class.getResource(
                        "/assets/characters/" + base + "_boots_" + boots + ".png"));
                graphic.drawImage(newLeyer, 0, 0, null);
            }

            if (armor != null && !armor.equals("null")) {
                newLeyer = ImageIO.read(ImageUtil.class.getResource(
                        "/assets/characters/" + base + "_armor_" + armor + ".png"));
                graphic.drawImage(newLeyer, 0, 0, null);
            }

            if (weapon != null && !weapon.equals("null")) {
                newLeyer = ImageIO.read(ImageUtil.class.getResource(
                        "/assets/characters/" + base + "_weapon_" + weapon + ".png"));
                graphic.drawImage(newLeyer, 0, 0, null);
            }
            
            return SwingFXUtils.toFXImage(scale(result, scale), null);
        } catch (IOException e) {
            log.error("Failed to load character skin part" , e);
        }
        return null;
    }

}
