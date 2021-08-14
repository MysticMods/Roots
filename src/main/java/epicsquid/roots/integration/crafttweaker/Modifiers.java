package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.Modifier;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Modifiers")
@ZenDocAppend({"docs/include/modifiers.example.md"})
@ZenDocClass("mods." + Roots.MODID + ".Modifiers")
public class Modifiers {
  @ZenMethod
  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "modifierName", info = "the modifier name to be disabled (if not provided in the format of roots:modifier, a resource location will be created from the string)")
      },
      description = "This is used to disable specific modifiers."
  )
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
