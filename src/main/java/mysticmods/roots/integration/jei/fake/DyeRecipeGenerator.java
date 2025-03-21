package mysticmods.roots.integration.jei.fake;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DyeRecipeGenerator {
  public static List<RecipeHolder<CraftingRecipe>> generate () {
    List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

    for (Item item : Arrays.asList(ModItems.HERB_POUCH.get())) {
      List<ItemStack> colouredPouches = new ArrayList<>();
      for (DyeColor dyeColor : DyeColor.values()) {
        ItemStack base = new ItemStack(item);
        base.set(DataComponents.BASE_COLOR, dyeColor);
        colouredPouches.add(base);
      }
      Ingredient pouchIngredient = Ingredient.of(colouredPouches.toArray(ItemStack[]::new));
      for (DyeColor dyeColor : DyeColor.values()) {
        NonNullList<Ingredient> recipeIngredients = NonNullList.create();
        recipeIngredients.add(pouchIngredient);
        recipeIngredients.add(Ingredient.of(dyeColor.getTag()));
        ShapedRecipePattern pattern = new ShapedRecipePattern(1, 2, recipeIngredients, Optional.empty());
        ItemStack output = new ItemStack(item);
        output.set(DataComponents.BASE_COLOR, dyeColor);
        recipes.add(new RecipeHolder<>(RootsAPI.rl("dye_" + item.builtInRegistryHolder().key().location().getPath() + "_" + dyeColor.getName()), new ShapedRecipe("", CraftingBookCategory.MISC, pattern, output)));
      }
    }

    return recipes;
  }
}
