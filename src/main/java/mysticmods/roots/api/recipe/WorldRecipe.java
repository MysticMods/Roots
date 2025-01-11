package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import mysticmods.roots.api.world.PartialBlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class WorldRecipe<W extends IWorldCrafting> extends RootsRecipe<ItemStackHandler, W> implements IWorldRecipe<W> {
  protected PartialBlockState outputState;
  protected WorldCondition condition;

  public WorldRecipe() {
  }

  public WorldRecipe(BaseRecipeData data, PartialBlockState outputState, WorldCondition condition) {
    super(data);
    this.outputState = outputState;
    this.condition = condition;
  }

  @Override
  public PartialBlockState getOutputState() {
    return this.outputState;
  }

  @Override
  public WorldCondition getCondition() {
    return condition;
  }

  @Override
  public boolean matches(W pContainer, Level pLevel) {
    return getCondition().test(pContainer.getBlockPos(), pLevel, pLevel.getRandom());
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
      if (newState == null) {
        newState = Blocks.AIR.defaultBlockState();
      }
      level.setBlock(pos, newState, 11);
      Player player = pInv.getPlayer();
      if (player != null) {
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(pInv.getPlayer(), newState));
        // TODO: Unlocks
/*        for (Grant grant : getUnlocks()) {
          grant.grant((ServerPlayer) player);
        }*/
      }
    }

    // TODO: List results
    if (hasItemOutput(provider)) {
      return getResultItem(provider).copy();
    } else {
      return ItemStack.EMPTY;
    }
  }

}
