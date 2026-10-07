package mysticmods.roots.api.grove.power;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.grove.power.consumer.IGrovePowerConsumer;
import mysticmods.roots.api.grove.power.consumer.PowerRequest;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;

public class PowerTicket {
  private final TicketDefinition definition;
  private final long tick;
  private final Object2IntMap<PowerRequest> suppliedMap = new Object2IntOpenHashMap<>();
  private final Object2LongMap<PowerRequest> suppliedFromMap = new Object2LongOpenHashMap<>();

  private PowerTicket(TicketDefinition definition, long tick) {
    this.definition = definition;
    this.tick = tick;
    this.suppliedMap.defaultReturnValue(-1);
  }

  // Can be supplied from multiple grove stones
  public int supplyTick(IGroveInstance grove, int amount) {
    if (amount <= 0) {
      return amount;
    }
    for (PowerRequest req : definition.requests()) {
      if (grove.is(req.tag())) {
        int fullRequired = req.value();
        int amountSupplied = suppliedMap.getInt(req);
        int required = fullRequired - amountSupplied;
        if (required <= amount) {
          amount -= required;
          suppliedMap.mergeInt(req, required, Integer::sum);
          suppliedFromMap.put(req, grove.getGrovePosition().asLong());
        }
      }
    }
    return amount;
  }

  public void finalizeTick(IGrovePowerConsumer consumer, int generatedThisTick, int remainingPower) {

  }

  public boolean wasFullfilled() {
    for (PowerRequest req : definition.requests()) {
      int supplied = suppliedMap.getInt(req);
      if (supplied != -1 && supplied >= req.value()) {
        return false;
      }
    }
    return true;
  }

  public int getSupplied (Holder<Grove> grove) {
    return getSupplied(grove.value());
  }

  public int getSupplied(Grove grove) {
    int total = 0;

    for (PowerRequest consumer : definition.requests()) {
      if (grove.is(consumer.tag())) {
        total += Math.max(suppliedMap.getInt(consumer), 0);
      }
    }

    return total;
  }

  public int getSupplied(TagKey<Grove> tag) {
    int total = 0;

    for (PowerRequest consumer : definition.requests()) {
      if (consumer.tag().equals(tag)) {
        total += Math.max(suppliedMap.getInt(consumer), 0);
      }
    }
    return total;
  }

  // TODO: ???
  protected int getSupplied(PowerRequest consumer) {
    return suppliedMap.getInt(consumer);
  }

  public long getTick() {
    return tick;
  }

  public boolean isValid(long tick) {
    return getTick() == tick;
  }

  public record TicketDefinition(ImmutableList<PowerRequest> requests) {
    public PowerTicket create(long tick) {
      return new PowerTicket(this, tick);
    }
  }
}
