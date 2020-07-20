package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.Modifier;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.util.zen.ZenDocClass;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Modifiers")
@ZenDocClass("mods." + Roots.MODID + ".Modifiers")
public class Modifiers {
  @ZenMethod
  public static void disableModifier(String modifierName) {
    ResourceLocation rl;
    if (!modifierName.contains(":")) {
      rl = new ResourceLocation(Roots.MODID, modifierName);
    } else {
      rl = new ResourceLocation(modifierName);
    }
    Modifier modifier = ModifierRegistry.get(rl);
    if (modifier == null) {
      CraftTweakerAPI.logError("Invalid modifier: " + modifierName);
      return;
    }

    ModifierRegistry.disable(modifier);
  }
}
