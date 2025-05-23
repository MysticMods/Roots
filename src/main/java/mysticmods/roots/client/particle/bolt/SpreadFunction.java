package mysticmods.roots.client.particle.bolt;

/**
 * A SpreadFunction defines how far bolt segments can stray from the straight-line vector, based on parallel 'progress' from start to finish.
 *
 * @author aidancbrady
 */
public interface SpreadFunction {

  /**
   * A steady linear increase in perpendicular noise.
   */
  SpreadFunction LINEAR_ASCENT = progress -> progress;
  /**
   * A steady linear increase in perpendicular noise, followed by a steady decrease after the halfway point.
   */
  SpreadFunction LINEAR_ASCENT_DESCENT = progress -> (progress - Math.max(0, 2 * progress - 1)) / 0.5F;
  /**
   * Represents a unit sine wave from 0 to PI, scaled by progress.
   */
  SpreadFunction SINE = progress -> (float) Math.sin(Math.PI * progress);

  float getMaxSpread(float progress);
}
