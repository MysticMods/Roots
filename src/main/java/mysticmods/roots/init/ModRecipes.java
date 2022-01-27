package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.Roots;
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

public class ModRecipes {
  public static class Serializers {
    // TODO:
    //  public static final RegistryEntry<CrystalWorkbenchRecipe.Serializer> CRYSTAL_WORKBENCH = REGISTRATE.simple("crystal_workbench", IRecipeSerializer.class, CrystalWorkbenchRecipe.Serializer::new);

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
      CHRYSOPOEIA = register(new ResourceLocation(Roots.MODID, "chrysopoeia"));
      FEY_CRAFTING = register(new ResourceLocation(Roots.MODID, "fey_crafting"));
      MORTAR = register(new ResourceLocation(Roots.MODID, "mortar"));
      SUMMON_CREATURES = register(new ResourceLocation(Roots.MODID, "summon_creatures"));
      RITUAL_CRAFTING = register(new ResourceLocation(Roots.MODID, "ritual_crafting"));
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
