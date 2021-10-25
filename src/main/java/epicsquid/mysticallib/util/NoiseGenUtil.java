package epicsquid.mysticallib.util;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.util.math.MathHelper;

public class NoiseGenUtil {

  private static Random random = new Random();

  public static double getNoise(long seed, int x, int y) {
    random.setSeed(simple_hash(new int[] { (int) seed, (int) (seed << 32), (int) Math.signum(y) * 512 + 512, (int) Math.signum(x) * 512 + 512, x, y }, 5));
    return random.nextDouble();
  }

  @Nonnull
  public static Random getRandom(int... args) {
    return new Random((long) simple_hash(args, args.length));
  }

  public static long getSeed(int seed, int x, int y) {
    return simple_hash(new int[] { seed, (int) Math.signum(y) * 512 + 512, (int) Math.signum(x) * 512 + 512, x, y }, 5);
  }

  public static int simple_hash(int[] is, int count) {
    int i;
    int hash = 80238287;

    for (i = 0; i < count; i++) {
      hash = (hash << 4) ^ (hash >> 28) ^ (is[i] * 5449 % 130651);
    }

    return hash % 75327403;
  }

  public static double get2DNoise(long seed, int x, int z) {
    return Math.pow(
        (80.0f * NoiseGenUtil.getOctave(seed, x, z, 112) + 20.0f * NoiseGenUtil.getOctave(seed, x, z, 68) + 6.0f * NoiseGenUtil.getOctave(seed, x, z, 34)
            + 4.0f * NoiseGenUtil.getOctave(seed, x, z, 21) + 2.0f * NoiseGenUtil.getOctave(seed, x, z, 11) + 1.0f * NoiseGenUtil.getOctave(seed, x, z, 4))
            / 93.0f, 1.6f);
  }

  public static double fastSin(double x) {
    if (x < -3.14159265) {
      x += 6.28318531;
    } else {
      if (x > 3.14159265) {
        x -= 6.28318531;
      }
    }

    if (x < 0) {
      return (1.27323954 * x + .405284735 * x * x);
    } else {
      return (1.27323954 * x - 0.405284735 * x * x);
    }
  }

  public static double fastCos(double x) {
    if (x < -3.14159265) {
      x += 6.28318531;
    } else {
      if (x > 3.14159265) {
        x -= 6.28318531;
      }
    }
    x += 1.57079632;
    if (x > 3.14159265) {
      x -= 6.28318531;
    }

    if (x < 0) {
      return (1.27323954 * x + 0.405284735 * x * x);
    } else {
      return (1.27323954 * x - 0.405284735 * x * x);
    }
  }

  public static double interpolate(double s, double e, double t) {
    double t2 = (1.0f - fastCos(t * 3.14159265358979323f)) / 2.0f;
    return (s * (1.0f - t2) + (e) * t2);
  }

  public static double interpolate(double s, double e, double t, double phase, double mult) {
    double t2 = (1.0f - MathHelper.cos((float) mult * ((float) t * 3.14159265358979323f + (float) phase)) / 2.0f);
    double coeff = (0.5f - Math.abs(0.5f - t)) / 0.5f;
    double t3 = t * (1.0f - coeff) + t2 * coeff;
    return (s * (1.0f - t3) + (e) * t3);
  }

  public static double lerp(double s, double e, double t) {
    return (s * (1.0f - t) + (e) * t);
  }

  public static double bilinear(double ul, double ur, double dr, double dl, double t1, double t2) {
    return interpolate(interpolate(ul, ur, t1), interpolate(dl, dr, t1), t2);
  }

  public static double getOctave(long seed, double x, double y, double dimen) {
    return bilinear(getNoise(seed, (int) (Math.floor(x / dimen) * dimen), (int) (Math.floor(y / dimen) * dimen)),
        getNoise(seed, (int) (Math.floor(x / dimen) * dimen + dimen), (int) (Math.floor(y / dimen) * dimen)),
        getNoise(seed, (int) (Math.floor(x / dimen) * dimen + dimen), (int) (Math.floor(y / dimen) * dimen + dimen)),
        getNoise(seed, (int) (Math.floor(x / dimen) * dimen), (int) (Math.floor(y / dimen) * dimen + dimen)),
        Math.abs(((x) - Math.floor((x / dimen)) * dimen) / (dimen)), Math.abs(((y) - Math.floor((y / dimen)) * dimen) / (dimen)));
  }
}
