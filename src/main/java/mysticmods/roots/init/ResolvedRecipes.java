package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.type.ResolvingRecipeType;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.recipe.grove.GroveCrafting;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarCrafting;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreCrafting;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.libs.noobutil.type.LazySupplier;

import java.util.Comparator;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class ResolvedRecipes {
  public static final ResolvingRecipeType<GroveCrafting, GroveRecipe> GROVE = new ResolvingRecipeType<>(LazySupplier.of(ModRecipes.GROVE), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<MortarCrafting, MortarRecipe> MORTAR = new ResolvingRecipeType<>(LazySupplier.of(ModRecipes.MORTAR), Comparator.comparing(o -> o.getId().getPath()));
  public static final ResolvingRecipeType<PyreCrafting, PyreRecipe> PYRE = new ResolvingRecipeType<>(LazySupplier.of(ModRecipes.PYRE), Comparator.comparing(o -> o.getId().getPath()));
/*  public static final ResolvingRecipeType<SummonCreaturesCrafting, SummonCreaturesRecipe> SUMMON_CREATURES = new ResolvingRecipeType<>(LazySupplier.of(ModRecipes.SUMMON_CREATURES), Comparator.comparing(o -> o.getId().getPath()));*/

  public static final ResolvingRecipeType<SimpleWorldCrafting, BarkRecipe> BARK = new ResolvingRecipeType<>(LazySupplier.of(ModRecipes.BARK), (o1, o2) -> Integer.compare(o1.getId().getNamespace().compareTo(o2.getId().getNamespace()), Integer.compare(o1.getPriority(), o2.getPriority())));

  @SubscribeEvent
  public static void onReloadListeners(AddReloadListenerEvent event) {
    event.addListener(GROVE);
    event.addListener(MORTAR);
    event.addListener(PYRE);
    event.addListener(BARK);
  }
}
