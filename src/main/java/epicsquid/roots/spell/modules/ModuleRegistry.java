package epicsquid.roots.spell.modules;

import epicsquid.roots.init.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {

  public static Map<String, SpellModule> moduleRegistry = new HashMap<>();

  public static SpellModule module_fire, module_touch;

  public static void init() {
    addModule(module_fire = new SpellModule("module_fire", new ItemStack(ModItems.infernal_bulb), TextFormatting.DARK_RED));
    addModule(module_touch = new SpellModule("module_touch", new ItemStack(Items.IRON_BOOTS), TextFormatting.DARK_AQUA));
  }

  public static void addModule(SpellModule module) {
    moduleRegistry.put(module.getName(), module);
  }

  public static SpellModule getModule(String string) {
    return moduleRegistry.getOrDefault(string, null);
  }

  public static SpellModule getModule(ItemStack stack) {
    for (Map.Entry<String, SpellModule> entry : moduleRegistry.entrySet()) {
      if (entry.getValue().getIngredient().isItemEqual(stack)) {
        return entry.getValue();
      }
    }
    return null;
  }

  public static boolean isModule(ItemStack heldItem) {
    for (Map.Entry<String, SpellModule> entry : moduleRegistry.entrySet()) {
      if (entry.getValue().getIngredient().isItemEqual(heldItem)) {
        return true;
      }
    }
    return false;
  }
}
