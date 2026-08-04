package mysticmods.roots.gen.loot;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.block.*;
import mysticmods.roots.block.crop.ElementalCropBlock;
import mysticmods.roots.block.crop.ElementalType;
import mysticmods.roots.block.crop.FourStageCropBlock;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.loot.conditions.HasHornsCondition;
import mysticmods.roots.loot.functions.FillHorns;
import mysticmods.roots.mixin.accessor.AccessorMixinBlockLootSubProvider;
import mysticmods.roots.mixin.accessor.AccessorMixinCropBlock;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public final class RootsLootTableProvider {
  public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
    return new LootTableProvider(output, Set.of(RootsAPI.HUT, RootsAPI.BARROW, RootsAPI.STANDING_STONES, RootsAPI.TENTACLES, RootsAPI.TURTLE_SCUTE, RootsAPI.GOAT_HORN), List.of(new LootTableProvider.SubProviderEntry(ChestLootTables::new, LootContextParamSets.CHEST), new LootTableProvider.SubProviderEntry(RootsBlockLootTables::new, LootContextParamSets.BLOCK), new LootTableProvider.SubProviderEntry(RootsEntityLootTables::new, LootContextParamSets.ENTITY), new LootTableProvider.SubProviderEntry(ChestLootTablesFixer::new, LootContextParamSets.CHEST), new LootTableProvider.SubProviderEntry(RootsAdditionalEntityLootTables::new, LootContextParamSets.ENTITY)), provider);
  }

  public static class RootsAdditionalEntityLootTables implements LootTableSubProvider {
    private final HolderLookup.Provider provider;

    public RootsAdditionalEntityLootTables(HolderLookup.Provider provider) {
      this.provider = provider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
      consumer.accept(
          RootsAPI.TURTLE_SCUTE,
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.TURTLE_SCUTE)
                      .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                      .when(LootItemRandomChanceCondition.randomChance(0.1f)).setWeight(1))
                  .add(EmptyLootItem.emptyItem().setWeight(15))
              )
              .setParamSet(LootContextParamSets.ENTITY)
      );
      consumer.accept(
          RootsAPI.GOAT_HORN,
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.GOAT_HORN)
                      .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                      .apply(FillHorns.fillHorns())
                      .when(LootItemRandomChanceCondition.randomChance(0.1f)).setWeight(1))
                  .add(EmptyLootItem.emptyItem().setWeight(15))
              )
              .setParamSet(LootContextParamSets.ENTITY)
      );
    }
  }

  public static class RootsEntityLootTables extends EntityLootSubProvider {

    public RootsEntityLootTables(HolderLookup.Provider provider) {
      super(FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
      List<EntityType<?>> blocks = new ArrayList<>();
      for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : BuiltInRegistries.ENTITY_TYPE.entrySet()) {
        if (entry.getKey().location().getNamespace().equals(RootsAPI.MODID)) {
          blocks.add(entry.getValue());
        }
      }
      return blocks.stream();
    }

    @Override
    public void generate() {
      add(ModEntities.BEETLE.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.CARAPACE.get())
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(2, 4)))
              ).setRolls(ConstantValue.exactly(1))
          ).withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(Items.SLIME_BALL)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0, 2)))
              )
          ));
      // TODO:
      add(ModEntities.JERBOA.get(), LootTable.lootTable());
      add(ModEntities.SYLVAN_SPIDER.get(), LootTable.lootTable());
      add(ModEntities.DEER.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(Items.LEATHER)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0, 3)))
              )
              .setRolls(ConstantValue.exactly(1))
          )
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.VENISON.get())
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0, 1)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              )
              .setRolls(ConstantValue.exactly(1))
          )
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.ANTLERS.get())
                  .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                  .when(HasHornsCondition.builder())
              )
              .setRolls(ConstantValue.exactly(1))
          ));
      add(ModEntities.FENNEC.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.PELT.get())
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 2)))
              )
              .setRolls(ConstantValue.exactly(1))
          )
      );
      add(ModEntities.TAN_SPROUT.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(Items.POTATO)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              ).add(LootItem.lootTableItem(Items.WHEAT)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              ).add(LootItem.lootTableItem(Items.GLOW_BERRIES)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true))))))
              .setRolls(ConstantValue.exactly(1))
          )
      );
      add(ModEntities.GREEN_SPROUT.get(),
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.MELON_SLICE)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                      .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                      .apply(SmeltItemFunction.smelted()
                          .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                              .flags(EntityFlagsPredicate.Builder.flags()
                                  .setOnFire(true)))))
                  )
                  .add(LootItem.lootTableItem(Items.COCOA_BEANS)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                      .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                      .apply(SmeltItemFunction.smelted()
                          .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                              .flags(EntityFlagsPredicate.Builder.flags()
                                  .setOnFire(true)))))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
      );
      add(ModEntities.RED_SPROUT.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(Items.BEETROOT)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              )
              .add(LootItem.lootTableItem(Items.APPLE)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              ).add(LootItem.lootTableItem(Items.RED_MUSHROOM)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              )
              .setRolls(ConstantValue.exactly(1))
          ));
      add(ModEntities.PURPLE_SPROUT.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.AUBERGINE.get())
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              )
              .setRolls(ConstantValue.exactly(1))
          ));
      add(ModEntities.SNOW_SPROUT.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(Items.SWEET_BERRIES)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              )
              .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              )
              .setRolls(ConstantValue.exactly(1))
          ));
      // TODO: Other stuff
      add(ModEntities.MELODY_SPROUT.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(Items.CHORUS_FRUIT)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
                  .apply(SmeltItemFunction.smelted()
                      .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                          .flags(EntityFlagsPredicate.Builder.flags()
                              .setOnFire(true)))))
              )
              .setRolls(ConstantValue.exactly(1))
          ));
      add(ModEntities.OWL.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(Items.FEATHER)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
              )
              .setRolls(ConstantValue.exactly(1))
          ));
      add(ModEntities.DUCK.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(Items.FEATHER)
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3)))
                  .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(1, 3)))
              )
              .setRolls(ConstantValue.exactly(1))
          )
      );
    }
  }

  public static class RootsBlockLootTables extends BlockLootSubProvider {
    protected RootsBlockLootTables(HolderLookup.Provider arg2) {
      super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), arg2);
    }

    @Override
    protected void generate() {
      dropSelf(ModBlocks.THATCH.get());
      dropSelf(ModBlocks.RUNESTONE.get());
      dropSelf(ModBlocks.MOSSY_RUNESTONE.get());
      dropSelf(ModBlocks.CHISELED_RUNESTONE.get());
      dropSelf(ModBlocks.RUNESTONE_BRICK.get());
      dropSelf(ModBlocks.RUNESTONE_TILE.get());
      dropSelf(ModBlocks.RUNED_OBSIDIAN.get());
      dropSelf(ModBlocks.CHISELED_RUNED_OBSIDIAN.get());
      dropSelf(ModBlocks.RUNED_BRICK.get());
      dropSelf(ModBlocks.RUNED_TILE.get());
      add(ModBlocks.SILVER_ORE.get(), createOreDrop(ModBlocks.SILVER_ORE.get(), ModItems.RAW_SILVER.get()));
      add(ModBlocks.DEEPSLATE_SILVER_ORE.get(), createOreDrop(ModBlocks.DEEPSLATE_SILVER_ORE.get(), ModItems.RAW_SILVER.get()));
      add(ModBlocks.GRANITE_QUARTZ_ORE.get(), createOreDrop(ModBlocks.GRANITE_QUARTZ_ORE.get(), Items.QUARTZ));
      dropSelf(ModBlocks.RAW_SILVER_BLOCK.get());
      dropSelf(ModBlocks.SILVER_BLOCK.get());
      dropSelf(ModBlocks.WILDWOOD_LOG.get());
      dropSelf(ModBlocks.STRIPPED_WILDWOOD_LOG.get());
      dropSelf(ModBlocks.WILDWOOD_WOOD.get());
      dropSelf(ModBlocks.STRIPPED_WILDWOOD_WOOD.get());
      dropSelf(ModBlocks.WILDWOOD_PLANKS.get());
      dropSelf(ModBlocks.WILDWOOD_SAPLING.get());
      dropSelf(ModBlocks.STONEPETAL.get());
      add(ModBlocks.WILDWOOD_LEAVES.get(), createWildwoodLeaves(ModBlocks.WILDWOOD_LEAVES.get()));
      dropSelf(ModBlocks.RUNESTONE_STAIRS.get());
      dropSelf(ModBlocks.MOSSY_RUNESTONE_STAIRS.get());
      dropSelf(ModBlocks.RUNESTONE_BRICK_STAIRS.get());
      dropSelf(ModBlocks.RUNESTONE_TILE_STAIRS.get());
      dropSelf(ModBlocks.RUNED_STAIRS.get());
      dropSelf(ModBlocks.RUNED_BRICK_STAIRS.get());
      dropSelf(ModBlocks.RUNED_TILE_STAIRS.get());
      dropSelf(ModBlocks.WILDWOOD_STAIRS.get());
      dropSelf(ModBlocks.RUNIC_DUST.get());
      dropSelf(ModBlocks.RUNESTONE_TICKER.get());
      add(ModBlocks.RUNESTONE_SLAB.get(), createSlabItemTable(ModBlocks.RUNESTONE_SLAB.get()));
      add(ModBlocks.MOSSY_RUNESTONE_SLAB.get(), createSlabItemTable(ModBlocks.MOSSY_RUNESTONE_SLAB.get()));
      add(ModBlocks.RUNESTONE_BRICK_SLAB.get(), createSlabItemTable(ModBlocks.RUNESTONE_BRICK_SLAB.get()));
      add(ModBlocks.RUNESTONE_TILE_SLAB.get(), createSlabItemTable(ModBlocks.RUNESTONE_TILE_SLAB.get()));
      add(ModBlocks.RUNED_SLAB.get(), createSlabItemTable(ModBlocks.RUNED_SLAB.get()));
      add(ModBlocks.RUNED_BRICK_SLAB.get(), createSlabItemTable(ModBlocks.RUNED_BRICK_SLAB.get()));
      add(ModBlocks.RUNED_TILE_SLAB.get(), createSlabItemTable(ModBlocks.RUNED_TILE_SLAB.get()));
      add(ModBlocks.WILDWOOD_SLAB.get(), createSlabItemTable(ModBlocks.WILDWOOD_SLAB.get()));
      dropSelf(ModBlocks.WILDWOOD_FENCE.get());
      dropSelf(ModBlocks.RUNESTONE_BUTTON.get());
      dropSelf(ModBlocks.RUNESTONE_BRICK_BUTTON.get());
      dropSelf(ModBlocks.RUNESTONE_TILE_BUTTON.get());
      dropSelf(ModBlocks.MOSSY_RUNESTONE_BUTTON.get());
      dropSelf(ModBlocks.RUNED_BUTTON.get());
      dropSelf(ModBlocks.RUNED_BRICK_BUTTON.get());
      dropSelf(ModBlocks.RUNED_TILE_BUTTON.get());
      dropSelf(ModBlocks.WILDWOOD_BUTTON.get());
      dropSelf(ModBlocks.RUNESTONE_PRESSURE_PLATE.get());
      dropSelf(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get());
      dropSelf(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get());
      dropSelf(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.get());
      dropSelf(ModBlocks.RUNED_PRESSURE_PLATE.get());
      dropSelf(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get());
      dropSelf(ModBlocks.RUNED_TILE_PRESSURE_PLATE.get());
      dropSelf(ModBlocks.WILDWOOD_PRESSURE_PLATE.get());
      add(ModBlocks.WILDWOOD_DOOR.get(), createDoorTable(ModBlocks.WILDWOOD_DOOR.get()));
      dropSelf(ModBlocks.WILDWOOD_TRAPDOOR.get());
      dropSelf(ModBlocks.WILDWOOD_LADDER.get());
      dropSelf(ModBlocks.WILDWOOD_GATE.get());
      dropSelf(ModBlocks.RUNESTONE_WALL.get());
      dropSelf(ModBlocks.MOSSY_RUNESTONE_WALL.get());
      dropSelf(ModBlocks.RUNESTONE_BRICK_WALL.get());
      dropSelf(ModBlocks.RUNESTONE_TILE_WALL.get());
      dropSelf(ModBlocks.RUNED_WALL.get());
      dropSelf(ModBlocks.RUNED_BRICK_WALL.get());
      dropSelf(ModBlocks.RUNED_TILE_WALL.get());
      dropSelf(ModBlocks.ELEMENTAL_SOIL.get());
      dropSelf(ModBlocks.AQUEOUS_SOIL.get());
      dropSelf(ModBlocks.CAELIC_SOIL.get());
      dropSelf(ModBlocks.MAGMATIC_SOIL.get());
      dropSelf(ModBlocks.TERRAN_SOIL.get());
      dropSelf(ModBlocks.ENCHANTED_TURF.get());
      dropSelf(ModBlocks.WILDWOOD_CHEST.get());
      dropSelf(ModBlocks.RITUAL_PEDESTAL.get());
      dropSelf(ModBlocks.REINFORCED_RITUAL_PEDESTAL.get());
      dropSelf(ModBlocks.GROVE_CRAFTER.get());
      dropSelf(ModBlocks.GROVE_PEDESTAL.get());
      dropSelf(ModBlocks.WILDWOOD_PEDESTAL.get());
      dropSelf(ModBlocks.DISPLAY_PEDESTAL.get());
      /*      dropSelf(ModBlocks.GROWTH_AMPLIFIER.get());*/
      add(ModBlocks.WILD_ROOTS.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .setRolls(ConstantValue.exactly(1f))
              .add(applyExplosionDecay(ModBlocks.WILD_ROOTS.get(), LootItem.lootTableItem(ModItems.WILDROOT.get())
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 3f))))))
          .withPool(LootPool.lootPool()
              .add(applyExplosionDecay(ModBlocks.WILD_ROOTS.get(), LootItem.lootTableItem(ModItems.GROVE_SPORES.get())
                  .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.8f)))
                  .when(new LootItemBlockStatePropertyCondition.Builder(ModBlocks.WILD_ROOTS.get()).setProperties(StatePropertiesPredicate.Builder.properties()
                      .hasProperty(WildRootsBlock.MOSSY, true))))))
      );
      add(ModBlocks.CREEPING_GROVE_MOSS.get(), applyExplosionDecay(ModBlocks.CREEPING_GROVE_MOSS.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.GROVE_MOSS.get())
                  .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))))
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.GROVE_MOSS.get())
                  .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.2f))))
              .when(new LootItemBlockStatePropertyCondition.Builder(ModBlocks.CREEPING_GROVE_MOSS.get()).setProperties(StatePropertiesPredicate.Builder.properties()
                  .hasProperty(CreepingGroveMossBlock.RITUAL_PLACED, false)))
          )
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.GROVE_SPORES.get())
                  .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.05f))))
              .when(new LootItemBlockStatePropertyCondition.Builder(ModBlocks.CREEPING_GROVE_MOSS.get()).setProperties(StatePropertiesPredicate.Builder.properties()
                  .hasProperty(CreepingGroveMossBlock.RITUAL_PLACED, false)))
          )));
      add(ModBlocks.HANGING_GROVE_MOSS.get(), applyExplosionDecay(ModBlocks.HANGING_GROVE_MOSS.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.GROVE_MOSS.get())
                  .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))))
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.GROVE_SPORES.get())
                  .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.2f)))))
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.GROVE_SPORES.get())
                  .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.05f)))))));
      add(ModBlocks.BAFFLECAP_BLOCK.get(), createSilkTouchDispatchTable(ModBlocks.BAFFLECAP_BLOCK.get(), applyExplosionDecay(ModBlocks.BAFFLECAP_BLOCK.get(), LootItem.lootTableItem(ModItems.BAFFLECAP.get())
          .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.083f))))));
      dropSelf(ModBlocks.INCENSE_BURNER.get());
      /*      dropSelf(ModBlocks.STONE_ALTAR.get());*/
      dropSelf(ModBlocks.MORTAR.get());
      dropSelf(ModBlocks.PYRE.get());
      dropSelf(ModBlocks.REINFORCED_PYRE.get());
      dropSelf(ModBlocks.SOUL_PYRE.get());
      dropSelf(ModBlocks.REINFORCED_SOUL_PYRE.get());
      dropSelf(ModBlocks.DECORATIVE_PYRE.get());
      dropSelf(ModBlocks.DECORATIVE_SOUL_PYRE.get());
      dropSelf(ModBlocks.UNENDING_BOWL.get());
      dropSelf(ModBlocks.BAFFLECAP.get());
      dropSelf(ModBlocks.FUNGAL_TRANSMUTER.get());
      addCropDrops(ModBlocks.WILDROOT_CROP.get(), ModItems.WILDROOT.get(), BeetrootBlock.AGE);
      addElementalCropDrops(ModBlocks.CLOUD_BERRY_CROP.get(), ModItems.CLOUD_BERRY.get(), ElementalType.AIR);
      addElementalCropDrops(ModBlocks.DEWGONIA_CROP.get(), ModItems.DEWGONIA.get(), ElementalType.WATER);
      addElementalCropDrops(ModBlocks.INFERNO_BULB_CROP.get(), ModItems.INFERNO_BULB.get(), ElementalType.FIRE);
      addElementalCropDrops(ModBlocks.STALICRIPE_CROP.get(), ModItems.STALICRIPE.get(), ElementalType.EARTH);
      addCropDrops(ModBlocks.MOONGLOW_CROP.get(), ModItems.MOONGLOW.get(), ModItems.MOONGLOW_SEEDS.get(), BaseBlocks.SeededCropsBlock.AGE);
      addCropDrops(ModBlocks.PERESKIA_CROP.get(), ModItems.PERESKIA.get(), ModItems.PERESKIA_BULB.get(), BaseBlocks.SeededCropsBlock.AGE);
      addCropDrops(ModBlocks.SPIRITLEAF_CROP.get(), ModItems.SPIRITLEAF.get(), ModItems.SPIRITLEAF_SEEDS.get(), FourStageCropBlock.AGE);
      addCropDrops(ModBlocks.WILDEWHEET_CROP.get(), ModItems.WILDEWHEET.get(), ModItems.WILDEWHEET_SEEDS.get(), BaseBlocks.SeededCropsBlock.AGE);
      addCropDrops(ModBlocks.AUBERGINE_CROP.get(), ModItems.AUBERGINE.get(), ModItems.AUBERGINE_SEEDS.get(), BaseBlocks.SeededCropsBlock.AGE);
      add(ModBlocks.WILD_AUBERGINE.get(), this.applyExplosionDecay(ModBlocks.WILD_AUBERGINE.get(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.AUBERGINE.get())
                  .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))))
          .withPool(LootPool.lootPool()
              .add(LootItem.lootTableItem(ModItems.AUBERGINE_SEEDS.get())
                  .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))))));
      add(ModBlocks.POTTED_BAFFLECAP.get(), createPotFlowerItemTable(ModBlocks.BAFFLECAP.get()));
      add(ModBlocks.POTTED_STONEPETAL.get(), createPotFlowerItemTable(ModBlocks.STONEPETAL.get()));
      add(ModBlocks.POTTED_WILDWOOD_SAPLING.get(), createPotFlowerItemTable(ModBlocks.WILDWOOD_SAPLING.get()));
      addFairyHouseDrops(ModBlocks.RED_FAIRY_HUT, ModItems.RED_FAIRY_HUT);
      addFairyHouseDrops(ModBlocks.BROWN_FAIRY_HUT, ModItems.BROWN_FAIRY_HUT);
      addFairyHouseDrops(ModBlocks.BAFFLECAP_FAIRY_HUT, ModItems.BAFFLECAP_FAIRY_HUT);
      addFairyHouseDrops(ModBlocks.CRIMSON_FAIRY_HUT, ModItems.CRIMSON_FAIRY_HUT);
      addFairyHouseDrops(ModBlocks.WARPED_FAIRY_HUT, ModItems.WARPED_FAIRY_HUT);

      addGroveStoneDrops(ModBlocks.PRIMAL_GROVE_STONE, ModItems.PRIMAL_GROVE_STONE);
      addGroveStoneDrops(ModBlocks.WILD_GROVE_STONE, ModItems.WILD_GROVE_STONE);
      addGroveStoneDrops(ModBlocks.FUNGAL_GROVE_STONE, ModItems.FUNGAL_GROVE_STONE);
      addGroveStoneDrops(ModBlocks.FAIRY_GROVE_STONE, ModItems.FAIRY_GROVE_STONE);
      addGroveStoneDrops(ModBlocks.CULTIVATION_GROVE_STONE, ModItems.CULTIVATION_GROVE_STONE);
      addGroveStoneDrops(ModBlocks.ELEMENTAL_GROVE_STONE, ModItems.ELEMENTAL_GROVE_STONE);
      addGroveStoneDrops(ModBlocks.TWILIGHT_GROVE_STONE, ModItems.TWILIGHT_GROVE_STONE);
    }

    protected void addElementalCropDrops(ElementalCropBlock cropBlock, Item cropItem, ElementalType matchingSoil) {
      IntegerProperty ageProperty = ((AccessorMixinCropBlock) cropBlock).rootsCallGetAgeProperty();

      HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);


      // Crop not grown
      LootItemCondition.Builder cropIsGrownCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ageProperty, cropBlock.getMaxAge()));

      // Soil conditions
      LootItemCondition.Builder normalSoilCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ElementalType.SOIL_TYPE, ElementalType.NONE));
      LootItemCondition.Builder plainElementalSoilCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ElementalType.SOIL_TYPE, ElementalType.DEFAULT));

      LootItemCondition.Builder matchingElementalSoilCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ElementalType.SOIL_TYPE, matchingSoil));

      List<ElementalType> others = new ArrayList<>();
      for (ElementalType type : ElementalType.values()) {
        if (type == matchingSoil) {
          continue;
        }
        others.add(type);
      }

      LootItemCondition.Builder otherType1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ElementalType.SOIL_TYPE, others.get(0)));
      LootItemCondition.Builder otherType2 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ElementalType.SOIL_TYPE, others.get(1)));
      LootItemCondition.Builder otherType3 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ElementalType.SOIL_TYPE, others.get(2)));

      LootItemConditionalFunction.Builder<?> fortune = ApplyBonusCount.addBonusBinomialDistributionCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.2714286F, 1);

      add(cropBlock, this.applyExplosionDecay(
          cropBlock,
          LootTable.lootTable()
              // Always drop 1 item as the seed, regardless of growth or soil type
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(cropItem))
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      // Matching Soil: Guaranteed 2 more (Total 3), plus 50% chance of 1 more
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))) // Exact 2 more
                          .when(matchingElementalSoilCondition)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))).apply(fortune)
                          .when(matchingElementalSoilCondition)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      // Base Elemental Soil: Guaranteed 1 more (Total 2), plus 50% chance of 1 more
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))) // Exact 1 more
                          .when(plainElementalSoilCondition)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))).apply(fortune)
                          .when(plainElementalSoilCondition)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      // Non-Matching Elemental Soil 3: Guaranteed 1 more (Total 2), plus 50% chance of 1 more
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))) // Exact 1 more
                          .when(otherType1)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))).apply(fortune)
                          .when(otherType1)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      // Non-Matching Elemental Soil 3: Guaranteed 1 more (Total 2), plus 50% chance of 1 more
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))) // Exact 1 more
                          .when(otherType2)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))).apply(fortune)
                          .when(otherType2)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      // Non-Matching Elemental Soil 3: Guaranteed 1 more (Total 2), plus 50% chance of 1 more
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))) // Exact 1 more
                          .when(otherType3)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))).apply(fortune)
                          .when(otherType3)
                      )
              )
              .withPool(
                  LootPool.lootPool()
                      .when(cropIsGrownCondition)
                      // Normal Soil: 50% chance of 1 more (Total 1 or 2)
                      .add(LootItem.lootTableItem(cropItem)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))).apply(fortune)
                          .when(normalSoilCondition)
                      )
              )
      ));
    }

    protected void addFairyHouseDrops(Holder<Block> house, Holder<Item> houseItem) {
      add(house.value(), applyExplosionDecay(houseItem.value(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .when(new LootItemBlockStatePropertyCondition.Builder(house.value()).setProperties(StatePropertiesPredicate.Builder.properties()
                  .hasProperty(FairyHutBlock.HALF, DoubleBlockHalf.LOWER)))
              .add(LootItem.lootTableItem(houseItem.value())
                  .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))))));
    }

    protected void addGroveStoneDrops(Holder<Block> groveStone, Holder<Item> groveStoneItem) {
      add(groveStone.value(), applyExplosionDecay(groveStoneItem.value(), LootTable.lootTable()
          .withPool(LootPool.lootPool()
              .when(new LootItemBlockStatePropertyCondition.Builder(groveStone.value()).setProperties(StatePropertiesPredicate.Builder.properties()
                  .hasProperty(GroveStoneBlock.PART, StateProperties.Part.BOTTOM)))
              .add(LootItem.lootTableItem(groveStoneItem.value())
                  .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))))));
    }

    protected void addCropDrops(Block cropBlock, Item grownCropItem, Item seedsItem, IntegerProperty ageProperty) {
      // TODO: Fortune affects non-seeds
      HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
      LootItemCondition.Builder dropGrownCropCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ageProperty, Collections.max(ageProperty.getPossibleValues())));
      add(cropBlock, this.applyExplosionDecay(
          cropBlock,
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(grownCropItem)
                      .when(dropGrownCropCondition)
                      .otherwise(LootItem.lootTableItem(seedsItem))))
              .withPool(
                  LootPool.lootPool()
                      .when(dropGrownCropCondition)
                      .add(
                          LootItem.lootTableItem(seedsItem)
                              .apply(ApplyBonusCount.addBonusBinomialDistributionCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))
                      )
              )));
    }

    protected void addCropDrops(Block cropBlock, Item grownCropItem, IntegerProperty ageProperty) {
      // TODO: Fortune affects non-seeds
      HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
      LootItemCondition.Builder dropGrownCropCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(ageProperty, Collections.max(ageProperty.getPossibleValues())));
      add(cropBlock, this.applyExplosionDecay(
          cropBlock,
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(grownCropItem)))
              .withPool(
                  LootPool.lootPool()
                      .when(dropGrownCropCondition)
                      .add(
                          LootItem.lootTableItem(grownCropItem)
                              .apply(ApplyBonusCount.addBonusBinomialDistributionCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.2714286F, 3))
                      )
              )));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
      List<Block> blocks = new ArrayList<>();
      for (Map.Entry<ResourceKey<Block>, Block> entry : BuiltInRegistries.BLOCK.entrySet()) {
        if (entry.getKey().location().getNamespace().equals(RootsAPI.MODID)) {
          blocks.add(entry.getValue());
        }
      }
      return blocks;
    }

    protected LootTable.Builder createWildwoodLeaves(Block block) {
      HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
      return this.createSilkTouchOrShearsDispatchTable(
              block,
              ((LootPoolSingletonContainer.Builder<?>) this.applyExplosionDecay(
                  ModBlocks.WILDWOOD_LEAVES.get(), LootItem.lootTableItem(Items.STICK)
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
              ))
                  .when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), ((AccessorMixinBlockLootSubProvider) this).rootsGetNORMAL_LEAVES_STICK_CHANCES()))
          )
          .withPool(
              LootPool.lootPool()
                  .setRolls(ConstantValue.exactly(1.0F))
                  .when(((AccessorMixinBlockLootSubProvider) this).RootsCallDoesNotHaveShearsOrSilkTouch())
                  .add(
                      ((LootPoolSingletonContainer.Builder<?>) this.applyExplosionCondition(ModBlocks.WILDWOOD_LEAVES.get(), LootItem.lootTableItem(ModItems.WILDROOT.get())))
                          .when(
                              BonusLevelTableCondition.bonusLevelFlatChance(
                                  registrylookup.getOrThrow(Enchantments.FORTUNE), 0.02f, 0.03f, 0.04f, 0.05f, 0.1f
                              )
                          )
                  )
          );
    }
  }

  public static class ChestLootTablesFixer implements LootTableSubProvider {
    private final HolderLookup.Provider provider;

    public ChestLootTablesFixer(HolderLookup.Provider provider) {
      this.provider = provider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
      ResourceKey<LootTable> old_stones = ResourceKey.create(Registries.LOOT_TABLE, RootsAPI.rl("standing_stones"));
      ResourceKey<LootTable> old_barrow = ResourceKey.create(Registries.LOOT_TABLE, RootsAPI.rl("barrow"));
      ResourceKey<LootTable> old_hut = ResourceKey.create(Registries.LOOT_TABLE, RootsAPI.rl("hut"));

      consumer.accept(
          old_stones,
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(
                      NestedLootTable.lootTableReference(RootsAPI.STANDING_STONES)
                  ))
      );
      consumer.accept(
          old_barrow,
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(
                      NestedLootTable.lootTableReference(RootsAPI.BARROW)
                  ))
      );
      consumer.accept(
          old_hut,
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(
                      NestedLootTable.lootTableReference(RootsAPI.HUT)
                  ))
      );
    }
  }

  public static class ChestLootTables implements LootTableSubProvider {
    private final HolderLookup.Provider provider;

    public ChestLootTables(HolderLookup.Provider provider) {
      this.provider = provider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
      consumer.accept(
          RootsAPI.TENTACLES,
          LootTable.lootTable()
              .withPool(LootPool.lootPool()
                  .add(LootItem.lootTableItem(ModItems.RAW_SQUID.get())
                      .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                      .apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider, UniformGenerator.between(0, 1)))
                      .apply(SmeltItemFunction.smelted()
                          .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                              .flags(EntityFlagsPredicate.Builder.flags()
                                  .setOnFire(true)))))
                  )
                  .setRolls(ConstantValue.exactly(1))
              )
      );
      consumer.accept(
          RootsAPI.STANDING_STONES,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(5, 7))
                      .setBonusRolls(UniformGenerator.between(1, 3))
                      .add(LootItem.lootTableItem(Blocks.SHORT_GRASS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0f, 15.0f))))
                      .add(LootItem.lootTableItem(Items.FERN)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.PUMPKIN)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.HAY_BLOCK)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.DRIED_KELP_BLOCK)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.PACKED_ICE)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.COBWEB)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.RED_MUSHROOM)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.SWEET_BERRIES)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 16.0F))))
                      .add(LootItem.lootTableItem(Items.POPPY)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.DANDELION)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 10.0F))))
                      .add(LootItem.lootTableItem(Items.OXEYE_DAISY)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
                      .add(LootItem.lootTableItem(Items.AZURE_BLUET)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.VINE)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.CACTUS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.TALL_GRASS)
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.LARGE_FERN)
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.BLUE_ORCHID)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.DEAD_BUSH)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))
                      .add(LootItem.lootTableItem(Items.STRING)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.LILY_PAD)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(ModItems.WILDROOT.get())
                          .setWeight(15)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 17))))
                      .add(LootItem.lootTableItem(ModItems.GROVE_SPORES.get())
                          .setWeight(4)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 8))))));

      // Hut/Ruined Hut chest
      consumer.accept(
          RootsAPI.HUT,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(3, 6))
                      .setBonusRolls(UniformGenerator.between(2, 4))
                      .add(LootItem.lootTableItem(Items.SHORT_GRASS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0f, 15.0f))))
                      .add(LootItem.lootTableItem(Items.FERN)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.PUMPKIN)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.HAY_BLOCK)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.DRIED_KELP_BLOCK)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.PACKED_ICE)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.COBWEB)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.RED_MUSHROOM)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.SWEET_BERRIES)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 16.0F))))
                      .add(LootItem.lootTableItem(Items.POPPY)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.DANDELION)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 10.0F))))
                      .add(LootItem.lootTableItem(Items.OXEYE_DAISY)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
                      .add(LootItem.lootTableItem(Items.AZURE_BLUET)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.VINE)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.CACTUS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.SHORT_GRASS)
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.LARGE_FERN)
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.BLUE_ORCHID)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.DEAD_BUSH)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))
                      .add(LootItem.lootTableItem(Items.STRING)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.LILY_PAD)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
              )
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(1, 3))
                      .setBonusRolls(UniformGenerator.between(2, 3))
                      .add(LootItem.lootTableItem(Items.RED_TULIP)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.ORANGE_TULIP)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.PINK_TULIP)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.LILY_OF_THE_VALLEY)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.ALLIUM)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.CORNFLOWER)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.SUNFLOWER)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.LILAC)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.ROSE_BUSH)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.BAMBOO)
                          .setWeight(5)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.SEAGRASS)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.SEA_PICKLE)
                          .setWeight(2)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.BREAD)
                          .setWeight(8)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.WHEAT)
                          .setWeight(8)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.AUBERGINE.get())
                          .setWeight(8)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.COOKED_AUBERGINE.get())
                          .setWeight(8)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.APPLE)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.CARROT)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.POTATO)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.BEETROOT)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.SPIDER_EYE)
                          .setWeight(2)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(ModItems.AUBERGINE_SALAD.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.BEETROOT_SALAD.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.STEWED_EGGPLANT.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.VINEGAR.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.PEONY_CORDIAL.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.ROSE_CORDIAL.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.LILAC_CORDIAL.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.CACTUS_SYRUP.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.APPLE_CORDIAL.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.DANDELION_CORDIAL.get())
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
              )
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(0, 2))
                      .setBonusRolls(UniformGenerator.between(1, 2))
                      .add(LootItem.lootTableItem(Items.WITHER_ROSE)
                          .setWeight(1)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 4.0F))))
                      .add(LootItem.lootTableItem(ModItems.CARAPACE.get())
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(ModItems.ANTLERS.get())
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.FEATHER)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.FLOWER_POT)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))
                      .add(LootItem.lootTableItem(Items.LAPIS_LAZULI)
                          .setWeight(4)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0f, 18.0f))))
                      .add(LootItem.lootTableItem(Items.TROPICAL_FISH)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.PUFFERFISH)
                          .setWeight(6)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))
                      .add(LootItem.lootTableItem(Items.SALMON)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.COD)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.CAKE)
                          .setWeight(3))
                      .add(LootItem.lootTableItem(Items.COOKIE)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0f, 18.0f))))
                      .add(LootItem.lootTableItem(Items.COCOA_BEANS)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.PUMPKIN_PIE)
                          .setWeight(9)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 12.0f))))
                      .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE)
                          .setWeight(2)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f))))
                      .add(LootItem.lootTableItem(Items.NAME_TAG)
                          .setWeight(5)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))
                      .add(LootItem.lootTableItem(Items.HONEY_BOTTLE)
                          .setWeight(2)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))
                      .add(LootItem.lootTableItem(Items.NAUTILUS_SHELL)
                          .setWeight(5)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))
                      .add(EmptyLootItem.emptyItem()
                          .setWeight(8))
              )
      );


      // BARROW

      consumer.accept(
          RootsAPI.BARROW,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(2, 5))
                      .setBonusRolls(UniformGenerator.between(1f, 5f))
                      .add(LootItem.lootTableItem(Items.WATER_BUCKET)
                          .setWeight(10)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))
                      .add(LootItem.lootTableItem(Items.CLAY_BALL)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.LAPIS_LAZULI)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.COAL)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.BOOK)
                          .setWeight(22)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(9.0F, 21.0F))))
                      .add(LootItem.lootTableItem(Items.BONE)
                          .setWeight(16)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.SHEARS)
                          .apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)))
                      .add(LootItem.lootTableItem(Items.CHAIN)
                          .setWeight(8)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.ARROW)
                          .setWeight(20)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.SADDLE))
              )
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(1, 2))
                      .setBonusRolls(UniformGenerator.between(1, 2))
                      .add(LootItem.lootTableItem(Items.LAVA_BUCKET)
                          .setWeight(12)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                      .add(LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS)
                          .setWeight(2)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.PRISMARINE_SHARD)
                          .setWeight(2)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.QUARTZ)
                          .setWeight(5)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.GOLD_INGOT)
                          .setWeight(2)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.REDSTONE)
                          .setWeight(8)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.GUNPOWDER)
                          .setWeight(4)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 12.0F))))
                      .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST)
                          .setWeight(4)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
                      .add(LootItem.lootTableItem(Items.SPONGE)
                          .setWeight(1))
                      .add(LootItem.lootTableItem(Items.MAGMA_CREAM)
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
              )
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(0, 2))
                      .setBonusRolls(UniformGenerator.between(1, 2))
                      .add(LootItem.lootTableItem(Items.ENDER_PEARL)
                          .setWeight(1)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 5.0F))))
                      .add(LootItem.lootTableItem(Items.FIREWORK_ROCKET)
                          .setWeight(5)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))
/*                      .add(LootItem.lootTableItem(ModItems.ANTLER_HAT.get()).setWeight(3).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))*/
                      .add(LootItem.lootTableItem(ModItems.BEETLE_HELMET.get())
                          .setWeight(6)
                          .apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)))
                      .add(LootItem.lootTableItem(ModItems.BEETLE_CHESTPLATE.get())
                          .setWeight(1)
                          .apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)))
                      .add(LootItem.lootTableItem(ModItems.BEETLE_BOOTS.get())
                          .setWeight(6)
                          .apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)))
                      .add(LootItem.lootTableItem(ModItems.BEETLE_LEGGINGS.get())
                          .setWeight(3)
                          .apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)))
                      .add(LootItem.lootTableItem(Items.OBSIDIAN)
                          .setWeight(5)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.NETHER_WART_BLOCK)
                          .setWeight(3)
                          .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))
                      .add(LootItem.lootTableItem(Items.PHANTOM_MEMBRANE)
                          .setWeight(2))
                      .add(LootItem.lootTableItem(ModItems.COPPER_HELMET.get())
                          .setWeight(8)
                          .apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)))
                      .add(LootItem.lootTableItem(ModItems.COPPER_CHESTPLATE.get())
                          .setWeight(12)
                          .apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)))
                      .add(LootItem.lootTableItem(Items.CROSSBOW)
                          .setWeight(3)
                          .apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)))
                      .add(EmptyLootItem.emptyItem()
                          .setWeight(4))
              )
      );
    }
  }
}
