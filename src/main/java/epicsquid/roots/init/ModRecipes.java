package epicsquid.roots.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.recipe.PowderPouchFillRecipe;
import epicsquid.roots.recipe.SpellRecipe;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipes {

  private static ArrayList<MortarRecipe> mortarRecipes = new ArrayList<>();
  private static ArrayList<PyreCraftingRecipe> pyreCraftingRecipes = new ArrayList<>();

  private static ResourceLocation getRL(@Nonnull String s) {
    return new ResourceLocation(Roots.MODID + ":" + s);
  }

  private static void registerShapeless(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result, Object... ingredients) {
    registry.register(new ShapelessOreRecipe(getRL(name), result, ingredients).setRegistryName(getRL(name)));
  }

  private static void registerShaped(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result, Object... ingredients) {
    registry.register(new ShapedOreRecipe(getRL(name), result, ingredients).setRegistryName(getRL(name)));
  }

  public static MortarRecipe getMortarRecipe(List<ItemStack> items) {
    for (MortarRecipe mortarRecipe : mortarRecipes) {
      if (mortarRecipe.matches(items)) {
        return mortarRecipe;
      }
    }
    return null;
  }

  private static void addMortarRecipe(MortarRecipe recipe) {
    for (MortarRecipe mortarRecipe : mortarRecipes) {
      if (mortarRecipe.matches(recipe.getIngredients())) {
        System.out.println("Recipe is already registered with output - " + recipe.getResult().getItem().getUnlocalizedName());
        return;
      }
    }

    mortarRecipes.add(recipe);
  }

  public static SpellBase getSpellRecipe(List<ItemStack> items) {
    for (SpellBase spell : SpellRegistry.spellRegistry.values()) {
      if (spell.matchesIngredients(items)) {
        return spell;
      }
    }
    return null;
  }

  public static PyreCraftingRecipe getCraftingRecipe(List<ItemStack> items) {
    for (PyreCraftingRecipe pyreCraftingRecipe : pyreCraftingRecipes) {
      if (pyreCraftingRecipe.matches(items)) {
        return pyreCraftingRecipe;
      }
    }
    return null;
  }

  private static void addCraftingRecipe(PyreCraftingRecipe pyreCraftingRecipe) {
    for (PyreCraftingRecipe existingRecipe : pyreCraftingRecipes) {
      if (existingRecipe.matches(pyreCraftingRecipe.getIngredients())) {
        System.out.println("A Crafting Recipe is already registered");
        return;
      }
    }

    pyreCraftingRecipes.add(pyreCraftingRecipe);
  }

  /**
   * Register all recipes
   */
  public static void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    addMortarRecipe(new MortarRecipe(new ItemStack(Items.DYE, 1, 12), new ItemStack[] { new ItemStack(ModItems.carapace) }, 1, 1, 1, 1, 1, 1));

    initCraftingRecipes();

    event.getRegistry().register(new PowderPouchFillRecipe().setRegistryName(getRL("powder_pouch_fill")));

  }

  private static void initCraftingRecipes(){
    addCraftingRecipe(new PyreCraftingRecipe(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.unending_bowl)))
    .addIngredients(new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.mortar)), new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.terra_moss), new ItemStack(ModItems.spirit_herb)));
  }

}
