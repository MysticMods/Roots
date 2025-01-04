package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class GroveRecipe extends RootsTileRecipe<GroveInventoryWrapper, GroveCrafterBlockEntity, GroveCrafting> {
  public GroveRecipe() {
    super();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    //return ModSerializers.GROVE_CRAFTING.get();
    return null;
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.GROVE.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.GROVE_RECIPE_GROUP;
  }
}
