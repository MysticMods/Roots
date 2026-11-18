package mysticmods.roots.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.HugeRedMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public class HugeBafflecapMushroomFeature extends HugeRedMushroomFeature {
  public HugeBafflecapMushroomFeature(Codec<HugeMushroomFeatureConfiguration> config) {
    super(config);
  }

  protected int getTreeHeight(RandomSource random) {
    return 5 + random.nextInt(3);
  }
}
