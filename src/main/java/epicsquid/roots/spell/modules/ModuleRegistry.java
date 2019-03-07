package epicsquid.roots.spell.modules;

import epicsquid.roots.init.ModItems;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {

    public static Map<String, SpellModule> spellRegistry = new HashMap<>();

    public static SpellModule module_fire;

    public static void init() {
        addModule(module_fire = new SpellModule("module_fire", new ItemStack(ModItems.infernal_bulb)));
    }

    public static void addModule(SpellModule module){
        spellRegistry.put(module.getName(), module);
    }

    public static SpellModule getModule(String string) {
        return spellRegistry.getOrDefault(string, null);
    }
}
