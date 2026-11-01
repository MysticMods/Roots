package mysticmods.roots.api.grove.power.consumer;

import net.minecraft.world.level.Level;

public interface IGrovePowerConsumer {
  default void maybeRebuildTicket(Level pLevel) {
    getTicketForTick(pLevel.getGameTime());
  }

  default PowerTicket getTicketForTick(long tick) {
    PowerTicket ticket = getTicket();

    if (ticket != null) {
      if (ticket.isFor(tick) && canBePowered()) {
        return ticket;
      }

      finalizeTicket(ticket);
    }

    if (!canBePowered()) {
      return null;
    }

    PowerTicket newTicket = createTicket(tick);

    setPowerTicket(newTicket);
    return newTicket;
  }

  PowerTicket getTicket();

  PowerTicket createTicket(long tick);

  void setPowerTicket(PowerTicket ticket);

  // Returns true if the consumer was fully powered the previous tick
  boolean wasPoweredLastTick();

  default boolean canBePowered() {
    return true;
  }

  void finalizeTicket(PowerTicket ticket);
}
