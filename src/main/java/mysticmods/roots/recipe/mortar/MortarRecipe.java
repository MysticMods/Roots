package mysticmods.roots.recipe.mortar;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.MortarBlockEntity;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MortarRecipe extends RootsTileRecipe<MortarInventory, MortarBlockEntity, MortarCrafting> {
  public MortarRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  @Override
  public boolean matches(MortarCrafting pInv, World pLevel) {
/*    MortarInventory inv = pInv.getHandler();
    if (inv.size() < getIngredients().size()) {
      return false;
    }

    Int2BooleanMap map = new Int2BooleanOpenHashMap();
    IntList usedSlots = new IntArrayList();

    List<ItemStack> stacks = inv.getContainedItems();

    outer: for (int i = 0; i < ingredients.size(); i++) {
      IngredientStack stack = ingredients.get(i);
      for (int z = 0; z < stacks.size(); z++) {
        if (usedSlots.contains(z)) {
          continue;
        }

        if (stack.apply(stacks.get(z))) {
          map.put(i, true);
          usedSlots.add(z);
          continue outer;
        }
      }
    }

    if (map.size() != ingredients.size()) {*/
    return false;
/*    }

    return true;*/
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.MORTAR.get();
  }

  @Override
  public IRecipeType<?> getType() {
    return ModRecipes.Types.MORTAR;
  }

  public static class Serializer extends RootsRecipe.Serializer<MortarInventory, MortarCrafting, MortarRecipe> {
    public Serializer() {
      super(MortarRecipe::new);
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    protected Builder(IItemProvider item, int count) {
      super(item, count);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.MORTAR.get();
    }
  }
}
