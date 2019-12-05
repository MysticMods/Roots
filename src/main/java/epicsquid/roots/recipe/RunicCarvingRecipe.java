package epicsquid.roots.recipe;

import javax.annotation.Nonnull;

import epicsquid.roots.api.Herb;
import net.minecraft.block.BlockState;

/**
 * Used to decide how a runic carving using a knife and herb should work.
 */
public class RunicCarvingRecipe {

  private final BlockState carvingBlock;
  private final BlockState runeBlock;
  private final Herb herb;
  private final String name;

  /**
   * Defines a runic carving recipe
   *
   * @param carvingBlock The block the recipe starts with
   * @param runeBlock    The block the recipe creates
   * @param herb         The herb needed
   */
  public RunicCarvingRecipe(@Nonnull BlockState carvingBlock, @Nonnull BlockState runeBlock, @Nonnull Herb herb, @Nonnull String name) {
    this.carvingBlock = carvingBlock;
    this.runeBlock = runeBlock;
    this.herb = herb;
    this.name = name;
  }

  public BlockState getCarvingBlock() {
    return carvingBlock;
  }

  public BlockState getRuneBlock() {
    return runeBlock;
  }

  public Herb getHerb() {
    return herb;
  }

  public String getName() {
    return name;
  }

  public boolean matches(RunicCarvingRecipe recipe) {
    return this.getCarvingBlock().equals(recipe.getCarvingBlock())
        && this.getRuneBlock().equals(recipe.getRuneBlock())
        && this.getHerb().equals(recipe.getHerb());
  }
}
