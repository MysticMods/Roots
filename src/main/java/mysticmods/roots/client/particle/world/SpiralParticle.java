package mysticmods.roots.client.particle.world;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class SpiralParticle extends TextureSheetParticle {
  protected final double centerX, centerZ;
  protected final double radius;
  protected double angle;
  protected double rAngle;
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rollSpeed;
  protected double wobblePhase;

  public static double calculateX(double x, double y, double z, double radius, double angle) {
    double wobblePhase = (x * 32 + z * 17 + y * 8) % (Math.PI * 2); // + 0.05;
    double wobble = 0.06 * Math.sin(wobblePhase);
    return x + Math.cos(angle) * (radius + wobble);
  }

  public static double calculateZ(double x, double y, double z, double radius, double angle) {
    double wobblePhase = (x * 32 + z * 17 + y * 8) % (Math.PI * 2); // + 0.05;
    double wobble = 0.06 * Math.sin(wobblePhase);
    return z + Math.sin(angle) * (radius + wobble);
  }

  protected SpiralParticle(ClientLevel level, double x, double y, double z, double radius, double angle, double zSpeed, int c1, int c2) {
    super(level, calculateX(x, y, z, radius, angle), y, calculateZ(x, y, z, radius, angle), 0, 0, 0);
    this.centerX = x;
    this.centerZ = z;
    this.radius = radius;
    this.angle = angle;
    this.rAngle = angle;
    this.lifetime = (int) Math.ceil((2 * Math.PI) / Math.abs(rAngle));
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.hasPhysics = false;
    this.quadSize = 0.05f;
    this.rollSpeed = (float) ((x * 17 + z * 4 + y * 8) % (Math.PI * 2)) * 0.01f;
    this.wobblePhase = (x * 31 + z * 17 + y * 8) % (Math.PI * 2);
    this.oRoll = 0;
    this.roll = this.oRoll + this.rollSpeed;

    double wobble = 0.06 * Math.sin(wobblePhase);
    double eRadius = radius + wobble;
    this.setPos(centerX + Math.cos(angle) * eRadius, y, centerZ + Math.sin(angle) * eRadius);
    this.xo = this.x;
    this.zo = this.z;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.GLOW_NO_MASK;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    if (this.age++ >= this.lifetime) {
      this.remove();
    }

    if (!this.removed) {
      float progress = (float) this.age / (float) this.lifetime;
      float f = (float) (0.5 * (1 - Math.cos(2 * Math.PI * progress)));
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      angle += rAngle;

      double wobble = 0.06 * Math.sin(wobblePhase);
      double eRadius = radius + wobble;

      wobblePhase += 0.1;
      this.oRoll = this.roll;
      this.roll += this.rollSpeed;

      this.setPos(centerX + Math.cos(angle) * eRadius, y, centerZ + Math.sin(angle) * eRadius);
      this.xo = this.x;
      this.zo = this.z;
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new SpiralParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
