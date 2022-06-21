package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.PlayerOffhandInventoryHandler;
import mysticmods.roots.api.recipe.ResolvingRecipeType;
import mysticmods.roots.recipe.chrysopoeia.ChrysopoeiaRecipe;
import mysticmods.roots.recipe.grove.GroveCrafting;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarCrafting;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreCrafting;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesCrafting;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.libs.noobutil.type.LazySupplier;

import java.util.Comparator;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class ResolvedRecipes {
  public static final ResolvingRecipeType<PlayerOffhandInventoryHandler, ChrysopoeiaRecipe> CHRYSOPOEIA = new ResolvingRecipeType<>(new LazySupplier<>(ModRecipes.Types.CHRYSOPOEIA), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<GroveCrafting, GroveRecipe> GROVE = new ResolvingRecipeType<>(new LazySupplier<>(ModRecipes.Types.GROVE), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<MortarCrafting, MortarRecipe> MORTAR = new ResolvingRecipeType<>(new LazySupplier<>(ModRecipes.Types.MORTAR), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<PyreCrafting, PyreRecipe> PYRE = new ResolvingRecipeType<>(new LazySupplier<>(ModRecipes.Types.PYRE), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<SummonCreaturesCrafting, SummonCreaturesRecipe> SUMMON_CREATURES = new ResolvingRecipeType<>(new LazySupplier<>(ModRecipes.Types.SUMMON_CREATURES), Comparator.comparing(o -> o.getId().getPath()));

  @SubscribeEvent
  public static void onReloadListeners(AddReloadListenerEvent event) {
    event.addListener(CHRYSOPOEIA);
    event.addListener(GROVE);
    event.addListener(MORTAR);
    event.addListener(PYRE);
    event.addListener(SUMMON_CREATURES);
  }
}
