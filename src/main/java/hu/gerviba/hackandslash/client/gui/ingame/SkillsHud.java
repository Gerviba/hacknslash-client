package hu.gerviba.hackandslash.client.gui.ingame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hackandslash.client.gui.ingame.item.ItemInstance;
import hu.gerviba.hackandslash.client.gui.ingame.item.ItemType;
import hu.gerviba.hackandslash.client.gui.ingame.item.Items;
import hu.gerviba.hackandslash.client.packets.SkillPacket;
import hu.gerviba.hackandslash.client.skills.FlameCircleSkill;
import hu.gerviba.hackandslash.client.skills.HealthPotion;
import hu.gerviba.hackandslash.client.skills.ManaPotion;
import hu.gerviba.hackandslash.client.skills.PoisonedAreaSkill;
import hu.gerviba.hackandslash.client.skills.PurpleMagicSkill;
import hu.gerviba.hackandslash.client.skills.Skill;
import hu.gerviba.hackandslash.client.skills.ThunderSkill;
import hu.gerviba.hackandslash.client.skills.WaterBeamSkill;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkillsHud implements CustomComponent {

    private static final int ITEM_SIZE = 64;

    static final int ITEM_COUNT = 7;
    
    private AnchorPane skillsWrapper;
    private Canvas[] itemCanvases;
    private Pane[] itemBackgrounds;
    private ItemType[] itemsInMenu;
    
    private final IngameWindow ingame;
    private final Map<Integer, Skill> skills = new HashMap<>();
    private final Map<String, Integer> mapper = new HashMap<>();
    
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
        itemsInMenu = new ItemType[ITEM_COUNT];
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
        
        this.skills.put(1, new PoisonedAreaSkill(1, 0, 4000));
        this.skills.put(2, new WaterBeamSkill(2, 0, 1500));
        this.skills.put(3, new PurpleMagicSkill(3, 0, 2000));
        this.skills.put(4, new FlameCircleSkill(4, 0, 3000));
        this.skills.put(5, new ThunderSkill(5, 0, 3000));
        this.skills.put(101, new HealthPotion(101, 0, 1000));
        this.skills.put(102, new HealthPotion(102, 0, 2500));
        this.skills.put(103, new HealthPotion(103, 0, 10000));
        this.skills.put(201, new ManaPotion(201, 0, 1000));
        this.skills.put(202, new ManaPotion(202, 0, 2500));
        this.skills.put(203, new ManaPotion(203, 0, 10000));
        
        for (int i = 1; i <= 7; ++i)
            mapper.put("F" + i, i - 1);
        
        return skillsWrapper;
    }

    private void setItem(int slot, int x, int y, int size) {
        itemCanvases[slot].getGraphicsContext2D().clearRect(0, 0, ITEM_SIZE, ITEM_SIZE);
        itemCanvases[slot].getGraphicsContext2D().drawImage(Items.TEXTURE, 
                x * size, y * size, 
                size, size, 
                0, 0, 
                size, size);
    }

    public void handleSkill(int slotUsed) {
        if (itemsInMenu[slotUsed] == null)
            return;
        int skillUid = itemsInMenu[slotUsed].getSkillUid();
        if (skillUid < 1)
            return;
        if (skills.containsKey(skillUid) && skills.get(skillUid).canCast()) {
            skills.get(skillUid).send();
            reloadTimer(itemBackgrounds[slotUsed], skills.get(skillUid));
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

    public void applySkill(byte[] o) {
        try {
            SkillPacket packet = HacknslashApplication.JSON_MAPPER.readValue(o, SkillPacket.class);
            skills.get(packet.getSkillUid()).cast(packet.getX(), packet.getY(), packet.getDirection(), ingame);
        } catch (IOException e) {
            log.error("Failed to cast skill", e);
        }
    }

    public void setItem(String key, ItemInstance item) {
        if (mapper.containsKey(key)) {
            if (item != null) {
                setItem(mapper.get(key), item.getType().getTextureX(), item.getType().getTextureY(), ITEM_SIZE);
                itemsInMenu[mapper.get(key)] = item.getType();
            } else {
                itemCanvases[mapper.get(key)].getGraphicsContext2D().clearRect(0, 0, ITEM_SIZE, ITEM_SIZE);
                itemsInMenu[mapper.get(key)] = null;
            }
            return;
        }
    }

}
