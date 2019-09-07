package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockFeyLight extends BlockBase {
  public BlockFeyLight(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    this.setLightLevel(1.0f);
    this.setLightOpacity(0);

    // This prevents this from being registered as an itemblock
    this.setItemBlock(null);
  }

  public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  public EnumBlockRenderType getRenderType(final IBlockState state) {
    return EnumBlockRenderType.INVISIBLE;
  }

  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return new AxisAlignedBB(0.33, 0.33, 0.33, 0.66, 0.66, 0.66);
  }

  public AxisAlignedBB getCollisionBoundingBox(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
    return null;
  }

  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
    List<Float> reds = new ArrayList<Float>();
    List<Float> greens = new ArrayList<Float>();
    List<Float> blues = new ArrayList<Float>();
    reds.add(177f);
    reds.add(255f);
    reds.add(255f);
    reds.add(219f);
    reds.add(122f);
    greens.add(255f);
    greens.add(223f);
    greens.add(163f);
    greens.add(179f);
    greens.add(144f);
    blues.add(117f);
    blues.add(163f);
    blues.add(255f);
    blues.add(255f);
    blues.add(255f);

    int ind = Util.rand.nextInt(5);

    float r = reds.get(ind);
    float g = greens.get(ind);
    float b = blues.get(ind);
    for (int i = 0; i < 2; i++) {
      ParticleUtil.spawnParticleGlow(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (random.nextFloat() - 0.5f) * 0.003f, 0f, (random.nextFloat() - 0.5f) * 0.003f, r, g, b, 0.25f, 3.0f, 240);
    }
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.AIR;
  }
}

