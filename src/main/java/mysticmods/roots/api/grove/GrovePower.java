package mysticmods.roots.api.grove;

public interface GrovePower {
  int getMaxPower ();
  int getReservedPower ();
  boolean reservePower (int amount);
}
