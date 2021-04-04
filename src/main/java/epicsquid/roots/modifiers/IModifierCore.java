package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;
import epicsquid.roots.util.types.IRegistryItem;
import net.minecraft.item.ItemStack;

public interface IModifierCore extends IRegistryItem {
  Herb getHerb();

  ItemStack getStack();

  String getTranslationKey();

  String getFormatting();

  int getKey();
}
