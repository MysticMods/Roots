package mysticmods.roots.ritual;

import mysticmods.roots.action.CraftItemAction;
import mysticmods.roots.action.CraftRecipeAction;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.SingleTickRitual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.ItemUtil;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class CraftingRitual extends SingleTickRitual {
  @Override
  public void singleTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int dur, RandomSource randomSource) {
    List<ItemStack> output = blockEntity.popStoredItems();
    if (output.isEmpty()) {
      return;
    }
    if (blockEntity.getLastPlayer() != null && ModActions.CRAFT_ITEM.get().shouldTest()) {
      for (ItemStack item : output) {
        CraftItemAction.Context context = new CraftItemAction.Context(
            (ServerLevel) blockEntity.getLevel(),
            (ServerPlayer) blockEntity.getLastPlayer(),
            item
        );
        ModActions.CRAFT_ITEM.get().accept(context);
      }
    }
    output = blockEntity.outputAdjacent(output); // Try to output to adjacent inventories
    for (ItemStack stack : output) { // Drop whatever's left over
      ItemUtil.Spawn.spawnItem(blockEntity.getLevel(), blockEntity.getBlockPos().above(), stack);
    }
    if (blockEntity.getLastPlayer() != null && blockEntity.getLastRecipe() != null && ModActions.CRAFT_RECIPE.get().shouldTest()) {
      CraftRecipeAction.Context context = new CraftRecipeAction.Context(
          (ServerLevel) blockEntity.getLevel(),
          (ServerPlayer) blockEntity.getLastPlayer(),
          blockEntity.getLastRecipe().id(),
          blockEntity.getLastRecipe().value(),
          blockEntity
      );
      ModActions.CRAFT_RECIPE.get().accept(context);
    }
  }

  @Override
  public void initialize(Holder<Ritual> holder) {
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.CRAFTING_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return null;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return null;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.CRAFTING_INTERVAL;
  }
}
