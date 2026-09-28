package mysticmods.roots.api.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.Block;

public class BlockTracker {
  private final Object2IntMap<Block> countMap = new Object2IntOpenHashMap<>();
  private final int maxCount;

  private BlockTracker(int maxCount) {
    this.maxCount = maxCount;
  }

  public boolean count(Block block) {
    if (countMap.containsKey(block)) {
      int currentValue = countMap.getInt(block);
      if (currentValue >= maxCount) {
        return false;
      }
      countMap.put(block, currentValue + 1);
    } else {
      countMap.put(block, 1);
    }
    return true;
  }

  public static BlockTracker create(int maxCount) {
    return new BlockTracker(maxCount);
  }
}
