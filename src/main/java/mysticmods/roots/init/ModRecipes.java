package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import noobanidus.libs.noobutil.ingredient.ExcludingIngredient;

import static mysticmods.roots.Roots.REGISTRATE;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {
  private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, RootsAPI.MODID);
  // TODO: Inline these ResourceLocations into Reference
  public static RegistryObject<RecipeType<PyreRecipe>> PYRE = TYPES.register("pyre", () -> RecipeType.simple(RootsAPI.rl("pyre")));
  public static RegistryObject<RecipeType<SummonCreaturesRecipe>> SUMMON_CREATURES = TYPES.register("summon_creatures", () -> RecipeType.simple(RootsAPI.rl("summon_creatures")));
  public static RegistryObject<RecipeType<MortarRecipe>> MORTAR = TYPES.register("mortar", () -> RecipeType.simple(RootsAPI.rl("mortar")));
  public static RegistryObject<RecipeType<GroveRecipe>> GROVE = TYPES.register("grove", () -> RecipeType.simple(RootsAPI.rl("grove")));
  public static RegistryObject<RecipeType<BarkRecipe>> BARK = TYPES.register("bark", () -> RecipeType.simple(RootsAPI.rl("bark")));

  public static RegistryObject<RecipeType<RunicBlockRecipe>> RUNIC_BLOCK = TYPES.register("runic_block", () -> RecipeType.simple(RootsAPI.rl("runic_block")));

  public static RegistryObject<RecipeType<RunicEntityRecipe>> RUNIC_ENTITY = TYPES.register("runic_entity", () -> RecipeType.simple(RootsAPI.rl("runic_entity")));

  static {
    REGISTRATE.addDataGenerator(ProviderType.RECIPE, (p) -> {
      PyreRecipe
        .builder(ModRituals.ANIMAL_HARVEST.get())
        .addIngredient(RootsTags.Items.WILDEWHEET_CROP)
        .addIngredient(ItemTags.WOOL)
        .addIngredient(Tags.Items.CROPS_CARROT)
        .addIngredient(Tags.Items.CROPS_POTATO)
        .addIngredient(RootsTags.Items.WILDROOT_CROP)
        .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
        .save(p, RootsAPI.rl("ritual/animal_harvest"));
      PyreRecipe
        .builder(ModRituals.OVERGROWTH.get())
        .addIngredient(RootsTags.Items.BARKS)
        .addIngredient(RootsTags.Items.BARKS)
        .addIngredient(RootsTags.Items.GROVE_MOSS_CROP)
        .addIngredient(Items.SUGAR_CANE)
        .addIngredient(Items.GRASS)
        .unlockedBy("has_sugar_cane", p.has(Items.SUGAR_CANE))
        .save(p, RootsAPI.rl("ritual/overgrowth"));
      PyreRecipe
        .builder(ModRituals.GROVE_SUPPLICATION.get())
        .addIngredient(ItemTags.DOORS)
        .addIngredient(Items.BOWL)
        .addIngredient(ItemTags.SAPLINGS)
        .addIngredient(RootsTags.Items.PETALS)
        .addIngredient(Items.BREAD)
        .unlockedBy("has_door", p.has(ItemTags.DOORS))
        .save(p, RootsAPI.rl("ritual/grove_supplication"));
      PyreRecipe
        .builder(ModRituals.WILDROOT_GROWTH.get())
        .addIngredient(RootsTags.Items.WILDROOT_CROP)
        .addIngredient(RootsTags.Items.BARKS)
        .addIngredient(RootsTags.Items.BARKS)
        .addIngredient(RootsTags.Items.SPIRITLEAF_CROP)
        .addIngredient(ItemTags.SAPLINGS)
        .unlockedBy("has_spiritleaf", p.has(RootsTags.Items.SPIRITLEAF_HERB))
        .addLevelCondition(ModConditions.MATURE_WILDROOT_CROP.get())
        .save(p, RootsAPI.rl("ritual/wildroot_growth"));
      PyreRecipe
        .builder(ModItems.CLOUD_BERRY.get(), 2)
        .addIngredient(Items.LIGHTNING_ROD)
        .addIngredient(Items.SUGAR) // TOOD: Make this a tag?
        .addIngredient(ItemTags.LEAVES)
        .addIngredient(ItemTags.WOOL)
        .addIngredient(RootsTags.Items.ACACIA_BARK)
        .unlockedBy("has_lightning_rod", p.has(Items.LIGHTNING_ROD))
        .save(p, RootsAPI.rl("pyre/cloud_berry"));
      PyreRecipe.builder(ModItems.DEWGONIA.get(), 2)
        .addIngredient(Items.WATER_BUCKET)
        .addIngredient(Items.CLAY_BALL)
        .addIngredient(Items.PUMPKIN)
        .addIngredient(Items.SUGAR_CANE)
        .addIngredient(Items.KELP)
        .unlockedBy("has_kelp", p.has(Items.KELP))
        .save(p, RootsAPI.rl("pyre/dewgonie"));
      PyreRecipe.builder(ModItems.INFERNO_BULB.get(), 2)
        .addIngredient(Items.MAGMA_CREAM)
        .addIngredient(Items.NETHERRACK)
        .addIngredient(ItemTags.COALS)
        .addIngredient(ModItems.FIRE_STARTER.get())
        .addIngredient(Items.BRICK)
        .unlockedBy("has_netherrack", p.has(Items.NETHERRACK))
        .save(p, RootsAPI.rl("pyre/inferno_bulb"));
      PyreRecipe.builder(ModItems.STALICRIPE.get(), 2)
        .addIngredient(Items.TUFF)
        .addIngredient(Items.COBBLED_DEEPSLATE)
        .addIngredient(RootsTags.Items.FLINT)
        .addIngredient(Tags.Items.RAW_MATERIALS_IRON) // TODO: Tag silver???
        .addIngredient(Items.GLOW_LICHEN)
        .unlockedBy("has_glow_lichen", p.has(Items.GLOW_LICHEN))
        .save(p, RootsAPI.rl("pyre/stalicripe"));
      MortarRecipe
        .multiBuilder(ModItems.FLOUR.get(), 2)
        .addIngredient(Tags.Items.CROPS_WHEAT)
        .unlockedBy("has_wheat", p.has(Tags.Items.CROPS_WHEAT))
        .save(p, RootsAPI.rl("mortar/flour_from_wheat"));
      MortarRecipe
        .multiBuilder(Items.STRING, 6)
        .addIngredient(ItemTags.WOOL)
        .unlockedBy("has_wool", p.has(ItemTags.WOOL))
        .save(p, RootsAPI.rl("mortar/string_from_wool"));
      MortarRecipe
        .multiBuilder(Items.FLINT, 6)
        .addIngredient(Tags.Items.GRAVEL)
        .unlockedBy("has_gravel", p.has(Tags.Items.GRAVEL))
        .save(p, RootsAPI.rl("mortar/flint_from_gravel"));
      MortarRecipe
        .multiBuilder(Items.MAGMA_CREAM, 5, 3)
        .addIngredient(Items.MAGMA_BLOCK)
        .unlockedBy("has_magma_block", p.has(Items.MAGMA_BLOCK))
        .save(p, RootsAPI.rl("mortar/magma_cream_from_magma_block"));
      GroveRecipe.builder(new ItemStack(ModItems.GLASS_EYE.get())).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.DUSTS_GLOWSTONE).unlockedBy("has_glowstone", p.has(Tags.Items.DUSTS_GLOWSTONE)).save(p, RootsAPI.rl("grove/glass_eye"));

      // Spells
      MortarRecipe.builder(5)
        .addIngredient(Items.ROTTEN_FLESH)
        .addIngredient(RootsTags.Items.BAFFLECAP_CROP)
        .addIngredient(RootsTags.Items.RUNIC_DUST)
        .addIngredient(Items.SPIDER_EYE)
        .addIngredient(ItemTags.WOOL)
        .unlockedBy("spider_eye", p.has(Items.SPIDER_EYE))
        .addGrant(Grant.spell(ModSpells.ACID_CLOUD.get()))
        .save(p, RootsAPI.rl("spell/acid_cloud"));
      MortarRecipe.builder(5)
        .addIngredient(Items.DANDELION)
        .addIngredient(Tags.Items.CROPS_WHEAT)
        .addIngredient(RootsTags.Items.PETALS)
        .addIngredient(Tags.Items.DYES_YELLOW)
        .addIngredient(Tags.Items.SEEDS)
        .addGrant(Grant.spell(ModSpells.DANDELION_WINDS.get()))
        .unlockedBy("has_dandelion", p.has(Items.DANDELION))
        .save(p, RootsAPI.rl("spell/dandelion_winds"));
      MortarRecipe.builder(5)
        .addIngredient(ItemTags.WOOL)
        .addIngredient(Items.TORCH)
        .addIngredient(Items.JACK_O_LANTERN)
        .addIngredient(RootsTags.Items.COPPER_NUGGET)
        .addIngredient(RootsTags.Items.RUNIC_DUST)
        .unlockedBy("has_torch", p.has(Items.TORCH))
        .addGrant(Grant.spell(ModSpells.FEY_LIGHT.get()))
        .save(p, RootsAPI.rl("spell/fey_light"));
      MortarRecipe.builder(5)
        .addIngredient(RootsTags.Items.PETALS)
        .addIngredient(Items.SHIELD)
        .addIngredient(Tags.Items.INGOTS_IRON)
        .addIngredient(Items.EGG)
        .addIngredient(Tags.Items.GLASS)
        .unlockedBy("has_shield", p.has(Items.SHIELD))
        .addGrant(Grant.spell(ModSpells.PETAL_SHELL.get()))
        .save(p, RootsAPI.rl("spell/petal_shell"));
      MortarRecipe.builder(5)
        .addIngredient(RootsTags.Items.BIRCH_BARK)
        .addIngredient(Items.REDSTONE_TORCH)
        .addIngredient(ItemTags.BOATS)
        .addIngredient(Tags.Items.TOOLS_BOWS)
        .addIngredient(Tags.Items.GUNPOWDER)
        .unlockedBy("has_gunpowder", p.has(Tags.Items.GUNPOWDER))
        .addGrant(Grant.spell(ModSpells.JAUNT.get()))
        .save(p, RootsAPI.rl("spell/jaunt"));
      MortarRecipe.builder(5)
        .addIngredient(Tags.Items.SEEDS)
        .addIngredient(Items.COMPOSTER)
        .addIngredient(Tags.Items.TOOLS_HOES)
        .addIngredient(Items.BONE_MEAL)
        .addIngredient(ItemTags.SMALL_FLOWERS)
        .unlockedBy("has_hoe", p.has(Tags.Items.TOOLS_HOES))
        .addGrant(Grant.spell(ModSpells.GROWTH_INFUSION.get()))
        .save(p, RootsAPI.rl("spell/growth_infusion"));
      MortarRecipe.builder(5)
        .addIngredient(Tags.Items.TOOLS_BOWS)
        .addIngredient(Items.PAPER)
        .addIngredient(Items.LADDER)
        .addIngredient(RootsTags.Items.CLOUD_BERRY_CROP)
        .addIngredient(Items.GRASS)
        .unlockedBy("has_bow", p.has(Tags.Items.TOOLS_BOWS))
        .addGrant(Grant.spell(ModSpells.SKY_SOARER.get()))
        .save(p, RootsAPI.rl("spell/sky_soarer"));
      ShapedRecipeBuilder.shaped(Blocks.MOSS_BLOCK)
        .pattern("XX")
        .pattern("XX")
        .define('X', RootsTags.Items.GROVE_MOSS_CROP)
        .unlockedBy("has_grove_moss", p.has(RootsTags.Items.GROVE_MOSS_CROP))
        .save(p, RootsAPI.rl("moss_block_from_grove_moss"));
    });
  }

  public static void register(IEventBus bus) {
    TYPES.register(bus);
  }

  @SubscribeEvent
  public static void registerRecipeSerializers(RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
      CraftingHelper.register(RootsAPI.rl("excluding_ingredient"), ExcludingIngredient.Serializer.INSTANCE);
    }
  }

  public static void load() {
  }
}
