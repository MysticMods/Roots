package mysticmods.roots.client.particle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public interface Beam {
  int MAX_DISTANCE = 24 * 24;

  default Vec3 getStart () {
    return getStart(0f);
  }
  default Vec3 getStop () {
    return getStop(0f);
  }
  Vec3 getStart (float partialTicks);
  Vec3 getStop (float partialTicks);
  int getAge ();
  int getMaxAge ();

  void tick ();

  void remove ();
  boolean isRemoved ();

  BeamColor getStyle ();

  abstract class BeamBase implements Beam {
    protected int age;
    protected final int maxAge;
    protected boolean removed;

    public BeamBase (int maxAge) {
      this.maxAge = maxAge;
      this.age = 0;
      this.removed = false;
    }

    @Override
    public void remove() {
      this.removed = true;
    }

    @Override
    public int getAge() {
      return age;
    }

    @Override
    public int getMaxAge() {
      return maxAge;
    }

    @Override
    public void tick() {
      if (this.age++ >= maxAge) {
        this.remove();
      }
      if (getStart().subtract(getStop()).lengthSqr() > MAX_DISTANCE) {
        this.remove();
      }
    }

    @Override
    public boolean isRemoved() {
      return removed;
    }
  }

  class EntityBeam extends BeamBase {
    private final Entity start;
    private final Entity stop;
    private final BeamColor style;

    public EntityBeam(BeamColor style, Entity start, Entity stop, int maxAge) {
      super(maxAge);
      this.start = start;
      this.stop = stop;
      this.style = style;
    }

    @Override
    public Vec3 getStart(float partialTicks) {
      return start.getEyePosition(partialTicks).subtract(0, 0.5, 0);
    }

    @Override
    public Vec3 getStop(float partialTicks) {
      return stop.getEyePosition(partialTicks).subtract(0, 0.5, 0);
    }

    @Override
    public void tick() {
      if (!isRemoved()) {
        if (start == null || stop == null || start.isRemoved() || stop.isRemoved() || !start.isAlive() || !stop.isAlive()) {
          this.remove();
        }

        super.tick();
      }
    }

    @Override
    public BeamColor getStyle() {
      return style;
    }
  }

  class StaticBeam extends BeamBase {
    private final Vec3 start, stop;
    private final BeamColor color;

    public StaticBeam(BeamColor color, int maxAge, Vec3 start, Vec3 stop) {
      super(maxAge);
      this.start = start;
      this.stop = stop;
      this.color = color;
    }

    @Override
    public Vec3 getStart(float partialTicks) {
      return start;
    }

    @Override
    public Vec3 getStop(float partialTicks) {
      return stop;
    }

    @Override
    public BeamColor getStyle() {
      return color;
    }
  }

  interface BeamColor {
    int getRed ();
    int getGreen ();
    int getBlue ();
    int getAlpha ();
  }

  record BeamAlpha (int alpha) implements BeamColor {

    @Override
    public int getRed() {
      return 255;
    }

    @Override
    public int getGreen() {
      return 255;
    }

    @Override
    public int getBlue() {
      return 255;
    }

    @Override
    public int getAlpha() {
      return alpha;
    }
  }

  record BeamColorVec4 (int r, int g, int b, int a) implements BeamColor {
    @Override
    public int getRed() {
      return r();
    }

    @Override
    public int getGreen() {
      return g();
    }

    @Override
    public int getBlue() {
      return b();
    }

    @Override
    public int getAlpha() {
      return a();
    }
  }
}
