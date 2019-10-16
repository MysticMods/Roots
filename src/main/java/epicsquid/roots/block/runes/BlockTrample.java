package epicsquid.roots.block.runes;

import epicsquid.mysticallib.block.BlockBase;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockTrample extends BlockBase {
  public static int SAFE_RANGE_X = 30;
  public static int SAFE_RANGE_Y = 5;
  public static int SAFE_RANGE_Z = 30;

  public BlockTrample(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    setDefaultState(this.getDefaultState().withProperty(BlockLiquid.LEVEL, 15));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, BlockLiquid.LEVEL);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(BlockLiquid.LEVEL, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(BlockLiquid.LEVEL);
  }

  @Override
  public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BlockLiquid.LEVEL).build());
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "handlers"));
  }
}
