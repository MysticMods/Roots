package epicsquid.roots.init;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.recipe.RecipeRegistry;
import epicsquid.roots.recipe.SpellRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRecipes {

  private static ResourceLocation getRL(@Nonnull String s) {
    return new ResourceLocation(Roots.MODID + ":" + s);
  }

  private static void registerShapeless(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result, Object... ingredients) {
    registry.register(new ShapelessOreRecipe(getRL(name), result, ingredients).setRegistryName(getRL(name)));
  }

  private static void registerShaped(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String name, @Nonnull ItemStack result, Object... ingredients) {
    registry.register(new ShapedOreRecipe(getRL(name), result, ingredients).setRegistryName(getRL(name)));
  }

  /**
   * Register all recipes
   */
  public static void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    RecipeRegistry.addMortarRecipe(new MortarRecipe(new ItemStack(Items.DYE, 1, 12), new ItemStack[] { new ItemStack(ModItems.carapace) }, 1, 1, 1, 1, 1, 1));

    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_orange_tulip").addIngredient(new ItemStack(Items.DYE, 1, 14)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 5))
            .addIngredient(new ItemStack(Items.GUNPOWDER, 1)).addIngredient(new ItemStack(Items.COAL, 1, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_red_tulip").addIngredient(new ItemStack(Items.DYE, 1, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 4))
            .addIngredient(new ItemStack(Blocks.VINE, 1)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_dandelion").addIngredient(new ItemStack(Items.FEATHER, 1)).addIngredient(new ItemStack(Blocks.YELLOW_FLOWER, 1))
            .addIngredient(new ItemStack(Items.SNOWBALL, 1)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(Items.WHEAT, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_rose").addIngredient(new ItemStack(Blocks.CACTUS, 1)).addIngredient(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4))
            .addIngredient(new ItemStack(Items.BONE, 1)).addIngredient(new ItemStack(Items.FERMENTED_SPIDER_EYE, 1))
            .addIngredient(new ItemStack(ModItems.terra_moss, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_azure_bluet").addIngredient(new ItemStack(Items.FLINT, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 3))
            .addIngredient(new ItemStack(Items.DYE, 1, 15)).addIngredient(new ItemStack(ModItems.terra_moss, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_peony").addIngredient(new ItemStack(Items.MELON_SEEDS, 1)).addIngredient(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5))
            .addIngredient(new ItemStack(Items.DYE, 1, 9)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(ModItems.pereskia_bulb, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_allium").addIngredient(new ItemStack(Items.SPIDER_EYE, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 2))
            .addIngredient(new ItemStack(Items.SLIME_BALL, 1)).addIngredient(new ItemStack(ModItems.terra_moss, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_white_tulip").addIngredient(new ItemStack(Items.ENDER_PEARL, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 6))
            .addIngredient(new ItemStack(Items.STRING, 1)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(ModItems.pereskia, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_oxeye_daisy").addIngredient(new ItemStack(Items.NETHER_WART, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 8))
            .addIngredient(new ItemStack(Items.DYE, 1, 0)).addIngredient(new ItemStack(ModItems.pereskia, 1))
            .addIngredient(new ItemStack(ModItems.aubergine_seed, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_sunflower").addIngredient(new ItemStack(Items.GLOWSTONE_DUST, 1)).addIngredient(new ItemStack(Blocks.DOUBLE_PLANT, 1, 0))
            .addIngredient(new ItemStack(Items.DYE, 1, 11)).addIngredient(new ItemStack(ModItems.pereskia, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_pink_tulip").addIngredient(new ItemStack(Items.BEETROOT, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 7))
            .addIngredient(new ItemStack(Items.BEETROOT_SEEDS, 1)).addIngredient(new ItemStack(Items.ROTTEN_FLESH, 1))
            .addIngredient(new ItemStack(ModItems.pereskia_bulb, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_lilac").addIngredient(new ItemStack(Blocks.SAPLING, 1, 2)).addIngredient(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1))
            .addIngredient(new ItemStack(Blocks.SAPLING, 1, 1)).addIngredient(new ItemStack(ModItems.terra_moss, 1))
            .addIngredient(new ItemStack(ModItems.pereskia, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_blue_orchid").addIngredient(new ItemStack(Items.RABBIT_FOOT, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 1))
            .addIngredient(new ItemStack(Items.SUGAR, 1)).addIngredient(new ItemStack(ModItems.aubergine_seed, 1))
            .addIngredient(new ItemStack(ModItems.moonglow_leaf, 1)));
    RecipeRegistry.addSpellRecipe(
        new SpellRecipe("spell_poppy").addIngredient(new ItemStack(Blocks.BROWN_MUSHROOM, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 0))
            .addIngredient(new ItemStack(Blocks.RED_MUSHROOM, 1)).addIngredient(new ItemStack(ModItems.aubergine_seed, 1))
            .addIngredient(new ItemStack(Items.DYE, 1, 3)));

  }
}
