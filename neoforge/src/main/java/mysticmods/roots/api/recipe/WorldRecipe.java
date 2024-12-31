package mysticmods.roots.api.recipe;

import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class WorldRecipe<W extends IWorldCrafting> extends RootsRecipe<ItemStackHandler, W> implements IWorldRecipe<W> {
  protected BlockState outputState;
  protected WorldCondition condition;

  public WorldRecipe() {
    super();
  }

  @Override
  public void setOutputState(BlockState outputState) {
    this.outputState = outputState;
  }

  @Override
  public BlockState getOutputState() {
    return this.outputState;
  }

  @Override
  public WorldCondition getCondition() {
    return condition;
  }

  @Override
  public void setCondition(WorldCondition condition) {
    this.condition = condition;
  }

  @Override
  public boolean matches(W pContainer, Level pLevel) {
    return getCondition().test(pContainer.getBlockPos(), pLevel);
  }

  @Override
  public BlockState modifyState(W pContainer, BlockState state, HolderLookup.Provider provider) {
    return state;
  }

  @Override
  public ItemStack assemble(W pInv, HolderLookup.Provider provider) {
    Level level = pInv.getLevel();
    if (level == null) {
      throw new IllegalStateException("Cannot assemble recipe without a world!");
    }
    if (!level.isClientSide()) {
      BlockPos pos = pInv.getBlockPos();
      BlockState newState = modifyState(pInv, level.getBlockState(pos), provider);
      level.setBlock(pos, newState, 11);
      Player player = pInv.getPlayer();
      if (player != null) {
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(pInv.getPlayer(), newState));
        for (Grant grant : getGrants()) {
          grant.grant((ServerPlayer) player);
        }
      }
    }

    if (hasItemOutput()) {
      return getResultItem(provider).copy();
    } else {
      return ItemStack.EMPTY;
    }
  }

}
