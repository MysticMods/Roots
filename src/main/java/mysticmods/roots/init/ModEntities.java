package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.entity.*;
import mysticmods.roots.entity.other.RoseThornsEntity;
import mysticmods.roots.entity.other.TimeStopEntity;
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


@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
@SuppressWarnings({"WeakerAccess"})
public class ModEntities {
  private static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(Registries.ENTITY_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<EntityType<?>, EntityType<BeetleEntity>> BEETLE = REGISTER.register("beetle", () -> EntityType.Builder.of(BeetleEntity::new, MobCategory.CREATURE)
      .sized(0.75f, 0.75f).eyeHeight(0.27f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
      .build("beetle"));
  public static final DeferredHolder<EntityType<?>, EntityType<DeerEntity>> DEER = REGISTER.register("deer", () -> EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE)
      .sized(0.8f, 1.2f).eyeHeight(1.1f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).build("deer"));
  public static final DeferredHolder<EntityType<?>, EntityType<FennecEntity>> FENNEC = REGISTER.register("fennec", () -> EntityType.Builder.of(FennecEntity::new, MobCategory.CREATURE)
      .sized(0.75f, 0.75f).eyeHeight(0.6f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3)
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
  public static final DeferredHolder<EntityType<?>, EntityType<OwlEntity>> OWL = REGISTER.register("owl", () -> EntityType.Builder.of(OwlEntity::new, MobCategory.CREATURE)
      .sized(0.5f, 1.35f).eyeHeight(1f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).build("owl"));
  public static final DeferredHolder<EntityType<?>, EntityType<DuckEntity>> DUCK = REGISTER.register("duck", () -> EntityType.Builder.of(DuckEntity::new, MobCategory.CREATURE)
      .sized(0.6f, 1f).eyeHeight(0.86f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).build("duck"));

  public static final DeferredHolder<EntityType<?>, EntityType<MeteorEntity>> METEOR = REGISTER.register("meteor", () -> EntityType.Builder.of(MeteorEntity::new, MobCategory.MISC)
      .sized(0.3f, 0.3f).eyeHeight(0.13f).clientTrackingRange(4).updateInterval(10).build("meteor"));
  public static final DeferredHolder<EntityType<?>, EntityType<WildfireEntity>> WILDFIRE = REGISTER.register("wildfire", () -> EntityType.Builder.<WildfireEntity>of(WildfireEntity::new, MobCategory.MISC)
      .sized(1f, 1f).clientTrackingRange(4).updateInterval(10).build("wildfire"));
  public static final DeferredHolder<EntityType<?>, EntityType<TimeStopEntity>> TIME_STOP = REGISTER.register("time_stop", () -> EntityType.Builder.of(TimeStopEntity::new, MobCategory.MISC)
      .sized(1f, 1f).clientTrackingRange(4).updateInterval(10).build("time_stop"));
  public static final DeferredHolder<EntityType<?>, EntityType<RoseThornsEntity>> ROSE_THORNS = REGISTER.register("rose_thorns", () -> EntityType.Builder.of(RoseThornsEntity::new, MobCategory.MISC)
      .sized(1f, 0.4f).clientTrackingRange(4).updateInterval(10).build("rose_thorns"));

  @SubscribeEvent
  public static void registerEntitySpawns(RegisterSpawnPlacementsEvent event) {
    event.register(DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(GREEN_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(TAN_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(RED_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(PURPLE_SPROUT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(FENNEC.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(OWL.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, OwlEntity::placement, RegisterSpawnPlacementsEvent.Operation.AND);
    event.register(DUCK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.BEETLE.get(), BeetleEntity.attributes().build());
    event.put(ModEntities.DEER.get(), DeerEntity.attributes().build());
    event.put(ModEntities.FENNEC.get(), FennecEntity.attributes().build());
    event.put(ModEntities.GREEN_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.TAN_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.RED_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.PURPLE_SPROUT.get(), SproutEntity.attributes().build());
    event.put(ModEntities.OWL.get(), OwlEntity.attributes().build());
    event.put(ModEntities.DUCK.get(), DuckEntity.attributes().build());
  }

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
