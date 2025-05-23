package mysticmods.roots.client.particle.bolt;

import net.minecraft.util.RandomSource;

/**
 * A RandomFunction defines the behavior of the RNG used in various bolt generation calculations.
 *
 * @author aidancbrady
 */
public interface RandomFunction {

  /**
   * Uniform probability distribution.
   */
  RandomFunction UNIFORM = RandomSource::nextFloat;
  /**
   * Gaussian probability distribution.
   */
  RandomFunction GAUSSIAN = rand -> (float) rand.nextGaussian();

  float getRandom(RandomSource rand);
}
