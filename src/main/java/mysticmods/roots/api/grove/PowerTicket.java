package mysticmods.roots.api.grove;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class PowerTicket {
  private final TicketDefinition definition;
  private final long tick;
  private final Object2IntMap<GrovePower.Consumer> suppliedMap = new Object2IntOpenHashMap<>();

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
        }
      }
    }
    return amount;
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
