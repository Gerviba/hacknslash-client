package hu.gerviba.hackandslash.client.gui.ingame.item;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hu.gerviba.hackandslash.client.ImageUtil;
import javafx.scene.image.Image;

public class Items {

    public static final Image TEXTURE = ImageUtil.loadImage("/assets/items/items.png", 2);

    public static final ItemType SKILL_POISON = new ItemType(1, 0, 0, 1, "Poison Skill", "skill", "Release a toxic gas cloud", -1, -1);
    public static final ItemType SKILL_WEATER_BEAM = new ItemType(2, 1, 0, 2, "Water Beam Skill", "skill", "Shoot a water beam", -1, -1);
    public static final ItemType SKILL_PURLE_MAGIC = new ItemType(3, 2, 0, 3, "Purple Magic Skill", "skill", "Shoot a purple magic beam", -1, -1);
    public static final ItemType SKILL_FIRE_CIRCLE = new ItemType(4, 3, 0, 4, "Flame Circle Skill", "skill", "Fire a flame crircle", -1, -1);
    public static final ItemType SKILL_STORM = new ItemType(5, 4, 0, 5, "Storm Skill", "skill", "Release a toxic gas cloud", -1, -1);
    
    public static final ItemType WEAPON_BLUE_MAGIC_WAND = new ItemType(6, 0, 2, -1, "Copper Sulphate Magic Wand", "weapon", "", 100, 50);
    public static final ItemType WEAPON_GOLD_MAGIC_WAND = new ItemType(7, 1, 2, -2, "Gold Magic Wand", "weapon", "", 300, 150);
    public static final ItemType WEAPON_BLUE_SWORD = new ItemType(8, 2, 2, -3, "Copper Sulphate Sword", "weapon", "", 100, 50);
    public static final ItemType WEAPON_GOLD_SWORD = new ItemType(9, 3, 2, -4, "Gold Sword", "weapon", "", 300, 150);
    
    public static final ItemType POTION_HEALTH_1 = new ItemType(101, 0, 3, 101, "Health Potion I", "potion", "Effect: +20 HP|Reload: 1 sec", -1, -1);
    public static final ItemType POTION_HEALTH_2 = new ItemType(102, 1, 3, 102, "Health Potion II", "potion", "Effect: +50 HP|Reload: 2.5 sec", -1, -1);
    public static final ItemType POTION_HEALTH_3 = new ItemType(103, 2, 3, 103, "Health Potion III", "potion", "Effect: +500 HP|Reload: 10 sec", -1, -1);

    public static final ItemType MANA_HEALTH_1 = new ItemType(201, 0, 4, 201, "Mana Potion I", "potion", "Effect: +20 Mana|Reload: 1 sec", -1, -1);
    public static final ItemType MANA_HEALTH_2 = new ItemType(202, 1, 4, 202, "Mana Potion II", "potion", "Effect: +50 Mana|Reload: 2.5 sec", -1, -1);
    public static final ItemType MANA_HEALTH_3 = new ItemType(203, 2, 4, 203, "Mana Potion III", "potion", "Effect: +500 Mana|Reload: 10 sec", -1, -1);
    
    public static final ItemType RING_SILVER = new ItemType(10, 0, 5, 0, "Silver Ring", "ring", "", -1, -1);
    public static final ItemType RING_CARBON = new ItemType(11, 1, 5, 0, "Carbon Ring", "ring", "", -1, -1);
    public static final ItemType RING_GOLDEN = new ItemType(12, 2, 5, 0, "Golden Ring", "ring", "", -1, -1);
    public static final ItemType RING_RUBY = new ItemType(13, 3, 5, 0, "Ruby Ring", "ring", "", -1, -1);

    public static final ItemType THING_BONE = new ItemType(14, 0, 6, 0, "Bone", "thing", "", -1, -1);
    public static final ItemType THING_EYE = new ItemType(15, 1, 6, 0, "Eye", "thing", "", -1, -1);
    public static final ItemType THING_COPPER_SULPHATE = new ItemType(14,2, 6, 0, "Copper Sulphate", "thing", "", -1, -1);
    public static final ItemType THING_RUBY = new ItemType(15, 3, 6, 0, "Ruby", "thing", "", -1, -1);
    
    public static List<ItemType> ALL;
    
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
    }
}
