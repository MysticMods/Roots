package mysticmods.roots.integration.jei.ingredient.ritual;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.util.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class RootsRitualHelper implements IIngredientHelper<Ritual> {
  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<Ritual> ingredients) {
    Registry<Ritual> registry = RootsRegistries.RITUALS;
    return TagUtil.getTagEquivalent(ingredients, Function.identity(), registry::getTags);
  }

  @Override
  public IIngredientType<Ritual> getIngredientType() {
    return RootsJEIPlugin.RITUAL_TYPE;
  }

  @Override
  public String getDisplayName(Ritual type) {
    return type.getName().getString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(Ritual ingredient) {
    return ingredient.builtInRegistryHolder().tags().map(TagKey::location);
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(Ritual ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(Ritual type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(Ritual type) {
    return Objects.requireNonNull(RootsRegistries.RITUALS.getKey(type));
  }

  @Override
  public Ritual copyIngredient(Ritual type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable Ritual type) {
    if (type == null) {
      return "null";
    }
    ResourceLocation name = getResourceLocation(type);
    if (name == null) {
      return "unnamed";
    }
    return name.toString();
  }

  @Override
  public ItemStack getCheatItemStack(Ritual ingredient) {
    return ingredient.getIcon().copy();
  }
}
