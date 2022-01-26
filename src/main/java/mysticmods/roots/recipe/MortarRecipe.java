package mysticmods.roots.recipe;

import mysticmods.roots.api.recipe.IMortarRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class MortarRecipe implements IMortarRecipe {
  private final List<Ingredient> ingredients;
  private final ItemStack result;
  private final List<Processor<


  @Override
  public boolean matches(MortarCrafting pInv, World pLevel) {
    return false;
  }

  @Override
  public ItemStack assemble(MortarCrafting pInv) {
    return null;
  }

  @Override
  public ItemStack getResultItem() {
    return null;
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
}
