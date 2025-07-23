package mysticmods.roots.api.grove;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;

public class PowerTicket {
  private final TicketDefinition definition;
  private final long tick;
  private final Object2IntMap<GrovePower.Consumer> suppliedMap = new Object2IntOpenHashMap<>();
  private final List<BlockPos> lastPoweredFrom = new ArrayList<>();

  private PowerTicket(TicketDefinition definition, long tick) {
    this.definition = definition;
    this.tick = tick;
  }

  public int supply(IGroveInstance grove, int amount) {
    if (amount <= 0) {
      return amount;
    }
    for (GrovePower.Consumer req : definition.requests()) {
      if (grove.is(req.tag())) {
        int fullRequired = req.value();
        int amountSupplied = suppliedMap.getInt(req);
        int required = fullRequired - amountSupplied;
        if (required <= amount) {
          amount -= required;
          suppliedMap.mergeInt(req, required, Integer::sum);
          lastPoweredFrom.add(grove.getGrovePosition());
        }
      }
    }
    return amount;
  }

  public List<BlockPos> getPoweredFrom() {
    return lastPoweredFrom;
  }

  public boolean wasFullfilled() {
    for (GrovePower.Consumer req : definition.requests()) {
      if (!suppliedMap.containsKey(req)) {
        return false;
      }
      if (suppliedMap.getInt(req) != req.value()) {
        return false;
      }
    }
    return true;
  }

  public int getSupplied(TagKey<Grove> tag) {
    for (GrovePower.Consumer consumer : definition.requests()) {
      if (consumer.tag().equals(tag)) {
        return suppliedMap.getInt(consumer);
      }
    }
    return 0;
  }

  public int getSupplied(GrovePower.Consumer consumer) {
    return suppliedMap.getInt(consumer);
  }

  public long getTick() {
    return tick;
  }

  public boolean isValid (long tick) {
    return this.tick == tick;
  }

  public record TicketDefinition(ImmutableList<GrovePower.Consumer> requests) {
    public PowerTicket create(long tick) {
      return new PowerTicket(this, tick);
    }
  }
}
