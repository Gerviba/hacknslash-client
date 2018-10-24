package hu.gerviba.hackandslash.client.gui.ingame;

import java.util.HashMap;
import java.util.Map;

import hu.gerviba.hackandslash.client.ImageUtil;
import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hackandslash.client.skills.FlameCircleSkill;
import hu.gerviba.hackandslash.client.skills.HealthPotion;
import hu.gerviba.hackandslash.client.skills.ManaPotion;
import hu.gerviba.hackandslash.client.skills.PoisonedAreaSkill;
import hu.gerviba.hackandslash.client.skills.PurpleMagicSkill;
import hu.gerviba.hackandslash.client.skills.Skill;
import hu.gerviba.hackandslash.client.skills.WaterBeamSkill;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SkillsHud implements CustomComponent {

    private static final int ITEM_SIZE = 64;

    static final int ITEM_COUNT = 7;
    
    private AnchorPane skillsWrapper;
    private Canvas[] itemCanvases;
    private Pane[] itemBackgrounds;
    
    private final IngameWindow ingame;
    private final Map<Integer, Skill> skills = new HashMap<>();
    
    public SkillsHud(IngameWindow ingame) {
        this.ingame = ingame;
    }
    
    @Override
    public Pane toPane() {
        skillsWrapper = new AnchorPane();
        skillsWrapper.setMinWidth(584);
        skillsWrapper.setMaxWidth(584);
        GridPane skills = new GridPane();
        skills.getStyleClass().add("skills");
        skills.setMinHeight(80);
        skills.setMinWidth(584);
        skills.setMaxWidth(584);
        skills.setVgap(2);
        skills.setHgap(10);
        skills.setAlignment(Pos.CENTER);
        skillsWrapper.getChildren().add(skills);
        AnchorPane.setLeftAnchor(skills, 0.0);
        AnchorPane.setRightAnchor(skills, 0.0);
        AnchorPane.setTopAnchor(skills, 10.0);
        
        itemCanvases = new Canvas[ITEM_COUNT];
        itemBackgrounds = new Pane[ITEM_COUNT];
        for (int i = 0; i < ITEM_COUNT; ++i) {
            StackPane item = new StackPane();
            item.setPrefWidth(ITEM_SIZE + 8);
            item.setPrefHeight(ITEM_SIZE + 8);
            item.setAlignment(Pos.CENTER);
            item.getStyleClass().add("item");
            item.setClip(new Rectangle(ITEM_SIZE + 8, ITEM_SIZE + 8));
            
            itemBackgrounds[i] = new Pane();
            itemBackgrounds[i].getStyleClass().add("background");
            itemBackgrounds[i].setTranslateY(ITEM_SIZE + 8);
            item.getChildren().add(itemBackgrounds[i]);
            
            itemCanvases[i] = new Canvas(ITEM_SIZE, ITEM_SIZE);
            item.getChildren().add(itemCanvases[i]);
            skills.add(item, i, 0);

            skills.setAlignment(Pos.CENTER);
            Text label = new Text("F" + (i + 1));
            label.getStyleClass().add("label");
            GridPane.setHalignment(label, HPos.CENTER);
            skills.add(label, i, 1);
        }
        
        this.skills.put(1, new PoisonedAreaSkill(0, 4000));
        this.skills.put(2, new WaterBeamSkill(0, 1500));
        this.skills.put(3, new PurpleMagicSkill(0, 2000));
        this.skills.put(4, new FlameCircleSkill(0, 3000));
        // 5
        this.skills.put(6, new HealthPotion(0, 1500));
        this.skills.put(7, new ManaPotion(0, 1000));
        
        addItem(0, 0, 0, ITEM_SIZE);
        addItem(1, 1, 0, ITEM_SIZE);
        addItem(2, 2, 0, ITEM_SIZE);
        addItem(3, 3, 0, ITEM_SIZE);
        addItem(4, 4, 0, ITEM_SIZE);
        addItem(5, 1, 3, ITEM_SIZE);
        addItem(6, 0, 4, ITEM_SIZE);
        
        return skillsWrapper;
    }

    private void addItem(int slot, int x, int y, int size) {
        Image image = ImageUtil.loadImage("/assets/items/items.png", 2);
        itemCanvases[slot].getGraphicsContext2D().clearRect(0, 0, ITEM_SIZE, ITEM_SIZE);
        itemCanvases[slot].getGraphicsContext2D().drawImage(image, 
                x * size, y * size, 
                size, size, 
                0, 0, 
                size, size);
    }

    public void handleSkill(int skillUid) {
        if (skills.containsKey(skillUid) && skills.get(skillUid).canCast()) {
            skills.get(skillUid).cast(ingame.getMe(), ingame);
            reloadTimer(itemBackgrounds[skillUid - 1], skills.get(skillUid));
        }
    }
    
    private void reloadTimer(Pane background, Skill skill) {
        Timeline timeline = new Timeline();
       
        timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(background.translateYProperty(), 0)),
            new KeyFrame(new Duration(skill.getReloadTime()),
                new KeyValue(background.translateYProperty(), ITEM_SIZE + 8))
        );
        timeline.play();
    }

}
