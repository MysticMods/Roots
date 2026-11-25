package mysticmods.roots.integration.jei.ingredient.condition;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.condition.ConditionIngredient;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class RootsConditionIngredientHelper implements IIngredientHelper<ConditionIngredient> {
  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<ConditionIngredient> ingredients) {
    TagKey<?> key = ingredients.stream().filter(o -> o.tag().isPresent()).map(o -> o.tag().get()).findFirst()
        .orElse(null);
    return Optional.ofNullable(key);
  }

  @Override
  public IIngredientType<ConditionIngredient> getIngredientType() {
    return RootsJEIPlugin.CONDITION_INGREDIENT;
  }

  @Override
  public String getDisplayName(ConditionIngredient type) {
    return type.getTitle().getString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(ConditionIngredient ingredient) {
    return ingredient.tag().map(TagKey::location).stream();
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(ConditionIngredient ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(ConditionIngredient type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(ConditionIngredient type) {
    return type.getKey().location();
  }

  @Override
  public ConditionIngredient copyIngredient(ConditionIngredient type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable ConditionIngredient type) {
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