package epicsquid.mysticallib.recipe.factories;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;

public class DelayedOreIngredient extends Ingredient {
  private OreIngredient delayed = null;
  private String oreName;

  public DelayedOreIngredient (String oreName) {
    super(0);
    this.oreName = oreName;
  }

  private void resolve () {
    if (delayed == null) {
      delayed = new OreIngredient(oreName);
    }
  }

  @Override
  public ItemStack[] getMatchingStacks() {
    resolve();
    return delayed.getMatchingStacks();
  }

  @Override
  public boolean apply(@Nullable ItemStack p_apply_1_) {
    resolve();
    return delayed.apply(p_apply_1_);
  }

  @Override
  public IntList getValidItemStacksPacked() {
    resolve();
    return delayed.getValidItemStacksPacked();
  }
}
