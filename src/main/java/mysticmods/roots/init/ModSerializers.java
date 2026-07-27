package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertySerializer;
import mysticmods.roots.api.property.PropertyType;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.recipe.PouchDyeRecipe;
import mysticmods.roots.recipe.grove.GrovePouchRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.knife.KnifeOffHandRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import mysticmods.roots.snapshot.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSerializers {
  private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, RootsAPI.MODID);
  private static final DeferredRegister<SnapshotType<?>> SNAPSHOT_TYPES = DeferredRegister.create(RootsRegistries.Keys.SNAPSHOT_TYPES, RootsAPI.MODID);
  private static final DeferredRegister<PropertySerializer<?>> PROPERTY_SERIALIZERS = DeferredRegister.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.MODID);
  private static final DeferredRegister<PropertyType<?>> PROPERTY_TYPES = DeferredRegister.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.MODID);

  // Recipe Serializers
  public static final DeferredHolder<RecipeSerializer<?>, GroveRecipe.Serializer> GROVE_CRAFTING = RECIPE_SERIALIZERS.register("grove", GroveRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, GrovePouchRecipe.Serializer> GROVE_POUCH_CRAFTING = RECIPE_SERIALIZERS.register("grove_pouch", GrovePouchRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, MortarRecipe.Serializer> MORTAR = RECIPE_SERIALIZERS.register("mortar", MortarRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, PyreRecipe.Serializer> PYRE = RECIPE_SERIALIZERS.register("pyre", PyreRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, SummonCreaturesRecipe.Serializer> SUMMON_CREATURES = RECIPE_SERIALIZERS.register("pyre_pedestal", SummonCreaturesRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, KnifeRecipe.Serializer> KNIFE = RECIPE_SERIALIZERS.register("knife", KnifeRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, KnifeOffHandRecipe.Serializer> KNIFE_OFF_HAND = RECIPE_SERIALIZERS.register("knife_off_hand", KnifeOffHandRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, RunicBlockRecipe.Serializer> RUNIC_BLOCK = RECIPE_SERIALIZERS.register("runic_block", RunicBlockRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, RunicEntityRecipe.Serializer> RUNIC_ENTITY = RECIPE_SERIALIZERS.register("runic_entity", RunicEntityRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PouchDyeRecipe>> DYE_POUCH = RECIPE_SERIALIZERS.register("dye_pouch", () -> new SimpleCraftingRecipeSerializer<>(PouchDyeRecipe::new));
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TransmutationRecipe>> TRANSMUTATION = RECIPE_SERIALIZERS.register("transmutation", TransmutationRecipe.Serializer::new);

  public static final DeferredHolder<SnapshotType<?>, SkySoarerSnapshot.Type> SKY_SOARER = SNAPSHOT_TYPES.register("sky_soarer", SkySoarerSnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, PetalShellSnapshot.Type> PETAL_SHELL = SNAPSHOT_TYPES.register("petal_shell", PetalShellSnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, ExtensionSnapshot.Type> EXTENSION = SNAPSHOT_TYPES.register("extension", ExtensionSnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, TemporalMorassEntitySnapshot.Type> TEMPORAL_MORASS = SNAPSHOT_TYPES.register("temporal_morass", TemporalMorassEntitySnapshot.Type::new);

  static {
    SNAPSHOT_TYPES.addAlias(RootsAPI.rl("time_stop"), RootsAPI.rl("temporal_morass"));
  }

  public static final DeferredHolder<SnapshotType<?>, AquaBubbleSnapshot.Type> AQUA_BUBBLE = SNAPSHOT_TYPES.register("aqua_bubble", AquaBubbleSnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, RoseThornsEntitySnapshot.Type> ROSE_THORNS = SNAPSHOT_TYPES.register("rose_thorns", RoseThornsEntitySnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, WildfireEntitySnapshot.Type> WILDFIRE = SNAPSHOT_TYPES.register("wildfire", WildfireEntitySnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, LightDrifterSnapshot.Type> LIGHT_DRIFTER = SNAPSHOT_TYPES.register("light_drifter", LightDrifterSnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, DandelionWindsSnapshot.Type> DANDELION_WINDS = SNAPSHOT_TYPES.register("dandelion_winds", DandelionWindsSnapshot.Type::new);

  // Integer
  public static final DeferredHolder<PropertySerializer<?>, PropertySerializer<Property.IntegerProperty>> INTEGER_PROPERTY_SERIALIZER = PROPERTY_SERIALIZERS.register("integer_property", Property.IntegerProperty.Serializer::new);
  public static final DeferredHolder<PropertyType<?>, PropertyType<Property.IntegerProperty>> INTEGER_PROPERTY_TYPE = PROPERTY_TYPES.register("integer_property", Property.IntegerProperty.Type::new);

  // Double
  public static final DeferredHolder<PropertySerializer<?>, PropertySerializer<Property.DoubleProperty>> DOUBLE_PROPERTY_SERIALIZER = PROPERTY_SERIALIZERS.register("double_property", Property.DoubleProperty.Serializer::new);
  public static final DeferredHolder<PropertyType<?>, PropertyType<Property.DoubleProperty>> DOUBLE_PROPERTY_TYPE = PROPERTY_TYPES.register("double_property", Property.DoubleProperty.Type::new);

  // Float
  public static final DeferredHolder<PropertySerializer<?>, PropertySerializer<Property.FloatProperty>> FLOAT_PROPERTY_SERIALIZER = PROPERTY_SERIALIZERS.register("float_property", Property.FloatProperty.Serializer::new);
  public static final DeferredHolder<PropertyType<?>, PropertyType<Property.FloatProperty>> FLOAT_PROPERTY_TYPE = PROPERTY_TYPES.register("float_property", Property.FloatProperty.Type::new);

  // String
  public static final DeferredHolder<PropertySerializer<?>, PropertySerializer<Property.StringProperty>> STRING_PROPERTY_SERIALIZER = PROPERTY_SERIALIZERS.register("string_property", Property.StringProperty.Serializer::new);
  public static final DeferredHolder<PropertyType<?>, PropertyType<Property.StringProperty>> STRING_PROPERTY_TYPE = PROPERTY_TYPES.register("string_property", Property.StringProperty.Type::new);

  // Bool
  public static final DeferredHolder<PropertySerializer<?>, PropertySerializer<Property.BooleanProperty>> BOOL_PROPERTY_SERIALIZER = PROPERTY_SERIALIZERS.register("boolean_property", Property.BooleanProperty.Serializer::new);
  public static final DeferredHolder<PropertyType<?>, PropertyType<Property.BooleanProperty>> BOOL_PROPERTY_TYPE = PROPERTY_TYPES.register("boolean_property", Property.BooleanProperty.Type::new);

  public static void register(IEventBus bus) {
    RECIPE_SERIALIZERS.register(bus);
    SNAPSHOT_TYPES.register(bus);
    PROPERTY_SERIALIZERS.register(bus);
    PROPERTY_TYPES.register(bus);
  }
}
