package mysticmods.roots.integration.jei.ingredient.grove;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GroveNumber;
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

public class RootsGroveNumberHelper implements IIngredientHelper<GroveNumber> {
  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<GroveNumber> ingredients) {
    Registry<Grove> registry = RootsRegistries.GROVES;
    return TagUtil.getTagEquivalent(ingredients, GroveNumber::grove, registry::getTags);
  }

  @Override
  public IIngredientType<GroveNumber> getIngredientType() {
    return RootsJEIPlugin.GROVE_NUMBER_TYPE;
  }

  @Override
  public String getDisplayName(GroveNumber type) {
    return type.grove().getName().getString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(GroveNumber ingredient) {
    return ingredient.grove().builtInRegistryHolder().tags().map(TagKey::location);
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(GroveNumber ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(GroveNumber type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(GroveNumber type) {
    return Objects.requireNonNull(RootsRegistries.GROVES.getKey(type.grove()));
  }

  @Override
  public GroveNumber copyIngredient(GroveNumber type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable GroveNumber type) {
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
