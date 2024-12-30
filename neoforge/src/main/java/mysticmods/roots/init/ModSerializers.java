package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.recipe.bark.DynamicBarkRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.recipe.summon.SummonCreaturesRecipe;
import mysticmods.roots.snapshot.ExtensionSnapshot;
import mysticmods.roots.snapshot.PetalShellSnapshot;
import mysticmods.roots.snapshot.SkySoarerSnapshot;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSerializers {
  private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, RootsAPI.MODID);
  private static final DeferredRegister<SnapshotSerializer<?>> SNAPSHOT_SERIALIZERS = DeferredRegister.create(RootsRegistries.Keys.SNAPSHOT_SERIALIZERS, RootsAPI.MODID);

  // Recipe Serializers
  public static final DeferredHolder<RecipeSerializer<?>, DynamicBarkRecipe.Serializer> DYNAMIC_BARK = RECIPE_SERIALIZERS.register("dynamic_bark", () -> new DynamicBarkRecipe.Serializer());
/*  public static final DeferredHolder<RecipeSerializer<?>, GroveRecipe.Serializer> GROVE_CRAFTING = RECIPE_SERIALIZERS.register("grove", () -> new GroveRecipe.Serializer());*/
  /*public static final DeferredHolder<RecipeSerializer<?>, MortarRecipe.Serializer> MORTAR = RECIPE_SERIALIZERS.register("mortar", () -> new MortarRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, SummonCreaturesRecipe.Serializer> SUMMON_CREATURES = RECIPE_SERIALIZERS.register("summon_creatures", () -> new SummonCreaturesRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, PyreRecipe.Serializer> PYRE = RECIPE_SERIALIZERS.register("pyre", () -> new PyreRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, BarkRecipe.Serializer> BARK = RECIPE_SERIALIZERS.register("bark", () -> new BarkRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, DynamicBarkRecipe.Serializer> DYNAMIC_BARK = RECIPE_SERIALIZERS.register("dynamic_bark", () -> new DynamicBarkRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, RunicBlockRecipe.Serializer> RUNIC_BLOCK = RECIPE_SERIALIZERS.register("runic_block", () -> new RunicBlockRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, RunicEntityRecipe.Serializer> RUNIC_ENTITY = RECIPE_SERIALIZERS.register("runic_entity", () -> new RunicEntityRecipe.Serializer());

  public static final DeferredHolder<SnapshotSerializer<?>, SkySoarerSnapshot.Serializer> SKY_SOARER = SNAPSHOT_SERIALIZERS.register("sky_soarer", () -> new SkySoarerSnapshot.Serializer(SkySoarerSnapshot::new));
  public static final DeferredHolder<SnapshotSerializer<?>, PetalShellSnapshot.Serializer> PETAL_SHELL = SNAPSHOT_SERIALIZERS.register("petal_shell", () -> new PetalShellSnapshot.Serializer(PetalShellSnapshot::new));
  public static final DeferredHolder<SnapshotSerializer<?>, ExtensionSnapshot.Serializer> EXTENSION = SNAPSHOT_SERIALIZERS.register("extension", () -> new ExtensionSnapshot.Serializer(ExtensionSnapshot::new));


  public static void register (IEventBus bus) {
    RECIPE_SERIALIZERS.register(bus);
    SNAPSHOT_SERIALIZERS.register(bus);
  }*/
}
