package mysticmods.roots.integration.jei.categories.ingredient.block;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mysticmods.roots.util.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class RootsBlockHelper<T extends IBlockType> implements IIngredientHelper<T> {
  private final IIngredientType<T> type;

  public RootsBlockHelper(IIngredientType<T> type) {
    this.type = type;
  }

  @Override
  public Optional<TagKey<?>> getTagKeyEquivalent(Collection<T> ingredients) {
    Registry<Block> registry = BuiltInRegistries.BLOCK;
    return TagUtil.getTagEquivalent(ingredients, T::block, registry::getTags);
  }

  @Override
  public IIngredientType<T> getIngredientType() {
    return type;
  }

  @Override
  public String getDisplayName(T type) {
    return type.block().getName().getString();
  }

  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(T type, UidContext context) {
    return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(type.block())).toString();
  }

  @Override
  public ResourceLocation getResourceLocation(T type) {
    return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(type.block()));
  }

  @Override
  public T copyIngredient(T type) {
    return type;
  }

  @Override
  public String getErrorInfo(@Nullable T type) {
    if (type == null) {
      return "null";
    }
    ResourceLocation name = BuiltInRegistries.BLOCK.getKey(type.block());
    if (name == null) {
      return "unnamed";
    }
    return name.toString();
  }

  @Override
  public ItemStack getCheatItemStack(T ingredient) {
    if (ingredient.stack().isEmpty()) {
      return ItemStack.EMPTY;
    }
    return ingredient.stack().copy();
  }
}
