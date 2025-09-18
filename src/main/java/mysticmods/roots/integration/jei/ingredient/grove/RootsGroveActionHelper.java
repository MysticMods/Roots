package mysticmods.roots.integration.jei.ingredient.grove;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.util.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class RootsGroveActionHelper implements IIngredientHelper<GroveAction> {
  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<GroveAction> ingredients) {
    Registry<GroveAction> registry = RootsRegistries.GROVE_ACTIONS;
    return TagUtil.getTagEquivalent(ingredients, Function.identity(), registry::getTags);
  }

  @Override
  public IIngredientType<GroveAction> getIngredientType() {
    return RootsJEIPlugin.GROVE_ACTION_TYPE;
  }

  @Override
  public String getDisplayName(GroveAction type) {
    // TODO:
    return type.toString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(GroveAction ingredient) {
    return ingredient.builtInRegistryHolder().tags().map(TagKey::location);
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(GroveAction ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(GroveAction type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(GroveAction type) {
    return Objects.requireNonNull(RootsRegistries.GROVE_ACTIONS.getKey(type));
  }

  @Override
  public GroveAction copyIngredient(GroveAction type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable GroveAction type) {
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
