package mysticmods.roots.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;

public class PositionCache {
  private final BlockPos position;
  private final AABB aabb;
  private final BoundingBox boundingBox;
  private final List<BlockPos> positions;
  private final Map<BiPredicate<Level, BlockPos>, RitualCacheEntry> cache = new HashMap<>();

  public PositionCache(BlockPos position, BoundingBox boundingBox, List<BlockPos> positions) {
    this.boundingBox = boundingBox;
    this.positions = positions;
    this.position = position;
    this.aabb = AABB.of(boundingBox);
  }

  public PositionCache(BlockPos position, BoundingBox boundingBox) {
    this(position, boundingBox, new ArrayList<>(BlockPos.betweenClosedStream(boundingBox).map(BlockPos::immutable)
        .toList()));
  }

  public BlockPos getPosition() {
    return position;
  }

  public boolean isInside(Vec3i pos) {
    return getBoundingBox().isInside(pos);
  }

  public BoundingBox getBoundingBox() {
    return boundingBox;
  }

  public AABB getAABB() {
    return aabb;
  }

  public List<BlockPos> getPositions() {
    return positions;
  }

  @Nullable
  public BlockPos random(RandomSource random) {
    if (positions.isEmpty()) {
      return null;
    }
    return positions.get(random.nextInt(positions.size()));
  }

  @Nullable
  public BlockPos random(BiPredicate<Level, BlockPos> predicate, RandomSource random) {
    RitualCacheEntry entry = cache.get(predicate);
    if (entry == null || entry.matches.isEmpty()) {
      return null;
    }
    return positions.get(entry.matches.getInt(random.nextInt(entry.matches.size())));
  }

  public Iterable<BlockPos> iterate(BiPredicate<Level, BlockPos> predicate, RandomSource random) {
    return () -> {
      RitualCacheEntry entry = cache.get(predicate);
      if (entry == null || entry.matches.isEmpty()) {
        return Collections.emptyIterator();
      }

      return entry.iterator(random);
    };
  }

  public Iterable<BlockPos> iterate(RandomSource random) {
    return () -> new It(random);
  }

  public void initCache(Level level, List<BiPredicate<Level, BlockPos>> predicates) {
    cache.clear();
    Map<BiPredicate<Level, BlockPos>, IntArrayList> matches = new HashMap<>();
    for (BiPredicate<Level, BlockPos> predicate : predicates) {
      matches.put(predicate, new IntArrayList());
    }
    for (int i = 0; i < positions.size(); i++) {
      for (BiPredicate<Level, BlockPos> predicate : predicates) {
        if (predicate.test(level, positions.get(i))) {
          matches.get(predicate).add(i);
        }
      }
    }

    for (BiPredicate<Level, BlockPos> predicate : predicates) {
      cache.put(predicate, new RitualCacheEntry(matches.get(predicate)));
    }
  }

  protected class RitualCacheEntry {
    private final IntArrayList matches;

    public RitualCacheEntry(IntArrayList matches) {
      this.matches = matches;
    }

    public It iterator(RandomSource random) {
      return new It(random);
    }

    protected class It implements java.util.Iterator<BlockPos> {

      private final RandomSource random;
      private final IntSet iteratorConsumed = new IntOpenHashSet();
      private int index;

      protected It(RandomSource random) {
        this.random = random;
      }

      @Override
      public boolean hasNext() {
        return iteratorConsumed.size() < matches.size();
      }

      @Override
      public BlockPos next() {
        if (matches.isEmpty()) {
          throw new NoSuchElementException();
        }

        while (index == -1 || iteratorConsumed.contains(index)) {
          index = random.nextInt(matches.size());
        }

        iteratorConsumed.add(index);
        if (index < 0 || index >= matches.size()) {
          throw new NoSuchElementException();
        }
        return positions.get(matches.getInt(index));
      }
    }
  }

  protected class It implements Iterator<BlockPos> {
    private final RandomSource random;
    private final IntSet iteratorConsumed = new IntOpenHashSet();
    private int index;

    protected It(RandomSource random) {
      this.random = random;
    }

    @Override
    public boolean hasNext() {
      return iteratorConsumed.size() < positions.size();
    }

    @Override
    public BlockPos next() {
      if (positions.isEmpty()) {
        throw new NoSuchElementException();
      }

      while (index == -1 || iteratorConsumed.contains(index)) {
        index = random.nextInt(positions.size());
      }

      iteratorConsumed.add(index);
      if (index < 0 || index >= positions.size()) {
        throw new NoSuchElementException();
      }
      return positions.get(index);
    }
  }
}
