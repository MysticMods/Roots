package epicsquid.roots.block;

import epicsquid.mysticallib.block.Block;
import epicsquid.roots.Roots;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.DirectionProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockCatalystPlate extends Block {
  public static final DirectionProperty FACING = DirectionProperty.create("facing");

  public BlockCatalystPlate(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
  }

  @Override
  public void attemptRegistry(@Nonnull Class<? extends TileEntity> c, String name) {
    if (!Block.classes.contains(c)) {
      Block.classes.add(c);
      GameRegistry.registerTileEntity(c, new ResourceLocation(Roots.MODID, "tile_entity_offertory_plate"));
    }
  }

  @Override
  public BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public int getMetaFromState(BlockState state) {
    return state.get(FACING).getIndex();
  }

  @Override
  public boolean isFullCube(@Nonnull BlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull BlockState state) {
    return false;
  }

  @Override
  public BlockState getStateFromMeta(int meta) {
    return getDefaultState().with(FACING, Direction.byIndex(meta));
  }

  @Override
  public BlockState getStateForPlacement(World world, BlockPos pos, Direction face, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
    return getDefaultState().with(FACING, placer.getHorizontalFacing().getOpposite());
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 0.875, 0.875);
  }

  @Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    return BlockFaceShape.BOWL;
  }
}
