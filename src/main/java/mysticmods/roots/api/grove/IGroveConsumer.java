package mysticmods.roots.api.grove;

public interface IGroveConsumer {

  PowerTicket getTicketForTick (long tick);

  // Returns true if the consumer was fully powered the previous tick
  boolean wasPoweredLastTick();
}
