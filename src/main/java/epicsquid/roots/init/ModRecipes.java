package epicsquid.roots.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.recipe.CraftingRecipe;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.recipe.PowderPouchFillRecipe;
import epicsquid.roots.recipe.SpellRecipe;
import net.minecraft.init.Blocks;
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
  private static ArrayList<SpellRecipe> spellRecipes = new ArrayList<>();
  private static ArrayList<CraftingRecipe> craftingRecipes = new ArrayList<>();

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
      if (mortarRecipe.matches(recipe.ingredients)) {
        System.out.println("Recipe is already registered with output - " + recipe.getResult().getItem().getUnlocalizedName());
        return;
      }
    }

    mortarRecipes.add(recipe);
  }

  public static SpellRecipe getSpellRecipe(List<ItemStack> items) {
    for (SpellRecipe spellRecipe : spellRecipes) {
      if (spellRecipe.matches(items)) {
        return spellRecipe;
      }
    }
    return null;
  }

  public static SpellRecipe getSpellRecipe(String spell) {
    for (SpellRecipe spellRecipe : spellRecipes) {
      if (spellRecipe.result.compareTo(spell) == 0) {
        return spellRecipe;
      }
    }
    return null;
  }

  private static void addSpellRecipe(SpellRecipe recipe) {
    for (SpellRecipe spellRecipe : spellRecipes) {
      if (spellRecipe.matches(recipe.ingredients)) {
        System.out.println("A Spell Recipe is already registered");
        return;
      }
    }

    spellRecipes.add(recipe);
  }

  public static CraftingRecipe getCraftingRecipe(List<ItemStack> items) {
    for (CraftingRecipe craftingRecipe : craftingRecipes) {
      if (craftingRecipe.matches(items)) {
        return craftingRecipe;
      }
    }
    return null;
  }

  private static void addCraftingRecipe(CraftingRecipe craftingRecipe) {
    for (CraftingRecipe existingRecipe : craftingRecipes) {
      if (existingRecipe.matches(craftingRecipe.getIngredients())) {
        System.out.println("A Crafting Recipe is already registered");
        return;
      }
    }

    craftingRecipes.add(craftingRecipe);
  }

  /**
   * Register all recipes
   */
  public static void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    addMortarRecipe(new MortarRecipe(new ItemStack(Items.DYE, 1, 12), new ItemStack[] { new ItemStack(ModItems.carapace) }, 1, 1, 1, 1, 1, 1));

    initSpellRecipes();
    initCraftingRecipes();

    event.getRegistry().register(new PowderPouchFillRecipe().setRegistryName(getRL("powder_pouch_fill")));

  }

  private static void initCraftingRecipes(){
    addCraftingRecipe(new CraftingRecipe(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.unending_bowl)))
    .addIngredients(new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.mortar)), new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.terra_moss), new ItemStack(ModItems.spirit_herb)));
  }

  private static void initSpellRecipes(){
    addSpellRecipe(new SpellRecipe("spell_wild_fire")
        .addIngredients(new ItemStack(Items.DYE, 1, 14), new ItemStack(Blocks.RED_FLOWER, 1, 5), new ItemStack(Items.GUNPOWDER, 1),
            new ItemStack(Items.COAL, 1, 1), new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(new SpellRecipe("spell_sanctuary")
        .addIngredients(new ItemStack(Items.DYE, 1, 1), new ItemStack(Blocks.RED_FLOWER, 1, 4), new ItemStack(Blocks.VINE, 1),
            new ItemStack(ModItems.moonglow_leaf, 1), new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(new SpellRecipe("spell_dandelion_winds")
        .addIngredients(new ItemStack(Items.FEATHER, 1), new ItemStack(Blocks.YELLOW_FLOWER, 1), new ItemStack(Items.SNOWBALL, 1),
            new ItemStack(ModItems.moonglow_leaf, 1), new ItemStack(Items.WHEAT, 1)));
    addSpellRecipe(new SpellRecipe("spell_rose_thorns")
        .addIngredients(new ItemStack(Blocks.CACTUS, 1), new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), new ItemStack(Items.BONE, 1),
            new ItemStack(Items.FERMENTED_SPIDER_EYE, 1), new ItemStack(ModItems.terra_moss, 1)));
    addSpellRecipe(new SpellRecipe("spell_shatter")
        .addIngredients(new ItemStack(Items.FLINT, 1), new ItemStack(Blocks.RED_FLOWER, 1, 3), new ItemStack(Items.DYE, 1, 15),
            new ItemStack(ModItems.terra_moss, 1), new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(new SpellRecipe("spell_petal_shell")
        .addIngredients(new ItemStack(Items.MELON_SEEDS, 1), new ItemStack(Blocks.DOUBLE_PLANT, 1, 5), new ItemStack(Items.DYE, 1, 9),
            new ItemStack(ModItems.moonglow_leaf, 1), new ItemStack(ModItems.pereskia_bulb, 1)));
    addSpellRecipe(new SpellRecipe("spell_acid_cloud")
        .addIngredients(new ItemStack(Items.SPIDER_EYE, 1), new ItemStack(Blocks.RED_FLOWER, 1, 2), new ItemStack(Items.SLIME_BALL, 1),
            new ItemStack(ModItems.terra_moss, 1), new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(new SpellRecipe("spell_light_drifter")
        .addIngredients(new ItemStack(Items.ENDER_PEARL, 1), new ItemStack(Blocks.RED_FLOWER, 1, 6), new ItemStack(Items.STRING, 1),
            new ItemStack(ModItems.moonglow_leaf, 1), new ItemStack(ModItems.pereskia, 1)));
    addSpellRecipe(new SpellRecipe("spell_time_stop")
        .addIngredients(new ItemStack(Items.NETHER_WART, 1), new ItemStack(Blocks.RED_FLOWER, 1, 8), new ItemStack(Items.DYE, 1, 0),
            new ItemStack(ModItems.pereskia, 1), new ItemStack(ModItems.aubergine_seed, 1)));
    addSpellRecipe(new SpellRecipe("spell_radiance")
        .addIngredients(new ItemStack(Items.GLOWSTONE_DUST, 1), new ItemStack(Blocks.DOUBLE_PLANT, 1, 0), new ItemStack(Items.DYE, 1, 11),
            new ItemStack(ModItems.pereskia, 1), new ItemStack(ModItems.wildroot, 1)));
    addSpellRecipe(new SpellRecipe("spell_life_drain")
        .addIngredients(new ItemStack(Items.BEETROOT, 1), new ItemStack(Blocks.RED_FLOWER, 1, 7), new ItemStack(Items.BEETROOT_SEEDS, 1),
            new ItemStack(Items.ROTTEN_FLESH, 1), new ItemStack(ModItems.pereskia_bulb, 1)));
    addSpellRecipe(new SpellRecipe("spell_growth_infusion")
        .addIngredients(new ItemStack(Blocks.SAPLING, 1, 2), new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), new ItemStack(Blocks.SAPLING, 1, 1),
            new ItemStack(ModItems.terra_moss, 1), new ItemStack(ModItems.pereskia, 1)));
    addSpellRecipe(new SpellRecipe("spell_gravity_boost")
        .addIngredients(new ItemStack(Items.RABBIT_FOOT, 1), new ItemStack(Blocks.RED_FLOWER, 1, 1), new ItemStack(Items.SUGAR, 1),
            new ItemStack(ModItems.aubergine_seed, 1), new ItemStack(ModItems.moonglow_leaf, 1)));
    addSpellRecipe(new SpellRecipe("spell_mind_ward")
        .addIngredients(new ItemStack(Blocks.BROWN_MUSHROOM, 1), new ItemStack(Blocks.RED_FLOWER, 1, 0), new ItemStack(Blocks.RED_MUSHROOM, 1),
            new ItemStack(ModItems.aubergine_seed, 1), new ItemStack(Items.DYE, 1, 3)));
    addSpellRecipe(new SpellRecipe("spell_sense_animals")
        .addIngredients(new ItemStack(Items.CARROT, 1), new ItemStack(Blocks.RED_FLOWER, 1, 0), new ItemStack(ModItems.moonglow_seed, 1),
            new ItemStack(ModItems.moonglow_leaf, 1), new ItemStack(Items.GOLDEN_CARROT, 1)));
    addSpellRecipe(new SpellRecipe("spell_terra_transmutation")
        .addIngredients(new ItemStack(Items.REDSTONE),new ItemStack(Blocks.RED_FLOWER, 1, 0),new ItemStack(Items.DYE),
            new ItemStack(ModItems.terra_moss),new ItemStack(ModItems.wildroot)));
  }
}
