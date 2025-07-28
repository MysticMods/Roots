package mysticmods.roots.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

public class VecUtil {
  public static Vec3 bezier(Vec3 startPos, Vec3 control1, Vec3 control2, Vec3 endPos, double t) {
    t = Mth.clamp(t, 0.0, 1.0);
    double u = 1.0 - t;

    Vec3 p0 = startPos.scale(u * u * u);
    Vec3 p1 = control1.scale(3 * u * u * t);
    Vec3 p2 = control2.scale(3 * u * t * t);
    Vec3 p3 = endPos.scale(t * t * t);

    return p0.add(p1).add(p2).add(p3);
  }

  public static Vec3 midpoint(Vec3 start, Vec3 stop) {
    return new Vec3((start.x + stop.x) / 2.0, (start.y + stop.y) / 2.0, (start.z + stop.z) / 2.0);
  }

  public static Vector2f bezier(Vector2f startPos, Vector2f control1, Vector2f control2, Vector2f endPos, double t) {
    t = Mth.clamp(t, 0.0, 1.0);
    double u = 1.0 - t;

    Vector2f p0 = new Vector2f((float) (startPos.x * u * u * u), (float) (startPos.y * u * u * u));
    Vector2f p1 = new Vector2f((float) (control1.x * 3 * u * u * t), (float) (control1.y * 3 * u * u * t));
    Vector2f p2 = new Vector2f((float) (control2.x * 3 * u * t * t), (float) (control2.y * 3 * u * t * t));
    Vector2f p3 = new Vector2f((float) (endPos.x * t * t * t), (float) (endPos.y * t * t * t));
    return new Vector2f(p0.x + p1.x + p2.x + p3.x, p0.y + p1.y + p2.y + p3.y);
  }

  public static Vector2f midpoint(Vector2f start, Vector2f stop) {
    return new Vector2f((start.x + stop.x) / 2.0f, (start.y + stop.y) / 2.0f);
  }


}
