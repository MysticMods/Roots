package epicsquid.mysticallib.material;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class MaterialTypes {
  public static Map<String, Item.ToolMaterial> materialMap = new HashMap<>();
  public static Map<String, ItemArmor.ArmorMaterial> armorMaterialMap = new HashMap<>();
  public static Map<String, KnifeStats> statsMap = new HashMap<>();
  public static Map<Item.ToolMaterial, KnifeStats> materialToStatsMap = new HashMap<>();

  public static void addMaterial(String name, Item.ToolMaterial material, ItemArmor.ArmorMaterial armor, float damage, float speed) {
    addMaterial(name, material, armor, new KnifeStats(damage, speed));
  }

  public static void addMaterial(String name, Item.ToolMaterial material, ItemArmor.ArmorMaterial armor, KnifeStats stats) {
    materialToStatsMap.put(material, stats);
    armorMaterialMap.put(name, armor);
    statsMap.put(name, stats);
    materialMap.put(name, material);
  }

  public static Item.ToolMaterial material(String name) {
    return materialMap.get(name);
  }

  public static ItemArmor.ArmorMaterial armor (String name) {
    return armorMaterialMap.get(name);
  }

  public static KnifeStats stats(String name) {
    return statsMap.get(name);
  }

  public static KnifeStats stats(Item.ToolMaterial tool) {
    return materialToStatsMap.get(tool);
  }

  public static class KnifeStats {
    public float damage;
    public float speed;

    public KnifeStats(float damage, float speed) {
      this.damage = damage;
      this.speed = speed;
    }
  }
}
