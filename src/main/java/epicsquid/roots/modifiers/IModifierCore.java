package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IModifierCore {
  Herb getHerb();

  ItemStack getStack();

  String getTranslationKey();

  String getFormatting();

  int getKey();
}
