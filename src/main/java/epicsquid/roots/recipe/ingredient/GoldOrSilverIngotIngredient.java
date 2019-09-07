package epicsquid.roots.recipe.ingredient;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreIngredient;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GoldOrSilverIngotIngredient extends Ingredient {
  private OreIngredient gold;
  private OreIngredient silver;
  private IntList packed;

  private OreIngredient getGold() {
    if (gold == null) {
      gold = new OreIngredient("ingotGold");
    }
    return gold;
  }

  private OreIngredient getSilver() {
    if (silver == null) {
      silver = new OreIngredient("ingotSilver");
    }
    return silver;
  }

  public GoldOrSilverIngotIngredient() {
  }

  @Override
  public ItemStack[] getMatchingStacks() {
    return ArrayUtils.addAll(getGold().getMatchingStacks(), getSilver().getMatchingStacks());
  }

  @Override
  public IntList getValidItemStacksPacked() {
    if (packed == null) {
      packed = new IntArrayList();
      packed.addAll(getGold().getValidItemStacksPacked());
      packed.addAll(getSilver().getValidItemStacksPacked());
    }
    return packed;
  }

  @Override
  public boolean apply(@Nullable ItemStack p_apply_1_) {
    return getGold().apply(p_apply_1_) || getSilver().apply(p_apply_1_);
  }

  @SuppressWarnings("unused")
  public static class Factory implements IIngredientFactory {
    GoldOrSilverIngotIngredient ingredient;

    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
      if (ingredient == null) {
        ingredient = new GoldOrSilverIngotIngredient();
      }

      return ingredient;
    }
  }
}
