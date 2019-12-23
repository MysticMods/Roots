package epicsquid.roots.ritual.conditions;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ConditionStandingStones implements ICondition {

  private final int height;
  private final int amount;

  public ConditionStandingStones(int height, int amount) {
    this.height = height;
    this.amount = amount;
  }

  public int getHeight() {
    return height;
  }

  public int getAmount() {
    return amount;
  }

  @Override
  public boolean checkCondition(TileEntityBonfire tile, EntityPlayer player) {
    return getStandingStones(tile.getWorld(), tile.getPos(), this.height) >= this.amount;
  }

  @Nullable
  @Override
  public ITextComponent failMessage() {
    return new TextComponentTranslation("roots.ritual.condition.standing_stones", new TextComponentTranslation("roots.ritual.condition.standing_stones.types", getAmount(), getHeight()));
  }

  protected int getStandingStones(World world, BlockPos pos, int height) {
    Block material = ModBlocks.runestone;
    int threeHighCount = 0;
    for (int i = -9; i <= 10; i++) {
      for (int j = -9; j <= 10; j++) {
        IBlockState state = world.getBlockState(pos.add(i, height - 1, j));
        if (state.getBlock() == ModBlocks.chiseled_runestone) {
          boolean stoneFound = true;
          for (int y = height - 1; y > 0; y--) {
            if (world.getBlockState(pos.add(i, 1, j)).getBlock() != material) {
              stoneFound = false;
            }
          }
          if (stoneFound) {
            threeHighCount++;
          }
        }
      }
    }
    return threeHighCount;
  }


}
