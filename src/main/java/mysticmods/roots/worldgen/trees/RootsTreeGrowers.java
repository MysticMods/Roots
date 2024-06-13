package mysticmods.roots.worldgen.trees;

import mysticmods.roots.init.ModFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class RootsTreeGrowers {
  public static final TreeGrower WILDWOOD_TREE = new TreeGrower("wildwood",   Optional.empty(),  Optional.of(ModFeatures.WILDWOOD_TREE.getKey()),  Optional.of(ModFeatures.WILDWOOD_TREE_BEES.getKey()));
}
