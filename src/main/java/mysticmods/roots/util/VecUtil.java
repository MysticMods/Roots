package mysticmods.roots.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class VecUtil {
  public static Vec3 bezier(Vec3 start, Vec3 end, Vec3 control1, Vec3 control2, double t) {
    t = Mth.clamp(t, 0.0, 1.0);
    double u = 1.0 - t;

    Vec3 p0 = start.scale(u * u * u);
    Vec3 p1 = control1.scale(3 * u * u * t);
    Vec3 p2 = control2.scale(3 * u * t * t);
    Vec3 p3 = end.scale(t * t * t);

    return p0.add(p1).add(p2).add(p3);
  }

  public static Vec3 midpoint(Vec3 start, Vec3 stop) {
    return new Vec3((start.x + stop.x) / 2.0, (start.y + stop.y) / 2.0, (start.z + stop.z) / 2.0);
  }


}
