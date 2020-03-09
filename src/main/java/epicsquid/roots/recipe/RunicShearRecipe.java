package epicsquid.roots.recipe;

import epicsquid.roots.recipe.transmutation.BlockStatePredicate;
import epicsquid.roots.recipe.transmutation.StatePredicate;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearRecipe extends RegistryItem {

  private BlockStatePredicate state;
  private IBlockState replacementState;
  private ItemStack drop;
  private ItemStack optionalDisplayItem;

  public RunicShearRecipe(ResourceLocation name, Block state, Block replacementState, ItemStack drop, ItemStack optionalDisplayItem) {
    this(name, new StatePredicate(state.getDefaultState()), replacementState.getDefaultState(), drop, optionalDisplayItem);
  }

  public RunicShearRecipe(ResourceLocation name, BlockStatePredicate state, IBlockState replacementState, ItemStack drop, ItemStack optionalDisplayItem) {
    setRegistryName(name);
    this.state = state;
    this.replacementState = replacementState;
    this.drop = drop;
    this.optionalDisplayItem = optionalDisplayItem;
  }

  public boolean matches (IBlockState state) {
    return this.state.test(state);
  }

  public IBlockState getReplacementState () {
    return replacementState;
  }

  public ItemStack getDrop() {
    return drop;
  }

  public ItemStack getOptionalDisplayItem() {
    return optionalDisplayItem;
  }
}
