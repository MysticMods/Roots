package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

// This needs to become entity bound
public class MagnetismParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rollAmount;
  private final Entity entity;
  protected double radius;
  protected final double yOffset;
  protected final float angle, rads;

  protected MagnetismParticle(ClientLevel level, double x, double y, double z, double radius, double angle, double yOffset, int c1, int c2, Entity entity) {
    super(level, entity.getX(), entity.getY(), entity.getZ());
    this.angle = (float) angle;
    this.rads = (float) Math.toRadians(angle);
    this.radius = radius;
    this.yOffset = yOffset;
    this.entity = entity;
    this.lifetime = 25;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.oRoll = this.roll = random.nextFloat() * 360f;
    this.rollAmount = random.nextFloat() * 0.1f;
    this.quadSize = 0.195f;
    this.gravity = 0.01f;
    this.updatePosition();
    this.tick();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.TRANSLUCENT_NO_MASK;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ >= this.lifetime || entity == null || entity.isRemoved()) {
      this.remove();
    } else {
      this.radius *= 0.93f;

      float f = (float) this.age / (float) this.lifetime;

      // Color lerp
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      // Roll and fade
      this.oRoll = this.roll;
      this.roll += this.rollAmount;
      this.alpha = 1f - f * f * f;
      updatePosition();
    }
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    updatePosition();
    super.render(buffer, renderInfo, partialTicks);
  }

  protected void updatePosition() {
    if (entity == null || entity.isRemoved() || removed) {
      return;
    }

    double offsetX = Mth.cos(rads) * radius;
    double offsetZ = Mth.sin(rads) * radius;

    this.x = Mth.lerp(0.0, entity.getX() + offsetX, this.xo);
    this.z = Mth.lerp(0.0, entity.getZ() + offsetZ, this.zo);
    this.y = Mth.lerp(0.0, entity.getY() + entity.getEyeHeight() - 0.5 + yOffset, this.yo);
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      var particle = new MagnetismParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2(), entity);
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
