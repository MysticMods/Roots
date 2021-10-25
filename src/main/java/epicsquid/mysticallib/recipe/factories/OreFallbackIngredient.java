package epicsquid.mysticallib.recipe.factories;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OreFallbackIngredient extends Ingredient {
  private Ingredient fallbackIngredient;
  private DelayedOreIngredient oreIngredient;

  public OreFallbackIngredient(String oreIngredient, Ingredient fallbackIngredient) {
    super(0);
    if (OreDictionary.getOres(oreIngredient, false).isEmpty()) {
      this.oreIngredient = null;
    } else {
      this.oreIngredient = new DelayedOreIngredient(oreIngredient);
    }
    this.fallbackIngredient = fallbackIngredient;
  }

  public OreFallbackIngredient(String oreIngredient, String fallbackIngredient) {
    if (OreDictionary.getOres(oreIngredient, false).isEmpty()) {
      this.oreIngredient = null;
    } else {
      this.oreIngredient = new DelayedOreIngredient(oreIngredient);
    }
    this.fallbackIngredient = new OreIngredient(fallbackIngredient);
  }

  @Override
  public ItemStack[] getMatchingStacks() {
    if (this.oreIngredient == null) {
      return this.fallbackIngredient.getMatchingStacks();
    } else {
      return this.oreIngredient.getMatchingStacks();
    }
  }

  @Override
  public boolean apply(@Nullable ItemStack p_apply_1_) {
    if (this.oreIngredient == null) {
      return this.fallbackIngredient.apply(p_apply_1_);
    } else {
      return this.oreIngredient.apply(p_apply_1_);
    }
  }

  @Override
  public IntList getValidItemStacksPacked() {
    if (this.oreIngredient == null) {
      return this.fallbackIngredient.getValidItemStacksPacked();
    } else {
      return this.oreIngredient.getValidItemStacksPacked();
    }
  }

  public static class Factory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
      String ore = JsonUtils.getString(json, "ore");
      if (JsonUtils.hasField(json, "fallback") && !JsonUtils.isString(json, "fallback")) {
        Ingredient fallback = CraftingHelper.getIngredient(JsonUtils.getJsonObject(json, "fallback"), context);
        return new OreFallbackIngredient(ore, fallback);
      } else {
        return new OreFallbackIngredient(ore, JsonUtils.getString(json, "fallback"));
      }
    }
  }
}
