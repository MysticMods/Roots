package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class LifeDrainParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rollAmount;
  private final Entity entity;
  protected double radius, prevRadius;
  protected double yOffset;
  protected final double angleRandom;
  protected final int angle;

  protected LifeDrainParticle(ClientLevel level, double x, double y, double z, double radius, double angleRandom, double angle, int c1, int c2, Entity entity) {
    super(level, entity.getX(), entity.getY(), entity.getZ());
    this.angleRandom = angleRandom;
    this.radius = this.prevRadius = radius;
    this.angle = (int) angle;
    this.yOffset = (random.nextDouble() - 1.5) * 0.15;
    this.entity = entity;
    this.lifetime = 15;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 0f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.oRoll = this.roll = random.nextFloat() * 360f;
    this.rollAmount = random.nextFloat() * 0.1f;
    this.quadSize = 0.395f;
    this.gravity = 0.01f;
  }

  @Override
  public AABB getRenderBoundingBox(float partialTicks) {
    return AABB.INFINITE;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    if (this.age++ >= this.lifetime || entity == null || entity.isRemoved()) {
      this.remove();
    } else {
      this.xo = this.x = entity.getX();
      this.yo = this.y = entity.getY();
      this.zo = this.z = entity.getZ();
      this.prevRadius = this.radius;

      this.radius *= 0.91f;
      this.yOffset -= 0.02f;

      float f = (float) this.age / (float) this.lifetime;

      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }
      this.oRoll = this.roll;
      this.roll += this.rollAmount;

      float FADE_IN_TICKS = 8f;
      if (this.age <= FADE_IN_TICKS) {
        this.alpha = (float) this.age / FADE_IN_TICKS;
      } else {
        this.alpha = 1f;
      }

      this.quadSize = 0.395f * (1.0f - f * f * f);
    }
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    if (entity == null || entity.isRemoved()) {
      return;
    }

    Vec3 pos = entity.getPosition(partialTicks);

    float yRot = entity.getViewYRot(partialTicks);
    double yawRad = Math.toRadians(-yRot);

    double coneOffset = Math.toRadians(angle / 2.0) * angleRandom;
    double finalAngle = yawRad + coneOffset;

    double radius = Mth.lerp(partialTicks, prevRadius, this.radius);

    double offsetX = Math.sin(finalAngle) * radius;
    double offsetZ = Math.cos(finalAngle) * radius;

    double x = pos.x + offsetX;
    double y = pos.y + entity.getEyeHeight() - 0.5 + yOffset;
    double z = pos.z + offsetZ;

    Vec3 cam = renderInfo.getPosition();
    float rx = (float) (x - cam.x);
    float ry = (float) (y - cam.y);
    float rz = (float) (z - cam.z);

    Quaternionf quaternion = new Quaternionf();
    this.getFacingCameraMode().setRotation(quaternion, renderInfo, partialTicks);
    if (this.roll != 0.0F) {
      quaternion.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
    }

    renderRotatedQuad(buffer, quaternion, rx, ry, rz, partialTicks);
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      var particle = new LifeDrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2(), entity);
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
