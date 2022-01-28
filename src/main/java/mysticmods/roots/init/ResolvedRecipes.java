package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.PlayerOffhandInventoryHandler;
import mysticmods.roots.api.recipe.ResolvingRecipeType;
import mysticmods.roots.recipe.chrysopoeia.ChrysopoeiaRecipe;
import mysticmods.roots.recipe.fey.FeyCrafting;
import mysticmods.roots.recipe.fey.FeyCraftingRecipe;
import mysticmods.roots.recipe.mortar.MortarCrafting;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.crafting.RitualCrafting;
import mysticmods.roots.recipe.pyre.crafting.RitualCraftingRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesCrafting;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.libs.noobutil.type.LazySupplier;

import java.util.Comparator;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class ResolvedRecipes {
  public static final ResolvingRecipeType<PlayerOffhandInventoryHandler, ChrysopoeiaRecipe> CHRYSOPOEIA = new ResolvingRecipeType<>(new LazySupplier<>(() -> ModRecipes.Types.CHRYSOPOEIA), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<FeyCrafting, FeyCraftingRecipe> FEY_CRAFTING = new ResolvingRecipeType<>(new LazySupplier<>(() -> ModRecipes.Types.FEY_CRAFTING), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<MortarCrafting, MortarRecipe> MORTAR = new ResolvingRecipeType<>(new LazySupplier<>(() -> ModRecipes.Types.MORTAR), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<RitualCrafting, RitualCraftingRecipe> RITUAL_CRAFTING = new ResolvingRecipeType<>(new LazySupplier<>(() -> ModRecipes.Types.RITUAL_CRAFTING), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<SummonCreaturesCrafting, SummonCreaturesRecipe> SUMMON_CREATURES = new ResolvingRecipeType<>(new LazySupplier<>(() -> ModRecipes.Types.SUMMON_CREATURES), Comparator.comparing(o -> o.getId().getPath()));

  @SubscribeEvent
  public static void onReloadListeners(AddReloadListenerEvent event) {
    event.addListener(CHRYSOPOEIA);
    event.addListener(FEY_CRAFTING);
    event.addListener(MORTAR);
    event.addListener(RITUAL_CRAFTING);
    event.addListener(SUMMON_CREATURES);
  }
}
