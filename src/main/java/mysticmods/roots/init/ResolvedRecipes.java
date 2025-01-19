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
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityCrafting;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.Collection;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class ResolvedRecipes {
  public static final ResolvingRecipeType<GroveCrafting, GroveRecipe> GROVE = new ResolvingRecipeType<>(ModRecipes.GROVE, (o1, o2) -> Integer.compare(o1.id().getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value().getPriority())));
  public static final ResolvingRecipeType<MortarCrafting, MortarRecipe> MORTAR = new ResolvingRecipeType<>((ModRecipes.MORTAR), (o1, o2) -> Integer.compare(o1.id().getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value().getPriority())));
  public static final ResolvingRecipeType<PyreCrafting, PyreRecipe> PYRE = new ResolvingRecipeType<>((ModRecipes.PYRE), (o1, o2) -> Integer.compare(o1.id().getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value().getPriority())));
  public static final ResolvingRecipeType<SimpleWorldCrafting, RunicBlockRecipe> RUNIC_BLOCK = new ResolvingRecipeType<>((ModRecipes.RUNIC_BLOCK), (o1, o2) -> Integer.compare(o1.id().getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value().getPriority())));
  public static final ResolvingRecipeType<RunicEntityCrafting, RunicEntityRecipe> RUNIC_ENTITY = new ResolvingRecipeType<>((ModRecipes.RUNIC_ENTITY), (o1, o2) -> Integer.compare(o1.id().getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value().getPriority())));
  public static final ResolvingRecipeType<SimpleWorldCrafting, BarkRecipe> BARK = new ResolvingRecipeType<>((ModRecipes.BARK), (o1, o2) -> Integer.compare(o1.id().getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value().getPriority())));

  @SubscribeEvent
  public static void onReloadListeners(AddReloadListenerEvent event) {
    event.addListener(GROVE);
    event.addListener(MORTAR);
    event.addListener(PYRE);
    event.addListener(BARK);
    event.addListener(RUNIC_BLOCK);
    event.addListener(RUNIC_ENTITY);
  }

  public static void reset () {
    GROVE.reset();
    MORTAR.reset();
    PYRE.reset();
    BARK.reset();
    RUNIC_BLOCK.reset();
    RUNIC_ENTITY.reset();
  }
}
