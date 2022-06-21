package mysticmods.roots.recipe.property;

import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RitualPropertyRecipe implements Recipe<PropertyContainer> {
  private final ResourceLocation recipeId;
  private final Property.RitualProperty<?> property;

  public RitualPropertyRecipe(ResourceLocation recipeId, Property.RitualProperty<?> property) {
    this.recipeId = recipeId;
    this.property = property;
  }

  @Override
  public boolean matches(PropertyContainer pContainer, Level pLevel) {
    return false;
  }

  @Override
  public ItemStack assemble(PropertyContainer pContainer) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }

  @Override
  public ItemStack getResultItem() {
    return ItemStack.EMPTY;
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public RecipeType<?> getType() {
    return null;
  }
}
