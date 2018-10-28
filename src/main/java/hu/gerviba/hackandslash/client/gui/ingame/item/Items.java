package hu.gerviba.hackandslash.client.gui.ingame.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hu.gerviba.hackandslash.client.ImageUtil;
import javafx.scene.image.Image;

public class Items {

    public static final Image TEXTURE = ImageUtil.loadImage("/assets/items/items.png", 2);

    public static final ItemType SKILL_POISON = new ItemType(1, 0, 0, 1, 
            "Poison Skill", "skill", 
            "Release a toxic gas cloud", 
            -1, -1);
    public static final ItemType SKILL_WEATER_BEAM = new ItemType(2, 1, 0, 2, 
            "Water Beam Skill", "skill", 
            "Shoot a water beam", 
            -1, -1);
    public static final ItemType SKILL_PURLE_MAGIC = new ItemType(3, 2, 0, 3, 
            "Purple Magic Skill", "skill", 
            "Shoot a purple magic beam",
            -1, -1);
    public static final ItemType SKILL_FIRE_CIRCLE = new ItemType(4, 3, 0, 4, 
            "Flame Circle Skill", "skill", 
            "Fire a flame crircle", 
            -1, -1);
    public static final ItemType SKILL_STORM = new ItemType(5, 4, 0, 5, 
            "Storm Skill", "skill", 
            "Release a toxic gas cloud",
            -1, -1);
    
    public static final ItemType WEAPON_BLUE_MAGIC_WAND = new ItemType(6, 0, 2, -1, 
            "Plancheite Magic Wand", "weapon", "", 
            100, 50);
    public static final ItemType WEAPON_GOLD_MAGIC_WAND = new ItemType(7, 1, 2, -2, 
            "Gold Magic Wand", "weapon", "", 
            300, 150);
    public static final ItemType WEAPON_BLUE_SWORD = new ItemType(8, 2, 2, -3, 
            "Plancheite Sword", "weapon", "", 
            100, 50);
    public static final ItemType WEAPON_GOLD_SWORD = new ItemType(9, 3, 2, -4, 
            "Gold Sword", "weapon", "", 
            300, 150);
    
    public static final ItemType POTION_HEALTH_1 = new ItemType(101, 0, 3, 101, 
            "Health Potion I", "potion", 
            "Effect: +20 HP|"
            + "Reload: 1 sec", 
            -1, -1);
    public static final ItemType POTION_HEALTH_2 = new ItemType(102, 1, 3, 102, 
            "Health Potion II", "potion", 
            "Effect: +50 HP|"
            + "Reload: 2.5 sec", 
            -1, -1);
    public static final ItemType POTION_HEALTH_3 = new ItemType(103, 2, 3, 103, 
            "Health Potion III", "potion", 
            "Effect: +500 HP|"
            + "Reload: 10 sec", 
            -1, -1);

    public static final ItemType MANA_HEALTH_1 = new ItemType(201, 0, 4, 201, 
            "Mana Potion I", "potion", 
            "Effect: +20 Mana|"
            + "Reload: 1 sec", 
            -1, -1);
    public static final ItemType MANA_HEALTH_2 = new ItemType(202, 1, 4, 202, 
            "Mana Potion II", "potion", 
            "Effect: +50 Mana|"
            + "Reload: 2.5 sec", 
            -1, -1);
    public static final ItemType MANA_HEALTH_3 = new ItemType(203, 2, 4, 203, 
            "Mana Potion III", "potion", 
            "Effect: +500 Mana|"
            + "Reload: 10 sec", 
            -1, -1);
    
    public static final ItemType RING_SILVER = new ItemType(10, 0, 5, 0, 
            "Silver Ring", "ring", "", 
            -1, -1);
    public static final ItemType RING_CARBON = new ItemType(11, 1, 5, 0, 
            "Plancheite Ring", "ring", "", 
            -1, -1);
    public static final ItemType RING_GOLDEN = new ItemType(12, 2, 5, 0, 
            "Golden Ring", "ring", "", 
            -1, -1);
    public static final ItemType RING_RUBY = new ItemType(13, 3, 5, 0, 
            "Ruby Ring", "ring", "", 
            -1, -1);

    public static final ItemType THING_BONE = new ItemType(14, 0, 6, 0, 
            "Bone", "thing", "", 
            -1, -1);
    public static final ItemType THING_EYE = new ItemType(15, 1, 6, 0, 
            "Eye", "thing", "", 
            -1, -1);
    public static final ItemType THING_COPPER_SULPHATE = new ItemType(14,2, 6, 0, 
            "Plancheite", "thing", "", 
            -1, -1);
    public static final ItemType THING_RUBY = new ItemType(15, 3, 6, 0, 
            "Ruby", "thing", "", 
            -1, -1);


    public static final ItemType HELMET_LEATHER = new ItemType(16, 0, 7, 0, 
            "Leather Helmet", "helmet", "", 
            50, 25);
    public static final ItemType HELMET_IRON = new ItemType(17, 1, 7, 0, 
            "Iron Armor", "helmet", "", 
            100, 50);
    public static final ItemType HELMET_GOLD = new ItemType(18, 2, 7, 0, 
            "Gold Armor", "helmet", "", 
            200, 100);
    
    public static final ItemType ARMOR_LEATHER = new ItemType(19, 0, 8, 0, 
            "Leather Armor", "armor", "", 
            100, 50);
    public static final ItemType ARMOR_IRON = new ItemType(20, 1, 8, 0, 
            "Iron Armor", "armor", "", 
            200, 100);
    public static final ItemType ARMOR_GOLD = new ItemType(21, 2, 8, 0, 
            "Gold Armor", "armor", "", 
            400, 200);

    public static final ItemType BOOTS_LEATHER = new ItemType(22, 0, 9, 0, 
            "Leather Helmet", "boots", "", 
            75, 30);
    public static final ItemType BOOTS_IRON = new ItemType(23, 1, 9, 0, 
            "Iron Armor", "boots", "", 
            150, 60);
    public static final ItemType BOOTS_GOLD = new ItemType(24, 2, 9, 0, 
            "Gold Armor", "boots", "", 
            300, 150);
    
    public static final List<ItemType> ALL;
    public static final List<String> ALL_TYPES;
    
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
                .collect(Collectors.toList());
        
        ALL_TYPES = ALL.stream()
                .map(x -> x.getType())
                .distinct()
                .collect(Collectors.toList());
    }
    
    private static final Map<String, Map<String, Map<String, Map<String, Map<String, Image>>>>> PLAYER_SKIN_REPO 
            = new HashMap<>();
    private static int SCALE = 2;
    
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
