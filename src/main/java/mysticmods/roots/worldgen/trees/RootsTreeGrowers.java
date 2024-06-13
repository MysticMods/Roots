package mysticmods.roots.worldgen.trees;

import mysticmods.roots.init.ModFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class RootsTreeGrowers {
  public static final TreeGrower WILDWOOD_TREE = new TreeGrower("wildwood",
  @Nullable
  @Override
  protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pLargeHive) {
    return pLargeHive ? ModFeatures.WILDWOOD_TREE_BEES.getHolder().get() : ModFeatures.WILDWOOD_TREE.getHolder().get();
  }
}
