package epicsquid.mysticallib.util;

import net.minecraft.util.math.Vec3d;

import java.util.Iterator;

public class VecUtil {
  public static Iterable<Vec3d> pointsBetween (Vec3d start, Vec3d stop) {
    return pointsBetween(start, stop, -1);
  }

  public static Iterable<Vec3d> pointsBetween (Vec3d start, Vec3d stop, int maxLength) {
    return new VecIterable(start, stop, maxLength);
  }

  public static Iterable<Vec3d> pointsFrom (Vec3d start, Vec3d vec) {
    return pointsBetween(start, start.add(vec), (int) vec.length());
  }

  protected static class VecIterable implements Iterable<Vec3d> {
    private Vec3d start;
    private Vec3d size;

    public VecIterable(Vec3d start, Vec3d stop, int maxLength) {
      this.start = start;
      Vec3d line = stop.subtract(start);
      if (maxLength != -1) {
        this.size = line.scale(line.length() / maxLength);
      } else {
        this.size = line;
      }
    }

    @Override
    public Iterator<Vec3d> iterator() {
      return new VecIterator();
    }

    protected class VecIterator implements Iterator<Vec3d> {
      private double progress = 0;

      @Override
      public boolean hasNext() {
        return progress < 1;
      }

      @Override
      public Vec3d next() {
        progress += 1.0 / size.length();
        return start.add(size.scale(progress));
      }
    }
  }
}
