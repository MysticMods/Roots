package mysticmods.roots.api.grove;

public interface IGroveConsumer {
  boolean isPowered ();
  void markPowered (IGroveInstance grove, boolean powered);
  int getRequiredPower (IGroveInstance grove);
}
