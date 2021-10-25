package epicsquid.mysticallib.util;

import javax.annotation.Nonnull;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtil {

  public static double nclamp(double d, double n) {
    return d - Math.floor(d / n) * n;
  }

  @Nonnull
  public static Vec3d rotateX(@Nonnull Vec3d v, float angle) {
    return new Vec3d(v.x * MathHelper.cos(angle) - v.y * MathHelper.sin(angle), v.x * MathHelper.sin(angle) + v.y * MathHelper.cos(angle), v.z);
  }

  @Nonnull
  public static Vec3d rotateZ(@Nonnull Vec3d v, float angle) {
    return new Vec3d(v.x, v.y * MathHelper.cos(angle) - v.z * MathHelper.sin(angle), v.y * MathHelper.sin(angle) + v.z * MathHelper.cos(angle));
  }

  @Nonnull
  public static Vec3d rotateY(@Nonnull Vec3d v, float angle) {
    return new Vec3d(v.z * MathHelper.cos(angle) - v.x * MathHelper.sin(angle), v.y, v.z * MathHelper.sin(angle) + v.x * MathHelper.cos(angle));
  }
}
