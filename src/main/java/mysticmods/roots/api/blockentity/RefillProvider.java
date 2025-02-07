package mysticmods.roots.api.blockentity;

import mysticmods.roots.api.recipe.RecipeUtil;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

public interface RefillProvider {
  RecipeInventory getRefillInventory();

  @Nullable
  Recipe<?> getRefillRecipe();

  @Nullable
  BlockCapabilityCache<IItemHandler, Direction> getBlockCapabilityCache();

  void setBlockCapabilityCache(BlockCapabilityCache<IItemHandler, Direction> blockCapabilityCache);

  default boolean tryRefill(ServerLevel level, BlockPos position) {
    if (getRefillInventory().isEmpty() && getRefillRecipe() != null) {
      IItemHandler handler = null;
      if (getBlockCapabilityCache() != null) {
        handler = getBlockCapabilityCache().getCapability();
      }

      Recipe<?> recipe = getRefillRecipe();

      if (handler == null) {
        handler = level.getCapability(Capabilities.ItemHandler.BLOCK, position, null);
        if (handler != null) {
          setBlockCapabilityCache(BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, level, position, null));
        }
      }
      if (handler != null) {
        return RecipeUtil.refillRecipe(handler, recipe, getRefillInventory());
      }
    }
    return false;
  }
}
