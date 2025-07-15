package mysticmods.roots.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class SpatialMap {
  private final Long2ObjectOpenHashMap<ObjectOpenHashSet<BlockPos>> index = new Long2ObjectOpenHashMap<>();

  public void add(BlockPos position) {
    long key = SectionPos.asLong(position);
    ObjectOpenHashSet<BlockPos> set = index.get(key);
    if (set == null) {
      set = new ObjectOpenHashSet<>();
      index.put(key, set);
    }
    set.add(position);
  }

  public boolean remove(BlockPos position) {
    long key = SectionPos.asLong(position);
    ObjectOpenHashSet<BlockPos> set = index.get(key);
    if (set != null) {
      boolean removed = set.remove(position);
      if (set.isEmpty()) {
        index.remove(key);
      }
      return removed;
    }
    return false;
  }

  public List<BlockPos> query(BlockPos start, double radiusSq) {
    int sectionRadius = Mth.ceil(Math.sqrt(radiusSq) / 16.0);
    int cx = SectionPos.blockToSectionCoord(start.getX());
    int cy = SectionPos.blockToSectionCoord(start.getY());
    int cz = SectionPos.blockToSectionCoord(start.getZ());

    List<BlockPos> result = new ArrayList<>();

    for (int dx = -sectionRadius; dx <= sectionRadius; dx++) {
      for (int dy = -sectionRadius; dy <= sectionRadius; dy++) {
        for (int dz = -sectionRadius; dz <= sectionRadius; dz++) {
          long key = SectionPos.asLong(cx + dx, cy + dy, cz + dz);
          ObjectOpenHashSet<BlockPos> set = index.get(key);
          if (set != null) {
            for (BlockPos pos : set) {
              if (start.distSqr(pos) <= radiusSq) {
                result.add(pos);
              }
            }
          }
        }
      }
    }

    return result;
  }
}
