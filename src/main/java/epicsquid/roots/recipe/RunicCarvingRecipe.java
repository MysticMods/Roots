package epicsquid.roots.recipe;

import javax.annotation.Nonnull;

import epicsquid.roots.api.Herb;
import net.minecraft.block.state.IBlockState;

/**
 * Used to decide how a runic carving using a knife and petal pouch should work.
 */
public class RunicCarvingRecipe {

  private final IBlockState carvingBlock;
  private final IBlockState runeBlock;
  private final Herb herb;

  /**
   * Defines a runic carving recipe
   * @param carvingBlock The block the recipe starts with
   * @param runeBlock The block the recipe creates
   * @param herb The herb needed in the petal pouch
   */
  public RunicCarvingRecipe(@Nonnull IBlockState carvingBlock, @Nonnull IBlockState runeBlock, @Nonnull Herb herb) {
    this.carvingBlock = carvingBlock;
    this.runeBlock = runeBlock;
    this.herb = herb;
  }

  public IBlockState getCarvingBlock() {
    return carvingBlock;
  }

  public IBlockState getRuneBlock() {
    return runeBlock;
  }

  public Herb getHerb() {
    return herb;
  }

  public boolean matches(RunicCarvingRecipe recipe) {
    return this.getCarvingBlock().equals(recipe.getCarvingBlock())
        && this.getRuneBlock().equals(recipe.getRuneBlock())
        && this.getHerb().equals(recipe.getHerb());
  }
}
