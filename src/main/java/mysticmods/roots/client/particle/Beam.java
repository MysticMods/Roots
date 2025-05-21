package mysticmods.roots.client.particle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public interface Beam {
  int MAX_DISTANCE = 24 * 24;

  Vec3 getStart ();
  Vec3 getStop ();
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
    public Vec3 getStart() {
      return start.getEyePosition().subtract(0, 0.5, 0);
    }

    @Override
    public Vec3 getStop() {
      return stop.getEyePosition().subtract(0, 0.5, 0);
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
    public Vec3 getStart() {
      return start;
    }

    @Override
    public Vec3 getStop() {
      return stop;
    }

    @Override
    public BeamColor getStyle() {
      return color;
    }
  }

  interface BeamColor {
    float getRed ();
    float getGreen ();
    float getBlue ();
    float getAlpha ();
  }

  record BeamAlpha (float alpha) implements BeamColor {

    @Override
    public float getRed() {
      return 1;
    }

    @Override
    public float getGreen() {
      return 1;
    }

    @Override
    public float getBlue() {
      return 1;
    }

    @Override
    public float getAlpha() {
      return alpha;
    }
  }

  record BeamColorVec4 (Vector4f colours) implements BeamColor {
    @Override
    public float getRed() {
      return colours.x();
    }

    @Override
    public float getGreen() {
      return colours.y();
    }

    @Override
    public float getBlue() {
      return colours.z();
    }

    @Override
    public float getAlpha() {
      return colours.w();
    }
  }
}
