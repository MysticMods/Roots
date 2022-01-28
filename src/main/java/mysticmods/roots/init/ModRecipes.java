package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.recipe.chrysopoeia.ChrysopoeiaRecipe;
import mysticmods.roots.recipe.fey.FeyCraftingRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.crafting.RitualCraftingRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRecipes {
  public static class Serializers {

    public static final RegistryEntry<ChrysopoeiaRecipe.Serializer> CHRYSOPOEIA = REGISTRATE.simple("chrysopoeia", IRecipeSerializer.class, ChrysopoeiaRecipe.Serializer::new);
    public static final RegistryEntry<FeyCraftingRecipe.Serializer> FEY_CRAFTING = REGISTRATE.simple("fey_crafting", IRecipeSerializer.class, FeyCraftingRecipe.Serializer::new);
    public static final RegistryEntry<MortarRecipe.Serializer> MORTAR = REGISTRATE.simple("mortar", IRecipeSerializer.class, MortarRecipe.Serializer::new);
    public static final RegistryEntry<SummonCreaturesRecipe.Serializer> SUMMON_CREATURES = REGISTRATE.simple("summon_creatures", IRecipeSerializer.class, SummonCreaturesRecipe.Serializer::new);
    public static final RegistryEntry<RitualCraftingRecipe.Serializer> RITUAL_CRAFTING = REGISTRATE.simple("ritual_crafting", IRecipeSerializer.class, RitualCraftingRecipe.Serializer::new);

    public static void load () {
    }
  }

  public static class Types {
    public static IRecipeType<ChrysopoeiaRecipe> CHRYSOPOEIA;
    public static IRecipeType<FeyCraftingRecipe> FEY_CRAFTING;
    public static IRecipeType<MortarRecipe> MORTAR;
    public static IRecipeType<SummonCreaturesRecipe> SUMMON_CREATURES;
    public static IRecipeType<RitualCraftingRecipe> RITUAL_CRAFTING;

    public static void register() {
      CHRYSOPOEIA = register(new ResourceLocation(RootsAPI.MODID, "chrysopoeia"));
      FEY_CRAFTING = register(new ResourceLocation(RootsAPI.MODID, "fey_crafting"));
      MORTAR = register(new ResourceLocation(RootsAPI.MODID, "mortar"));
      SUMMON_CREATURES = register(new ResourceLocation(RootsAPI.MODID, "summon_creatures"));
      RITUAL_CRAFTING = register(new ResourceLocation(RootsAPI.MODID, "ritual_crafting"));
    }

    private static <T extends IRecipe<?>> IRecipeType<T> register(final ResourceLocation key) {
      return Registry.register(Registry.RECIPE_TYPE, key, new IRecipeType<T>() {
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
