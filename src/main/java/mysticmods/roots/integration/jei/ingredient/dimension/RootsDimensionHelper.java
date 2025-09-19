package mysticmods.roots.integration.jei.ingredient.dimension;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class RootsDimensionHelper implements IIngredientHelper<RootsDimensionType> {

  @Override
  public IIngredientType<RootsDimensionType> getIngredientType() {
    return RootsJEIPlugin.DIMENSION_TYPE;
  }

  @Override
  public String getDisplayName(RootsDimensionType type) {
    Component name = type.getName();
    return name.getString();
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(RootsDimensionType ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(RootsDimensionType type, UidContext context) {
    return type.dimension().toString();
  }

  @Override
  public ResourceLocation getResourceLocation(RootsDimensionType type) {
    return type.dimension().location();
  }

  @Override
  public RootsDimensionType copyIngredient(RootsDimensionType type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable RootsDimensionType type) {
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
