package mysticmods.roots.integration.jei.categories.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

public class RootsEntityHelper implements IIngredientHelper<RootsEntityType> {

  @Override
  public IIngredientType<RootsEntityType> getIngredientType() {
    return RootsJEIPlugin.ENTITY_TYPE;
  }

  @Override
  public String getDisplayName(RootsEntityType type) {
    return type.entity().getDescription().getString();
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(RootsEntityType type, UidContext context) {
    return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(type.entity())).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(RootsEntityType type) {
    return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(type.entity()));
  }

  @Override
  public RootsEntityType copyIngredient(RootsEntityType type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable RootsEntityType type) {
    if (type == null) {
      return "null";
    }
    ResourceLocation name = BuiltInRegistries.ENTITY_TYPE.getKey(type.entity());
    if (name == null) {
      return "unnamed";
    }
    return name.toString();
  }
}
