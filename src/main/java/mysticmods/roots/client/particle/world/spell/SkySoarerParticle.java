package mysticmods.roots.client.particle.world.spell;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class SkySoarerParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rollAmount;

  private float swirlAngle, swirlRadius, swirlSpeed;

  protected SkySoarerParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.lifetime = 38;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = xSpeed;
    this.yd = ySpeed;
    this.zd = zSpeed;
    this.hasPhysics = false;
    // These values seem innaccurate
    this.oRoll = this.roll = random.nextFloat() * 360f;
    this.rollAmount = random.nextFloat() * 0.1f;
    this.quadSize = 0.12f;
    this.gravity = 0.05f;

    this.swirlAngle = random.nextFloat() * 360f;
    this.swirlRadius = 0.15f + random.nextFloat() * 0.08f;
    this.swirlSpeed = 11f + random.nextFloat() * 16f;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.GLOW_NO_DEPTH;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.removed) {
      float f = (float) this.age / (float) this.lifetime;

      // Color lerp
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      // Curl around Z axis
      Vec3 dir = new Vec3(xd, 0, zd).normalize();
      double px = -dir.z;
      double pz = dir.x;

      swirlAngle += swirlSpeed;
      double dynamicRadius = swirlRadius * (0.5 + f * 1.3);
      double rad = Math.toRadians(swirlAngle);
      double swirlX = px * Math.cos(rad) * dynamicRadius;
      double swirlZ = pz * Math.sin(rad) * dynamicRadius;
      double swirlY = Math.sin(rad * 0.4) * swirlRadius * 0.3;

      this.xd += swirlX * 0.2;
      this.zd += swirlZ * 0.2;
      this.yd += swirlY * 0.2;

      // Increase gravity pull after halfway
      if ((f * f) > 0.45f) {
        this.yd -= gravity * 1.2f;
      }

      // Roll and fade
      this.oRoll = this.roll;
      this.roll += this.rollAmount;
      this.alpha = 1f - f * f * f;
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new SkySoarerParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
