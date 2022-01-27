package mysticmods.roots.recipe.mortar;

import mysticmods.roots.api.recipe.IBoundlessRecipe;
import mysticmods.roots.api.recipe.IIngredientStackRecipe;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.block.entity.MortarBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.ArrayList;
import java.util.List;

public class MortarRecipe extends RootsRecipe<MortarInventory, MortarBlockEntity, MortarCrafting> {
  public MortarRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  // TODO:
  @Override
  public boolean matches(MortarCrafting pInv, World pLevel) {
    return false;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public IRecipeType<?> getType() {
    return null;
  }
}
