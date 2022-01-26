package mysticmods.roots.recipe;

import mysticmods.roots.api.recipe.IMortarRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.ArrayList;
import java.util.List;

public class MortarRecipe implements IMortarRecipe {
  private final NonNullList<Ingredient> ingredients;
  private final ItemStack result;
  private final List<Processor<MortarCrafting>> processors = new ArrayList<>();
  private final ResourceLocation recipeId;

  public MortarRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  @Override
  public boolean matches(MortarCrafting pInv, World pLevel) {
    return false;
  }

  @Override
  public ItemStack assemble(MortarCrafting pInv) {
    ItemStack resultCopy = getResultItem();
    for (Processor<MortarCrafting> processor : processors) {
      //resultCopy = processor.processOutput(resultCopy, getIngredients(), null, pInv);
    }
    return resultCopy;
  }

  @Override
  public ItemStack getResultItem() {
    return result.copy();
  }

  @Override
  public ResourceLocation getId() {
    return null;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public IRecipeType<?> getType() {
    return null;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }
}
