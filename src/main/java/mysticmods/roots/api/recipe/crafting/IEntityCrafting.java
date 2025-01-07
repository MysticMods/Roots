package mysticmods.roots.api.recipe.crafting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.ItemStackHandler;

public interface IEntityCrafting extends IRootsCrafting<ItemStackHandler> {
  LivingEntity getEntity ();

  @Override
  default boolean isEmpty() {
    return false;
  }

  @Override
  default int size() {
    return 0;
  }

  @Override
  default ItemStack getItem(int pSlot) {
    return ItemStack.EMPTY;
  }

  @Override
  default ItemStackHandler getHandler() {
    return NULL_HANDLER;
  }
}
