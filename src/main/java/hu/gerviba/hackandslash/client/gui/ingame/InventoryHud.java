package hu.gerviba.hackandslash.client.gui.ingame;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import hu.gerviba.hackandslash.client.HacknslashApplication;
import hu.gerviba.hackandslash.client.gui.CustomComponent;
import hu.gerviba.hackandslash.client.gui.ingame.item.ItemCategory;
import hu.gerviba.hackandslash.client.gui.ingame.item.ItemInstance;
import hu.gerviba.hackandslash.client.gui.ingame.item.Items;
import hu.gerviba.hackandslash.client.packets.ItemChangePacket;
import hu.gerviba.hackandslash.client.packets.ItemChangePacket.ChangeMethod;
import hu.gerviba.hackandslash.client.packets.TemplatePacketBuilder;
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
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InventoryHud implements CustomComponent {
    
    @RequiredArgsConstructor
    @ToString
    class ItemComponent implements CustomComponent {
        
        @Getter
        private final int id;
        
        @Getter
        private int count;
        
        @Getter
        private final String note;
        
        @Getter
        private final List<ItemCategory> allowedTypes;
        
        private Text noteText;
        private Text countText;
        private Canvas canvas;
        private ItemInstance item = null;
        
        public ItemComponent(int id) {
            this.id = id;
            this.note = "";
            this.allowedTypes = Arrays.asList(ItemCategory.values());
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
            
            HacknslashApplication.getInstance().getConnection().appendTask(stomp -> {
                stomp.send("/app/switch-item", TemplatePacketBuilder
                        .buildChangeItem(this.getId(), other.getId()));
            });
            
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
                //TODO: Cehck null
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
    
    ItemComponent weapon;
    ItemComponent ring;
    ItemComponent helmet;
    ItemComponent armor;
    ItemComponent boots;
    
    ItemComponent[] skills;
    ItemComponent[] items;
    
    private Map<Integer, Supplier<ItemComponent>> ITEM_COMPONENT_MAPPER = new HashMap<>();
    
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
        
        initMapper();
        
        close();
        
        return inventoryWrapper;
    }

    private void initMapper() {
        ITEM_COMPONENT_MAPPER.put(0, () -> weapon);
        ITEM_COMPONENT_MAPPER.put(1, () -> ring);
        ITEM_COMPONENT_MAPPER.put(2, () -> helmet);
        ITEM_COMPONENT_MAPPER.put(3, () -> armor);
        ITEM_COMPONENT_MAPPER.put(4, () -> boots);

        for (int i = 0; i < MAX_SKILLS; ++i) {
            final int iInstance = i;
            ITEM_COMPONENT_MAPPER.put(i + 10, () -> skills[iInstance]);
        }

        for (int i = 0; i < 40; ++i) {
            final int iInstance = i;
            ITEM_COMPONENT_MAPPER.put(i + 22, () -> items[iInstance]);
        }
        
        ITEM_COMPONENT_MAPPER.put(-1, () -> {
            for (ItemComponent ic : items)
                if (ic.item == null)
                    return ic;
            return null;
        });
    }

    private GridPane initWear() {
        GridPane wearWrapper = new GridPane();
        wearWrapper.getStyleClass().add("wear");

        wearWrapper.setHgap(8);
        wearWrapper.setVgap(8);
        
        wearWrapper.add((weapon = new ItemComponent(0, "WEAPON", Arrays.asList(ItemCategory.WEAPON))).toPane(), 0, 1);
        wearWrapper.add((ring = new ItemComponent(1, "RING", Arrays.asList(ItemCategory.RING))).toPane(), 2, 1);
        wearWrapper.add((helmet = new ItemComponent(2, "HELMET", Arrays.asList(ItemCategory.HELMET))).toPane(), 1, 0);
        wearWrapper.add((armor = new ItemComponent(3, "ARMOR", Arrays.asList(ItemCategory.ARMOR))).toPane(), 1, 1);
        wearWrapper.add((boots = new ItemComponent(4, "BOOTS", Arrays.asList(ItemCategory.BOOTS))).toPane(), 1, 2);
        return wearWrapper;
    }
    
    private GridPane initSkills() {
        GridPane skillsWrapper = new GridPane();
        skillsWrapper.getStyleClass().add("skill");

        skillsWrapper.setHgap(8);
        skillsWrapper.setVgap(8);
        
        skills = new ItemComponent[MAX_SKILLS];
        for (int i = 0; i < MAX_SKILLS; ++i)
            skillsWrapper.add((skills[i] = new ItemComponent(i + 10, "F" + (i + 1), 
                    Arrays.asList(ItemCategory.SKILL, ItemCategory.POTION, ItemCategory.WEAPON))).toPane(), 
                    i % 3, i / 3);
        
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
            itemsStorage.add((items[i] = new ItemComponent(i + 22)).toPane(), i % 4, i / 4);
        
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
    
    public void update(byte[] o) {
        ItemChangePacket packet;
        try {
            packet = HacknslashApplication.JSON_MAPPER.readValue(o, ItemChangePacket.class);
            if (packet.getMethod() == ChangeMethod.OVERRIDE) {
                helmet.setItem(null);
                armor.setItem(null);
                boots.setItem(null);
                weapon.setItem(null);
                ring.setItem(null);
                
                for (ItemComponent ic : skills)
                    ic.setItem(null);
                for (ItemComponent ic : items)
                    ic.setItem(null);
            }
            
            packet.getChanges().forEach(change -> {
                Supplier<ItemComponent> sup = ITEM_COMPONENT_MAPPER.get(change.getSlot());
                if (sup == null) {
                    log.error("The inventory is already full");
                    return;
                }
                sup.get().setItem(new ItemInstance(Items.getItem(change.getItemId()), change.getCount()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
