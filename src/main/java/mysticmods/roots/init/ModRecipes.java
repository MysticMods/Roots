package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.RootsTags;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.Grant;
import mysticmods.roots.api.spells.Spells;
import mysticmods.roots.recipe.chrysopoeia.ChrysopoeiaRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRecipes {
  static {
    REGISTRATE.addDataGenerator(ProviderType.RECIPE, (p) -> {
      PyreRecipe.builder(ModRituals.TRANSMUTATION.get()).addIngredient(Tags.Items.COBBLESTONE).addIngredient(Tags.Items.COBBLESTONE).addIngredient(Tags.Items.COBBLESTONE).addIngredient(Tags.Items.COBBLESTONE).addIngredient(Tags.Items.COBBLESTONE).build(p, new ResourceLocation(RootsAPI.MODID, "ritual/transmutation"));
      PyreRecipe.builder(ModItems.Herbs.MOONGLOW_LEAF.get(), 1).addIngredient(ItemTags.LEAVES).addIngredient(Tags.Items.GLASS).addIngredient(Tags.Items.GEMS_QUARTZ).addIngredient(RootsTags.Items.BIRCH_BARK).addIngredient(RootsTags.Items.BIRCH_BARK).build(p, new ResourceLocation(RootsAPI.MODID, "moonglow_leaf"));
      PyreRecipe.builder(ModRituals.ANIMAL_HARVEST.get()).addIngredient(RootsTags.Items.WILDEWHEET_CROP).addIngredient(ItemTags.WOOL).addIngredient(Tags.Items.CROPS_CARROT).addIngredient(Tags.Items.SLIMEBALLS).addIngredient(RootsTags.Items.WILDROOT_CROP).build(p, new ResourceLocation(RootsAPI.MODID, "ritual/animal_harvest"));
      MortarRecipe.builder(4).addIngredient(Tags.Items.SLIMEBALLS).addIngredient(RootsTags.Items.WILDROOT_CROP).addGrant(new Grant(Grant.GrantType.SPELL, Spells.GROWTH_INFUSION.location())).build(p, new ResourceLocation(RootsAPI.MODID, "spell/growth_infusion"));
    });
  }

  public static class Serializers {

    public static final RegistryEntry<ChrysopoeiaRecipe.Serializer> CHRYSOPOEIA = REGISTRATE.simple("chrysopoeia", RecipeSerializer.class, ChrysopoeiaRecipe.Serializer::new);
    public static final RegistryEntry<GroveRecipe.Serializer> GROVE_CRAFTING = REGISTRATE.simple("grove", RecipeSerializer.class, GroveRecipe.Serializer::new);
    public static final RegistryEntry<MortarRecipe.Serializer> MORTAR = REGISTRATE.simple("mortar", RecipeSerializer.class, MortarRecipe.Serializer::new);
    public static final RegistryEntry<SummonCreaturesRecipe.Serializer> SUMMON_CREATURES = REGISTRATE.simple("summon_creatures", RecipeSerializer.class, SummonCreaturesRecipe.Serializer::new);
    public static final RegistryEntry<PyreRecipe.Serializer> PYRE = REGISTRATE.simple("pyre", RecipeSerializer.class, PyreRecipe.Serializer::new);

    public static void load() {
    }
  }

  public static class Types {
    private static final DeferredRegister<RecipeType<?>> SERIALIZER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, RootsAPI.MODID);

    public static RegistryObject<RecipeType<ChrysopoeiaRecipe>> CHRYSOPOEIA = SERIALIZER.register("chrysopoeia", () -> new RecipeType<>() {
      @Override
      public String toString() {
        return "roots:chrysopoeia";
      }
    });

    public static RegistryObject<RecipeType<GroveRecipe>> GROVE = SERIALIZER.register("grove", () -> new RecipeType<>() {
      @Override
      public String toString() {
        return "roots:grove";
      }
    });

    public static RegistryObject<RecipeType<MortarRecipe>> MORTAR = SERIALIZER.register("mortar", () -> new RecipeType<>() {
      @Override
      public String toString() {
        return "roots:mortar";
      }
    });

    public static RegistryObject<RecipeType<SummonCreaturesRecipe>> SUMMON_CREATURES = SERIALIZER.register("summon_creatures", () -> new RecipeType<>() {
      @Override
      public String toString() {
        return "roots:summon_creatures";
      }
    });

    public static RegistryObject<RecipeType<PyreRecipe>> PYRE = SERIALIZER.register("pyre", () -> new RecipeType<>() {
      @Override
      public String toString() {
        return "roots:pyre";
      }
    });

    public static void register(IEventBus bus) {
      SERIALIZER.register(bus);
    }
  }

  public static void load() {
    Serializers.load();
  }
}
