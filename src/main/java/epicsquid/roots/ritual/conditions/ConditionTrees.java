package epicsquid.roots.ritual.conditions;

import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ConditionTrees implements Condition {

  private final BlockPlanks.EnumType treeType;
  private final int amount;

  public ConditionTrees(BlockPlanks.EnumType treeType, int amount) {
    this.treeType = treeType;
    this.amount = amount;
  }

  public BlockPlanks.EnumType getTreeType() {
    return treeType;
  }

  public int getAmount() {
    return amount;
  }

  @Override
  public boolean checkCondition(TileEntityBonfire tile, PlayerEntity player) {
    return getTreeAmount(tile.getWorld(), tile.getPos()) >= this.amount;
  }

  @Nullable
  @Override
  public ITextComponent failMessage() {
    return new TranslationTextComponent("roots.ritual.condition.trees", new TranslationTextComponent("roots.ritual.condition.trees.types", getAmount(), new TranslationTextComponent(getTreeType().getTranslationKey())));
  }

  private int getTreeAmount(World world, BlockPos pos) {
    int treeCount = 0;
    for (int i = -9; i < 10; i++) {
      for (int j = -9; j < 10; j++) {
        BlockState state = world.getBlockState(pos.add(i, 1, j));
        if (state.getBlock() instanceof BlockOldLog) {
          if (state.getValue(BlockOldLog.VARIANT) == treeType) {
            treeCount++;
          }
        } else if (state.getBlock() instanceof BlockNewLog) {
          if (state.getValue(BlockNewLog.VARIANT) == treeType) {
            treeCount++;
          }
        }
      }
    }
    return treeCount;
  }
}
