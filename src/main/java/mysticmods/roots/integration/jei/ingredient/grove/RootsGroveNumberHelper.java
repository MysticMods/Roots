package mysticmods.roots.integration.jei.ingredient.grove;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveNumber;
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
import java.util.stream.Stream;

public abstract class RootsGroveNumberHelper<T extends IGroveNumber> implements IIngredientHelper<T> {
  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<T> ingredients) {
    Registry<Grove> registry = RootsRegistries.GROVES;
    return TagUtil.getTagEquivalent(ingredients, IGroveNumber::grove, registry::getTags);
  }

  @Override
  public abstract IIngredientType<T> getIngredientType();

  @Override
  public String getDisplayName(T type) {
    return type.grove().getName().getString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(T ingredient) {
    return ingredient.grove().builtInRegistryHolder().tags().map(TagKey::location);
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(T ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(T type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public abstract ResourceLocation getResourceLocation(T type);

  @Override
  public T copyIngredient(T type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable T type) {
    if (type == null) {
      return "null";
    }
    ResourceLocation name = getResourceLocation(type);
    if (name == null) {
      return "unnamed";
    }
    return name.toString();
  }

  public static class Power extends RootsGroveNumberHelper<GrovePower> {

    @Override
    public IIngredientType<GrovePower> getIngredientType() {
      return RootsJEIPlugin.GROVE_POWER_TYPE;
    }

    @Override
    public ResourceLocation getResourceLocation(GrovePower type) {
      return Objects.requireNonNull(RootsRegistries.GROVES.getKey(type.grove())).withSuffix("_power");
    }
  }

  public static class Reputation extends RootsGroveNumberHelper<GroveReputation> {

    @Override
    public IIngredientType<GroveReputation> getIngredientType() {
      return RootsJEIPlugin.GROVE_REPUTATION_TYPE;
    }

    @Override
    public ResourceLocation getResourceLocation(GroveReputation type) {
      return Objects.requireNonNull(RootsRegistries.GROVES.getKey(type.grove())).withSuffix("_reputation");
    }
  }
}
