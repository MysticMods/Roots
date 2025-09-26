package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.entity.*;
import mysticmods.roots.entity.other.FairyHutEntity;
import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.entity.other.RoseThornsEntity;
import mysticmods.roots.entity.other.TemporalMorassEntity;
import mysticmods.roots.entity.projectile.LivingArrowEntity;
import mysticmods.roots.entity.projectile.MeteorEntity;
import mysticmods.roots.entity.projectile.WildfireEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


@EventBusSubscriber(modid = RootsAPI.MODID)
@SuppressWarnings({"WeakerAccess"})
public class ModEntities {
  private static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(Registries.ENTITY_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<EntityType<?>, EntityType<BeetleEntity>> BEETLE = REGISTER.register("beetle", () -> EntityType.Builder.of(BeetleEntity::new, MobCategory.CREATURE)
      .sized(0.75f, 0.75f).eyeHeight(0.27f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(3)
      .build("beetle"));
  public static final DeferredHolder<EntityType<?>, EntityType<DeerEntity>> DEER = REGISTER.register("deer", () -> EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE)
      .sized(0.8f, 1.2f).eyeHeight(1.1f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("deer"));
  public static final DeferredHolder<EntityType<?>, EntityType<FennecEntity>> FENNEC = REGISTER.register("fennec", () -> EntityType.Builder.of(FennecEntity::new, MobCategory.CREATURE)
      .sized(0.75f, 0.75f).eyeHeight(0.6f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(3)
      .build("fennec"));
  public static final DeferredHolder<EntityType<?>, EntityType<SproutEntity>> TAN_SPROUT = REGISTER.register("tan_sprout", () -> EntityType.Builder.of(SproutEntity::new, MobCategory.CREATURE)
      .sized(0.5f, 1.0f).eyeHeight(0.6f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("tan_sprout"));
  public static final DeferredHolder<EntityType<?>, EntityType<SproutEntity>> GREEN_SPROUT = REGISTER.register("green_sprout", () -> EntityType.Builder.of(SproutEntity::new, MobCategory.CREATURE)
      .sized(0.5f, 1.0f).eyeHeight(0.6f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("green_sprout"));
  public static final DeferredHolder<EntityType<?>, EntityType<SproutEntity>> RED_SPROUT = REGISTER.register("red_sprout", () -> EntityType.Builder.of(SproutEntity::new, MobCategory.CREATURE)
      .sized(0.5f, 1.0f).eyeHeight(0.6f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("red_sprout"));
  public static final DeferredHolder<EntityType<?>, EntityType<SproutEntity>> PURPLE_SPROUT = REGISTER.register("purple_sprout", () -> EntityType.Builder.of(SproutEntity::new, MobCategory.CREATURE)
      .sized(0.5f, 1.0f).eyeHeight(0.6f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("purple_sprout"));
  public static final DeferredHolder<EntityType<?>, EntityType<SproutEntity>> SNOW_SPROUT = REGISTER.register("snow_sprout", () -> EntityType.Builder.of(SproutEntity::new, MobCategory.CREATURE)
      .sized(0.5f, 1.0f).eyeHeight(0.6f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("snow_sprout"));
  public static final DeferredHolder<EntityType<?>, EntityType<SproutEntity>> MELODY_SPROUT = REGISTER.register("melody_sprout", () -> EntityType.Builder.of(SproutEntity::new, MobCategory.CREATURE)
      .sized(0.5f, 1.0f).eyeHeight(0.6f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("melody_sprout"));
  public static final DeferredHolder<EntityType<?>, EntityType<OwlEntity>> OWL = REGISTER.register("owl", () -> EntityType.Builder.of(OwlEntity::new, MobCategory.CREATURE)
      .sized(0.5f, 1.35f).eyeHeight(1f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("owl"));
  public static final DeferredHolder<EntityType<?>, EntityType<DuckEntity>> DUCK = REGISTER.register("duck", () -> EntityType.Builder.of(DuckEntity::new, MobCategory.CREATURE)
      .sized(0.6f, 1f).eyeHeight(0.86f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("duck"));
  public static final DeferredHolder<EntityType<?>, EntityType<JerboaEntity>> JERBOA = REGISTER.register("jerboa", () -> EntityType.Builder.of(JerboaEntity::new, MobCategory.CREATURE)
      .sized(0.4f, 0.4f).eyeHeight(0.2f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("duck"));

  public static final DeferredHolder<EntityType<?>, EntityType<LivingArrowEntity>> LIVING_ARROW = REGISTER.register("living_arrow", () -> EntityType.Builder.<LivingArrowEntity>of(LivingArrowEntity::new, MobCategory.MISC)
      .sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20).build("living_arrow"));
  public static final DeferredHolder<EntityType<?>, EntityType<MeteorEntity>> METEOR = REGISTER.register("meteor", () -> EntityType.Builder.of(MeteorEntity::new, MobCategory.MISC)
      .sized(0.3f, 0.3f).eyeHeight(0.13f).clientTrackingRange(4).updateInterval(10).build("meteor"));
  public static final DeferredHolder<EntityType<?>, EntityType<WildfireEntity>> WILDFIRE = REGISTER.register("wildfire", () -> EntityType.Builder.<WildfireEntity>of(WildfireEntity::new, MobCategory.MISC)
      .sized(1.9f, 1.9f).clientTrackingRange(4).updateInterval(10).build("wildfire"));
  public static final DeferredHolder<EntityType<?>, EntityType<TemporalMorassEntity>> TEMPORAL_MORASS = REGISTER.register("temporal_morass", () -> EntityType.Builder.of(TemporalMorassEntity::new, MobCategory.MISC)
      .sized(6f, 3f).clientTrackingRange(4).updateInterval(10).build("temporal_morass"));
  public static final DeferredHolder<EntityType<?>, EntityType<LightDrifterEntity>> LIGHT_DRIFTER = REGISTER.register("light_drifter", () -> EntityType.Builder.of(LightDrifterEntity::new, MobCategory.MISC)
      .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(10).build("light_drifter"));

  public static final DeferredHolder<EntityType<?>, EntityType<FairyHutEntity>> FAIRY_HUT = REGISTER.register("fairy_hut", () -> EntityType.Builder.of(FairyHutEntity::new, MobCategory.MISC)
      .sized(1f, 1f).clientTrackingRange(4).updateInterval(10).build("fairy_hut"));

  static {
    REGISTER.addAlias(RootsAPI.rl("time_stop"), RootsAPI.rl("temporal_morass"));
  }

  public static final DeferredHolder<EntityType<?>, EntityType<RoseThornsEntity>> ROSE_THORNS = REGISTER.register("rose_thorns", () -> EntityType.Builder.of(RoseThornsEntity::new, MobCategory.MISC)
      .sized(1f, 0.4f).clientTrackingRange(4).updateInterval(10).build("rose_thorns"));

  @SubscribeEvent
  public static void registerEntitySpawns(RegisterSpawnPlacementsEvent event) {
    event.register(DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(GREEN_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(TAN_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(RED_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(PURPLE_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(SNOW_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SproutEntity::checkSnowSpawnRule, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(MELODY_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SproutEntity::checkMelodySpawnRule, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(FENNEC.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(JERBOA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(OWL.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, OwlEntity::placement, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(DUCK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.BEETLE.get(), BeetleEntity.attributes().build());
    event.put(ModEntities.JERBOA.get(), JerboaEntity.attributes().build());
    event.put(ModEntities.DEER.get(), DeerEntity.attributes().build());
    event.put(ModEntities.FENNEC.get(), FennecEntity.attributes().build());
    event.put(ModEntities.GREEN_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.TAN_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.RED_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.PURPLE_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.SNOW_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.MELODY_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.OWL.get(), OwlEntity.attributes().build());
    event.put(ModEntities.DUCK.get(), DuckEntity.attributes().build());
  }

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
