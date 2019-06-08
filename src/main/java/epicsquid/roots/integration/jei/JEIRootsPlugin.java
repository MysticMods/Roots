package epicsquid.roots.integration.jei;

import epicsquid.roots.Roots;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.handler.SpellHandler;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.jei.carving.BarkRecipeCategory;
import epicsquid.roots.integration.jei.carving.BarkRecipeWrapper;
import epicsquid.roots.integration.jei.carving.RunicCarvingCategory;
import epicsquid.roots.integration.jei.carving.RunicCarvingWrapper;
import epicsquid.roots.integration.jei.grove.GroveCategory;
import epicsquid.roots.integration.jei.grove.GroveWrapper;
import epicsquid.roots.integration.jei.mortar.MortarCategory;
import epicsquid.roots.integration.jei.mortar.MortarWrapper;
import epicsquid.roots.integration.jei.ritual.RitualCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingWrapper;
import epicsquid.roots.integration.jei.ritual.RitualWrapper;
import epicsquid.roots.integration.jei.shears.RunicShearsCategory;
import epicsquid.roots.integration.jei.shears.RunicShearsWrapper;
import epicsquid.roots.integration.jei.spell.SpellCostCategory;
import epicsquid.roots.integration.jei.spell.SpellCostWrapper;
import epicsquid.roots.integration.jei.spell.SpellModifierCategory;
import epicsquid.roots.integration.jei.spell.SpellModifierWrapper;
import epicsquid.roots.recipe.*;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JEIPlugin
public class JEIRootsPlugin implements IModPlugin {

  public static final String RUNIC_SHEARS = Roots.MODID + ".runic_shears";
  public static final String RUNIC_SHEARS_ENTITY = Roots.MODID + ".runic_shears_entity";
  public static final String BARK_CARVING = Roots.MODID + ".bark_carving";
  public static final String RUNIC_CARVING = Roots.MODID + ".runic_carving";
  public static final String RITUAL_CRAFTING = Roots.MODID + ".ritual_crafting";
  public static final String MORTAR_AND_PESTLE = Roots.MODID + ".mortar_and_pestle";
  public static final String RITUAL = Roots.MODID + ".ritual";
  public static final String GROVE_CRAFTING = Roots.MODID + ".grove_crafting";
  public static final String SPELL_COSTS = Roots.MODID + ".spell_costs";
  public static final String SPELL_MODIFIERS = Roots.MODID + ".spell_modifiers";

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new RunicShearsCategory(helper),
        new RunicCarvingCategory(helper),
        new RitualCraftingCategory(helper),
        new MortarCategory(helper),
        new RitualCategory(helper),
        new GroveCategory(helper),
        new SpellCostCategory(helper),
        new SpellModifierCategory(helper),
        new BarkRecipeCategory(helper)
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
    registry.handleRecipes(GroveCraftingRecipe.class, GroveWrapper::new, GROVE_CRAFTING);
    registry.handleRecipes(SpellBase.class, SpellCostWrapper::new, SPELL_COSTS);
    registry.handleRecipes(SpellBase.class, SpellModifierWrapper::new, SPELL_MODIFIERS);
    registry.handleRecipes(BarkRecipe.class, BarkRecipeWrapper::new, BARK_CARVING);

    Collection<SpellBase> spells = SpellRegistry.spellRegistry.values();

    Map<String, RunicShearRecipe> runicShearRecipes = ModRecipes.getRunicShearRecipes();
    List<RunicShearRecipe> runicShearBlockRecipes = runicShearRecipes.values().stream().filter(RunicShearRecipe::isBlockRecipe).collect(Collectors.toList());

    // TODO:
    List<RunicShearRecipe> runicShearEntityRecipes = runicShearRecipes.values().stream().filter(RunicShearRecipe::isEntityRecipe).collect(Collectors.toList());

    registry.addRecipes(runicShearBlockRecipes, RUNIC_SHEARS);
    registry.addRecipes(ModRecipes.getRunicCarvingRecipes(), RUNIC_CARVING);
    registry.addRecipes(ModRecipes.getPyreCraftingRecipes().values(), RITUAL_CRAFTING);
    registry.addRecipes(ModRecipes.getMortarRecipes(), MORTAR_AND_PESTLE);
    registry.addRecipes(spells, MORTAR_AND_PESTLE);
    registry.addRecipes(spells, SPELL_COSTS);
    registry.addRecipes(RitualRegistry.ritualRegistry.values(), RITUAL);
    registry.addRecipes(ModRecipes.getGroveCraftingRecipes().values(), GROVE_CRAFTING);
    registry.addRecipes(spells.stream().filter(SpellBase::hasModules).collect(Collectors.toList()), SPELL_MODIFIERS);
    registry.addRecipes(ModRecipes.getBarkRecipes(), BARK_CARVING);

    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS);
    registry.addRecipeCatalyst(new ItemStack(ModItems.wood_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.stone_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.iron_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.gold_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.diamond_knife), RUNIC_CARVING);

    registry.addRecipeCatalyst(new ItemStack(ModItems.wood_knife), BARK_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.stone_knife), BARK_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.iron_knife), BARK_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.gold_knife), BARK_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.diamond_knife), BARK_CARVING);

    registry.addRecipeCatalyst(new ItemStack(epicsquid.mysticalworld.init.ModItems.copper_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(epicsquid.mysticalworld.init.ModItems.silver_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(epicsquid.mysticalworld.init.ModItems.amethyst_knife), RUNIC_CARVING);

    registry.addRecipeCatalyst(new ItemStack(epicsquid.mysticalworld.init.ModItems.copper_knife), BARK_CARVING);
    registry.addRecipeCatalyst(new ItemStack(epicsquid.mysticalworld.init.ModItems.silver_knife), BARK_CARVING);
    registry.addRecipeCatalyst(new ItemStack(epicsquid.mysticalworld.init.ModItems.amethyst_knife), BARK_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.bonfire), RITUAL_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.mortar), MORTAR_AND_PESTLE);
    registry.addRecipeCatalyst(new ItemStack(ModItems.pestle), MORTAR_AND_PESTLE);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.bonfire), RITUAL);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.grove_crafter), GROVE_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.staff), SPELL_COSTS);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.imbuer), SPELL_MODIFIERS);

    registry.addIngredientInfo(new ItemStack(ModItems.terra_moss), VanillaTypes.ITEM, I18n.format("jei.roots.terra_moss.desc"));
    registry.addIngredientInfo(new ItemStack(ModItems.terra_spores), VanillaTypes.ITEM, I18n.format("jei.roots.terra_spores.desc"));
    registry.addIngredientInfo(new ItemStack(ModItems.wildroot), VanillaTypes.ITEM, I18n.format("jei.roots.wildroot.desc"));

    /*List<ItemStack> bark = new ArrayList<>();
    bark.add(new ItemStack(ModItems.bark_oak));
    bark.add(new ItemStack(ModItems.bark_acacia));
    bark.add(new ItemStack(ModItems.bark_birch));
    bark.add(new ItemStack(ModItems.bark_dark_oak));
    bark.add(new ItemStack(ModItems.bark_jungle));
    bark.add(new ItemStack(ModItems.bark_spruce));
    bark.add(new ItemStack(ModItems.bark_wildwood));

    registry.addIngredientInfo(bark, VanillaTypes.ITEM, I18n.format("jei.roots.bark.desc"));*/

    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_log), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_sapling), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood_sapling.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_leaves), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood_leaves.desc"));


    //Elemental Soil Crafting Information Panels
    String airSoilLocalized = new TextComponentTranslation("jei.roots.elemental_soil_air.desc").getFormattedText();
    airSoilLocalized = airSoilLocalized.replace("@LEVEL", ((Integer) GeneralConfig.AirSoilMinY).toString());
    String earthSoilLocalized = new TextComponentTranslation("jei.roots.elemental_soil_earth.desc").getFormattedText();
    earthSoilLocalized = earthSoilLocalized.replace("@LEVEL", ((Integer) GeneralConfig.EarthSoilMaxY).toString());

    registry.addIngredientInfo(new ItemStack(ModBlocks.elemental_soil_fire), VanillaTypes.ITEM, I18n.format("jei.roots.elemental_soil_fire.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.elemental_soil_water), VanillaTypes.ITEM, I18n.format("jei.roots.elemental_soil_water.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.elemental_soil_air), VanillaTypes.ITEM, airSoilLocalized);
    registry.addIngredientInfo(new ItemStack(ModBlocks.elemental_soil_earth), VanillaTypes.ITEM, earthSoilLocalized);

    registry.getRecipeTransferRegistry().addRecipeTransferHandler(new GroveCrafterTransfer());
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    ISubtypeRegistry.ISubtypeInterpreter spellInterpreter = itemStack -> {
      Item stackItem = itemStack.getItem();
      if (stackItem != ModItems.spell_dust) return ISubtypeRegistry.ISubtypeInterpreter.NONE;
      SpellBase spell = SpellHandler.fromStack(itemStack).getSelectedSpell();
      if (spell != null) {
        return spell.getName();
      }

      return ISubtypeRegistry.ISubtypeInterpreter.NONE;
    };

    subtypeRegistry.registerSubtypeInterpreter(ModItems.spell_dust, spellInterpreter);
  }
}
