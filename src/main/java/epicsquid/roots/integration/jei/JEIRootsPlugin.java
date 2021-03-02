package epicsquid.roots.integration.jei;

import epicsquid.roots.Roots;
import epicsquid.roots.config.ElementalSoilConfig;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.jei.carving.*;
import epicsquid.roots.integration.jei.chrysopoeia.ChrysopoeiaCategory;
import epicsquid.roots.integration.jei.chrysopoeia.ChrysopoeiaWrapper;
import epicsquid.roots.integration.jei.fey.FeyCategory;
import epicsquid.roots.integration.jei.fey.FeyWrapper;
import epicsquid.roots.integration.jei.interact.*;
import epicsquid.roots.integration.jei.loot.LootCategory;
import epicsquid.roots.integration.jei.loot.LootWrapper;
import epicsquid.roots.integration.jei.mortar.MortarCategory;
import epicsquid.roots.integration.jei.mortar.MortarWrapper;
import epicsquid.roots.integration.jei.ritual.RitualCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingWrapper;
import epicsquid.roots.integration.jei.ritual.RitualWrapper;
import epicsquid.roots.integration.jei.shears.*;
import epicsquid.roots.integration.jei.spell.SpellCostCategory;
import epicsquid.roots.integration.jei.spell.SpellCostWrapper;
import epicsquid.roots.integration.jei.summon.SummonCreaturesCategory;
import epicsquid.roots.integration.jei.summon.SummonCreaturesWrapper;
import epicsquid.roots.integration.jei.transmutation.TransmutationCategory;
import epicsquid.roots.integration.jei.transmutation.TransmutationWrapper;
import epicsquid.roots.recipe.*;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellChrysopoeia;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.SpellDustInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.util.RitualUtil;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.*;
import java.util.stream.Collectors;

@JEIPlugin
public class JEIRootsPlugin implements IModPlugin {

  public static final String RUNIC_SHEARS = Roots.MODID + ".runic_shears";
  public static final String RUNIC_SHEARS_ENTITY = Roots.MODID + ".runic_shears_entity";
  public static final String RUNIC_SHEARS_SUMMON_ENTITY = Roots.MODID + ".runic_shears_summon_entity";
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
  public static final String LOOT = Roots.MODID + ".loot";
  public static final String BLOCK_BREAK = Roots.MODID + ".block_break";
  public static final String RIGHT_CLICK_BLOCK = Roots.MODID + ".right_click_block";

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new RunicShearsCategory(helper),
        new RitualCraftingCategory(helper),
        new ChrysopoeiaCategory(helper),
        new RitualCategory(helper),
        new FeyCategory(helper),
        new SpellCostCategory(helper),
        new BarkRecipeCategory(helper),
        new MossRecipeCategory(helper),
        new RunicShearsEntityCategory(helper),
        new SummonCreaturesCategory(helper),
        new MortarCategory(helper),
        new TransmutationCategory(helper),
        new RunedWoodCategory(helper),
        new LootCategory(helper),
        new RunicShearsSummonEntityCategory(helper),
        new BlockBreakCategory(helper),
        new BlockRightClickCategory(helper)
    );
  }

  private List<IRecipeWrapper> generateRepairRecipes(Ingredient repairItem, List<ItemStack> itemsToRepair, IVanillaRecipeFactory vanillaFactory) {
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
    registry.handleRecipes(BarkRecipe.class, BarkRecipeWrapper::new, BARK_CARVING);
    registry.handleRecipes(MossRecipe.class, MossRecipeWrapper::new, TERRA_MOSS);
    registry.handleRecipes(SummonCreatureRecipe.class, SummonCreaturesWrapper::new, SUMMON_CREATURES);
    registry.handleRecipes(SummonCreatureIntermediate.class, SummonCreaturesWrapper::new, SUMMON_CREATURES);
    registry.handleRecipes(SummonCreatureIntermediate.class, RunicShearsSummonEntityWrapper::new, RUNIC_SHEARS_SUMMON_ENTITY);
    registry.handleRecipes(ChrysopoeiaRecipe.class, ChrysopoeiaWrapper::new, CHRYSOPOEIA);
    registry.handleRecipes(TransmutationRecipe.class, TransmutationWrapper::new, TRANSMUTATION);
    registry.handleRecipes(RitualUtil.RunedWoodType.class, RunedWoodWrapper::new, RUNED_WOOD);
    registry.handleRecipes(LootWrapper.LootRecipe.class, LootWrapper::new, LOOT);
    registry.handleRecipes(BlockBreakRecipe.class, BlockBreakWrapper::new, BLOCK_BREAK);
    registry.handleRecipes(BlockRightClickRecipe.class, BlockRightClickWrapper::new, RIGHT_CLICK_BLOCK);

    Collection<SpellBase> spells = SpellRegistry.spellRegistry.values().stream().filter(o -> !o.isDisabled()).collect(Collectors.toList());

    List<BlockBreakRecipe> blockBreakRecipes = new ArrayList<>();
    blockBreakRecipes.add(new BlockBreakRecipe(new OreIngredient("tallgrass"), new ItemStack(ModItems.terra_spores)));
    blockBreakRecipes.add(new BlockBreakRecipe(new OreIngredient("tallgrass"), new ItemStack(ModItems.wildroot)));

    registry.addRecipes(blockBreakRecipes, BLOCK_BREAK);

    List<BlockRightClickRecipe> blockRightClickRecipes = new ArrayList<>();
    List<ItemStack> source = new ArrayList<>();
    List<ItemStack> result = new ArrayList<>();
    for (Map.Entry<ItemStack, ItemStack> i : MossConfig.getMossyCobblestones().entrySet()) {
      source.add(i.getKey());
      result.add(i.getValue());
    }
    blockRightClickRecipes.add(new BlockRightClickRecipe(new ItemStack(ModItems.terra_spores), result, source));

    registry.addRecipes(blockRightClickRecipes, RIGHT_CLICK_BLOCK);

    registry.addRecipes(Arrays.asList(new LootWrapper.LootRecipe(ModItems.spirit_bag, SpiritDrops.getPouch()), new LootWrapper.LootRecipe(ModItems.reliquary, SpiritDrops.getReliquary())), LOOT);
    registry.addRecipes(ModRecipes.getRunicShearRecipes().values(), RUNIC_SHEARS);
    registry.addRecipes(ModRecipes.getRunicShearEntityRecipes().values(), RUNIC_SHEARS_ENTITY);
    registry.addRecipes(ModRecipes.getPyreCraftingRecipes().values(), RITUAL_CRAFTING);
    registry.addRecipes(ModRecipes.getMortarRecipes(), MORTAR_AND_PESTLE);
    registry.addRecipes(spells, MORTAR_AND_PESTLE);
    registry.addRecipes(spells, SPELL_COSTS);
    registry.addRecipes(RitualRegistry.ritualRegistry.values(), RITUAL);
    registry.addRecipes(ModRecipes.getFeyCraftingRecipes().values(), FEY_CRAFTING);
    registry.addRecipes(spells, SPELL_MODIFIERS);
    registry.addRecipes(ModRecipes.getBarkRecipes(), BARK_CARVING);
    registry.addRecipes(MossRecipe.getRecipeList(), TERRA_MOSS);
    registry.addRecipes(ModRecipes.getSummonCreatureEntries(), SUMMON_CREATURES);
    registry.addRecipes(ModRecipes.getChrysopoeiaRecipes(), CHRYSOPOEIA);
    registry.addRecipes(ModRecipes.getTransmutationRecipes(), TRANSMUTATION);
    registry.addRecipes(Arrays.asList(RitualUtil.RunedWoodType.values()), RUNED_WOOD);

    ModRecipes.generateLifeEssence();
    List<SummonCreatureIntermediate> summonGenerated = ModRecipes.getLifeEssenceList().stream().map(SummonCreatureIntermediate::create).collect(Collectors.toList());
    registry.addRecipes(summonGenerated, SUMMON_CREATURES);
    registry.addRecipes(summonGenerated, RUNIC_SHEARS_SUMMON_ENTITY);

    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS);
    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS_ENTITY);
    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS_SUMMON_ENTITY);
    registry.addRecipeCatalyst(new ItemStack(ModItems.ritual_summon_creatures), SUMMON_CREATURES);
    registry.addRecipeCatalyst(new ItemStack(ModItems.ritual_transmutation), TRANSMUTATION);
    registry.addRecipeCatalyst(new ItemStack(Blocks.TALLGRASS, 1, BlockTallGrass.EnumType.GRASS.getMeta()), BLOCK_BREAK);
    registry.addRecipeCatalyst(new ItemStack(ModItems.terra_spores), RIGHT_CLICK_BLOCK);

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
    registry.addRecipeCatalyst(new ItemStack(ModItems.reliquary), LOOT);

    // TODO:
    ItemStack spellDust = new ItemStack(ModItems.spell_dust);
    DustSpellStorage.fromStack(spellDust).setSpellToSlot(SpellChrysopoeia.instance);
    registry.addRecipeCatalyst(spellDust, CHRYSOPOEIA);
    ItemStack spellIcon = new ItemStack(ModItems.spell_icon);
    DustSpellStorage.fromStack(spellIcon).setSpellToSlot(SpellChrysopoeia.instance);
    registry.addRecipeCatalyst(spellIcon, CHRYSOPOEIA);

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
      if (stackItem != ModItems.spell_dust && stackItem != ModItems.spell_icon) return ISubtypeRegistry.ISubtypeInterpreter.NONE;
      SpellDustInfo info = DustSpellStorage.fromStack(itemStack).getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell != null) {
        return spell.getName();
      }

      return ISubtypeRegistry.ISubtypeInterpreter.NONE;
    };

    subtypeRegistry.registerSubtypeInterpreter(ModItems.spell_dust, spellInterpreter);
    subtypeRegistry.registerSubtypeInterpreter(ModItems.spell_icon, spellInterpreter);
  }

  public static IJeiRuntime runtime = null;

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    runtime = jeiRuntime;
  }
}
