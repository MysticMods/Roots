package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.tileentity.TileEntityMortar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockMortar extends BlockTEBase {

  public BlockMortar(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return false;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.4375, 0.6875);
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    return BlockFaceShape.BOWL;
  }

  @Override
  public boolean hasComparatorInputOverride(IBlockState state) {
    return true;
  }

  @Override
  /*
    0 = No items or invalid recipe
    1-5 = How many items
    15 = Valid recipe
   */
  public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
    TileEntity te = worldIn.getTileEntity(pos);
    if (te instanceof TileEntityMortar) {
      TileEntityMortar mortar = (TileEntityMortar) te;
      List<ItemStack> ingredients = mortar.getIngredients();
      if (ingredients.isEmpty()) {
        return 0;
      }
      SpellBase spell = ModRecipes.getSpellRecipe(ingredients);
      if (spell != null) {
        return 15;
      }
      MortarRecipe mortarRecipe = ModRecipes.getMortarRecipe(ingredients);
      if (mortarRecipe != null) {
        return 15;
      }
      return ingredients.size();
    }

    return 0;
  }
}
