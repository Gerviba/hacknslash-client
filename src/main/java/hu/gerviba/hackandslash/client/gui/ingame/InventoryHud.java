package hu.gerviba.hackandslash.client.gui.ingame;

import java.util.Arrays;
import java.util.List;

import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hackandslash.client.gui.ingame.item.ItemInstance;
import hu.gerviba.hackandslash.client.gui.ingame.item.ItemType;
import hu.gerviba.hackandslash.client.gui.ingame.item.Items;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryHud implements CustomComponent {
    
    @RequiredArgsConstructor
    class ItemComponent implements CustomComponent {
        
        @Getter
        private int count;
        
        @Getter
        private final String note;
        
        @Getter
        private final List<String> allowedTypes;
        
        private Text noteText;
        private Text countText;
        private Canvas canvas;
        private ItemInstance item = null;
        
        public ItemComponent() {
            this.note = "";
            this.allowedTypes = Items.ALL_TYPES;
        }
        
        public void setItem(ItemInstance item) {
            this.item = item;
            canvas.getGraphicsContext2D().clearRect(0, 0, ITEM_SIZE, ITEM_SIZE);
            ingame.getSkillsComponent().setItem(note, item);
            
            if (item == null) {
                countText.setText("");
                noteText.setText(note);
                return;
            }
            
            noteText.setText("");
            canvas.getGraphicsContext2D().drawImage(Items.TEXTURE, 
                    item.getType().getTextureX() * ITEM_SIZE,
                    item.getType().getTextureY() * ITEM_SIZE, 
                    ITEM_SIZE, ITEM_SIZE, 
                    0, 0, 
                    ITEM_SIZE, ITEM_SIZE);
            if (item.getCount() > 1)
                countText.setText(item.getCount() + "");
        }
        
        public void switchItems(ItemComponent other) {
            if (!(other.item == null || this.allowedTypes.contains(other.item.getType().getType())) ||
                    !(this.item == null || other.allowedTypes.contains(this.item.getType().getType())))
                return;
            
            ItemInstance temp = this.item;
            this.item = other.item;
            other.setItem(temp);
            this.setItem(this.item);
        }
        
        @Override
        public Pane toPane() {
            StackPane pane = new StackPane();
            pane.setUserData(this);
            addMoveEvents(pane);
            addDescBoxEvents(pane);
            pane.getStyleClass().add("item-comp");
            pane.setPrefHeight(ITEM_SIZE + 8);
            pane.setPrefWidth(ITEM_SIZE + 8);

            noteText = new Text("" + note);
            noteText.setUserData(this);
            noteText.getStyleClass().add("note-text");
            pane.getChildren().add(noteText);
            StackPane.setAlignment(noteText, Pos.CENTER);
            
            canvas = new Canvas(ITEM_SIZE, ITEM_SIZE);
            canvas.setUserData(this);
            pane.getChildren().add(canvas);
            
            countText = new Text();
            countText.setUserData(this);
            countText.getStyleClass().add("count-text");
            pane.getChildren().add(countText);
            StackPane.setAlignment(countText, Pos.BOTTOM_RIGHT);
            
            return pane;
        }
        
        private void addMoveEvents(StackPane pane) {
            pane.setOnMouseDragged(event -> {
                toDrag = ItemComponent.this;
                descriptionBox.setVisible(false);
            });
            pane.setOnMouseReleased(event -> {
                if (event.getPickResult().getIntersectedNode().getUserData() instanceof ItemComponent) {
                    ((ItemComponent) event.getPickResult().getIntersectedNode().getUserData()).switchItems(this);
                    descriptionBox.setTranslateX(event.getSceneX() + 30);
                    descriptionBox.setTranslateY(event.getSceneY());
                }
            });
        }

        private void addDescBoxEvents(StackPane pane) {
            pane.setOnMouseEntered(event -> {
                if (item == null)
                    return;
                descriptionTitle.setText(item.getType().getName());
                descriptionBox.getChildren().remove(descriptionLore);
                if (!item.getType().getLore().equals("")) {
                    descriptionLore.setText(item.getType().getLore().replace("|", "\n"));
                    descriptionBox.add(descriptionLore, 0, 1);
                }
                descriptionBox.setTranslateX(event.getSceneX() + 30);
                descriptionBox.setTranslateY(event.getSceneY());
                descriptionBox.setVisible(true);
            });
            pane.setOnMouseExited(event -> descriptionBox.setVisible(false));
            pane.setOnMouseMoved(event -> {
                descriptionBox.setTranslateX(event.getSceneX() + 30);
                descriptionBox.setTranslateY(event.getSceneY());
            });
        }
        
    }
    
    static final int MAX_SKILLS = 7;
    static final int MAX_ITEMS = 4 * 12;
    static final int ITEM_SIZE = 64;
    final IngameWindow ingame;
    
    ItemComponent helmet;
    ItemComponent armor;
    ItemComponent boots;
    ItemComponent weapon;
    ItemComponent ring;
    
    ItemComponent[] skills;
    ItemComponent[] items;
    
    ItemComponent toDrag;
    AnchorPane inventoryWrapper;
    
    StackPane highestLayer;
    GridPane descriptionBox = null;
    Text descriptionTitle;
    Text descriptionLore;
    
    @Override
    public Pane toPane() {
        inventoryWrapper = new AnchorPane();
        inventoryWrapper.getStyleClass().add("inventory");
        inventoryWrapper.setMaxWidth(((ITEM_SIZE + 8) * 7) + (8 * 6) + 10);
        inventoryWrapper.setMaxHeight(((ITEM_SIZE + 8) * 6) + (8 * 5));
        AnchorPane.setBottomAnchor(inventoryWrapper, 0.0);
        AnchorPane.setLeftAnchor(inventoryWrapper, 0.0);
        AnchorPane.setRightAnchor(inventoryWrapper, 0.0);
        AnchorPane.setTopAnchor(inventoryWrapper, 0.0);

        GridPane inventory = new GridPane();
        inventory.setHgap(8);
        inventory.setVgap(8);
        inventoryWrapper.getChildren().add(inventory);
        
        GridPane wearWrapper = initWear();
        inventory.add(wearWrapper, 0, 0);
        
        GridPane skillsWrapper = initSkills();
        inventory.add(skillsWrapper, 0, 1);
        
        ScrollPane itemsWrapper = initItems();
        inventory.add(itemsWrapper, 1, 0, 1, 2);
        
        close();
        
        return inventoryWrapper;
    }

    private GridPane initWear() {
        GridPane wearWrapper = new GridPane();
        wearWrapper.getStyleClass().add("wear");

        wearWrapper.setHgap(8);
        wearWrapper.setVgap(8);
        
        wearWrapper.add((helmet = new ItemComponent("HELMET", Arrays.asList("helmet"))).toPane(), 1, 0);
        wearWrapper.add((armor = new ItemComponent("WEAPON", Arrays.asList("weapon"))).toPane(), 0, 1);
        wearWrapper.add((boots = new ItemComponent("ARMOR", Arrays.asList("armor"))).toPane(), 1, 1);
        wearWrapper.add((weapon = new ItemComponent("RING", Arrays.asList("ring"))).toPane(), 2, 1);
        wearWrapper.add((ring = new ItemComponent("BOOTS", Arrays.asList("boots"))).toPane(), 1, 2);
        return wearWrapper;
    }
    
    private GridPane initSkills() {
        GridPane skillsWrapper = new GridPane();
        skillsWrapper.getStyleClass().add("skill");

        skillsWrapper.setHgap(8);
        skillsWrapper.setVgap(8);
        
        skills = new ItemComponent[MAX_SKILLS];
        for (int i = 0; i < MAX_SKILLS; ++i)
            skillsWrapper.add((skills[i] = new ItemComponent("F" + (i + 1), 
                    Arrays.asList("skill", "potion", "weapon"))).toPane(), i % 3, i / 3);
        
        return skillsWrapper;
    }
    
    private ScrollPane initItems() {
        ScrollPane itemsWrapper = new ScrollPane();
        GridPane itemsStorage = new GridPane();
        itemsWrapper.setMaxHeight(((ITEM_SIZE + 8) * 6) + (8 * 5));
        itemsWrapper.setContent(itemsStorage);
        itemsWrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        itemsWrapper.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        itemsWrapper.getStyleClass().add("items-scroll");
        
        itemsStorage.setHgap(8);
        itemsStorage.setVgap(8);
        itemsStorage.getStyleClass().add("items-storage");
        
        items = new ItemComponent[MAX_ITEMS];
        for (int i = 0; i < MAX_ITEMS; ++i)
            itemsStorage.add((items[i] = new ItemComponent()).toPane(), i % 4, i / 4);
        
        int i = 0;
        for (ItemType it : Items.ALL)
            items[i++].setItem(new ItemInstance(it, 1));
        
        return itemsWrapper;
    }
    
    public void show() {
        inventoryWrapper.setDisable(false);
        inventoryWrapper.setVisible(true);
    }
    
    public void close() {
        inventoryWrapper.setDisable(true);
        inventoryWrapper.setVisible(false);
        if (descriptionBox != null)
            descriptionBox.setVisible(false);
    }
    
    public void setHighestLayer(StackPane highest) {
        this.highestLayer = highest;
        
        descriptionBox = new GridPane();
        descriptionBox.setMaxWidth(80);
        descriptionBox.setMaxHeight(200);
        descriptionBox.getStyleClass().add("description-box");
        descriptionTitle = new Text("Test name");
        descriptionTitle.getStyleClass().add("title");
        descriptionBox.add(descriptionTitle, 0, 0);
        descriptionBox.setVgap(10);
        
        descriptionLore = new Text();
        descriptionLore.getStyleClass().add("text");
        descriptionBox.add(descriptionLore, 0, 1);
        
        descriptionBox.setVisible(false);
        highestLayer.setDisable(true);
        
        this.highestLayer.getChildren().add(descriptionBox);
    }
    
}
