package mysticmods.roots.recipe.fake;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.item.util.DyeableWithDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DyeRecipeGenerator {
  public static List<RecipeHolder<CraftingRecipe>> generate() {
    List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

    for (Item item : Arrays.asList(ModItems.HERB_POUCH.get(), ModItems.APOTHECARY_POUCH.get(), ModItems.COMPONENT_POUCH.get())) {
      List<ItemStack> colouredPouches = new ArrayList<>();
      ItemStack def = new ItemStack(item);
      def.set(ModAttachments.DYEABLE, DyeableWithDefault.DEFAULT);
      colouredPouches.add(def);
      for (DyeColor dyeColor : DyeColor.values()) {
        ItemStack base = new ItemStack(item);
        base.set(ModAttachments.DYEABLE, DyeableWithDefault.fromColor(dyeColor));
        colouredPouches.add(base);
      }
      Ingredient pouchIngredient = Ingredient.of(colouredPouches.toArray(ItemStack[]::new));
      for (DyeColor dyeColor : DyeColor.values()) {
        NonNullList<Ingredient> recipeIngredients = NonNullList.create();
        recipeIngredients.add(pouchIngredient);
        recipeIngredients.add(Ingredient.of(dyeColor.getTag()));
        ShapedRecipePattern pattern = new ShapedRecipePattern(1, 2, recipeIngredients, Optional.empty());
        ItemStack output = new ItemStack(item);
        output.set(ModAttachments.DYEABLE, DyeableWithDefault.fromColor(dyeColor));
        recipes.add(new RecipeHolder<>(RootsAPI.rl("dye_" + item.builtInRegistryHolder().key().location()
            .getPath() + "_" + dyeColor.getName()), new ShapedRecipe("", CraftingBookCategory.MISC, pattern, output)));
      }
    }

    return recipes;
  }
}
