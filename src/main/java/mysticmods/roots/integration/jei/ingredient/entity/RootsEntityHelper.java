package mysticmods.roots.integration.jei.ingredient.entity;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.util.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class RootsEntityHelper implements IIngredientHelper<RootsEntityType> {

  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<RootsEntityType> ingredients) {
		Registry<EntityType<?>> registry = BuiltInRegistries.ENTITY_TYPE;
		return TagUtil.getTagEquivalent(ingredients, RootsEntityType::entity, registry::getTags);
  }

  @Override
  public IIngredientType<RootsEntityType> getIngredientType() {
    return RootsJEIPlugin.ENTITY_TYPE;
  }

  @Override
  public String getDisplayName(RootsEntityType type) {
    return type.entity().getDescription().getString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(RootsEntityType ingredient) {
    return ingredient.entity().getTags().map(TagKey::location);
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(RootsEntityType ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(RootsEntityType type, UidContext context) {
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

  @Override
  public ItemStack getCheatItemStack(RootsEntityType ingredient) {
    SpawnEggItem item = DeferredSpawnEggItem.byId(ingredient.entity());
    if (item == null) {
      return ItemStack.EMPTY;
    }

    return new ItemStack(item);
  }
}
