package mysticmods.roots.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.client.ClientRecipes;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.integration.jei.categories.*;
import mysticmods.roots.integration.jei.fake.DyeRecipeGenerator;
import mysticmods.roots.integration.jei.fake.GroveWithReputation;
import mysticmods.roots.integration.jei.fake.SproutGiftRecipe;
import mysticmods.roots.integration.jei.ingredient.block.*;
import mysticmods.roots.integration.jei.ingredient.damage.RootsDamageHelper;
import mysticmods.roots.integration.jei.ingredient.damage.RootsDamageRenderer;
import mysticmods.roots.integration.jei.ingredient.damage.RootsDamageType;
import mysticmods.roots.integration.jei.ingredient.dimension.RootsDimensionHelper;
import mysticmods.roots.integration.jei.ingredient.dimension.RootsDimensionRenderer;
import mysticmods.roots.integration.jei.ingredient.dimension.RootsDimensionType;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityHelper;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityRenderer;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityType;
import mysticmods.roots.integration.jei.ingredient.grove.*;
import mysticmods.roots.integration.jei.ingredient.ritual.RootsRitualHelper;
import mysticmods.roots.integration.jei.ingredient.ritual.RootsRitualRenderer;
import mysticmods.roots.integration.jei.ingredient.spell.RootsSpellHelper;
import mysticmods.roots.integration.jei.ingredient.spell.RootsSpellRenderer;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class RootsJEIPlugin implements IModPlugin {
  public static IDrawable INFO_DRAWABLE;

  public static final IIngredientType<RootsEntityType> ENTITY_TYPE = () -> RootsEntityType.class;
  // Block
  public static final IIngredientType<SimpleBlockType> BLOCK_TYPE = () -> SimpleBlockType.class;
  // PartialBlockState
  public static final IIngredientType<BlockStateType> BLOCK_STATE_TYPE = () -> BlockStateType.class;
  public static final IIngredientType<Spell> SPELL_TYPE = () -> Spell.class;
  public static final IIngredientType<Ritual> RITUAL_TYPE = () -> Ritual.class;
  public static final IIngredientType<RootsDimensionType> DIMENSION_TYPE = () -> RootsDimensionType.class;
  public static final IIngredientType<RootsDamageType> DAMAGE_TYPE = () -> RootsDamageType.class;
  public static final IIngredientType<Grove> GROVE_TYPE = () -> Grove.class;
  public static final IIngredientType<GroveNumber> GROVE_NUMBER_TYPE = () -> GroveNumber.class;
  public static final IIngredientType<GroveAction> GROVE_ACTION_TYPE = () -> GroveAction.class;

  @Override
  public ResourceLocation getPluginUid() {
    return RootsAPI.rl("jei");
  }

  public static final RecipeType<GroveRecipe> GROVE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("grove_recipe"), GroveRecipe.class);
  public static final RecipeType<MortarRecipe> MORTAR_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("mortar_recipe"), MortarRecipe.class);
  public static final RecipeType<KnifeRecipe> KNIFE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("knife_recipe"), KnifeRecipe.class);
  public static final RecipeType<PyreRecipe> PYRE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("pyre_recipe"), PyreRecipe.class);
  public static final RecipeType<RunicBlockRecipe> RUNIC_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("runic_recipe"), RunicBlockRecipe.class);
  public static final RecipeType<RunicEntityRecipe> RUNIC_ENTITY_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("runic_entity_recipe"), RunicEntityRecipe.class);
  public static final RecipeType<SproutGiftRecipe> SPROUT_GIFTS_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("sprout_gift_recipe"), SproutGiftRecipe.class);
  public static final RecipeType<SummonCreaturesRecipe> SUMMON_CREATURES_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("summon_creatures_recipe"), SummonCreaturesRecipe.class);
  public static final RecipeType<AnimalHarvestRecipe> ANIMAL_HARVEST_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("animal_harvest_recipe"), AnimalHarvestRecipe.class);
  public static final RecipeType<TransmutationRecipe> TRANSMUTATION_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("transmutation_recipe"), TransmutationRecipe.class);
  public static final RecipeType<GroveWithReputation> GROVE_REPUTATION_ENTRY_TYPE = new RecipeType<>(RootsAPI.rl("grove_reputation_entry"), GroveWithReputation.class);

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

    registration.addRecipeCategories(new GroveCategory(guiHelper));
    registration.addRecipeCategories(new MortarCategory(guiHelper));
    registration.addRecipeCategories(new PyreCategory(guiHelper));
    registration.addRecipeCategories(new KnifeCategory(guiHelper));
    registration.addRecipeCategories(new RunicBlockCategory(guiHelper));
    registration.addRecipeCategories(new RunicEntityCategory(guiHelper));
    registration.addRecipeCategories(new SproutGiftCategory(guiHelper));
    registration.addRecipeCategories(new SummonCreaturesCategory(guiHelper));
    registration.addRecipeCategories(new AnimalHarvestCategory(guiHelper));
    registration.addRecipeCategories(new FungalTransmuterCategory(guiHelper));
    registration.addRecipeCategories(new GroveWithReputationCategory(guiHelper));

    INFO_DRAWABLE = guiHelper.drawableBuilder(RootsAPI.rl("textures/gui/jei/info.png"), 0, 0, 9, 11)
        .setTextureSize(9, 11).build();
  }

  // SORT THEM ALPHABETICALLY >:0
  public static final Comparator<RecipeHolder<?>> RECIPE_COMPARATOR = Comparator.comparing(o -> o.id().toString());

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    Level level = Minecraft.getInstance().level;
    // TODO: Recipe sorting?
    registration.addRecipes(GROVE_RECIPE_TYPE, ResolvedRecipes.GROVE.getRecipes(level).stream()
        .sorted(RECIPE_COMPARATOR).map(RecipeHolder::value)
        .toList());
    registration.addRecipes(MORTAR_RECIPE_TYPE, ResolvedRecipes.MORTAR.getRecipes(level).stream()
        .sorted(RECIPE_COMPARATOR)
        .map(RecipeHolder::value)
        .toList());
    registration.addRecipes(PYRE_RECIPE_TYPE, ResolvedRecipes.PYRE.getRecipes(level).stream().sorted(RECIPE_COMPARATOR)
        .map(RecipeHolder::value)
        .toList());
    registration.addRecipes(KNIFE_RECIPE_TYPE, ResolvedRecipes.KNIFE.getRecipes(level).stream()
        .sorted(RECIPE_COMPARATOR).map(RecipeHolder::value)
        .toList());
    registration.addRecipes(RUNIC_RECIPE_TYPE, ResolvedRecipes.RUNIC_BLOCK.getRecipes(level).stream()
        .sorted(RECIPE_COMPARATOR)
        .map(RecipeHolder::value)
        .toList());
    registration.addRecipes(RUNIC_ENTITY_RECIPE_TYPE, ResolvedRecipes.RUNIC_ENTITY.getRecipes(level).stream()
        .sorted(RECIPE_COMPARATOR)
        .map(RecipeHolder::value)
        .toList());
    registration.addRecipes(RecipeTypes.CRAFTING, DyeRecipeGenerator.generate());
    IVanillaRecipeFactory factory = registration.getJeiHelpers().getVanillaRecipeFactory();
    registration.addRecipes(RecipeTypes.ANVIL, RootsRepairRecipes.getRootsAnvilRepairRecipes(factory, registration.getIngredientManager()));
    registration.addRecipes(SPROUT_GIFTS_RECIPE_TYPE, SproutGiftRecipe.getRecipes());
    registration.addRecipes(SUMMON_CREATURES_RECIPE_TYPE, ResolvedRecipes.SUMMON_CREATURES.getRecipes(level).stream()
        .sorted(RECIPE_COMPARATOR)
        .map(RecipeHolder::value)
        .toList());
    registration.addRecipes(ANIMAL_HARVEST_RECIPE_TYPE, ClientRecipes.ANIMAL_HARVEST_RECIPES);
    registration.addRecipes(TRANSMUTATION_RECIPE_TYPE, ResolvedRecipes.TRANSMUTATION.getRecipes(level).stream()
        .sorted(RECIPE_COMPARATOR)
        .map(RecipeHolder::value)
        .toList());
    List<GroveWithReputation> recipes = new ArrayList<>();
    for (GroveAction entry : RootsRegistries.GROVE_ACTIONS) {
      List<GroveReputationEntry> entries = entry.builtInRegistryHolder().getData(DataMaps.GROVE_ACTION_REPUTATIONS);
      if (entries == null) {
        RootsAPI.LOG.error("Grove action {} has no reputation entries", entry);
        continue;
      }
      for (GroveReputationEntry gEntry : entries) {
        recipes.add(new GroveWithReputation(entry, gEntry));
      }
    }
    registration.addRecipes(GROVE_REPUTATION_ENTRY_TYPE, recipes);
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalysts(GROVE_RECIPE_TYPE, ModBlocks.GROVE_CRAFTER.get(), ModBlocks.GROVE_PEDESTAL.get(), ModBlocks.WILDWOOD_PEDESTAL.get());
    registration.addRecipeCatalysts(MORTAR_RECIPE_TYPE, ModBlocks.MORTAR.get(), ModItems.PESTLE.get());
    registration.addRecipeCatalysts(PYRE_RECIPE_TYPE, ModBlocks.PYRE.get(), ModBlocks.SOUL_PYRE.get(), ModBlocks.REINFORCED_PYRE.get(), ModBlocks.REINFORCED_SOUL_PYRE.get());
    registration.addRecipeCatalysts(KNIFE_RECIPE_TYPE, ModItems.COPPER_KNIFE.get(), ModItems.SILVER_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.NETHERITE_KNIFE.get(), ModItems.STONE_KNIFE.get(), ModItems.WOODEN_KNIFE.get());
    registration.addRecipeCatalyst(ModItems.AUBERGINE.get(), SPROUT_GIFTS_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModItems.RUNIC_SHEARS.get(), RUNIC_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModItems.RUNIC_SHEARS.get(), RUNIC_ENTITY_RECIPE_TYPE);
    registration.addRecipeCatalysts(SUMMON_CREATURES_RECIPE_TYPE, ModItems.RITUAL_SUMMON_CREATURES.get());
    registration.addRecipeCatalyst(ModItems.RITUAL_ANIMAL_HARVEST.get(), ANIMAL_HARVEST_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModItems.FUNGAL_TRANSMUTER.get(), TRANSMUTATION_RECIPE_TYPE);
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistration registration) {
    ISubtypeInterpreter<ItemStack> colorInterpreter = new ISubtypeInterpreter<>() {
      @Override
      public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(ModAttachments.DYEABLE);
      }

      @Override
      public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return "color";
      }
    };

    registration.registerSubtypeInterpreter(ModItems.HERB_POUCH.get(), colorInterpreter);
    registration.registerSubtypeInterpreter(ModItems.APOTHECARY_POUCH.get(), colorInterpreter);
    registration.registerSubtypeInterpreter(ModItems.COMPONENT_POUCH.get(), colorInterpreter);
    registration.registerSubtypeInterpreter(ModItems.SYLVAN_POUCH.get(), colorInterpreter);
  }

  public static final RootsEntityRenderer MAIN_ENTITY_RENDERER = new RootsEntityRenderer(32);
  public static final RootsEntityRenderer SMALL_ENTITY_RENDERER = new RootsEntityRenderer(16);
  public static final RootsBlockRenderer<SimpleBlockType> BLOCK_RENDERER = new RootsBlockRenderer<>();
  public static final RootsBlockRenderer<BlockStateType> BLOCK_STATE_RENDERER = new RootsBlockRenderer<>();
  public static final RootsSpellRenderer SPELL_RENDERER = new RootsSpellRenderer();
  public static final RootsRitualRenderer RITUAL_RENDERER = new RootsRitualRenderer();
  public static final RootsDimensionRenderer DIMENSION_RENDERER = new RootsDimensionRenderer();
  public static final RootsDamageRenderer DAMAGE_RENDERER = new RootsDamageRenderer();
  public static final RootsGroveNumberRenderer GROVE_NUMBER_RENDERER = new RootsGroveNumberRenderer();
  public static final RootsGroveRenderer GROVE_RENDERER = new RootsGroveRenderer();
  public static final RootsGroveActionRenderer GROVE_ACTION_RENDERER = new RootsGroveActionRenderer();

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    registration.register(ENTITY_TYPE, Collections.emptyList(), new RootsEntityHelper(), SMALL_ENTITY_RENDERER, BuiltInRegistries.ENTITY_TYPE.byNameCodec()
        .xmap(RootsEntityType::new, RootsEntityType::entity));
    registration.register(BLOCK_TYPE, Collections.emptyList(), new RootsBlockHelper<>(BLOCK_TYPE), BLOCK_RENDERER, BuiltInRegistries.BLOCK.byNameCodec()
        .xmap(SimpleBlockType::new, IBlockType::block));
    registration.register(BLOCK_STATE_TYPE, Collections.emptyList(), new RootsBlockHelper<>(BLOCK_STATE_TYPE), BLOCK_STATE_RENDERER, PartialBlockState.CODEC
        .xmap(BlockStateType::new, BlockStateType::partial));
    registration.register(SPELL_TYPE, Collections.emptyList(), new RootsSpellHelper(), SPELL_RENDERER, Spell.CODEC);
    registration.register(RITUAL_TYPE, Collections.emptyList(), new RootsRitualHelper(), RITUAL_RENDERER, Ritual.CODEC);
    registration.register(DIMENSION_TYPE, Collections.emptyList(), new RootsDimensionHelper(), DIMENSION_RENDERER, RootsDimensionType.CODEC);
    registration.register(DAMAGE_TYPE, Collections.emptyList(), new RootsDamageHelper(), DAMAGE_RENDERER, RootsDamageType.CODEC);
    registration.register(GROVE_TYPE, Collections.emptyList(), new RootsGroveHelper(), GROVE_RENDERER, RootsRegistries.GROVES.byNameCodec());
    registration.register(GROVE_NUMBER_TYPE, Collections.emptyList(), new RootsGroveNumberHelper(), GROVE_NUMBER_RENDERER, GroveNumber.CODEC);
    registration.register(GROVE_ACTION_TYPE, RootsRegistries.GROVE_ACTIONS.stream()
        .toList(), new RootsGroveActionHelper(), GROVE_ACTION_RENDERER, RootsRegistries.GROVE_ACTIONS.byNameCodec());
  }

  public static IJeiRuntime runtime = null;

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    runtime = jeiRuntime;
  }

  @Override
  public void onRuntimeUnavailable() {
    runtime = null;
  }
}
