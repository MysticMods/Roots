package epicsquid.roots.block;

import epicsquid.mysticallib.block.Block;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.tileentity.TileEntityMortar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockMortar extends Block {

  public BlockMortar(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
  }

  @Override
  public boolean isFullCube(@Nonnull BlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull BlockState state) {
    return false;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.4375, 0.6875);
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    return BlockFaceShape.BOWL;
  }

  @Override
  public boolean hasComparatorInputOverride(BlockState state) {
    return true;
  }

  @Override
  /*
    0 = No items or invalid recipe
    1-5 = How many items
    15 = Valid recipe
   */
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
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
