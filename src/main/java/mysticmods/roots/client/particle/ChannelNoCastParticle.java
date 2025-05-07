package mysticmods.roots.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ChannelNoCastParticle extends TextureSheetParticle {
  private static final int threshold = 13;

  private final float oR, oG, oB, rR, rG, rB;
  private double fallSpeed = 0;
  private final Entity entity;
  private final double angle, radius, hand;

  public ChannelNoCastParticle(ClientLevel level, double x, double y, double z,
                               double radius, double angle, double hand,
                               int color1, int color2, Entity entity) {
    super(level, x, y, z);
    this.angle = angle;
    this.radius = radius;
    this.hand = hand;
    this.entity = entity;
    this.oR = ((color1 >> 16) & 0xFF) / 255.0f;
    this.oG = ((color1 >> 8) & 0xFF) / 255.0f;
    this.oB = ((color1) & 0xFF) / 255.0f;
    this.rR = ((color2 >> 16) & 0xFF) / 255.0f;
    this.rG = ((color2 >> 8) & 0xFF) / 255.0f;
    this.rB = ((color2) & 0xFF) / 255.0f;

    this.rCol = oR;
    this.gCol = oG;
    this.bCol = oB;

    this.lifetime = 11 + random.nextInt(10); // ~1.5s
    this.quadSize = 0.2f;
    this.alpha = 1f;
    this.hasPhysics = false;
    updatePosition();
  }

  private void updatePosition() {
    if (entity != null) {
      Vec3 lookDir = entity.getViewVector(1.0f).normalize();
      Vec3 rightVec = lookDir.cross(new Vec3(0, 1, 0)).normalize();
      Vec3 upVec = rightVec.cross(lookDir).normalize();

      double localX = Math.cos(angle) * radius;
      double localY = Math.sin(angle) * radius;
      Vec3 circleOffset = rightVec.scale(localX).add(upVec.scale(localY));

      Vec3 eyePos = entity.getEyePosition(1.0f);
      Vec3 start = eyePos.add(lookDir.scale(0.6)).add(circleOffset).add(rightVec.scale(hand));

      this.x = start.x;
      if (age > threshold) {
        this.y = start.y - fallSpeed;
      } else {
        this.y = start.y;
      }
      this.z = start.z;
    }
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      if (age > threshold) {
        fallSpeed += 0.06; // acceleration
      }
      float t = (float) age / lifetime;

      this.rCol = oR + (rR - oR) * t;
      this.gCol = oG + (rG - oG) * t;
      this.bCol = oB + (rB - oB) * t;

      this.alpha = 1.0f - (t * t); // non-linear fade

      // Begin falling after short delay
      updatePosition();
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    updatePosition();
    super.render(buffer, renderInfo, partialTicks);
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  public static class Provider implements ParticleProvider<RootsParticleOptions> {
    private final SpriteSet sprite;

    public Provider(SpriteSet sprite) {
      this.sprite = sprite;
    }

    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double radius, double unusedY, double unusedZ) {
      Entity entity = level.getEntity(type.entityId());
      ChannelNoCastParticle p = new ChannelNoCastParticle(level, x, y, z, radius, unusedY, unusedZ, type.color1(), type.color2(), entity);
      p.pickSprite(sprite);
      return p;
    }
  }
}
