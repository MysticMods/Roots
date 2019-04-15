package epicsquid.roots.integration.jei;

import java.util.ArrayList;
import java.util.List;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.jei.carving.RunicCarvingCategory;
import epicsquid.roots.integration.jei.carving.RunicCarvingWrapper;
import epicsquid.roots.integration.jei.mortar.MortarCategory;
import epicsquid.roots.integration.jei.mortar.MortarWrapper;
import epicsquid.roots.integration.jei.ritual.RitualCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingWrapper;
import epicsquid.roots.integration.jei.ritual.RitualWrapper;
import epicsquid.roots.integration.jei.shears.RunicShearsCategory;
import epicsquid.roots.integration.jei.shears.RunicShearsWrapper;
import epicsquid.roots.inventory.SpellHolder;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import epicsquid.roots.recipe.SpellRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIRootsPlugin implements IModPlugin {

  public static final String RUNIC_SHEARS = Roots.MODID + ".runic_shears";
  public static final String RUNIC_CARVING = Roots.MODID + ".runic_carving";
  public static final String RITUAL_CRAFTING = Roots.MODID + ".ritual_crafting";
  public static final String MORTAR_AND_PESTLE = Roots.MODID + ".mortar_and_pestle";
  public static final String RITUAL = Roots.MODID + ".ritual";

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(
        new RunicShearsCategory(helper),
        new RunicCarvingCategory(helper),
        new RitualCraftingCategory(helper),
        new MortarCategory(helper),
        new RitualCategory(helper)
    );
  }

  @Override
  public void register(IModRegistry registry) {
    registry.handleRecipes(RunicShearRecipe.class, RunicShearsWrapper::new, RUNIC_SHEARS);
    registry.handleRecipes(RunicCarvingRecipe.class, RunicCarvingWrapper::new, RUNIC_CARVING);
    registry.handleRecipes(PyreCraftingRecipe.class, RitualCraftingWrapper::new, RITUAL_CRAFTING);
    registry.handleRecipes(MortarRecipe.class, MortarWrapper::new, MORTAR_AND_PESTLE);
    registry.handleRecipes(SpellBase.class, MortarWrapper::new, MORTAR_AND_PESTLE);
    registry.handleRecipes(RitualBase.class, RitualWrapper::new, RITUAL);

    registry.addRecipes(ModRecipes.getRunicShearRecipes().values(), RUNIC_SHEARS);
    registry.addRecipes(ModRecipes.getRunicCarvingRecipes(), RUNIC_CARVING);
    registry.addRecipes(ModRecipes.getPyreCraftingRecipes().values(), RITUAL_CRAFTING);
    registry.addRecipes(ModRecipes.getMortarRecipes(), MORTAR_AND_PESTLE);
    registry.addRecipes(SpellRegistry.spellRegistry.values(), MORTAR_AND_PESTLE);
    registry.addRecipes(RitualRegistry.ritualRegistry.values(), RITUAL);

    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS);
    registry.addRecipeCatalyst(new ItemStack(ModItems.wood_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.stone_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.iron_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.gold_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.diamond_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.copper_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.silver_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.bonfire), RITUAL_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.mortar), MORTAR_AND_PESTLE);
    registry.addRecipeCatalyst(new ItemStack(ModItems.pestle), MORTAR_AND_PESTLE);

    registry.addIngredientInfo(new ItemStack(ModItems.terra_moss), VanillaTypes.ITEM, I18n.format("jei.roots.terra_moss.desc"));
    registry.addIngredientInfo(new ItemStack(ModItems.terra_spores), VanillaTypes.ITEM, I18n.format("jei.roots.terra_spores.desc"));
    registry.addIngredientInfo(new ItemStack(ModItems.wildroot), VanillaTypes.ITEM, I18n.format("jei.roots.wildroot.desc"));

    List<ItemStack> bark = new ArrayList<>();
    bark.add(new ItemStack(ModItems.bark_oak));
    bark.add(new ItemStack(ModItems.bark_acacia));
    bark.add(new ItemStack(ModItems.bark_birch));
    bark.add(new ItemStack(ModItems.bark_dark_oak));
    bark.add(new ItemStack(ModItems.bark_jungle));
    bark.add(new ItemStack(ModItems.bark_spruce));
    bark.add(new ItemStack(ModItems.bark_wildwood));

    registry.addIngredientInfo(bark, VanillaTypes.ITEM, I18n.format("jei.roots.bark.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwoodLog), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood.desc"));
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    ISubtypeRegistry.ISubtypeInterpreter spellInterpreter = itemStack -> {
      Item stackItem = itemStack.getItem();
      if (stackItem != ModItems.spell_dust) return ISubtypeRegistry.ISubtypeInterpreter.NONE;
      SpellBase spell = SpellHolder.fromStack(itemStack).getSelectedSpell();
      if (spell != null) {
        return spell.getName();
      }

      return ISubtypeRegistry.ISubtypeInterpreter.NONE;
    };

    subtypeRegistry.registerSubtypeInterpreter(ModItems.spell_dust, spellInterpreter);
  }
}
