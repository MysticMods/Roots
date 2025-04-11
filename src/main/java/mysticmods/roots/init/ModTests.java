package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.growth.CanGrowFunction;
import mysticmods.roots.api.growth.CanHarvestFunction;
import mysticmods.roots.api.growth.HarvestFunction;
import mysticmods.roots.api.growth.LightFunction;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.api.test.world.*;
import mysticmods.roots.growth.growable.*;
import mysticmods.roots.growth.harvest.*;
import mysticmods.roots.growth.harvestable.*;
import mysticmods.roots.test.entity.EntityTagTest;
import mysticmods.roots.test.entity.EntityTypeTest;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTests {
  private static final DeferredRegister<EntityTestType<?>> ENTITY_TESTS = DeferredRegister.create(RootsRegistries.Keys.ENTITY_TEST_TYPES, RootsAPI.MODID);
  private static final DeferredRegister<WorldTestType<?>> WORLD_TESTS = DeferredRegister.create(RootsRegistries.Keys.WORLD_TEST_TYPES, RootsAPI.MODID);
  private static final DeferredRegister<CanGrowFunction> CAN_GROW_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.CAN_GROW_FUNCTIONS, RootsAPI.MODID);
  private static final DeferredRegister<LightFunction> LIGHT_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.LIGHT_FUNCTIONS, RootsAPI.MODID);
  private static final DeferredRegister<CanHarvestFunction> CAN_HARVEST_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.CAN_HARVEST_FUNCTIONS, RootsAPI.MODID);
  private static final DeferredRegister<HarvestFunction> HARVEST_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.HARVEST_FUNCTIONS, RootsAPI.MODID);

  public static final DeferredHolder<EntityTestType<?>, EntityTestType<EntityTagTest>> ENTITY_TAG_TEST = ENTITY_TESTS.register("entity_tag_test", EntityTagTest.Type::new);
  public static final DeferredHolder<EntityTestType<?>, EntityTestType<EntityTypeTest>> ENTITY_TYPE_TEST = ENTITY_TESTS.register("entity_type_test", EntityTypeTest.Type::new);

  public static final DeferredHolder<WorldTestType<?>, WorldTestType<AlwaysTrueWorldTest>> ALWAYS_TRUE_TEST = WORLD_TESTS.register(AlwaysTrueWorldTest.ALWAYS_TRUE_TEST_KEY.location()
      .getPath(), AlwaysTrueWorldTest.Type::new);
  public static final DeferredHolder<WorldTestType<?>, WorldTestType<BlockMatchWorldTest>> BLOCK_MATCH_TEST = WORLD_TESTS.register(BlockMatchWorldTest.BLOCK_MATCH_TEST_KEY.location()
      .getPath(), BlockMatchWorldTest.Type::new);
  public static final DeferredHolder<WorldTestType<?>, WorldTestType<TagMatchWorldTest>> TAG_MATCH_TEST = WORLD_TESTS.register(TagMatchWorldTest.TAG_MATCH_TEST_KEY.location()
      .getPath(), TagMatchWorldTest.Type::new);
  public static final DeferredHolder<WorldTestType<?>, WorldTestType<PartialBlockStateMatchWorldTest>> PARTIAL_BLOCK_STATE_MATCH_TEST = WORLD_TESTS.register(PartialBlockStateMatchWorldTest.PARTIAL_BLOCK_STATE_MATCH_TEST_KEY.location()
      .getPath(), PartialBlockStateMatchWorldTest.Type::new);

  public static final DeferredHolder<CanGrowFunction, AlwaysCanGrowFunction> ALWAYS_CAN_GROW = CAN_GROW_FUNCTIONS.register("always_can_grow", AlwaysCanGrowFunction::new);
  public static final DeferredHolder<CanGrowFunction, AlwaysCanGrowUp> ALWAYS_CAN_GROW_UP = CAN_GROW_FUNCTIONS.register("always_can_grow_up", AlwaysCanGrowUp::new);
  public static final DeferredHolder<CanGrowFunction, AgeCanGrowFunction> AGE_CAN_GROW = CAN_GROW_FUNCTIONS.register("age_can_grow", AgeCanGrowFunction::new);
  public static final DeferredHolder<CanGrowFunction, CactusCanGrowFunction> CACTUS_CANE_CAN_GROW = CAN_GROW_FUNCTIONS.register("cactus_or_cane_can_grow", CactusCanGrowFunction::new);
  public static final DeferredHolder<CanGrowFunction, KelpCanGrowFunction> KELP_CAN_GROW = CAN_GROW_FUNCTIONS.register("kelp_can_grow", KelpCanGrowFunction::new);
  public static final DeferredHolder<CanGrowFunction, VinesCanSpreadFunction> VINES_CAN_SPREAD = CAN_GROW_FUNCTIONS.register("vines_can_spread", VinesCanSpreadFunction::new);
  public static final DeferredHolder<CanGrowFunction, AgeCanGrowDirectionFunction> AGE_CAN_GROW_UP = CAN_GROW_FUNCTIONS.register("age_can_grow_up", () -> new AgeCanGrowDirectionFunction(Direction.UP));
  public static final DeferredHolder<CanGrowFunction, AgeCanGrowDirectionFunction> AGE_CAN_GROW_DOWN = CAN_GROW_FUNCTIONS.register("age_can_grow_down", () -> new AgeCanGrowDirectionFunction(Direction.DOWN));
  public static final DeferredHolder<CanGrowFunction, StemBlockCanGrow> STEM_BLOCK_CAN_GROW = CAN_GROW_FUNCTIONS.register("stem_block_can_grow", StemBlockCanGrow::new);

  public static final DeferredHolder<LightFunction, LightFunction.AnyLightFunction> ANY_LIGHT = LIGHT_FUNCTIONS.register("any_light", LightFunction.AnyLightFunction::new);
  public static final DeferredHolder<LightFunction, LightFunction.LightGreaterThanFunction> LIGHT_ABOVE_ZERO = LIGHT_FUNCTIONS.register("light_above_zero", () -> new LightFunction.LightGreaterThanFunction(0));
  public static final DeferredHolder<LightFunction, LightFunction.LightGreaterThanFunction> LIGHT_ABOVE_EIGHT = LIGHT_FUNCTIONS.register("light_above_eight", () -> new LightFunction.LightGreaterThanFunction(8));
  public static final DeferredHolder<LightFunction, LightFunction.LightLessThanFunction> LIGHT_BELOW_THIRTEEN = LIGHT_FUNCTIONS.register("light_below_thirteen", () -> new LightFunction.LightLessThanFunction(13));

  public static final DeferredHolder<CanHarvestFunction, SingleCropAgeCanHarvest> SINGLE_CROP_AGE = CAN_HARVEST_FUNCTIONS.register("single_crop_age", SingleCropAgeCanHarvest::new);

  public static final DeferredHolder<CanHarvestFunction, SingleCropAgeCanSafeHarvest> SAFE_SINGLE_CROP_AGE = CAN_HARVEST_FUNCTIONS.register("safe_single_crop_age", SingleCropAgeCanSafeHarvest::new);

  public static final DeferredHolder<CanHarvestFunction, CanHarvestLowestBlock> CAN_HARVEST_LOWEST = CAN_HARVEST_FUNCTIONS.register("can_harvest_lowest_block", CanHarvestLowestBlock::new);

  public static final DeferredHolder<CanHarvestFunction, CanHarvestTwoBlockPlantAge> CAN_HARVEST_TWO_BLOCK_PLANT_AGE = CAN_HARVEST_FUNCTIONS.register("can_harvest_two_block_plant_age", CanHarvestTwoBlockPlantAge::new);
  public static final DeferredHolder<CanHarvestFunction, CanHarvestGrowingPlantBlock> CAN_HARVEST_GROWING_PLANT_BLOCK = CAN_HARVEST_FUNCTIONS.register("can_harvest_growing_plant_block", CanHarvestGrowingPlantBlock::new);
  public static final DeferredHolder<CanHarvestFunction, CanHarvestGlowBerries> CAN_HARVEST_GLOW_BERRIES = CAN_HARVEST_FUNCTIONS.register("can_harvest_glow_berries", CanHarvestGlowBerries::new);

  public static final DeferredHolder<CanHarvestFunction, CanHarvestStemBlock> CAN_HARVEST_STEM_BLOCK = CAN_HARVEST_FUNCTIONS.register("can_harvest_stem_block", CanHarvestStemBlock::new);
  public static final DeferredHolder<CanHarvestFunction, CanHarvestOnFarmland> CAN_SAFE_HARVEST_FARMLAND = CAN_HARVEST_FUNCTIONS.register("safe_harvest_farmland", CanHarvestOnFarmland::new);

  public static final DeferredHolder<HarvestFunction, HarvestAllAboveSameBlock> HARVEST_ALL_ABOVE_SAME_BLOCK = HARVEST_FUNCTIONS.register("harvest_all_above_same_block", HarvestAllAboveSameBlock::new);
  public static final DeferredHolder<HarvestFunction, HarvestBreakSingleBlock> HARVEST_BREAK_SINGLE_BLOCK = HARVEST_FUNCTIONS.register("harvest_break_single_block", HarvestBreakSingleBlock::new);
  public static final DeferredHolder<HarvestFunction, HarvestCropAndAbove> HARVEST_CROP_AND_ABOVE = HARVEST_FUNCTIONS.register("harvest_crop_and_above", HarvestCropAndAbove::new);
  public static final DeferredHolder<HarvestFunction, HarvestSingleCropBlock> HARVEST_SINGLE_CROP_BLOCK = HARVEST_FUNCTIONS.register("harvest_single_crop_block", HarvestSingleCropBlock::new);
  public static final DeferredHolder<HarvestFunction, HarvestGrowingPlantBlock> HARVEST_GROWING_PLANT_BLOCK = HARVEST_FUNCTIONS.register("harvest_growing_plant_block", HarvestGrowingPlantBlock::new);
  public static final DeferredHolder<HarvestFunction, HarvestSweetBerries> HARVEST_SWEET_BERRIES = HARVEST_FUNCTIONS.register("harvest_sweet_berries", HarvestSweetBerries::new);
  public static final DeferredHolder<HarvestFunction, HarvestGlowBerries> HARVEST_GLOW_BERRIES = HARVEST_FUNCTIONS.register("harvest_glow_berries", HarvestGlowBerries::new);

  public static void register(IEventBus bus) {
    ENTITY_TESTS.register(bus);
    WORLD_TESTS.register(bus);
    CAN_GROW_FUNCTIONS.register(bus);
    CAN_HARVEST_FUNCTIONS.register(bus);
    LIGHT_FUNCTIONS.register(bus);
    HARVEST_FUNCTIONS.register(bus);
  }
}
