package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.test.entity.EntityTagTest;
import mysticmods.roots.test.entity.EntityTypeTest;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModTests {
  public static final RegistryEntry<EntityTestType<EntityTagTest>> ENTITY_TAG_TEST = REGISTRATE.simple("entity_tag_test", RootsAPI.ENTITY_TEST_TYPE_REGISTRY, () -> () -> EntityTagTest.CODEC);
  public static final RegistryEntry<EntityTestType<EntityTypeTest>> ENTITY_TYPE_TEST = REGISTRATE.simple("entity_type_test", RootsAPI.ENTITY_TEST_TYPE_REGISTRY, () -> () -> EntityTypeTest.CODEC);

  public static void load () {
  }
}
