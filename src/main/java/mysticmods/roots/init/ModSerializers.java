package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertySerializer;
import mysticmods.roots.api.property.PropertyType;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.recipe.bark.DynamicBarkRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
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
  private static final DeferredRegister<SnapshotType<?>> SNAPSHOT_TYPES = DeferredRegister.create(RootsRegistries.Keys.SNAPSHOT_TYPES, RootsAPI.MODID);
  private static final DeferredRegister<PropertySerializer<?>> PROPERTY_SERIALIZERS = DeferredRegister.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.MODID);
  private static final DeferredRegister<PropertyType<?>> PROPERTY_TYPES = DeferredRegister.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.MODID);

  // Recipe Serializers
  public static final DeferredHolder<RecipeSerializer<?>, DynamicBarkRecipe.Serializer> DYNAMIC_BARK = RECIPE_SERIALIZERS.register("dynamic_bark", DynamicBarkRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, GroveRecipe.Serializer> GROVE_CRAFTING = RECIPE_SERIALIZERS.register("grove", GroveRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, MortarRecipe.Serializer> MORTAR = RECIPE_SERIALIZERS.register("mortar", MortarRecipe.Serializer::new);
  /*public static final DeferredHolder<RecipeSerializer<?>, SummonCreaturesRecipe.Serializer> SUMMON_CREATURES = RECIPE_SERIALIZERS.register("summon_creatures", () -> new SummonCreaturesRecipe.Serializer());*/
  public static final DeferredHolder<RecipeSerializer<?>, PyreRecipe.Serializer> PYRE = RECIPE_SERIALIZERS.register("pyre", PyreRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, BarkRecipe.Serializer> BARK = RECIPE_SERIALIZERS.register("bark", BarkRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, RunicBlockRecipe.Serializer> RUNIC_BLOCK = RECIPE_SERIALIZERS.register("runic_block", RunicBlockRecipe.Serializer::new);
  public static final DeferredHolder<RecipeSerializer<?>, RunicEntityRecipe.Serializer> RUNIC_ENTITY = RECIPE_SERIALIZERS.register("runic_entity", RunicEntityRecipe.Serializer::new);

  public static final DeferredHolder<SnapshotType<?>, SkySoarerSnapshot.Type> SKY_SOARER = SNAPSHOT_TYPES.register("sky_soarer", SkySoarerSnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, PetalShellSnapshot.Type> PETAL_SHELL = SNAPSHOT_TYPES.register("petal_shell", PetalShellSnapshot.Type::new);
  public static final DeferredHolder<SnapshotType<?>, ExtensionSnapshot.Type> EXTENSION = SNAPSHOT_TYPES.register("extension", ExtensionSnapshot.Type::new);

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
  public static final DeferredHolder<PropertySerializer<?>, PropertySerializer<Property.BooleanProperty>> BOOL_PROPERTY_SERIALIZER = PROPERTY_SERIALIZERS.register("bool_property", Property.BooleanProperty.Serializer::new);
  public static final DeferredHolder<PropertyType<?>, PropertyType<Property.BooleanProperty>> BOOL_PROPERTY_TYPE = PROPERTY_TYPES.register("bool_property", Property.BooleanProperty.Type::new);

  public static void register(IEventBus bus) {
    RECIPE_SERIALIZERS.register(bus);
    SNAPSHOT_TYPES.register(bus);
    PROPERTY_SERIALIZERS.register(bus);
    PROPERTY_TYPES.register(bus);
  }
}
