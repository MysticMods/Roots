package epicsquid.roots.block;

import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.util.types.BlockPosDimension;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class BonfireBlock extends Block {

  public static BooleanProperty BURNING = BooleanProperty.create("burning");

  public BonfireBlock(Properties properties) {
    super(properties);
  }

  // TODO: Light
  // TODO: Voxel shape

  /*@Override
  public int getLightValue(BlockState state, IBlockAccess world, BlockPos pos) {
    if (state.getValue(BURNING))
      return 15;
    else
      return 0;
  }*/

  /*@Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    return BlockFaceShape.BOWL;
  }*/

  public static void setState(boolean burning, World world, BlockPos pos) {
    if (burning) world.setBlockState(pos, world.getBlockState(pos).with(BURNING, true), 3);
    else world.setBlockState(pos, world.getBlockState(pos).with(BURNING, false), 3);
  }

  // TODO

/*  @Override
  @OnlyIn(Dist.CLIENT)
  public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (stateIn.get(BURNING)) {
      worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
    }
  }*/

  @Override
  public boolean hasComparatorInputOverride(BlockState state) {
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
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
    TileEntity te = worldIn.getTileEntity(pos);
    if (te instanceof TileEntityBonfire) {
      TileEntityBonfire bon = (TileEntityBonfire) te;
      boolean lit = bon.getBurnTime() != 0;

      BlockPosDimension pdos = new BlockPosDimension(pos, worldIn.getDimension().getType().getId());
      RitualBase ritual = ritualCache.get(pdos);
      if (ritual == null || !ritual.isRitualRecipe(bon, null)) {
        ritual = RitualRegistry.getRitual(bon, null);
        if (ritual != null && !ritual.isDisabled()) {
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
}
