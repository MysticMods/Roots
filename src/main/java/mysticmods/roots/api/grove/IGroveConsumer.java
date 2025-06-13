package mysticmods.roots.api.grove;

// TODO: More complex interfaces for multi-faceted power requirements or multiple tick consumption
public interface IGroveConsumer {
  // Returns true if the consumer has been fully powered.
  boolean isPowered ();

  // Informs the consumer that the required amount of power for the grove instance has either been met or not met.
  void markPowered (IGroveInstance grove, boolean powered);

  // Returns the amount of power this consumer requires this tick. This allows for dynamic power requirements of different types of grove power, or just any generic type of grove power.
  int getRequiredPower (IGroveInstance grove);
}
