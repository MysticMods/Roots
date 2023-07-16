package mysticmods.roots.recipe;

import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SimpleWorldCrafting implements IWorldCrafting {
  private final Player player;
  private final Level level;
  private final BlockPos pos;
  private final BlockState state;
  private final UseOnContext context;

  public SimpleWorldCrafting(Player player, Level level, BlockPos pos, BlockState blockstate, UseOnContext context) {
    this.player = player;
    this.level = level;
    this.pos = pos;
    this.state = blockstate;
    this.context = context;
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

  public UseOnContext getContext() {
    return context;
  }
}
