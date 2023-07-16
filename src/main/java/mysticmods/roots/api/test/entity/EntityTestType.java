package mysticmods.roots.api.test.entity;

import com.mojang.serialization.Codec;
import net.minecraftforge.registries.RegistryObject;

public interface EntityTestType<P extends EntityTest> {
  Codec<P> codec();
}
