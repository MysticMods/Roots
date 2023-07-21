package mysticmods.roots.api.test.entity;

import com.mojang.serialization.Codec;

public interface EntityTestType<P extends EntityTest> {
  Codec<P> codec();
}
