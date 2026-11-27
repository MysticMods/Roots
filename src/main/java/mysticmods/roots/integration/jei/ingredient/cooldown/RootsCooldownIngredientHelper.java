package mysticmods.roots.integration.jei.ingredient.cooldown;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.output.Cooldown;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class RootsCooldownIngredientHelper implements IIngredientHelper<Cooldown> {
  @Override
  public IIngredientType<Cooldown> getIngredientType() {
    return RootsJEIPlugin.COOLDOWN_INGREDIENT;
  }

  @Override
  public String getDisplayName(Cooldown type) {
    return type.asComponent().getString();
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(Cooldown ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(Cooldown type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(Cooldown type) {
    return RootsAPI.rl("cooldown");
  }

  @Override
  public Cooldown copyIngredient(Cooldown type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable Cooldown type) {
    if (type == null) {
      return "null";
    }
    ResourceLocation name = getResourceLocation(type);
    if (name == null) {
      return "unnamed";
    }
    return name.toString();
  }
}