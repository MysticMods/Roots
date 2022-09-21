package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import mysticmods.roots.snapshot.SkySoarerSnapshot;
import net.minecraft.core.Registry;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModSerializers {
  // Recipe Serializers
  public static final RegistryEntry<GroveRecipe.Serializer> GROVE_CRAFTING = REGISTRATE.simple("grove", Registry.RECIPE_SERIALIZER_REGISTRY, GroveRecipe.Serializer::new);
  public static final RegistryEntry<MortarRecipe.Serializer> MORTAR = REGISTRATE.simple("mortar", Registry.RECIPE_SERIALIZER_REGISTRY, MortarRecipe.Serializer::new);
  public static final RegistryEntry<SummonCreaturesRecipe.Serializer> SUMMON_CREATURES = REGISTRATE.simple("summon_creatures", Registry.RECIPE_SERIALIZER_REGISTRY, SummonCreaturesRecipe.Serializer::new);
  public static final RegistryEntry<PyreRecipe.Serializer> PYRE = REGISTRATE.simple("pyre", Registry.RECIPE_SERIALIZER_REGISTRY, PyreRecipe.Serializer::new);

  // Snapshot Serializers
  public static final RegistryEntry<SkySoarerSnapshot.Serializer> SKY_SOARER = REGISTRATE.simple("sky_soarer", RootsAPI.SNAPSHOT_SERIALIZER_REGISTRY, () -> new SkySoarerSnapshot.Serializer(SkySoarerSnapshot::new));

  public static void load() {
  }
}
