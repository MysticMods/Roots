package epicsquid.roots.modifiers;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class BaseModifiers {
  public static IModifierCore AIR = new IModifierCore() {
    @Override
    public Herb getHerb() {
      return null;
    }

    @Override
    public ItemStack getStack() {
      return ItemStack.EMPTY;
    }

    @Override
    public String getTranslationKey() {
      return "roots.modifiers.cores.air";
    }

    @Override
    public String getFormatting() {
      return "";
    }

    @Override
    public int getKey() {
      return -1;
    }
  };

  public static Modifier NULL = new Modifier(new ResourceLocation(Roots.MODID, "null"), AIR, Cost.noCost());
}
