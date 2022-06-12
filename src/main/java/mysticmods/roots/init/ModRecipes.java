package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.recipe.chrysopoeia.ChrysopoeiaRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.Tags;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRecipes {
  static {
    REGISTRATE.addDataGenerator(ProviderType.RECIPE, (p) -> {
      PyreRecipe.builder(ModRituals.TRANSMUTATION.get()).addIngredient(Tags.Items.COBBLESTONE).addIngredient(Tags.Items.COBBLESTONE).addIngredient(Tags.Items.COBBLESTONE).addIngredient(Tags.Items.COBBLESTONE).addIngredient(Tags.Items.COBBLESTONE).build(p, new ResourceLocation(RootsAPI.MODID, "ritual/recipe/transmutation"));
    });
  }

  public static class Serializers {

    public static final RegistryEntry<ChrysopoeiaRecipe.Serializer> CHRYSOPOEIA = REGISTRATE.simple("chrysopoeia", RecipeSerializer.class, ChrysopoeiaRecipe.Serializer::new);
    public static final RegistryEntry<GroveRecipe.Serializer> GROVE_CRAFTING = REGISTRATE.simple("grove", RecipeSerializer.class, GroveRecipe.Serializer::new);
    public static final RegistryEntry<MortarRecipe.Serializer> MORTAR = REGISTRATE.simple("mortar", RecipeSerializer.class, MortarRecipe.Serializer::new);
    public static final RegistryEntry<SummonCreaturesRecipe.Serializer> SUMMON_CREATURES = REGISTRATE.simple("summon_creatures", RecipeSerializer.class, SummonCreaturesRecipe.Serializer::new);
    public static final RegistryEntry<PyreRecipe.Serializer> PYRE = REGISTRATE.simple("pyre", RecipeSerializer.class, PyreRecipe.Serializer::new);

    public static void load () {
    }
  }

  public static class Types {
    public static RecipeType<ChrysopoeiaRecipe> CHRYSOPOEIA;
    public static RecipeType<GroveRecipe> GROVE;
    public static RecipeType<MortarRecipe> MORTAR;
    public static RecipeType<SummonCreaturesRecipe> SUMMON_CREATURES;
    public static RecipeType<PyreRecipe> PYRE;

    public static void register() {
      CHRYSOPOEIA = register(new ResourceLocation(RootsAPI.MODID, "chrysopoeia"));
      GROVE = register(new ResourceLocation(RootsAPI.MODID, "fey_crafting"));
      MORTAR = register(new ResourceLocation(RootsAPI.MODID, "mortar"));
      SUMMON_CREATURES = register(new ResourceLocation(RootsAPI.MODID, "summon_creatures"));
      PYRE = register(new ResourceLocation(RootsAPI.MODID, "ritual_crafting"));
    }

    private static <T extends Recipe<?>> RecipeType<T> register(final ResourceLocation key) {
      return Registry.register(Registry.RECIPE_TYPE, key, new RecipeType<T>() {
        public String toString() {
          return key.toString();
        }
      });
    }

    public static void load () {
    }
  }

  public static void load() {
    Serializers.load();
    Types.load();
  }
}
