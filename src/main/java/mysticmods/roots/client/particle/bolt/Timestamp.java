package mysticmods.roots.client.particle.bolt;

public class Timestamp {

  private final long ticks;
  private final float partial;

  public Timestamp() {
    this(0, 0);
  }

  public Timestamp(long ticks, float partial) {
    this.ticks = ticks;
    this.partial = partial;
  }

  public Timestamp subtract(Timestamp other) {
    long newTicks = ticks - other.ticks;
    float newPartial = partial - other.partial;
    if (newPartial < 0) {
      newPartial += 1;
      newTicks -= 1;
    }
    return new Timestamp(newTicks, newPartial);
  }

  public float partial() {
    return partial;
  }

  public float value() {
    return ticks + partial;
  }

  public boolean isPassed(Timestamp prev, double duration) {
    long ticksPassed = ticks - prev.ticks;
    if (ticksPassed > duration) {
      return true;
    }
    duration -= ticksPassed;
    if (duration >= 1) {
      return false;
    }
    return (partial - prev.partial) >= duration;
  }
}
