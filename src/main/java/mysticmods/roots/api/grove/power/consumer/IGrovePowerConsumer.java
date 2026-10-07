package mysticmods.roots.api.grove.power.consumer;

import mysticmods.roots.api.grove.power.PowerTicket;
import org.jetbrains.annotations.Nullable;

public interface IGrovePowerConsumer {
  @Nullable
  PowerTicket getTicketForTick(long tick);

  // Returns true if the consumer was fully powered the previous tick
  boolean wasPoweredLastTick();
}
