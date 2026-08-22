package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class MagnetismParticle extends RootsEntityParticle {
  protected double radius, oRadius;
  protected final double yOffset;
  protected final float angle, rads;

  protected MagnetismParticle(ClientLevel level, double x, double y, double z, double radius, double angle, double yOffset, int c1, int c2, Entity entity) {
    super(level, entity.getX(), entity.getY(), entity.getZ(), entity);
    this.angle = (float) angle;
    this.rads = (float) Math.toRadians(angle);
    this.radius = this.oRadius = radius;
    this.yOffset = yOffset;
    this.lifetime = 25;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bCol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.oRoll = this.roll = random.nextFloat() * 360f;
    this.rollAmount = random.nextFloat() * 0.1f;
    this.quadSize = 0.195f;
    this.gravity = 0.01f;
  }

  @Override
  public AABB getRenderBoundingBox(float partialTicks) {
    return AABB.INFINITE;
  }

  @Override
  public void tick() {
    this.oRadius = this.radius;
    this.radius *= 0.81f;
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.radius <= 0.1f) {
      this.remove();
    }
    if (!this.removed) {
      if (this.entity == null || this.entity.isRemoved() || (this.living != null && this.living.isDeadOrDying())) {
        this.remove();
      } else {
        this.x = this.entity.getX();
        this.y = this.entity.getY();
        this.z = this.entity.getZ();
        float f = generateF();
        updateMovement(f);
        updateColour(f);
        updateAlpha(f);
        updateRoll(f);
        updateQuadSize(f);
        updateSprite(f);
        particleTick(f);
      }
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_TRANSLUCENT;
  }

  @Override
  protected void updateAlpha(float f) {
    this.alpha = 1f - f * f * f;
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    if (!RenderTickHandler.isRenderingDelayedParticles() || delayedRender) {
      Vec3 pos = entity.getPosition(partialTicks);

      double radius = Mth.lerp(partialTicks, this.oRadius, this.radius);
      double offsetX = Mth.cos(rads) * radius;
      double offsetZ = Mth.sin(rads) * radius;

      double x = pos.x + offsetX;
      double y = pos.y + entity.getEyeHeight() - 0.5 + yOffset;
      double z = pos.z + offsetZ;

      Vec3 cam = renderInfo.getPosition();
      float rx = (float) (x - cam.x);
      float ry = (float) (y - cam.y);
      float rz = (float) (z - cam.z);

      Quaternionf quaternion = new Quaternionf();
      this.getFacingCameraMode().setRotation(quaternion, renderInfo, partialTicks);
      if (this.roll != 0.0f) {
        quaternion.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
      }

      renderRotatedQuad(buffer, quaternion, rx, ry, rz, partialTicks);
    }
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
