package mysticmods.roots.client.particle.world;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MoveRandomlyInAABBParticle extends RootsParticle {
  private Vec3 nextMovement = null;
  private int randomTick;

  private final float height = 3f;
  private final float width = 6f;

  private final AABB bounds;

  private final boolean glow;

  protected MoveRandomlyInAABBParticle(ClientLevel level, double x, double y, double z, double centerX, double centerY, double centerZ, int c1, int c2) {
    super(level, x, y, z, c1, c2);
    this.glow = random.nextBoolean();
    this.lifetime = 65;
    this.alpha = 1f;
    this.bounds = new AABB(
        centerX - width / 2, centerY - height / 2, centerZ - width / 2,
        centerX + width / 2, centerY + height / 2, centerZ + width / 2
    );
    Vec3 current = new Vec3(x, y, z);
    Vec3 randomPos = randomInBounds();
    while (current.distanceToSqr(randomPos) < 3 * 3) {
      randomPos = randomInBounds();
    }
    Vec3 direction = randomPos.subtract(current).normalize().scale(0.025);


    this.xd = direction.x;
    this.yd = direction.y;
    this.zd = direction.z;
    this.randomTick = 6 + random.nextInt(12);
    this.nextMovement = randomInBounds().subtract(randomPos).normalize().scale(0.03);
    this.rollAmount = (random.nextFloat() - 0.5f) * 0.2f;
  }

  @Override
  protected void updateAlpha(float f) {
    if (age < 4) {
      float f2 = (float) age / 4f;
      this.alpha = f2 * f2;
    }
    super.updateAlpha(f * f * f * f);
  }

  protected Vec3 randomInBounds() {
    double x = Mth.lerp(random.nextDouble(), bounds.minX, bounds.maxX);
    double y = Mth.lerp(random.nextDouble(), bounds.minY, bounds.maxY);
    double z = Mth.lerp(random.nextDouble(), bounds.minZ, bounds.maxZ);
    return new Vec3(x, y, z);
  }

  @Override
  protected int getLightColor(float partialTick) {
    return super.getLightColor(partialTick);
/*    int rawLight = getLightColorRaw(partialTick);
    Vec3 currentPos = new Vec3(this.x, this.y, this.z);
    Vec3 center = bounds.getCenter();

    double maxDist = Math.sqrt(bounds.getXsize() * bounds.getXsize() + bounds.getYsize() * bounds.getYsize() + bounds.getZsize() * bounds.getZsize()) / 2.0;
    double dist = currentPos.distanceTo(center);
    double brightnessFactor = 1.0 - Mth.clamp(dist / maxDist, 0.0, 1.0);

    int blockLight = (rawLight >> 4) & 0xF;
    int skyLight = (rawLight >> 20) & 0xF;

    blockLight = (int) (blockLight * brightnessFactor);
    skyLight = (int) (skyLight * brightnessFactor);

    return (skyLight << 20) | (blockLight << 4);*/
  }

  @Override
  protected void updateQuadSize(float f) {
  }

  @Override
  protected void updateMovement(float f) {
    if (randomTick-- == 0) {
      this.randomTick = 4 + random.nextInt(12);
      Vec3 pos = new Vec3(this.x, this.y, this.z);
      Vec3 randomPos = randomInBounds();
      while (pos.distanceToSqr(randomPos) < 3 * 3) {
        randomPos = randomInBounds();
      }
      double speed = 0.02 + random.nextDouble() * 0.03;
      this.nextMovement = randomPos.subtract(pos).normalize().scale(speed);
    } else if (nextMovement != null) {
      Vec3 currentMovement = new Vec3(this.xd, this.yd, this.zd);
      Vec3 newMovement = currentMovement.lerp(this.nextMovement, 1.0 - Math.pow(0.8, f));
      this.xd = newMovement.x;
      this.yd = newMovement.y;
      this.zd = newMovement.z;
    }

    super.updateMovement(f);
  }

  @Override
  public ParticleRenderType getRenderType() {
    if (this.glow) {
      return RootsParticleRenderTypes.GLOW;
    } else {
      return RootsParticleRenderTypes.DELAYED_TRANSLUCENT;
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new MoveRandomlyInAABBParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
