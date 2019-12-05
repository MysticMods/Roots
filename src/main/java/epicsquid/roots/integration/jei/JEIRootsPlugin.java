package epicsquid.roots.integration.jei;

import epicsquid.roots.Roots;
import mezz.jei.api.IModPlugin;
import net.minecraft.util.ResourceLocation;

//@JEIPlugin
public abstract class JEIRootsPlugin implements IModPlugin {

  public static final ResourceLocation RUNIC_SHEARS = new ResourceLocation(Roots.MODID, "runic_shears");
  public static final ResourceLocation RUNIC_SHEARS_ENTITY = new ResourceLocation(Roots.MODID, "runic_shears_entity");
  public static final ResourceLocation BARK_CARVING = new ResourceLocation(Roots.MODID, "bark_carving");
  public static final ResourceLocation RUNIC_CARVING = new ResourceLocation(Roots.MODID, "runic_carving");
  public static final ResourceLocation RITUAL_CRAFTING = new ResourceLocation(Roots.MODID, "ritual_crafting");
  public static final ResourceLocation MORTAR_AND_PESTLE = new ResourceLocation(Roots.MODID, "mortar_and_pestle");
  public static final ResourceLocation RITUAL = new ResourceLocation(Roots.MODID, "ritual");
  public static final ResourceLocation FEY_CRAFTING = new ResourceLocation(Roots.MODID, "fey_crafting");
  public static final ResourceLocation SPELL_COSTS = new ResourceLocation(Roots.MODID, "spell_costs");
  public static final ResourceLocation SPELL_MODIFIERS = new ResourceLocation(Roots.MODID, "spell_modifiers");
  public static final ResourceLocation TERRA_MOSS = new ResourceLocation(Roots.MODID, "terra_moss");

/*  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new RunicShearsCategory(helper),
        new RunicCarvingCategory(helper),
        new RitualCraftingCategory(helper),
        new MortarCategory(helper),
        new RitualCategory(helper),
        new FeyCategory(helper),
        new SpellCostCategory(helper),
        new SpellModifierCategory(helper),
        new BarkRecipeCategory(helper),
        new MossRecipeCategory(helper),
        new RunicShearsEntityCategory(helper)
    );
  }

  @Override
  public void register(IModRegistry registry) {
    registry.handleRecipes(RunicShearRecipe.class, RunicShearsWrapper::new, RUNIC_SHEARS);
    registry.handleRecipes(RunicShearEntityRecipe.class, RunicShearsEntityWrapper::new, RUNIC_SHEARS_ENTITY);
    registry.handleRecipes(RunicCarvingRecipe.class, RunicCarvingWrapper::new, RUNIC_CARVING);
    registry.handleRecipes(PyreCraftingRecipe.class, RitualCraftingWrapper::new, RITUAL_CRAFTING);
    registry.handleRecipes(MortarRecipe.class, MortarWrapper::new, MORTAR_AND_PESTLE);
    registry.handleRecipes(SpellBase.class, MortarWrapper::new, MORTAR_AND_PESTLE);
    registry.handleRecipes(RitualBase.class, RitualWrapper::new, RITUAL);
    registry.handleRecipes(FeyCraftingRecipe.class, FeyWrapper::new, FEY_CRAFTING);
    registry.handleRecipes(SpellBase.class, SpellCostWrapper::new, SPELL_COSTS);
    registry.handleRecipes(SpellBase.class, SpellModifierWrapper::new, SPELL_MODIFIERS);
    registry.handleRecipes(BarkRecipe.class, BarkRecipeWrapper::new, BARK_CARVING);
    registry.handleRecipes(MossRecipe.class, MossRecipeWrapper::new, TERRA_MOSS);

    Collection<SpellBase> spells = SpellRegistry.spellRegistry.values();

    // TODO:
    //List<RunicShearRecipe> runicShearEntityRecipes = runicShearRecipes.values().stream().filter(RunicShearRecipe::isEntityRecipe).collect(Collectors.toList());

    registry.addRecipes(ModRecipes.getRunicShearRecipes().values(), RUNIC_SHEARS);
    registry.addRecipes(ModRecipes.getRunicShearEntityRecipes().values(), RUNIC_SHEARS_ENTITY);
    registry.addRecipes(ModRecipes.getRunicCarvingRecipes(), RUNIC_CARVING);
    registry.addRecipes(ModRecipes.getPyreCraftingRecipes().values(), RITUAL_CRAFTING);
    registry.addRecipes(ModRecipes.getMortarRecipes(), MORTAR_AND_PESTLE);
    registry.addRecipes(spells, MORTAR_AND_PESTLE);
    registry.addRecipes(spells, SPELL_COSTS);
    registry.addRecipes(RitualRegistry.ritualRegistry.values(), RITUAL);
    registry.addRecipes(ModRecipes.getFeyCraftingRecipes().values(), FEY_CRAFTING);
    registry.addRecipes(spells.stream().filter(SpellBase::hasModules).collect(Collectors.toList()), SPELL_MODIFIERS);
    registry.addRecipes(ModRecipes.getBarkRecipes(), BARK_CARVING);
    registry.addRecipes(MossRecipe.getRecipeList(), TERRA_MOSS);

    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS);
    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS_ENTITY);

    for (Item knife : ModItems.knives) {
      registry.addRecipeCatalyst(new ItemStack(knife), RUNIC_CARVING);
      registry.addRecipeCatalyst(new ItemStack(knife), BARK_CARVING);
      registry.addRecipeCatalyst(new ItemStack(knife), TERRA_MOSS);
    }

    registry.addRecipeCatalyst(new ItemStack(ModBlocks.bonfire), RITUAL_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.reinforced_bonfire), RITUAL_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.mortar), MORTAR_AND_PESTLE);
    registry.addRecipeCatalyst(new ItemStack(ModItems.pestle), MORTAR_AND_PESTLE);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.bonfire), RITUAL);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.reinforced_bonfire), RITUAL);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.fey_crafter), FEY_CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.staff), SPELL_COSTS);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.imbuer), SPELL_MODIFIERS);

    registry.addIngredientInfo(new ItemStack(ModItems.terra_moss), VanillaTypes.ITEM, I18n.format("jei.roots.terra_moss.desc"));
    registry.addIngredientInfo(new ItemStack(ModItems.terra_spores), VanillaTypes.ITEM, I18n.format("jei.roots.terra_spores.desc"));
    registry.addIngredientInfo(new ItemStack(ModItems.wildroot), VanillaTypes.ITEM, I18n.format("jei.roots.wildroot.desc"));

    for (TransmutationRecipe recipe : ModRecipes.getTransmutationRecipes()) {
      BlockState endState = recipe.getEndState();
      ItemStack endStack = recipe.getEndStack();
      if (endStack != null && !endStack.isEmpty()) {
        registry.addIngredientInfo(endStack, VanillaTypes.ITEM, I18n.format(recipe.getKey()));
      } else if (endState != null) {
        Block endBlock = endState.getBlock();
        ItemStack drop = new ItemStack(Item.getItemFromBlock(endBlock), 1, endBlock.damageDropped(endState));
        if (!drop.isEmpty()) {
          registry.addIngredientInfo(drop, VanillaTypes.ITEM, I18n.format(recipe.getKey()));
        }
      }
    }

    //registry.addIngredientInfo(new ItemStack(ModItems.fey_leather), VanillaTypes.ITEM, I18n.format("jei.roots.fey_leather.source.desc"));

    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_log), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_sapling), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood_sapling.desc"));
    registry.addIngredientInfo(new ItemStack(ModBlocks.wildwood_leaves), VanillaTypes.ITEM, I18n.format("jei.roots.wildwood_leaves.desc"));


    //Elemental Soil Crafting Information Panels
    String airSoilLocalized = new TranslationTextComponent("jei.roots.elemental_soil_air.desc").getFormattedText();
    airSoilLocalized = airSoilLocalized.replace("@LEVEL", ((Integer) ElementalSoilConfig.AirSoilMinY).toString());
    String earthSoilLocalized = new TranslationTextComponent("jei.roots.elemental_soil_earth.desc").getFormattedText();
    earthSoilLocalized = earthSoilLocalized.replace("@LEVEL", ((Integer) ElementalSoilConfig.EarthSoilMaxY).toString());

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
      SpellBase spell = SpellHandler.fromStack(itemStack).getSelectedSpell();
      if (spell != null) {
        return spell.getName();
      }

      return ISubtypeRegistry.ISubtypeInterpreter.NONE;
    };

    subtypeRegistry.registerSubtypeInterpreter(ModItems.spell_dust, spellInterpreter);
  }*/
}
