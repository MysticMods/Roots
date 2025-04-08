package mysticmods.roots.api.recipe;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.api.test.world.WorldTest;
import mysticmods.roots.recipe.knife.OutputStateMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class WorldRecipe<W extends IWorldCrafting> extends RootsRecipe<ItemStackHandler, W> implements IWorldRecipe<W> {
  @Nullable
  protected final WorldTest test;
  @Nullable
  protected PartialBlockState outputState;
  protected List<WorldCondition> conditions;
  protected List<String> skipProperties;
  protected OutputStateMapper stateMapper;

  public WorldRecipe(BaseRecipeData data, WorldTest test, PartialBlockState outputState, List<WorldCondition> conditions, List<String> skipProperties) {
    super(data);
    this.test = test;
    this.outputState = outputState;
    this.conditions = conditions;
    this.skipProperties = skipProperties;
  }

  @Override
  @Nullable
  public PartialBlockState getOutputState() {
    return this.outputState;
  }

  @Nullable
  public WorldTest getTest() {
    return test;
  }

  @Override
  public List<WorldCondition> getConditions() {
    return conditions;
  }

  @Override
  public List<String> getSkipProperties() {
    return skipProperties;
  }

  @Override
  public boolean matches(W pContainer, Level pLevel) {
    if (test != null) {
      BlockState state = pContainer.getBlockState();
      if (!test.test(state, pLevel.getRandom())) {
        return false;
      }
    }
    for (WorldCondition condition : getConditions()) {
      if (!condition.test(pContainer.getBlockPos(), pLevel, pLevel.getRandom())) {
        return false;
      }
    }
    return true;
  }

  @Nullable
  public OutputStateMapper getStateMapper() {
    return stateMapper;
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
        for (Unlock<?> unlock : getUnlocks()) {
          RootsAPI.getInstance().unlock((ServerPlayer) player, unlock);
        }
      }
    }

    if (hasItemOutput(provider)) {
      return getResultItem(provider).copy();
    } else {
      return ItemStack.EMPTY;
    }
  }

}
