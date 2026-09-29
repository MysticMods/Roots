package mysticmods.roots.api.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public abstract class Tracker<T, V> {
  private final Object2IntMap<T> countMap = new Object2IntOpenHashMap<>();
  private final int maxCount;

  private Tracker(int maxCount) {
    this.maxCount = maxCount;
  }

  public boolean countExact(T block) {
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

  public abstract boolean count(V convertable);

  public static class BlockTracker extends Tracker<Block, Block> {
    private BlockTracker(int maxCount) {
      super(maxCount);
    }

    @Override
    public boolean count(Block convertable) {
      return super.countExact(convertable);
    }
  }

  public static class EntityTracker extends Tracker<EntityType<?>, Entity> {
    private EntityTracker(int maxCount) {
      super(maxCount);
    }

    @Override
    public boolean count(Entity convertable) {
      return countExact(convertable.getType());
    }
  }

  public static BlockTracker createBlockTracker(int maxCount) {
    return new BlockTracker(maxCount);
  }

  public static EntityTracker createEntityTracker(int maxCount) {
    return new EntityTracker(maxCount);
  }
}
