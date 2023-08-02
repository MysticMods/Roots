package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.reference.Spells;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import net.minecraft.core.Registry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
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
        .addIngredient(net.minecraftforge.common.Tags.Items.CROPS_CARROT)
        .addIngredient(net.minecraftforge.common.Tags.Items.SLIMEBALLS)
        .addIngredient(RootsTags.Items.WILDROOT_CROP)
        .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
        .save(p, RootsAPI.rl("ritual/animal_harvest"));
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
      MortarRecipe.builder(4).addIngredient(net.minecraftforge.common.Tags.Items.SLIMEBALLS).addIngredient(RootsTags.Items.WILDROOT_CROP).addGrant(new Grant(Grant.Type.SPELL, Spells.GROWTH_INFUSION.location())).unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP)).save(p, RootsAPI.rl("spell/growth_infusion"));
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
