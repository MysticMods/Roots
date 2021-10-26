package epicsquid.roots.block;

import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.util.RitualUtil;
import epicsquid.roots.util.types.BlockPosDimension;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockPyre extends Block {

  public static BooleanProperty BURNING = BooleanProperty.create("burning");

  public BlockPyre(Properties props) {
    super(props);
    setDefaultState(getDefaultState().with(BURNING, false));
  }

/*

  @Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    return BlockFaceShape.BOWL;
  }
*/

/*
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(-0.125, 0, -0.125, 1.125, 0.25, 1.125);
  }
*/

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BURNING);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return this.getDefaultState().with(BURNING, false);
  }

  public static void setState(boolean burning, World world, BlockPos pos) {
    if (burning) {
      world.setBlockState(pos, world.getBlockState(pos).with(BURNING, true), 3);
    } else {
      world.setBlockState(pos, world.getBlockState(pos).with(BURNING, false), 3);
    }
  }


  @Override
  @OnlyIn(Dist.CLIENT)
  public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
    if (stateIn.get(BURNING)) {
      world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 1.0F, false);

      List<BlockPos> standingStones = RitualUtil.getNearbyPositions(RitualUtil.Runestone.get(), world, pos, -1);
      for (RitualUtil.RunedWoodType type : RitualUtil.RunedWoodType.values()) {
        standingStones.addAll(RitualUtil.getNearbyPositions(type, world, pos, -1));
      }
      if (!standingStones.isEmpty()) {
        Vector3d me = new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.5, 0.5);
        for (BlockPos runestone : standingStones) {
          if (rand.nextInt(6) == 0) {
            Vector3d origAngle = me.subtract(new Vector3d(runestone.getX(), runestone.getY(), runestone.getZ()).add(0.5 + rand.nextDouble() - 0.5, 0.5 + rand.nextDouble() - 0.5, 0.5 + rand.nextDouble() - 0.5));
            Vector3d angle = origAngle.normalize().scale(0.05);
/*            ClientProxy.particleRenderer.spawnParticle(world, ParticlePyreLeaf.class,
                (double) runestone.getX() + 0.5D,
                (double) runestone.getY() + 0.5D,
                (double) runestone.getZ() + 0.5D,
                angle.x * 0.5f,
                angle.y * 0.5f,
                angle.z * 0.5f,
                (origAngle.lengthSquared() * 10) - 5,
                60 / 255.0 + rand.nextDouble() * 0.05,
                120 / 255.0 + rand.nextDouble() * 0.05,
                60 / 255.0 + rand.nextDouble() * 0.05,
                1f,
                2.5f,
                0,
                me.x,
                me.y,
                me.z,
                0.65
            );*/
          }
        }
      }
    }
  }

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
/*    if (te instanceof TileEntityPyre) {
      TileEntityPyre bon = (TileEntityPyre) te;
      boolean lit = bon.getBurnTime() != 0;

      BlockPosDimension pdos = new BlockPosDimension(pos, worldIn.provider.getDimension());
      RitualBase ritual = ritualCache.get(pdos);
      if (ritual == null || !ritual.isRitualRecipe(bon, null)) {
        ritual = RitualRegistry.getRitual(bon, null);
        if (ritual != null && !ritual.isDisabled()) {
          ritualCache.put(pdos, ritual);
        }
      }
      if (ritual != null) {
        if (ritual.equals(bon.getLastRitualUsed()) && lit) return 5;

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
        if (recipe.equals(bon.getLastRecipeUsed()) && lit) return 5;

        return lit ? 4 : 1;
      }

      return lit ? 3 : 0;
    }*/

    // Empty, no items in it
    return 0;
  }
/*
  @Override
  public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
    super.breakBlock(worldIn, pos, state);*/
}
