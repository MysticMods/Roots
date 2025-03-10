package mysticmods.roots.integration.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ChanceOutputHelper implements IIngredientHelper<ChanceOutput> {
  @Override
  public IIngredientType<ChanceOutput> getIngredientType() {
    return RootsJEIPlugin.CHANCE_OUTPUT;
  }

  @Override
  public String getDisplayName(ChanceOutput ingredient) {
    return ingredient.getOutput().getItem().getDescription().getString();
  }

  @Override
  public String getUniqueId(ChanceOutput ingredient, UidContext context) {
    return getResourceLocation(ingredient).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(ChanceOutput ingredient) {
    return ingredient.getOutput().getItem().builtInRegistryHolder().key().location();
  }

  @Override
  public ChanceOutput copyIngredient(ChanceOutput ingredient) {
    return new ChanceOutput(ingredient.getOutput().copy(), ingredient.getChance());
  }

  @Override
  public String getErrorInfo(@Nullable ChanceOutput ingredient) {
    if (ingredient == null) {
      return "null";
    }
    ResourceLocation rl = getResourceLocation(ingredient);
    if (rl == null) {
      return "unnamed";
    }
    return rl.toString();
  }
}
