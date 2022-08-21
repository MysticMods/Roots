package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.reference.RitualProperties;
import mysticmods.roots.api.reference.Rituals;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.condition.LevelCondition;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.ritual.AnimalHarvestRitual;
import mysticmods.roots.ritual.CraftingRitual;
import net.minecraft.resources.ResourceKey;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRituals {
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_4_HIGH = REGISTRATE.simple("rune_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.runePillar(4)));
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_3_HIGH = REGISTRATE.simple("rune_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.runePillar(3)));

  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_4_HIGH = REGISTRATE.simple("acacia_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 4)));
  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_3_HIGH = REGISTRATE.simple("acacia_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 3)));

  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_4_HIGH = REGISTRATE.simple("birch_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 4)));
  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_3_HIGH = REGISTRATE.simple("birch_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 3)));

  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_4_HIGH = REGISTRATE.simple("crimson_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 4)));
  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_3_HIGH = REGISTRATE.simple("crimson_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 3)));

  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_4_HIGH = REGISTRATE.simple("dark_oak_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 4)));
  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_3_HIGH = REGISTRATE.simple("dark_oak_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 3)));

  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_4_HIGH = REGISTRATE.simple("jungle_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 4)));
  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_3_HIGH = REGISTRATE.simple("jungle_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 3)));

  public static final RegistryEntry<LevelCondition> OAK_PILLAR_4_HIGH = REGISTRATE.simple("oak_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 4)));
  public static final RegistryEntry<LevelCondition> OAK_PILLAR_3_HIGH = REGISTRATE.simple("oak_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 3)));

  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_4_HIGH = REGISTRATE.simple("spruce_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 4)));
  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_3_HIGH = REGISTRATE.simple("spruce_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 3)));

  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_4_HIGH = REGISTRATE.simple("warped_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 4)));
  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_3_HIGH = REGISTRATE.simple("warped_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 3)));

  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_4_HIGH = REGISTRATE.simple("wildwood_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 4)));
  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_3_HIGH = REGISTRATE.simple("wildwood_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 3)));

  public static final RegistryEntry<Ritual> TRANSMUTATION = ritual(Rituals.TRANSMUTATION, () -> new Ritual() {
    @Override
    protected void functionalTick(PyreBlockEntity blockEntity, int duration) {

    }

    @Override
    protected void animationTick(PyreBlockEntity blockEntity, int duration) {

    }

    @Override
    public void initialize() {

    }
  });
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_COUNT = REGISTRATE.simple("transmutation/count", RitualProperty.class, () -> new RitualProperty<>(TRANSMUTATION, 10, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  public static final RegistryEntry<CraftingRitual> CRAFTING = ritual(Rituals.CRAFTING, CraftingRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> CRAFTING_DURATION = REGISTRATE.simple("crafting/duration", RitualProperty.class, () -> new RitualProperty<>(CRAFTING::get, 160, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> CRAFTING_INTERVAL = REGISTRATE.simple("crafting/interval", RitualProperty.class, () -> new RitualProperty<>(CRAFTING::get, 120, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  public static final RegistryEntry<AnimalHarvestRitual> ANIMAL_HARVEST = ritual(Rituals.ANIMAL_HARVEST, AnimalHarvestRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_DURATION = REGISTRATE.simple("animal_harvest/duration", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 3200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_INTERVAL = REGISTRATE.simple("animal_harvest/interval", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 110, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_COUNT = REGISTRATE.simple("animal_harvest/count", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 1, Property.INTEGER_SERIALIZER, RitualProperties.COUNT));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_RADIUS_XZ = REGISTRATE.simple("animal_harvest/radius_xz", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_RADIUS_Y = REGISTRATE.simple("animal_harvest/radius_y", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));
  public static final RegistryEntry<RitualProperty<Float>> ANIMAL_HARVEST_LOOTING_CHANCE = REGISTRATE.simple("animal_harvest/looting_chance", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 0.2f, Property.FLOAT_SERIALIZER, "Chance per operation that the loot level will be set to looting_value"));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_LOOTING_VALUE = REGISTRATE.simple("animal_harvest/looting_value", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 2, Property.INTEGER_SERIALIZER, "The value passed to the loot function if looting_chance was successful."));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_GLOW_DURATION = REGISTRATE.simple("animal_harvest/glow_duration", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 10, Property.INTEGER_SERIALIZER, "The duration of the glow effect applied to entities that have been harvest."));


  private static <T extends Ritual> RegistryEntry<T> ritual(ResourceKey<Ritual> key, NonNullSupplier<T> builder) {
    return REGISTRATE.simple(key.location().getPath(), Ritual.class, builder);
  }

  public static void load() {
  }
}
