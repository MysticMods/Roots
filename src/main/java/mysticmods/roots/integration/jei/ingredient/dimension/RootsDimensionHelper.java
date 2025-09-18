package mysticmods.roots.integration.jei.ingredient.dimension;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.util.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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

public class RootsDimensionHelper implements IIngredientHelper<DimensionType> {

  @Override
  public IIngredientType<DimensionType> getIngredientType() {
    return RootsJEIPlugin.DIMENSION_TYPE;
  }

  @Override
  public String getDisplayName(DimensionType type) {
    Component name = Component.translatable("dimension." + type.dimension().location().toString().replace(":", "."));
    return name.getString();
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(DimensionType ingredient, UidContext context) {
    return getUid(ingredient, context);
  }

  @Override
  public String getUid(DimensionType type, UidContext context) {
    return type.dimension().toString();
  }

  @Override
  public ResourceLocation getResourceLocation(DimensionType type) {
    return type.dimension().location();
  }

  @Override
  public DimensionType copyIngredient(DimensionType type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable DimensionType type) {
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
