package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertySerializer;
import mysticmods.roots.api.property.PropertyType;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.recipe.bark.DynamicBarkRecipe;
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
  private static final DeferredRegister<PropertySerializer<?>> PROPERTY_SERIALIZERS = DeferredRegister.create(RootsRegistries.Keys.PROPERTY_SERIALIZERS, RootsAPI.MODID);
  private static final DeferredRegister<PropertyType<?>> PROPERTY_TYPES = DeferredRegister.create(RootsRegistries.Keys.PROPERTY_TYPES, RootsAPI.MODID);

  // Recipe Serializers
  public static final DeferredHolder<RecipeSerializer<?>, DynamicBarkRecipe.Serializer> DYNAMIC_BARK = RECIPE_SERIALIZERS.register("dynamic_bark", DynamicBarkRecipe.Serializer::new);
  /*  public static final DeferredHolder<RecipeSerializer<?>, GroveRecipe.Serializer> GROVE_CRAFTING = RECIPE_SERIALIZERS.register("grove", () -> new GroveRecipe.Serializer());*/
  /*public static final DeferredHolder<RecipeSerializer<?>, MortarRecipe.Serializer> MORTAR = RECIPE_SERIALIZERS.register("mortar", () -> new MortarRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, SummonCreaturesRecipe.Serializer> SUMMON_CREATURES = RECIPE_SERIALIZERS.register("summon_creatures", () -> new SummonCreaturesRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, PyreRecipe.Serializer> PYRE = RECIPE_SERIALIZERS.register("pyre", () -> new PyreRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, BarkRecipe.Serializer> BARK = RECIPE_SERIALIZERS.register("bark", () -> new BarkRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, DynamicBarkRecipe.Serializer> DYNAMIC_BARK = RECIPE_SERIALIZERS.register("dynamic_bark", () -> new DynamicBarkRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, RunicBlockRecipe.Serializer> RUNIC_BLOCK = RECIPE_SERIALIZERS.register("runic_block", () -> new RunicBlockRecipe.Serializer());
  public static final DeferredHolder<RecipeSerializer<?>, RunicEntityRecipe.Serializer> RUNIC_ENTITY = RECIPE_SERIALIZERS.register("runic_entity", () -> new RunicEntityRecipe.Serializer());*/

  public static final DeferredHolder<SnapshotSerializer<?>, SkySoarerSnapshot.Serializer> SKY_SOARER = SNAPSHOT_SERIALIZERS.register("sky_soarer", () -> new SkySoarerSnapshot.Serializer(SkySoarerSnapshot::new));
  public static final DeferredHolder<SnapshotSerializer<?>, PetalShellSnapshot.Serializer> PETAL_SHELL = SNAPSHOT_SERIALIZERS.register("petal_shell", () -> new PetalShellSnapshot.Serializer(PetalShellSnapshot::new));
  public static final DeferredHolder<SnapshotSerializer<?>, ExtensionSnapshot.Serializer> EXTENSION = SNAPSHOT_SERIALIZERS.register("extension", () -> new ExtensionSnapshot.Serializer(ExtensionSnapshot::new));

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
    SNAPSHOT_SERIALIZERS.register(bus);
    PROPERTY_SERIALIZERS.register(bus);
    PROPERTY_TYPES.register(bus);
  }
}
