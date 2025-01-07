package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.test.entity.EntityTagTest;
import mysticmods.roots.test.entity.EntityTypeTest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTests {
  private static final DeferredRegister<EntityTestType<?>> REGISTER = DeferredRegister.create(RootsRegistries.Keys.ENTITY_TEST_TYPES, RootsAPI.MODID);

  public static final DeferredHolder<EntityTestType<?>, EntityTestType<EntityTagTest>> ENTITY_TAG_TEST = REGISTER.register("entity_tag_test", () -> () -> EntityTagTest.CODEC);
  public static final DeferredHolder<EntityTestType<?>, EntityTestType<EntityTypeTest>> ENTITY_TYPE_TEST = REGISTER.register("entity_type_test", () -> () -> EntityTypeTest.CODEC);

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
