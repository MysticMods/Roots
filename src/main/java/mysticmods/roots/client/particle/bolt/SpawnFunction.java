package mysticmods.roots.client.particle.bolt;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/**
 * A bolt's spawn function defines its spawn behavior (handled by the renderer). A spawn function generates a lower and upper bound on a spawn delay (via
 * getSpawnDelayBounds()), for which an intermediate value is chosen randomly from a uniform distribution (getSpawnDelay()). Spawn functions can also be defined as
 * 'consecutive,' in which cases the Bolt Renderer will always begin rendering a new bolt instance when one expires.
 *
 * @author aidancbrady
 */
public interface SpawnFunction {

  /**
   * Allow for bolts to be spawned each update call without any delay.
   */
  SpawnFunction NO_DELAY = new SpawnDelayBounds(0F, 0F);
  /**
   * Will re-spawn a bolt each time one expires.
   */
  SpawnFunction CONSECUTIVE = new SpawnFunction() {
    private final SpawnDelayBounds BOUNDS = new SpawnDelayBounds(0F, 0F);

    @Override
    public SpawnDelayBounds getSpawnDelayBounds(RandomSource rand) {
      return BOUNDS;
    }

    @Override
    public boolean isConsecutive() {
      return true;
    }
  };

  /**
   * Spawn bolts with a specified constant delay.
   */
  static SpawnFunction delay(float delay) {
    return new SpawnDelayBounds(delay, delay);
  }

  /**
   * Spawns bolts with a specified delay and specified noise value, which will be randomly applied at either end of the delay bounds.
   */
  static SpawnFunction noise(float delay, float noise) {
    return new SpawnDelayBounds(delay - noise, delay + noise);
  }

  SpawnDelayBounds getSpawnDelayBounds(RandomSource rand);

  default float getSpawnDelay(RandomSource rand) {
    SpawnDelayBounds bounds = getSpawnDelayBounds(rand);
    return Mth.lerp(rand.nextFloat(), bounds.start(), bounds.end());
  }

  default boolean isConsecutive() {
    return false;
  }

  record SpawnDelayBounds(float start, float end) implements SpawnFunction {

    @Override
    public SpawnDelayBounds getSpawnDelayBounds(RandomSource rand) {
      return this;
    }
  }
}
