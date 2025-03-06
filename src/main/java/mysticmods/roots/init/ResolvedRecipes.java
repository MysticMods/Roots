package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.recipe.type.ResolvingRecipeType;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.recipe.grove.GroveCrafting;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarCrafting;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreCrafting;
import mysticmods.roots.recipe.pyre.PyrePedestalCrafting;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityCrafting;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class ResolvedRecipes {
  public static final ResolvingRecipeType<Void, GroveCrafting, GroveRecipe> GROVE = new ResolvingRecipeType<>(ModRecipes.GROVE, (o1, o2) -> Integer.compare(o1.id()
      .getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value()
      .getPriority())), null);
  public static final ResolvingRecipeType<Spell, MortarCrafting, MortarRecipe> MORTAR = new ResolvingRecipeType<>(ModRecipes.MORTAR, (o1, o2) -> Integer.compare(o1.id()
      .getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value()
      .getPriority())), o -> {
    if (o.getUnlocks().isEmpty()) {
      return null;
    }
    Unlock<?> unlock = o.getUnlocks().getFirst();
    if (unlock instanceof Unlock.SpellUnlock) {
      return ((Unlock.SpellUnlock) unlock).value().value();
    }

    return null;
  });
  public static final ResolvingRecipeType<Ritual, PyreCrafting, PyreRecipe> PYRE = new ResolvingRecipeType<>(ModRecipes.PYRE, (o1, o2) -> Integer.compare(o1.id()
      .getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value()
      .getPriority())), PyreRecipe::getRitual);
  public static final ResolvingRecipeType<Void, PyrePedestalCrafting, SummonCreaturesRecipe> SUMMON_CREATURES = new ResolvingRecipeType<>(ModRecipes.SUMMON_CREATURES, (o1, o2) -> Integer.compare(o1.id()
      .getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value()
      .getPriority())), null);
  public static final ResolvingRecipeType<Void, SimpleWorldCrafting, RunicBlockRecipe> RUNIC_BLOCK = new ResolvingRecipeType<>(ModRecipes.RUNIC_BLOCK, (o1, o2) -> Integer.compare(o1.id()
      .getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value()
      .getPriority())), null);
  public static final ResolvingRecipeType<Void, RunicEntityCrafting, RunicEntityRecipe> RUNIC_ENTITY = new ResolvingRecipeType<>(ModRecipes.RUNIC_ENTITY, (o1, o2) -> Integer.compare(o1.id()
      .getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value()
      .getPriority())), null);
  public static final ResolvingRecipeType<Void, SimpleWorldCrafting, KnifeRecipe> BARK = new ResolvingRecipeType<>(ModRecipes.KNIFE, (o1, o2) -> Integer.compare(o1.id()
      .getNamespace().compareTo(o2.id().getNamespace()), Integer.compare(o1.value().getPriority(), o2.value()
      .getPriority())), null);

  @SubscribeEvent
  public static void onReloadListeners(AddReloadListenerEvent event) {
    event.addListener(GROVE);
    event.addListener(MORTAR);
    event.addListener(PYRE);
    event.addListener(BARK);
    event.addListener(RUNIC_BLOCK);
    event.addListener(RUNIC_ENTITY);
    event.addListener(SUMMON_CREATURES);
  }

  public static void reset() {
    GROVE.reset();
    MORTAR.reset();
    PYRE.reset();
    BARK.reset();
    RUNIC_BLOCK.reset();
    RUNIC_ENTITY.reset();
    SUMMON_CREATURES.reset();
  }
}
