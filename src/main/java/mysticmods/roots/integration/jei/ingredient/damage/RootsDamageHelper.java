package mysticmods.roots.integration.jei.ingredient.damage;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.util.TagUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class RootsDamageHelper implements IIngredientHelper<RootsDamageType> {

  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<RootsDamageType> ingredients) {
    Registry<DamageType> registry = Minecraft.getInstance().player.connection.registryAccess()
        .registry(Registries.DAMAGE_TYPE).get();
    return TagUtil.getTagEquivalent(ingredients, o -> o.type().value(), registry::getTags);
  }

  @Override
  public IIngredientType<RootsDamageType> getIngredientType() {
    return RootsJEIPlugin.DAMAGE_TYPE;
  }

  @Override
  public String getDisplayName(RootsDamageType type) {
    // TODO: Improve this
    return type.type().value().msgId();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(RootsDamageType ingredient) {
    return ingredient.type().tags().map(TagKey::location);
  }

  @Override
  public String getUniqueId(RootsDamageType ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(RootsDamageType type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(RootsDamageType type) {
    return Minecraft.getInstance().player.connection.registryAccess().registry(Registries.DAMAGE_TYPE).get()
        .getKey(type.type().value());
  }

  @Override
  public RootsDamageType copyIngredient(RootsDamageType type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable RootsDamageType type) {
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
