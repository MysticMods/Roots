package mysticmods.roots.integration.jei.ingredient.grove;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.condition.GroveRankReputation;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.util.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RootsGroveRankHelper implements IIngredientHelper<GroveRankReputation> {
  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<GroveRankReputation> ingredients) {
    Registry<Grove> registry = RootsRegistries.GROVES;
    Collection<Grove> groves = ingredients.stream().map(GroveRankReputation::grove).collect(Collectors.toSet());;
    return TagUtil.getTagEquivalent(groves, Function.identity(), registry::getTags);
  }

  @Override
  public IIngredientType<GroveRankReputation> getIngredientType() {
    return RootsJEIPlugin.GROVE_RANK_TYPE;
  }

  @Override
  public String getDisplayName(GroveRankReputation type) {
    return Component.translatable("roots.grove.ranked_grove", type.grove().getStyledName(), type.minimumRank().getName()).getString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(GroveRankReputation ingredient) {
    return ingredient.grove().builtInRegistryHolder().tags().map(TagKey::location);
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(GroveRankReputation ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(GroveRankReputation type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(GroveRankReputation type) {
    return Objects.requireNonNull(RootsRegistries.GROVES.getKey(type.grove()));
  }

  @Override
  public GroveRankReputation copyIngredient(GroveRankReputation type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable GroveRankReputation type) {
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
