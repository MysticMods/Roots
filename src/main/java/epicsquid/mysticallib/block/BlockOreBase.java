package epicsquid.mysticallib.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockOreBase extends BlockBase {
  private final Item drop;
  private final int minXP;
  private final int maxXP;

  public BlockOreBase(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nullable Item drop, int level, int minXP, int maxXP) {
    super(mat, type, hardness, name);
    this.drop = drop;
    this.setHarvestLevel("pickaxe", level);
    this.minXP = minXP;
    this.maxXP = maxXP;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    if (drop != null) {
      return drop;
    }

    return super.getItemDropped(state, rand, fortune);
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, Random random) {
    if (this.drop == null) {
      return super.quantityDroppedWithBonus(fortune, random);
    }

    int i = random.nextInt(fortune + 2) - 1;

    if (i < 0) {
      i = 0;
    }

    return this.quantityDropped(random) * (i + 1);

  }

  @Override
  public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
    if (minXP == -1 || maxXP == -1)
      return 0;

    Random rand = world instanceof World ? ((World) world).rand : new Random();
    return MathHelper.getInt(rand, minXP, maxXP);
  }
}
