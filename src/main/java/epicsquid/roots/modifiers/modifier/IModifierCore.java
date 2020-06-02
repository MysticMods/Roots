package epicsquid.roots.modifiers.modifier;

import epicsquid.roots.api.Herb;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IModifierCore {
  boolean isHerb();

  @Nullable
  Herb getHerb();

  ItemStack getStack();

  String getTranslationKey ();

  String getFormatting ();

  boolean isBasic ();
}
