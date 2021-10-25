package epicsquid.roots.recipe;

import epicsquid.roots.recipe.transmutation.BlockStatePredicate;
import epicsquid.roots.recipe.transmutation.StatePredicate;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearRecipe extends RegistryItem {

  protected BlockStatePredicate state;
  protected BlockState replacementState;
  protected ItemStack drop;
  protected ItemStack optionalDisplayItem;
  protected Ingredient dropMatch;

  public RunicShearRecipe(ResourceLocation name, Block state, Block replacementState, ItemStack drop, ItemStack optionalDisplayItem) {
    this(name, new StatePredicate(state.getDefaultState()), replacementState.getDefaultState(), drop, optionalDisplayItem);
  }

  public RunicShearRecipe(ResourceLocation name, BlockStatePredicate state, BlockState replacementState, ItemStack drop, ItemStack optionalDisplayItem) {
    setRegistryName(name);
    this.state = state;
    this.replacementState = replacementState;
    this.drop = drop;
    this.dropMatch = Ingredient.fromStacks(drop);
    this.optionalDisplayItem = optionalDisplayItem;
  }

  public boolean matches(BlockState state) {
    return this.state.test(state);
  }

  public BlockState getReplacementState() {
    return replacementState;
  }

  public ItemStack getDrop() {
    return drop;
  }

  public Ingredient getDropMatch() {
    return dropMatch;
  }

  public ItemStack getOptionalDisplayItem() {
    return optionalDisplayItem;
  }
}
