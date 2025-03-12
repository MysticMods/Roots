package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.growth.*;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.api.test.world.*;
import mysticmods.roots.growth.drops.GenerateCropsDrop;
import mysticmods.roots.growth.drops.NoDrops;
import mysticmods.roots.growth.grow.*;
import mysticmods.roots.growth.harvest.CanHarvestLowestBlock;
import mysticmods.roots.growth.harvest.CanHarvestStemBlock;
import mysticmods.roots.growth.harvest.SingleCropAgeCanHarvest;
import mysticmods.roots.growth.replant.*;
import mysticmods.roots.test.entity.EntityTagTest;
import mysticmods.roots.test.entity.EntityTypeTest;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTests {
  private static final DeferredRegister<EntityTestType<?>> ENTITY_TESTS = DeferredRegister.create(RootsRegistries.Keys.ENTITY_TEST_TYPES, RootsAPI.MODID);
  private static final DeferredRegister<WorldTestType<?>> WORLD_TESTS = DeferredRegister.create(RootsRegistries.Keys.WORLD_TEST_TYPES, RootsAPI.MODID);
  private static final DeferredRegister<CanGrowFunction> CAN_GROW_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.CAN_GROW_FUNCTIONS, RootsAPI.MODID);
  private static final DeferredRegister<ReplantFunction> REPLANT_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.REPLANT_FUNCTIONS, RootsAPI.MODID);
  private static final DeferredRegister<LightFunction> LIGHT_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.LIGHT_FUNCTIONS, RootsAPI.MODID);
  private static final DeferredRegister<CanHarvestFunction> CAN_HARVEST_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.CAN_HARVEST_FUNCTIONS, RootsAPI.MODID);
  private static final DeferredRegister<GetDropsFunction> GET_DROPS_FUNCTIONS = DeferredRegister.create(RootsRegistries.Keys.GET_DROPS_FUNCTIONS, RootsAPI.MODID);

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
  public static final DeferredHolder<CanGrowFunction, AgeCanGrowDirectionFunction> AGE_CAN_GROW_UP = CAN_GROW_FUNCTIONS.register("age_can_grow_up", () -> new AgeCanGrowDirectionFunction(Direction.UP));
  public static final DeferredHolder<CanGrowFunction, AgeCanGrowDirectionFunction> AGE_CAN_GROW_DOWN = CAN_GROW_FUNCTIONS.register("age_can_grow_down", () -> new AgeCanGrowDirectionFunction(Direction.DOWN));

  public static final DeferredHolder<ReplantFunction, NoReplantFunction> NO_REPLANT = REPLANT_FUNCTIONS.register("no_replant", NoReplantFunction::new);
  public static final DeferredHolder<ReplantFunction, AgeReplantFunction> AGE_REPLANT = REPLANT_FUNCTIONS.register("age_replant", AgeReplantFunction::new);
  public static final DeferredHolder<ReplantFunction ,BreakBlockFunction> BREAK_BLOCK = REPLANT_FUNCTIONS.register("break_block", BreakBlockFunction::new);
  public static final DeferredHolder<ReplantFunction, AirReplantFunction> REPLACE_WITH_AIR = REPLANT_FUNCTIONS.register("replace_with_air", AirReplantFunction::new);
  public static final DeferredHolder<ReplantFunction, AgeAndBreakAboveReplantFunction> AGE_REPLANT_BREAK_ABOVE = REPLANT_FUNCTIONS.register("age_replant_break_above", AgeAndBreakAboveReplantFunction::new);
  public static final DeferredHolder<ReplantFunction, SelfReplantFunction> SELF_REPLANT = REPLANT_FUNCTIONS.register("self_replant", SelfReplantFunction::new);

  public static final DeferredHolder<LightFunction, LightFunction.AnyLightFunction> ANY_LIGHT = LIGHT_FUNCTIONS.register("any_light", LightFunction.AnyLightFunction::new);
  public static final DeferredHolder<LightFunction, LightFunction.LightGreaterThanFunction> LIGHT_ABOVE_ZERO = LIGHT_FUNCTIONS.register("light_above_zero", () -> new LightFunction.LightGreaterThanFunction(0));
  public static final DeferredHolder<LightFunction, LightFunction.LightGreaterThanFunction> LIGHT_ABOVE_EIGHT = LIGHT_FUNCTIONS.register("light_above_eight", () -> new LightFunction.LightGreaterThanFunction(8));
  public static final DeferredHolder<LightFunction, LightFunction.LightLessThanFunction> LIGHT_BELOW_THIRTEEN = LIGHT_FUNCTIONS.register("light_below_thirteen", () -> new LightFunction.LightLessThanFunction(13));

  public static final DeferredHolder<CanHarvestFunction, SingleCropAgeCanHarvest> SINGLE_CROP_AGE = CAN_HARVEST_FUNCTIONS.register("single_crop_age", SingleCropAgeCanHarvest::new);
  public static final DeferredHolder<CanHarvestFunction, CanHarvestLowestBlock> CAN_HARVEST_LOWEST = CAN_HARVEST_FUNCTIONS.register("can_harvest_lowest_block", CanHarvestLowestBlock::new);

  public static final DeferredHolder<CanHarvestFunction, CanHarvestStemBlock> CAN_HARVEST_PUMKPIN = CAN_HARVEST_FUNCTIONS.register("can_harvest_pumpkin", () -> new CanHarvestStemBlock(Blocks.ATTACHED_PUMPKIN_STEM));

  public static final DeferredHolder<CanHarvestFunction, CanHarvestStemBlock> CAN_HARVEST_MELON = CAN_HARVEST_FUNCTIONS.register("can_harvest_melon", () -> new CanHarvestStemBlock(Blocks.ATTACHED_MELON_STEM));

  public static final DeferredHolder<GetDropsFunction, NoDrops> NO_DROPS = GET_DROPS_FUNCTIONS.register("no_drops", NoDrops::new);

  public static final DeferredHolder<GetDropsFunction, GenerateCropsDrop> GENERATE_DROPS = GET_DROPS_FUNCTIONS.register("generate_drops", GenerateCropsDrop::new);

  public static void register(IEventBus bus) {
    ENTITY_TESTS.register(bus);
    WORLD_TESTS.register(bus);
    REPLANT_FUNCTIONS.register(bus);
    CAN_GROW_FUNCTIONS.register(bus);
    LIGHT_FUNCTIONS.register(bus);
  }
}
