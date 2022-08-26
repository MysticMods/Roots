package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.util.types.BlockPosDimension;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockBonfire extends BlockTEBase {

  public static PropertyBool BURNING = PropertyBool.create("burning");

  public BlockBonfire(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    setDefaultState(blockState.getBaseState().withProperty(BURNING, false));
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
    if (state.getValue(BURNING))
      return 15;
    else
      return 0;
  }

  @Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    return BlockFaceShape.BOWL;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(-0.125, 0, -0.125, 1.125, 0.25, 1.125);
  }

  //Concerning the blockstate --------------------
  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, BURNING);
  }

  @Nonnull
  @Override
  public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
    return this.getDefaultState().withProperty(BURNING, false);
  }

  public static void setState(boolean burning, World world, BlockPos pos) {
    if (burning) world.setBlockState(pos, ModBlocks.bonfire.getDefaultState().withProperty(BURNING, true), 3);
    else world.setBlockState(pos, ModBlocks.bonfire.getDefaultState().withProperty(BURNING, false), 3);
  }



  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(BURNING, meta == 1);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(BURNING) ? 1 : 0;
  }

  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (stateIn.getValue(BURNING)) {
      worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
    }
  }

  @Override
  public boolean hasComparatorInputOverride(IBlockState state) {
    return true;
  }

  public static Map<BlockPosDimension, RitualBase> ritualCache = new HashMap<>();
  public static Map<BlockPosDimension, PyreCraftingRecipe> recipeCache = new HashMap<>();

  @Override
  /*
    0 = Unlit, no recipe
    1 = Unlit, valid recipe or ritual
    3 = Lit, no items
    4 = Lit, valid recipe of ritual
    5 = Lit, valid recipe or ritual matches current ritual or recipe
   */
  public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
    TileEntity te = worldIn.getTileEntity(pos);
    if (te instanceof TileEntityBonfire) {
      TileEntityBonfire bon = (TileEntityBonfire) te;
      boolean lit = bon.getBurnTime() != 0;

      BlockPosDimension pdos = new BlockPosDimension(pos, worldIn.provider.getDimension());
      RitualBase ritual = ritualCache.get(pdos);
      if (ritual == null || !ritual.isRitualRecipe(bon, null)) {
        ritual = RitualRegistry.getRitual(bon, null);
        if (ritual != null) {
          ritualCache.put(pdos, ritual);
        }
      }
      if (ritual != null) {
        if (bon.getLastRitualUsed().equals(ritual) && lit) return 5;

        return lit ? 4 : 1;
      }

      // Check for crafting
      PyreCraftingRecipe recipe = recipeCache.get(pdos);
      if (recipe == null) {
        recipe = bon.getCurrentRecipe();
        if (recipe != null) {
          recipeCache.put(pdos, recipe);
        }
      }
      if (recipe != null) {
        if (bon.getLastRecipeUsed().equals(recipe) && lit) return 5;

        return lit ? 4 : 1;
      }

      return lit ? 3 : 0;
    }

    // Empty, no items in it
    return 0;
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    super.breakBlock(worldIn, pos, state);
  }
}
