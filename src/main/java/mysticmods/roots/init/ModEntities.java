package mysticmods.roots.init;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.tterrag.registrate.util.LazySpawnEggItem;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.entity.*;
import mysticmods.roots.loot.predicates.HasHornsCondition;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static mysticmods.roots.Roots.REGISTRATE;

@SuppressWarnings({"WeakerAccess", "ConstantConditions", "unchecked", "deprecation"})
@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
  public static RegistryEntry<EntityType<BeetleEntity>> BEETLE = REGISTRATE.entity("beetle", BeetleEntity::new, MobCategory.CREATURE)
      .properties(o -> o.sized(0.75f, 0.75f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .loot((p, e) -> p.add(e, LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.CARAPACE.get())
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                  .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(2, 4)))
              )
              .add(LootItem.lootTableItem(Items.SLIME_BALL)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                  .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 2)))
              )
              .setRolls(ConstantValue.exactly(1))
          )
      ))
      .register();

  public static RegistryEntry<EntityType<DeerEntity>> DEER = REGISTRATE.entity("deer", DeerEntity::new, MobCategory.CREATURE)
      .loot((p, e) ->
          p.add(e, LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.LEATHER)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 3)))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(ModItems.VENISON.get())
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
                      .apply(SmeltItemFunction.smelted()
                          .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build()))))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(ModItems.ANTLERS.get())
                      .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                      .when(HasHornsCondition.builder())
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
          )
      )
      .properties(o -> o.sized(1.0f, 1.0f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .register();

  public static RegistryEntry<EntityType<FennecEntity>> FENNEC = REGISTRATE.entity("fennec", FennecEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(ModItems.PELT.get())
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 2)))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
          )
      )
      .properties(o -> o.sized(0.75f, 0.75f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .register();

  public static RegistryEntry<EntityType<SproutEntity>> TAN_SPROUT = REGISTRATE.entity("tan_sprout", SproutEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.POTATO)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 3)))
                      .apply(SmeltItemFunction.smelted()
                          .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build()))))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
          )
      )
      .properties(o -> o.sized(0.5f, 1.0f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .register();

  public static RegistryEntry<EntityType<SproutEntity>> GREEN_SPROUT = REGISTRATE.entity("green_sprout", SproutEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.MELON_SLICE)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 3)))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
          )
      )
      .properties(o -> o.sized(0.5f, 1.0f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .register();

  public static RegistryEntry<EntityType<SproutEntity>> RED_SPROUT = REGISTRATE.entity("red_sprout", SproutEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.BEETROOT)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 3)))
                      .apply(SmeltItemFunction.smelted()
                          .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build()))))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
          )
      )
      .properties(o -> o.sized(0.5f, 1.0f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .register();

  public static RegistryEntry<EntityType<SproutEntity>> PURPLE_SPROUT = REGISTRATE.entity("purple_sprout", SproutEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(ModItems.AUBERGINE.get())
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 3)))
                      .apply(SmeltItemFunction.smelted()
                          .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build()))))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
          )
      )
      .properties(o -> o.sized(0.5f, 1.0f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .register();

  public static RegistryEntry<EntityType<OwlEntity>> OWL = REGISTRATE.entity("owl", OwlEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.FEATHER)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 3)))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
          )
      )
      .properties(o -> o.sized(0.5f, 0.9f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .register();

  public static RegistryEntry<EntityType<DuckEntity>> DUCK = REGISTRATE.entity("duck", DuckEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.FEATHER)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3)))
                      .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 3)))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
          )
      )
      .properties(o -> o.sized(0.5f, 0.9f).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3))
      .register();

  public static void registerEntities() {
    SpawnPlacements.register(DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    SpawnPlacements.register(GREEN_SPROUT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    SpawnPlacements.register(TAN_SPROUT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    SpawnPlacements.register(RED_SPROUT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    SpawnPlacements.register(PURPLE_SPROUT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    SpawnPlacements.register(FENNEC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    SpawnPlacements.register(BEETLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    SpawnPlacements.register(OWL.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, OwlEntity::placement);
    SpawnPlacements.register(DUCK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
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

  public static void load() {
  }
}
