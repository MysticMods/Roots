package mysticmods.roots.recipe.bark;

import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BarkCrafting implements IWorldCrafting {
  private final Player player;
  private final ServerLevel level;
  private final BlockPos pos;
  private BlockState state;

  public BarkCrafting(Player player, ServerLevel level, BlockPos pos) {
    this.player = player;
    this.level = level;
    this.pos = pos;
  }

  @Nullable
  @Override
  public Player getPlayer() {
    return player;
  }

  @Nullable
  @Override
  public Level getLevel() {
    return level;
  }

  @Override
  public void setBlockState(BlockState state) {
    this.state = state;
  }

  @Override
  public BlockState getBlockState() {
    return state;
  }

  @Override
  public BlockPos getBlockPos() {
    return pos;
  }

  @Override
  public void setChanged() {

  }

  @Override
  public boolean stillValid(Player pPlayer) {
    return true;
  }

  @Override
  public void clearContent() {

  }
}
