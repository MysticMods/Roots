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
import net.minecraft.resources.ResourceLocation;
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

@Mod.EventBusSubscriber(modid=RootsAPI.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {
  private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, RootsAPI.MODID);
  public static RegistryObject<RecipeType<PyreRecipe>> PYRE = TYPES.register("pyre", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:pyre";
    }
  });
  public static RegistryObject<RecipeType<SummonCreaturesRecipe>> SUMMON_CREATURES = TYPES.register("summon_creatures", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:summon_creatures";
    }
  });
  public static RegistryObject<RecipeType<MortarRecipe>> MORTAR = TYPES.register("mortar", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:mortar";
    }
  });
  // TODO: Inline these strings into Reference
  public static RegistryObject<RecipeType<GroveRecipe>> GROVE = TYPES.register("grove", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:grove";
    }
  });
  public static RegistryObject<RecipeType<BarkRecipe>> BARK = TYPES.register("bark", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:bark";
    }
  });

  public static RegistryObject<RecipeType<RunicBlockRecipe>> RUNIC_BLOCK = TYPES.register("runic_block", () -> new RecipeType<>() {
    @Override
    public String toString() {
      return "roots:runic_block";
    }
  });

  // TODO: Update everything to use RecipeType.simple
  public static RegistryObject<RecipeType<RunicEntityRecipe>> RUNIC_ENTITY = TYPES.register("runic_entity", () -> RecipeType.simple(new ResourceLocation(RootsAPI.MODID, "runic_entity")));

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
        .save(p, new ResourceLocation(RootsAPI.MODID, "ritual/animal_harvest"));
      PyreRecipe
        .builder(ModRituals.GROVE_SUPPLICATION.get())
        .addIngredient(ItemTags.DOORS)
        .addIngredient(Items.BOWL)
        .addIngredient(ItemTags.SAPLINGS)
        .addIngredient(RootsTags.Items.PETALS)
        .addIngredient(Items.BREAD)
        .unlockedBy("has_door", p.has(ItemTags.DOORS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "ritual/grove_supplication"));
      MortarRecipe.builder(4).addIngredient(net.minecraftforge.common.Tags.Items.SLIMEBALLS).addIngredient(RootsTags.Items.WILDROOT_CROP).addGrant(new Grant(Grant.Type.SPELL, Spells.GROWTH_INFUSION.location())).unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP)).save(p, new ResourceLocation(RootsAPI.MODID, "spell/growth_infusion"));
      MortarRecipe
        .multiBuilder(ModItems.FLOUR.get(), 2)
        .addIngredient(Tags.Items.CROPS_WHEAT)
        .unlockedBy("has_wheat", p.has(Tags.Items.CROPS_WHEAT))
        .save(p, new ResourceLocation(RootsAPI.MODID, "mortar/flour_from_wheat"));
      MortarRecipe
        .multiBuilder(Items.STRING, 6)
        .addIngredient(ItemTags.WOOL)
        .unlockedBy("has_wool", p.has(ItemTags.WOOL))
        .save(p, new ResourceLocation(RootsAPI.MODID, "mortar/string_from_wool"));
      MortarRecipe
        .multiBuilder(Items.FLINT, 6)
        .addIngredient(Tags.Items.GRAVEL)
        .unlockedBy("has_gravel", p.has(Tags.Items.GRAVEL))
        .save(p, new ResourceLocation(RootsAPI.MODID, "mortar/flint_from_gravel"));

      GroveRecipe.builder(new ItemStack(ModItems.GLASS_EYE.get())).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.DUSTS_GLOWSTONE).unlockedBy("has_glowstone", p.has(Tags.Items.DUSTS_GLOWSTONE)).save(p, new ResourceLocation(RootsAPI.MODID, "grove/glass_eye"));
    });
  }

  public static void register(IEventBus bus) {
    TYPES.register(bus);
  }

  @SubscribeEvent
  public static void registerRecipeSerializers(RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
      CraftingHelper.register(new ResourceLocation(RootsAPI.MODID, "excluding_ingredient"), ExcludingIngredient.Serializer.INSTANCE);
    }
  }

  public static void load() {
  }
}
