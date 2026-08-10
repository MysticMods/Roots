package mysticmods.roots.integration.jei.ingredient.spell;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
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

public class RootsSpellHelper implements IIngredientHelper<Spell> {
  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<Spell> ingredients) {
    Registry<Spell> registry = RootsRegistries.SPELLS;
    return TagUtil.getTagEquivalent(ingredients, Function.identity(), registry::getTags);
  }

  @Override
  public IIngredientType<Spell> getIngredientType() {
    return RootsJEIPlugin.SPELL_TYPE;
  }

  @Override
  public String getDisplayName(Spell type) {
    return type.getName().getString();
  }

  @Override
  public Stream<ResourceLocation> getTagStream(Spell ingredient) {
    return ingredient.builtInRegistryHolder().tags().map(TagKey::location);
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(Spell ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(Spell type, UidContext context) {
    return getResourceLocation(type).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(Spell type) {
    return Objects.requireNonNull(RootsRegistries.SPELLS.getKey(type));
  }

  @Override
  public Spell copyIngredient(Spell type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable Spell type) {
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
  public ItemStack getCheatItemStack(Spell ingredient) {
    return ingredient.getSpellIcon().copy();
  }
}
