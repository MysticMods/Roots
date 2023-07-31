package mysticmods.roots.gen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableGenerator extends LootTableProvider {
  private static final ResourceLocation HUT = RootsAPI.rl("hut");
  private static final ResourceLocation BARROW = RootsAPI.rl("barrow");
  private static final ResourceLocation STANDING_STONES = RootsAPI.rl("standing_stones");

  private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = ImmutableList.of(Pair.of(ChestLootTables::new, LootContextParamSets.CHEST));

  public LootTableGenerator(DataGenerator dataGeneratorIn) {
    super(dataGeneratorIn);
  }

  @Override
  protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
    return tables;
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
    map.forEach((id, table) -> LootTables.validate(validationtracker, id, table));
  }

  @SuppressWarnings("Duplicates")
  public static class ChestLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {

      consumer.accept(
        STANDING_STONES,
      LootTable.lootTable()
        .withPool(
          LootPool.lootPool()
            .setRolls(UniformGenerator.between(5, 7))
              .setBonusRolls(UniformGenerator.between(1, 3))
            .add(LootItem.lootTableItem(Items.GRASS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0f, 15.0f))))
            .add(LootItem.lootTableItem(Items.FERN).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 15.0F))))
            .add(LootItem.lootTableItem(Items.PUMPKIN).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
            .add(LootItem.lootTableItem(Items.HAY_BLOCK).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
            .add(LootItem.lootTableItem(Items.DRIED_KELP_BLOCK).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
            .add(LootItem.lootTableItem(Items.PACKED_ICE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
            .add(LootItem.lootTableItem(Items.COBWEB).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
            .add(LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
            .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
            .add(LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 16.0F))))
            .add(LootItem.lootTableItem(Items.POPPY).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
            .add(LootItem.lootTableItem(Items.DANDELION).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 10.0F))))
            .add(LootItem.lootTableItem(Items.OXEYE_DAISY).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
            .add(LootItem.lootTableItem(Items.AZURE_BLUET).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
            .add(LootItem.lootTableItem(Items.VINE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
            .add(LootItem.lootTableItem(Items.CACTUS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
            .add(LootItem.lootTableItem(Items.TALL_GRASS).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
            .add(LootItem.lootTableItem(Items.LARGE_FERN).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
            .add(LootItem.lootTableItem(Items.BLUE_ORCHID).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
            .add(LootItem.lootTableItem(Items.DEAD_BUSH).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))
            .add(LootItem.lootTableItem(Items.STRING).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
            .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
            .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
            .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
            .add(LootItem.lootTableItem(Items.LILY_PAD).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
              .add(LootItem.lootTableItem(ModItems.WILDROOT.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 17))))
                .add(LootItem.lootTableItem(ModItems.GROVE_SPORES.get()).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 8))))));

      // Hut/Ruined Hut chest
      consumer.accept(
          HUT,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(3, 6))
                      .setBonusRolls(UniformGenerator.between(2, 4))
                      .add(LootItem.lootTableItem(Items.GRASS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0f, 15.0f))))
                      .add(LootItem.lootTableItem(Items.FERN).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.PUMPKIN).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.HAY_BLOCK).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.DRIED_KELP_BLOCK).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.PACKED_ICE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.COBWEB).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 16.0F))))
                      .add(LootItem.lootTableItem(Items.POPPY).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.DANDELION).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 10.0F))))
                      .add(LootItem.lootTableItem(Items.OXEYE_DAISY).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
                      .add(LootItem.lootTableItem(Items.AZURE_BLUET).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.VINE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.CACTUS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F))))
                      .add(LootItem.lootTableItem(Items.TALL_GRASS).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.LARGE_FERN).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.BLUE_ORCHID).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.DEAD_BUSH).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))
                      .add(LootItem.lootTableItem(Items.STRING).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
                      .add(LootItem.lootTableItem(Items.LILY_PAD).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))))
              )
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(1, 3))
                      .setBonusRolls(UniformGenerator.between(2, 3))
                      .add(LootItem.lootTableItem(Items.RED_TULIP).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.ORANGE_TULIP).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.PINK_TULIP).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.LILY_OF_THE_VALLEY).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.ALLIUM).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.CORNFLOWER).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.SUNFLOWER).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.LILAC).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.ROSE_BUSH).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.BAMBOO).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.SEAGRASS).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.SEA_PICKLE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.BREAD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.WHEAT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.AUBERGINE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.COOKED_AUBERGINE.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.APPLE).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.CARROT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.POTATO).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.BEETROOT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(ModItems.AUBERGINE_SALAD.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.DANDELION_CORNFLOWER_SALAD.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.CACTUS_DANDELION_SALAD.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.BEETROOT_SALAD.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.STEWED_EGGPLANT.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.VINEGAR.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.PEONY_CORDIAL.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.ROSE_CORDIAL.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.LILAC_CORDIAL.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.CACTUS_SYRUP.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.APPLE_CORDIAL.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
                      .add(LootItem.lootTableItem(ModItems.DANDELION_CORDIAL.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
              )
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(0, 2))
                      .setBonusRolls(UniformGenerator.between(1, 2))
                      .add(LootItem.lootTableItem(Items.WITHER_ROSE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 4.0F))))
                      .add(LootItem.lootTableItem(ModItems.CARAPACE.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(ModItems.ANTLERS.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.FEATHER).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.FLOWER_POT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))
                      .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0f, 18.0f))))
                      .add(LootItem.lootTableItem(Items.TROPICAL_FISH).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.PUFFERFISH).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))
                      .add(LootItem.lootTableItem(Items.SALMON).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.COD).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.CAKE).setWeight(3))
                      .add(LootItem.lootTableItem(Items.COOKIE).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0f, 18.0f))))
                      .add(LootItem.lootTableItem(Items.COCOA_BEANS).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.PUMPKIN_PIE).setWeight(9).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 12.0f))))
                      .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f))))
                      .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))
                      .add(LootItem.lootTableItem(Items.HONEY_BOTTLE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))
                      .add(LootItem.lootTableItem(Items.NAUTILUS_SHELL).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))
                      .add(EmptyLootItem.emptyItem().setWeight(8))
              )
      );


      // BARROW

      consumer.accept(
          BARROW,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(2, 5))
                      .setBonusRolls(UniformGenerator.between(1f, 5f))
                      .add(LootItem.lootTableItem(Items.WATER_BUCKET).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))
                      .add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(8.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.COAL).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.BOOK).setWeight(22).apply(SetItemCountFunction.setCount(UniformGenerator.between(9.0F, 21.0F))))
                      .add(LootItem.lootTableItem(Items.BONE).setWeight(16).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.SHEARS).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(LootItem.lootTableItem(Items.CHAIN).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.ARROW).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 15.0F))))
                      .add(LootItem.lootTableItem(Items.SADDLE))
              )
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(1, 2))
                      .setBonusRolls(UniformGenerator.between(1, 2))
                      .add(LootItem.lootTableItem(Items.LAVA_BUCKET).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                      .add(LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.PRISMARINE_SHARD).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.QUARTZ).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                      .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                      .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F))))
                      .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 12.0F))))
                      .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F))))
                      .add(LootItem.lootTableItem(Items.SPONGE).setWeight(1))
                      .add(LootItem.lootTableItem(Items.MAGMA_CREAM).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))))
              )
              .withPool(
                  LootPool.lootPool()
                      .setRolls(UniformGenerator.between(0, 2))
                      .setBonusRolls(UniformGenerator.between(1, 2))
                      .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 5.0F))))
                      .add(LootItem.lootTableItem(Items.FIREWORK_ROCKET).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))
                      .add(LootItem.lootTableItem(ModItems.ANTLER_HAT.get()).setWeight(3).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(LootItem.lootTableItem(ModItems.BEETLE_HELMET.get()).setWeight(6).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(LootItem.lootTableItem(ModItems.BEETLE_CHESTPLATE.get()).setWeight(1).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(LootItem.lootTableItem(ModItems.BEETLE_BOOTS.get()).setWeight(6).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(LootItem.lootTableItem(ModItems.BEETLE_LEGGINGS.get()).setWeight(3).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 7.0f))))
                      .add(LootItem.lootTableItem(Items.NETHER_WART_BLOCK).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))
                      .add(LootItem.lootTableItem(Items.PHANTOM_MEMBRANE).setWeight(2))
                      .add(LootItem.lootTableItem(ModItems.COPPER_HELMET.get()).setWeight(8).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(LootItem.lootTableItem(ModItems.COPPER_CHESTPLATE.get()).setWeight(12).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(LootItem.lootTableItem(Items.CROSSBOW).setWeight(3).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                      .add(EmptyLootItem.emptyItem().setWeight(4))
              )
      );
    }
  }
}
