package mysticmods.roots.integration.jei.ingredient.grove;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.grove.Grove;
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

public class RootsGroveHelper implements IIngredientHelper<Grove> {
  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<Grove> ingredients) {
    Registry<Grove> registry = RootsRegistries.GROVES;
    return TagUtil.getTagEquivalent(ingredients, Function.identity(), registry::getTags);
  }

  @Override
  public IIngredientType<Grove> getIngredientType() {
    return RootsJEIPlugin.GROVE_TYPE;
  }

  @Override
  public String getDisplayName(Grove type) {
    return type.getName().getString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(Grove ingredient) {
    return ingredient.builtInRegistryHolder().tags().map(TagKey::location);
  }

  @Override
  public String getUniqueId(Grove ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(Grove type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(Grove type) {
    return Objects.requireNonNull(RootsRegistries.GROVES.getKey(type));
  }

  @Override
  public Grove copyIngredient(Grove type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable Grove type) {
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
