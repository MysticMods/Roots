package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.api.world.AlwaysTrueWorldTest;
import mysticmods.roots.api.world.BlockMatchWorldTest;
import mysticmods.roots.api.world.TagMatchWorldTest;
import mysticmods.roots.api.world.WorldTestType;
import mysticmods.roots.test.entity.EntityTagTest;
import mysticmods.roots.test.entity.EntityTypeTest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTests {
  private static final DeferredRegister<EntityTestType<?>> ENTITY_TESTS = DeferredRegister.create(RootsRegistries.Keys.ENTITY_TEST_TYPES, RootsAPI.MODID);
  private static final DeferredRegister<WorldTestType<?>> WORLD_TESTS = DeferredRegister.create(RootsRegistries.Keys.WORLD_TEST_TYPES, RootsAPI.MODID);

  public static final DeferredHolder<EntityTestType<?>, EntityTestType<EntityTagTest>> ENTITY_TAG_TEST = ENTITY_TESTS.register("entity_tag_test", EntityTagTest.Type::new);
  public static final DeferredHolder<EntityTestType<?>, EntityTestType<EntityTypeTest>> ENTITY_TYPE_TEST = ENTITY_TESTS.register("entity_type_test", EntityTypeTest.Type::new);

  public static final DeferredHolder<WorldTestType<?>, WorldTestType<AlwaysTrueWorldTest>> ALWAYS_TRUE_TEST = WORLD_TESTS.register(AlwaysTrueWorldTest.ALWAYS_TRUE_TEST_KEY.location().getPath(), AlwaysTrueWorldTest.Type::new);
  public static final DeferredHolder<WorldTestType<?>, WorldTestType<BlockMatchWorldTest>> BLOCK_MATCH_TEST = WORLD_TESTS.register(BlockMatchWorldTest.BLOCK_MATCH_TEST_KEY.location().getPath(), BlockMatchWorldTest.Type::new);
  public static final DeferredHolder<WorldTestType<?>, WorldTestType<TagMatchWorldTest>> TAG_MATCH_TEST = WORLD_TESTS.register(TagMatchWorldTest.TAG_MATCH_TEST_KEY.location().getPath(), TagMatchWorldTest.Type::new);

  public static void register(IEventBus bus) {
    ENTITY_TESTS.register(bus);
  }
}
