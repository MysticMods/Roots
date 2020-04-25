package epicsquid.roots.integration.jei;

import epicsquid.roots.Roots;
import epicsquid.roots.config.ElementalSoilConfig;
import epicsquid.roots.spell.SpellChrysopoeia;
import epicsquid.roots.spell.info.SpellDustInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.jei.carving.*;
import epicsquid.roots.integration.jei.chrysopoeia.ChrysopoeiaCategory;
import epicsquid.roots.integration.jei.chrysopoeia.ChrysopoeiaWrapper;
import epicsquid.roots.integration.jei.fey.FeyCategory;
import epicsquid.roots.integration.jei.fey.FeyWrapper;
import epicsquid.roots.integration.jei.mortar.MortarCategory;
import epicsquid.roots.integration.jei.mortar.MortarWrapper;
import epicsquid.roots.integration.jei.ritual.RitualCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingWrapper;
import epicsquid.roots.integration.jei.ritual.RitualWrapper;
import epicsquid.roots.integration.jei.shears.RunicShearsCategory;
import epicsquid.roots.integration.jei.shears.RunicShearsEntityCategory;
import epicsquid.roots.integration.jei.shears.RunicShearsEntityWrapper;
import epicsquid.roots.integration.jei.shears.RunicShearsWrapper;
import epicsquid.roots.integration.jei.spell.SpellCostCategory;
import epicsquid.roots.integration.jei.spell.SpellCostWrapper;
import epicsquid.roots.integration.jei.spell.SpellModifierCategory;
import epicsquid.roots.integration.jei.spell.SpellModifierWrapper;
import epicsquid.roots.integration.jei.summon.SummonCreaturesCategory;
import epicsquid.roots.integration.jei.summon.SummonCreaturesWrapper;
import epicsquid.roots.integration.jei.transmutation.TransmutationCategory;
import epicsquid.roots.integration.jei.transmutation.TransmutationWrapper;
import epicsquid.roots.recipe.*;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.RitualUtil;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.oredict.OreIngredient;

import java.util.*;
import java.util.stream.Collectors;

@JEIPlugin
public class JEIRootsPlugin implements IModPlugin {

  public static final String RUNIC_SHEARS = Roots.MODID + ".runic_shears";
  public static final String RUNIC_SHEARS_ENTITY = Roots.MODID + ".runic_shears_entity";
  public static final String BARK_CARVING = Roots.MODID + ".bark_carving";
  public static final String RITUAL_CRAFTING = Roots.MODID + ".ritual_crafting";
  public static final String MORTAR_AND_PESTLE = Roots.MODID + ".mortar_and_pestle";
  public static final String RITUAL = Roots.MODID + ".ritual";
  public static final String FEY_CRAFTING = Roots.MODID + ".fey_crafting";
  public static final String SPELL_COSTS = Roots.MODID + ".spell_costs";
  public static final String SPELL_MODIFIERS = Roots.MODID + ".spell_modifiers";
  public static final String TERRA_MOSS = Roots.MODID + ".terra_moss";
  public static final String SUMMON_CREATURES = Roots.MODID + ".summon_creatures";
  public static final String CHRYSOPOEIA = Roots.MODID + ".chrysopoeia";
  public static final String TRANSMUTATION = Roots.MODID + ".transmutation";
  public static final String RUNED_WOOD = Roots.MODID + ".runed_wood";

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new RunicShearsCategory(helper),
        new RitualCraftingCategory(helper),
        new ChrysopoeiaCategory(helper),
        new RitualCategory(helper),
        new FeyCategory(helper),
        new SpellCostCategory(helper),
        new SpellModifierCategory(helper),
        new BarkRecipeCategory(helper),
        new MossRecipeCategory(helper),
        new RunicShearsEntityCategory(helper),
        new SummonCreaturesCategory(helper),
        new MortarCategory(helper),
        new TransmutationCategory(helper),
        new RunedWoodCategory(helper)
    );
  }

  private List<IRecipeWrapper> generateRepairRecipes (Ingredient repairItem, List<ItemStack> itemsToRepair, IVanillaRecipeFactory vanillaFactory) {
    List<IRecipeWrapper> result = new ArrayList<>();
    boolean first = true;
    for (ItemStack repairMat : repairItem.getMatchingStacks()) {
      for (ItemStack toRepair : itemsToRepair) {
        ItemStack damaged1 = toRepair.copy();
        damaged1.setItemDamage(damaged1.getMaxDamage());
        ItemStack damaged2 = toRepair.copy();
        damaged2.setItemDamage(damaged2.getMaxDamage() * 3 / 4);
        ItemStack damaged3 = toRepair.copy();
        damaged3.setItemDamage(damaged3.getMaxDamage() * 2 / 4);

        if (first) {
          IRecipeWrapper repairWithSame = vanillaFactory.createAnvilRecipe(damaged2, Collections.singletonList(damaged2), Collections.singletonList(damaged3));
          result.add(repairWithSame);
          first = false;
        }

        IRecipeWrapper repairWithMaterial = vanillaFactory.createAnvilRecipe(damaged1, Collections.singletonList(repairMat), Collections.singletonList(damaged2));
        result.add(repairWithMaterial);
      }
    }
    return result;
  }

  @Override
  public void register(IModRegistry registry) {
    IVanillaRecipeFactory vanillaFactory = registry.getJeiHelpers().getVanillaRecipeFactory();

    registry.addRecipes(generateRepairRecipes(new OreIngredient("runestone"), Collections.singletonList(new ItemStack(ModItems.runic_shears)), vanillaFactory), VanillaRecipeCategoryUid.ANVIL);
    registry.addRecipes(generateRepairRecipes(new OreIngredient("plankWood"), Collections.singletonList(new ItemStack(ModItems.wooden_shears)), vanillaFactory), VanillaRecipeCategoryUid.ANVIL);
    registry.addRecipes(generateRepairRecipes(new OreIngredient("rootsBarkWildwood"), Arrays.asList(new ItemStack(ModItems.wildwood_quiver), new ItemStack(ModItems.wildwood_boots), new ItemStack(ModItems.wildwood_bow), new ItemStack(ModItems.wildwood_chestplate), new ItemStack(ModItems.wildwood_helmet), new ItemStack(ModItems.wildwood_leggings)), vanillaFactory), VanillaRecipeCategoryUid.ANVIL);
    registry.addRecipes(generateRepairRecipes(new OreIngredient("rootsBark"), Arrays.asList(new ItemStack(ModItems.living_axe), new ItemStack(ModItems.living_hoe), new ItemStack(ModItems.living_pickaxe), new ItemStack(ModItems.living_pickaxe), new ItemStack(ModItems.living_shovel), new ItemStack(ModItems.living_sword)), vanillaFactory), VanillaRecipeCategoryUid.ANVIL);
    registry.addRecipes(generateRepairRecipes(new OreIngredient("feyLeather"), Arrays.asList(new ItemStack(ModItems.sylvan_boots), new ItemStack(ModItems.sylvan_leggings), new ItemStack(ModItems.sylvan_chestplate), new ItemStack(ModItems.sylvan_helmet)), vanillaFactory), VanillaRecipeCategoryUid.ANVIL);

    registry.handleRecipes(RunicShearRecipe.class, RunicShearsWrapper::new, RUNIC_SHEARS);
    registry.handleRecipes(RunicShearEntityRecipe.class, RunicShearsEntityWrapper::new, RUNIC_SHEARS_ENTITY);
    registry.handleRecipes(PyreCraftingRecipe.class, RitualCraftingWrapper::new, RITUAL_CRAFTING);
    registry.handleRecipes(MortarRecipe.class, MortarWrapper::new, MORTAR_AND_PESTLE);
    registry.handleRecipes(SpellBase.class, MortarWrapper::new, MORTAR_AND_PESTLE);
    registry.handleRecipes(RitualBase.class, RitualWrapper::new, RITUAL);
    registry.handleRecipes(FeyCraftingRecipe.class, FeyWrapper::new, FEY_CRAFTING);
    registry.handleRecipes(SpellBase.class, SpellCostWrapper::new, SPELL_COSTS);
    registry.handleRecipes(SpellBase.class, SpellModifierWrapper::new, SPELL_MODIFIERS);
    registry.handleRecipes(BarkRecipe.class, BarkRecipeWrapper::new, BARK_CARVING);
    registry.handleRecipes(MossRecipe.class, MossRecipeWrapper::new, TERRA_MOSS);
    registry.handleRecipes(SummonCreatureRecipe.class, SummonCreaturesWrapper::new, SUMMON_CREATURES);
    registry.handleRecipes(SummonCreatureIntermediate.class, SummonCreaturesWrapper::new, SUMMON_CREATURES);
    registry.handleRecipes(ChrysopoeiaRecipe.class, ChrysopoeiaWrapper::new, CHRYSOPOEIA);
    registry.handleRecipes(TransmutationRecipe.class, TransmutationWrapper::new, TRANSMUTATION);
    registry.handleRecipes(RitualUtil.RunedWoodType.class, RunedWoodWrapper::new, RUNED_WOOD);

    Collection<SpellBase> spells = SpellRegistry.spellRegistry.values();

    registry.addRecipes(ModRecipes.getRunicShearRecipes().values(), RUNIC_SHEARS);
    registry.addRecipes(ModRecipes.getRunicShearEntityRecipes().values(), RUNIC_SHEARS_ENTITY);
    registry.addRecipes(ModRecipes.getPyreCraftingRecipes().values(), RITUAL_CRAFTING);
    registry.addRecipes(ModRecipes.getMortarRecipes(), MORTAR_AND_PESTLE);
    registry.addRecipes(spells, MORTAR_AND_PESTLE);
    registry.addRecipes(spells, SPELL_COSTS);
    registry.addRecipes(RitualRegistry.ritualRegistry.values(), RITUAL);
    registry.addRecipes(ModRecipes.getFeyCraftingRecipes().values(), FEY_CRAFTING);
    registry.addRecipes(spells.stream().filter(SpellBase::hasModules).collect(Collectors.toList()), SPELL_MODIFIERS);
    registry.addRecipes(ModRecipes.getBarkRecipes(), BARK_CARVING);
    registry.addRecipes(MossRecipe.getRecipeList(), TERRA_MOSS);
    registry.addRecipes(ModRecipes.getSummonCreatureEntries(), SUMMON_CREATURES);
    registry.addRecipes(ModRecipes.getChrysopoeiaRecipes(), CHRYSOPOEIA);
    registry.addRecipes(ModRecipes.getTransmutationRecipes(), TRANSMUTATION);
    registry.addRecipes(Arrays.asList(RitualUtil.RunedWoodType.values()), RUNED_WOOD);

    ModRecipes.generateLifeEssence();
    List<SummonCreatureIntermediate> summonGenerated = ModRecipes.getLifeEssenceList().stream().map(SummonCreatureIntermediate::create).collect(Collectors.toList());
    registry.addRecipes(summonGenerated, SUMMON_CREATURES);

    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS);
    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS_ENTITY);
    registry.addRecipeCatalyst(new ItemStack(ModItems.ritual_summon_creatures), SUMMON_CREATURES);
    registry.addRecipeCatalyst(new ItemStack(ModItems.ritual_transmutation), TRANSMUTATION);

    for (Item knife : ModItems.knives) {
      registry.addRecipeCatalyst(new ItemStack(knife), BARK_CARVING);
      registry.addRecipeCatalyst(new ItemStack(knife), TERRA_MOSS);
      registry.addRecipeCatalyst(new ItemStack(knife), RUNED_WOOD);
    }

    registry.addRecipeCatalyst(new ItemStack(ModBlocks.pyre), RITUAL_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.reinforced_pyre), RITUAL_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.mortar), MORTAR_AND_PESTLE);
    registry.addRecipeCatalyst(new ItemStack(ModItems.pestle), MORTAR_AND_PESTLE);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.pyre), RITUAL);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.reinforced_pyre), RITUAL);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.fey_crafter), FEY_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.staff), SPELL_COSTS);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.imbuer), SPELL_MODIFIERS);
    registry.addRecipeCatalyst(new ItemStack(ModItems.spell_chrysopoeia), CHRYSOPOEIA);

    // TODO:
    ItemStack spellDust = new ItemStack(ModItems.spell_dust);
    DustSpellStorage.fromStack(spellDust).setSpellToSlot(SpellChrysopoeia.instance);
    registry.addRecipeCatalyst(spellDust, CHRYSOPOEIA);

    // Improve this
    registry.addIngredientInfo(new ItemStack(ModItems.terra_spores), VanillaTypes.ITEM, I18n.format("jei.roots.terra_spores.desc"));
    registry.addIngredientInfo(new ItemStack(ModItems.wildroot), VanillaTypes.ITEM, I18n.format("jei.roots.wildroot.desc"));

    // TODO: Try to improve this somehow
    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_log), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_sapling), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood_sapling.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_leaves), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood_leaves.desc"));

    //Elemental Soil Crafting Information Panels
    // TODO: JUST USE TOKENS IDIOT (me)
    String airSoilLocalized = new TextComponentTranslation("jei.roots.elemental_soil_air.desc").getFormattedText();
    airSoilLocalized = airSoilLocalized.replace("@LEVEL", ((Integer) ElementalSoilConfig.AirSoilMinY).toString());
    String earthSoilLocalized = new TextComponentTranslation("jei.roots.elemental_soil_earth.desc").getFormattedText();
    earthSoilLocalized = earthSoilLocalized.replace("@LEVEL", ((Integer) ElementalSoilConfig.EarthSoilMaxY).toString());

    // TODO: Improve these with config
    registry.addIngredientInfo(new ItemStack(ModBlocks.elemental_soil_fire), VanillaTypes.ITEM, I18n.format("jei.roots.elemental_soil_fire.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.elemental_soil_water), VanillaTypes.ITEM, I18n.format("jei.roots.elemental_soil_water.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.elemental_soil_air), VanillaTypes.ITEM, airSoilLocalized);
    registry.addIngredientInfo(new ItemStack(ModBlocks.elemental_soil_earth), VanillaTypes.ITEM, earthSoilLocalized);

    registry.getRecipeTransferRegistry().addRecipeTransferHandler(new FeyCrafterTransfer());
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    ISubtypeRegistry.ISubtypeInterpreter spellInterpreter = itemStack -> {
      Item stackItem = itemStack.getItem();
      if (stackItem != ModItems.spell_dust) return ISubtypeRegistry.ISubtypeInterpreter.NONE;
      // TODO
      SpellDustInfo info = DustSpellStorage.fromStack(itemStack).getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell != null) {
        return spell.getName();
      }

      return ISubtypeRegistry.ISubtypeInterpreter.NONE;
    };

    subtypeRegistry.registerSubtypeInterpreter(ModItems.spell_dust, spellInterpreter);

    // TODO: Handler for Life Essence
  }

  public static IJeiRuntime runtime = null;

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    runtime = jeiRuntime;
  }
}
