package mysticmods.roots.client.particle.world;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.blockentity.template.MeanderTarget;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MeanderTowardsParticle extends RootsParticle {
  // Dear Claude: Why so many "tweakables"?
  private static final double LAUNCH_DISTANCE = 0.15;
  private static final double DESCENT_HEIGHT = 0.6;
  private static final double STREAM_WIDTH = 0.025;
  private static final double SPREAD_WIDTH = 0.4;
  private static final double SPREAD_PEAK = 0.3;
  private static final double CURVE_HANDLE = 0.2;
  private static final double ARRIVAL_HANDLE = 0.15;
  private static final double MIN_HANDLE = 0.4;
  private static final double WANDER_SMOOTH = 0.12;
  private static final double SPAWN_Y = 6.5 / 16.0;
  private static final int ARC_SAMPLES = 48;
  private static final double SPEED = 0.04;

  private final int meanderFrequency;
  private final double meanderAmount;

  private final Vec3 start;
  private final Vec3 launchEnd;
  private final Vec3 c1;
  private final Vec3 c2;
  private final Vec3 aboveDest;
  private final Vec3 dest;

  private final double[] arcTable = new double[ARC_SAMPLES + 1];
  private final double launchLen;
  private final double curveLen;
  private final double descentLen;
  private final double totalLen;

  private final Vec3 baseOffset;
  private Vec3 wanderOffset = Vec3.ZERO;
  private Vec3 wanderTarget = Vec3.ZERO;

  protected MeanderTowardsParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double meanderFrequency, double meanderAmount, double intensity, int c1, int c2) {
    super(level, x, y, z, 0, 0, 0, c1, c2);
    this.meanderFrequency = (int) meanderFrequency;
    this.meanderAmount = meanderAmount;

    BlockPos destPos = type.pos();
    if (destPos == null) {
      throw new IllegalStateException("MeanderTowardsParticle must provide a destination");
    }
    if (level.getBlockEntity(destPos) instanceof MeanderTarget target) {
      this.dest = target.destination();
    } else {
      this.dest = Vec3.atCenterOf(destPos);
    }

    BlockPos stonePos = BlockPos.containing(x, y, z);
    BlockState state = level.getBlockState(stonePos);
    Direction facing;
    if (state.is(RootsTags.Blocks.GROVE_STONES)) {
      facing = state.getValue(GroveStoneBlock.FACING);
      this.start = new Vec3(stonePos.getX() + 0.5, stonePos.getY() + SPAWN_Y, stonePos.getZ() + 0.5);
    } else {
      facing = Direction.NORTH;
      this.start = new Vec3(x, y, z);
    }

    Direction.Axis axis = facing.getAxis();
    Vec3 axisVec = axis == Direction.Axis.X ? new Vec3(1, 0, 0) : new Vec3(0, 0, 1);
    double along = dest.subtract(start).dot(axisVec);
    double sign = Math.abs(along) < 0.05 ? (random.nextBoolean() ? 1 : -1) : Math.signum(along);
    Vec3 dir = axisVec.scale(sign);

    this.launchEnd = start.add(dir.scale(LAUNCH_DISTANCE));
    this.aboveDest = dest.add(0, DESCENT_HEIGHT, 0);
    double chordLen = aboveDest.subtract(launchEnd).length();
    this.c1 = launchEnd.add(dir.scale(Math.max(MIN_HANDLE, chordLen * CURVE_HANDLE)));
    this.c2 = aboveDest.add(0, Math.max(MIN_HANDLE, chordLen * ARRIVAL_HANDLE), 0);

    Vec3 prev = launchEnd;
    arcTable[0] = 0;
    for (int i = 1; i <= ARC_SAMPLES; i++) {
      Vec3 p = cubic(launchEnd, this.c1, this.c2, aboveDest, i / (double) ARC_SAMPLES);
      arcTable[i] = arcTable[i - 1] + p.distanceTo(prev);
      prev = p;
    }
    this.launchLen = LAUNCH_DISTANCE;
    this.curveLen = Math.max(1.0E-4, arcTable[ARC_SAMPLES]);
    this.descentLen = aboveDest.distanceTo(dest);
    this.totalLen = launchLen + curveLen + descentLen;

    this.baseOffset = randomUnit().scale(random.nextDouble());

    setPos(start.x, start.y, start.z);
    this.xo = start.x;
    this.yo = start.y;
    this.zo = start.z;
    this.xd = this.yd = this.zd = 0;

    this.alpha = 1f;
    this.hasPhysics = false;
    this.quadSize = 0.195f;
    this.lifetime = Math.max(10, Mth.ceil(totalLen / SPEED));
    this.quadSize = (float) (0.195 * Mth.lerp(intensity, 0.6, 1.4));
  }

  private Vec3 randomUnit() {
    Vec3 v = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
    return v.lengthSqr() < 1.0E-7 ? new Vec3(0, 1, 0) : v.normalize();
  }

  @Override
  protected void updateAlpha(float f) {
  }

  private static double smoothstep(double v) {
    v = Math.max(0, Math.min(1, v));
    return v * v * (3 - 2 * v);
  }

  private static double spreadProfile(double s) {
    if (s <= SPREAD_PEAK) {
      return smoothstep(s / SPREAD_PEAK);
    }
    return smoothstep((1.0 - s) / (1.0 - SPREAD_PEAK));
  }

  private static Vec3 cubic(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, double s) {
    double u = 1.0 - s;
    double a = u * u * u;
    double b = 3 * u * u * s;
    double c = 3 * u * s * s;
    double d = s * s * s;
    return new Vec3(
        a * p0.x + b * p1.x + c * p2.x + d * p3.x,
        a * p0.y + b * p1.y + c * p2.y + d * p3.y,
        a * p0.z + b * p1.z + c * p2.z + d * p3.z
    );
  }

  private double curveParamAt(double distance) {
    if (distance <= 0) {
      return 0;
    }
    if (distance >= arcTable[ARC_SAMPLES]) {
      return 1;
    }
    int lo = 0;
    int hi = ARC_SAMPLES;
    while (hi - lo > 1) {
      int mid = (lo + hi) >>> 1;
      if (arcTable[mid] < distance) {
        lo = mid;
      } else {
        hi = mid;
      }
    }
    double span = arcTable[hi] - arcTable[lo];
    double frac = span <= 0 ? 0 : (distance - arcTable[lo]) / span;
    return (lo + frac) / ARC_SAMPLES;
  }

  private Vec3 positionAt(double t) {
    double d = t * totalLen;
    Vec3 base;
    double profile;
    if (d <= launchLen) {
      base = start.lerp(launchEnd, d / launchLen);
      profile = 0;
    } else if (d < launchLen + curveLen) {
      double along = d - launchLen;
      base = cubic(launchEnd, c1, c2, aboveDest, curveParamAt(along));
      profile = spreadProfile(along / curveLen);
    } else {
      base = descentLen <= 0 ? dest : aboveDest.lerp(dest, (d - launchLen - curveLen) / descentLen);
      profile = 0;
    }
    double width = STREAM_WIDTH + SPREAD_WIDTH * profile;
    Vec3 offset = baseOffset.scale(width).add(wanderOffset.scale(SPREAD_WIDTH * profile));
    return base.add(offset);
  }

  private void updateWander() {
    if (meanderFrequency > 0 && this.age % meanderFrequency == 0) {
      wanderTarget = randomUnit().scale(meanderAmount * random.nextDouble());
    }
    wanderOffset = wanderOffset.lerp(wanderTarget, WANDER_SMOOTH);
  }

  @Override
  public void tick() {
    updateWander();
    double next = Math.min(1.0, (this.age + 1) / (double) this.lifetime);
    Vec3 target = positionAt(next);
    this.xd = target.x - this.x;
    this.yd = target.y - this.y;
    this.zd = target.z - this.z;
    super.tick();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_TRANSLUCENT;
  }

  // TODO: Override light

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new MeanderTowardsParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
