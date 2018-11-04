package epicsquid.roots.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.recipe.PowderPouchFillRecipe;
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

  private static ArrayList<MortarRecipe> mortarRecipes = new ArrayList<>();
  private static ArrayList<SpellRecipe> spellRecipes = new ArrayList<>();

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
    for (int i = 0; i < mortarRecipes.size(); i++) {
      if (mortarRecipes.get(i).matches(items)) {
        return mortarRecipes.get(i);
      }
    }
    return null;
  }

  public static void addMortarRecipe(MortarRecipe recipe) {
    for (int i = 0; i < mortarRecipes.size(); i++) {
      if (mortarRecipes.get(i).matches(recipe.ingredients)) {
        System.out.println("Recipe is already registered with output - " + recipe.getResult().getItem().getUnlocalizedName());
        return;
      }
    }

    mortarRecipes.add(recipe);
  }

  public static SpellRecipe getSpellRecipe(List<ItemStack> items) {
    for (int i = 0; i < spellRecipes.size(); i++) {
      if (spellRecipes.get(i).matches(items)) {
        return spellRecipes.get(i);
      }
    }
    return null;
  }

  public static SpellRecipe getSpellRecipe(String spell) {
    for (int i = 0; i < spellRecipes.size(); i++) {
      if (spellRecipes.get(i).result.compareTo(spell) == 0) {
        return spellRecipes.get(i);
      }
    }
    return null;
  }

  public static void addSpellRecipe(SpellRecipe recipe) {
    for (int i = 0; i < spellRecipes.size(); i++) {
      if (spellRecipes.get(i).matches(recipe.ingredients)) {
        System.out.println("A Spell Recipe is already registered");
        return;
      }
    }

    spellRecipes.add(recipe);
  }

  /**
   * Register all recipes
   */
  public static void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    addMortarRecipe(new MortarRecipe(new ItemStack(Items.DYE, 1, 12), new ItemStack[] { new ItemStack(ModItems.carapace) }, 1, 1, 1, 1, 1, 1));

    addSpellRecipe(
        new SpellRecipe("spell_wild_fire").addIngredient(new ItemStack(Items.DYE, 1, 14)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 5))
            .addIngredient(new ItemStack(Items.GUNPOWDER, 1)).addIngredient(new ItemStack(Items.COAL, 1, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_sanctuary").addIngredient(new ItemStack(Items.DYE, 1, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 4))
            .addIngredient(new ItemStack(Blocks.VINE, 1)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_dandelion_winds").addIngredient(new ItemStack(Items.FEATHER, 1)).addIngredient(new ItemStack(Blocks.YELLOW_FLOWER, 1))
            .addIngredient(new ItemStack(Items.SNOWBALL, 1)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(Items.WHEAT, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_rose_thorns").addIngredient(new ItemStack(Blocks.CACTUS, 1)).addIngredient(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4))
            .addIngredient(new ItemStack(Items.BONE, 1)).addIngredient(new ItemStack(Items.FERMENTED_SPIDER_EYE, 1))
            .addIngredient(new ItemStack(ModItems.terra_moss, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_shatter").addIngredient(new ItemStack(Items.FLINT, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 3))
            .addIngredient(new ItemStack(Items.DYE, 1, 15)).addIngredient(new ItemStack(ModItems.terra_moss, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_petal_shell").addIngredient(new ItemStack(Items.MELON_SEEDS, 1)).addIngredient(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5))
            .addIngredient(new ItemStack(Items.DYE, 1, 9)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(ModItems.pereskia_bulb, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_acid_cloud").addIngredient(new ItemStack(Items.SPIDER_EYE, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 2))
            .addIngredient(new ItemStack(Items.SLIME_BALL, 1)).addIngredient(new ItemStack(ModItems.terra_moss, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_light_drifter").addIngredient(new ItemStack(Items.ENDER_PEARL, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 6))
            .addIngredient(new ItemStack(Items.STRING, 1)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(ModItems.pereskia, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_time_stop").addIngredient(new ItemStack(Items.NETHER_WART, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 8))
            .addIngredient(new ItemStack(Items.DYE, 1, 0)).addIngredient(new ItemStack(ModItems.pereskia, 1))
            .addIngredient(new ItemStack(ModItems.aubergine_seed, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_radiance").addIngredient(new ItemStack(Items.GLOWSTONE_DUST, 1)).addIngredient(new ItemStack(Blocks.DOUBLE_PLANT, 1, 0))
            .addIngredient(new ItemStack(Items.DYE, 1, 11)).addIngredient(new ItemStack(ModItems.pereskia, 1))
            .addIngredient(new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_life_drain").addIngredient(new ItemStack(Items.BEETROOT, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 7))
            .addIngredient(new ItemStack(Items.BEETROOT_SEEDS, 1)).addIngredient(new ItemStack(Items.ROTTEN_FLESH, 1))
            .addIngredient(new ItemStack(ModItems.pereskia_bulb, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_growth_infusion").addIngredient(new ItemStack(Blocks.SAPLING, 1, 2)).addIngredient(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1))
            .addIngredient(new ItemStack(Blocks.SAPLING, 1, 1)).addIngredient(new ItemStack(ModItems.terra_moss, 1))
            .addIngredient(new ItemStack(ModItems.pereskia, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_gravity_boost").addIngredient(new ItemStack(Items.RABBIT_FOOT, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 1))
            .addIngredient(new ItemStack(Items.SUGAR, 1)).addIngredient(new ItemStack(ModItems.aubergine_seed, 1))
            .addIngredient(new ItemStack(ModItems.moonglow_leaf, 1)));
    addSpellRecipe(
        new SpellRecipe("spell_mind_ward").addIngredient(new ItemStack(Blocks.BROWN_MUSHROOM, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 0))
            .addIngredient(new ItemStack(Blocks.RED_MUSHROOM, 1)).addIngredient(new ItemStack(ModItems.aubergine_seed, 1))
            .addIngredient(new ItemStack(Items.DYE, 1, 3)));
    addSpellRecipe(
        new SpellRecipe("spell_sense_animals").addIngredient(new ItemStack(Items.CARROT, 1)).addIngredient(new ItemStack(Blocks.RED_FLOWER, 1, 0))
            .addIngredient(new ItemStack(ModItems.moonglow_seed, 1)).addIngredient(new ItemStack(ModItems.moonglow_leaf, 1))
            .addIngredient(new ItemStack(Items.GOLDEN_CARROT, 1)));

    event.getRegistry().register(new PowderPouchFillRecipe().setRegistryName(getRL("powder_pouch_fill")));

  }
}
