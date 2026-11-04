package mysticmods.roots.api.grove.power.consumer;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;

import java.util.HashSet;
import java.util.Set;

public class PowerTicket {
  private final PowerCollection definition;
  private final long tick;
  private final Object2IntMap<PowerRequest> suppliedThisTick = new Object2IntOpenHashMap<>();
  private final Object2ObjectMap<PowerRequest, Set<BlockPos>> suppliedFromMap = new Object2ObjectOpenHashMap<>();

  protected PowerTicket(PowerCollection definition, long tick) {
    this.definition = definition;
    this.tick = tick;
    this.suppliedThisTick.defaultReturnValue(0);
  }

  public int supplyTick(IGroveInstance grove, int incomingAmount) {
    int used = 0;
    for (PowerRequest req : definition) {
      if (grove.is(req.tag()) && suppliedThisTick.getInt(req) == 0) {
        int required = req.rate(grove);
        if (required > 0 && required <= (incomingAmount - used)) {
          used += required;
          suppliedThisTick.mergeInt(req, required, Integer::sum);
          suppliedFromMap.computeIfAbsent(req, k -> new HashSet<>()).add(grove.getGrovePosition());
        }
      }
    }
    return used;
  }

  public boolean isFor(long tick) {
    return tick == this.tick;
  }

  public boolean wasFulfilled() {
    if (suppliedThisTick.isEmpty()) {
      return false;
    }

    for (PowerRequest req : definition) {
      int supplied = suppliedThisTick.getInt(req);
      if (supplied != req.total()) {
        return false;
      }
    }
    return true;
  }

  public int getSupplied(Holder<Grove> grove) {
    return getSupplied(grove.value());
  }

  public int getSupplied(Grove grove) {
    int total = 0;

    for (PowerRequest consumer : definition) {
      if (grove.is(consumer.tag())) {
        total += Math.max(suppliedThisTick.getInt(consumer), 0);
      }
    }

    return total;
  }

  public int getSupplied(TagKey<Grove> tag) {
    int total = 0;

    for (PowerRequest consumer : definition) {
      if (consumer.tag().equals(tag)) {
        total += Math.max(suppliedThisTick.getInt(consumer), 0);
      }
    }
    return total;
  }

  public int getSupplied(PowerRequest consumer) {
    return suppliedThisTick.getInt(consumer);
  }

  public void updateRecord (PowerRecord record) {
    if (!record.isFor(definition)) {
      throw new IllegalStateException("Definition for power ticket ('" + this +"') is not valid for record '" + record + "'");
    }

    for (PowerRequest req : definition) {
      record.supply(req, suppliedThisTick.getInt(req));
    }
  }
}
