package hu.gerviba.hackandslash.client.gui.ingame.item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hu.gerviba.hackandslash.client.ImageUtil;
import javafx.scene.image.Image;

/**
 * Item storage and utility functions
 * @author Gergely Szabó
 */
public class Items {

    public static final Image TEXTURE = ImageUtil.loadImage("/assets/items/items.png", 2);

    public static final ItemType SKILL_POISON = new ItemType(1, 0, 0, 1, 
            "Poison Skill", ItemCategory.SKILL, 
            "Release a toxic gas cloud", 
            -1, -1);
    public static final ItemType SKILL_WEATER_BEAM = new ItemType(2, 1, 0, 2, 
            "Water Beam Skill", ItemCategory.SKILL, 
            "Shoot a water beam", 
            -1, -1);
    public static final ItemType SKILL_PURLE_MAGIC = new ItemType(3, 2, 0, 3, 
            "Purple Magic Skill", ItemCategory.SKILL, 
            "Shoot a purple magic beam",
            -1, -1);
    public static final ItemType SKILL_FLAME_CIRCLE = new ItemType(4, 3, 0, 4, 
            "Flame Circle Skill", ItemCategory.SKILL, 
            "Fire a flame crircle", 
            -1, -1);
    public static final ItemType SKILL_STORM = new ItemType(5, 4, 0, 5, 
            "Storm Skill", ItemCategory.SKILL, 
            "Release a toxic gas cloud",
            -1, -1);
    
    public static final ItemType WEAPON_BLUE_MAGIC_WAND = new ItemType(6, 0, 2, -1, 
            "Plancheite Magic Wand", ItemCategory.WEAPON, "", 
            100, 50);
    public static final ItemType WEAPON_GOLD_MAGIC_WAND = new ItemType(7, 1, 2, -2, 
            "Gold Magic Wand", ItemCategory.WEAPON, "", 
            300, 150);
    public static final ItemType WEAPON_BLUE_SWORD = new ItemType(8, 2, 2, -3, 
            "Plancheite Sword", ItemCategory.WEAPON, "", 
            100, 50);
    public static final ItemType WEAPON_GOLD_SWORD = new ItemType(9, 3, 2, -4, 
            "Gold Sword", ItemCategory.WEAPON, "", 
            300, 150);
    
    public static final ItemType POTION_HEALTH_1 = new ItemType(101, 0, 3, 101, 
            "Health Potion I", ItemCategory.POTION, 
            "Effect: +20 HP|"
            + "Reload: 1 sec", 
            -1, -1);
    public static final ItemType POTION_HEALTH_2 = new ItemType(102, 1, 3, 102, 
            "Health Potion II", ItemCategory.POTION, 
            "Effect: +50 HP|"
            + "Reload: 2.5 sec", 
            -1, -1);
    public static final ItemType POTION_HEALTH_3 = new ItemType(103, 2, 3, 103, 
            "Health Potion III", ItemCategory.POTION, 
            "Effect: +500 HP|"
            + "Reload: 10 sec", 
            -1, -1);

    public static final ItemType POTION_MANA_1 = new ItemType(201, 0, 4, 201, 
            "Mana Potion I", ItemCategory.POTION, 
            "Effect: +20 Mana|"
            + "Reload: 1 sec", 
            -1, -1);
    public static final ItemType POTION_MANA_2 = new ItemType(202, 1, 4, 202, 
            "Mana Potion II", ItemCategory.POTION, 
            "Effect: +50 Mana|"
            + "Reload: 2.5 sec", 
            -1, -1);
    public static final ItemType POTION_MANA_3 = new ItemType(203, 2, 4, 203, 
            "Mana Potion III", ItemCategory.POTION, 
            "Effect: +500 Mana|"
            + "Reload: 10 sec", 
            -1, -1);
    
    public static final ItemType RING_SILVER = new ItemType(10, 0, 5, 0, 
            "Silver Ring", ItemCategory.RING, "", 
            -1, -1);
    public static final ItemType RING_CARBON = new ItemType(11, 1, 5, 0, 
            "Plancheite Ring", ItemCategory.RING, "", 
            -1, -1);
    public static final ItemType RING_GOLDEN = new ItemType(12, 2, 5, 0, 
            "Golden Ring", ItemCategory.RING, "", 
            -1, -1);
    public static final ItemType RING_RUBY = new ItemType(13, 3, 5, 0, 
            "Ruby Ring", ItemCategory.RING, "", 
            -1, -1);

    public static final ItemType THING_BONE = new ItemType(14, 0, 6, 0, 
            "Bone", ItemCategory.THING, "", 
            -1, -1);
    public static final ItemType THING_EYE = new ItemType(15, 1, 6, 0, 
            "Eye", ItemCategory.THING, "", 
            -1, -1);
    public static final ItemType THING_COPPER_SULPHATE = new ItemType(16, 2, 6, 0, 
            "Plancheite", ItemCategory.THING, "", 
            -1, -1);
    public static final ItemType THING_RUBY = new ItemType(17, 3, 6, 0, 
            "Ruby", ItemCategory.THING, "", 
            -1, -1);

    public static final ItemType HELMET_LEATHER = new ItemType(18, 0, 7, 0, 
            "Leather Helmet", ItemCategory.HELMET, "", 
            50, 25);
    public static final ItemType HELMET_IRON = new ItemType(19, 1, 7, 0, 
            "Iron Armor", ItemCategory.HELMET, "", 
            100, 50);
    public static final ItemType HELMET_GOLD = new ItemType(20, 2, 7, 0, 
            "Gold Armor", ItemCategory.HELMET, "", 
            200, 100);
    
    public static final ItemType ARMOR_LEATHER = new ItemType(21, 0, 8, 0, 
            "Leather Armor", ItemCategory.ARMOR, "", 
            100, 50);
    public static final ItemType ARMOR_IRON = new ItemType(22, 1, 8, 0, 
            "Iron Armor", ItemCategory.ARMOR, "", 
            200, 100);
    public static final ItemType ARMOR_GOLD = new ItemType(23, 2, 8, 0, 
            "Gold Armor", ItemCategory.ARMOR, "", 
            400, 200);

    public static final ItemType BOOTS_LEATHER = new ItemType(24, 0, 9, 0, 
            "Leather Helmet", ItemCategory.BOOTS, "", 
            75, 30);
    public static final ItemType BOOTS_IRON = new ItemType(25, 1, 9, 0, 
            "Iron Armor", ItemCategory.BOOTS, "", 
            150, 60);
    public static final ItemType BOOTS_GOLD = new ItemType(26, 2, 9, 0, 
            "Gold Armor", ItemCategory.BOOTS, "", 
            300, 150);
    
    public static final Map<Integer, ItemType> ALL;
    
    /**
     * Static initialization
     */
    static {
        ALL = Stream.of(Items.class.getFields())
                .filter(field -> field.getType() == ItemType.class)
                .map(field -> {
                    try {
                        return (ItemType) field.get(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                    }
                    return null;
                })
                .filter(item -> item != null)
                .collect(Collectors.toMap(entry -> entry.getId(), Function.identity()));
        
    }
    
    /**
     * Item getter by id
     * @param itemId ID of the item
     * @return The item with the specified ID
     */
    public static ItemType getItem(int itemId) {
        return ALL.get(itemId);
    }
    
    private static final Map<String, Map<String, Map<String, Map<String, Map<String, Image>>>>> 
            PLAYER_SKIN_REPO = new HashMap<>();
    private static int SCALE = 2;
    
    /**
     * Get merged (and cached) texture of the player and its clothing.
     * @param base Player base texture
     * @param weapon Selected weapon
     * @param helmet Selected helmet
     * @param armor Selected armor
     * @param boots Selected boots
     * @return The merged image of the player.
     * @see ImageUtil#loadImage(String, String, String, String, String, int)
     */
    public static Image getImageByComponents(String base, String weapon, String helmet, String armor, String boots) {
        if (!PLAYER_SKIN_REPO.containsKey(base))
            PLAYER_SKIN_REPO.put(base, new HashMap<>());
        
        if (!PLAYER_SKIN_REPO.get(base).containsKey(weapon))
            PLAYER_SKIN_REPO.get(base).put(weapon, new HashMap<>());
        if (!PLAYER_SKIN_REPO.get(base).get(weapon).containsKey(helmet))
            PLAYER_SKIN_REPO.get(base).get(weapon).put(helmet, new HashMap<>());
        if (!PLAYER_SKIN_REPO.get(base).get(weapon).get(helmet).containsKey(armor))
            PLAYER_SKIN_REPO.get(base).get(weapon).get(helmet).put(armor, new HashMap<>());
        if (!PLAYER_SKIN_REPO.get(base).get(weapon).get(helmet).get(armor).containsKey(boots))
            PLAYER_SKIN_REPO.get(base).get(weapon).get(helmet).get(armor).put(boots, 
                    ImageUtil.loadImage(base, weapon, helmet, armor, boots, SCALE));
        
        return PLAYER_SKIN_REPO.get(base).get(weapon).get(helmet).get(armor).get(boots);
    }

}
