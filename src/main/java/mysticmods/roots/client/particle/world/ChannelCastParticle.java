package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ChannelCastParticle extends SortedEntityParticle {
  private final double radius;
  private double angle, oAngle;
  private final InteractionHand hand;
  private final double depthOffset;

  public ChannelCastParticle(ClientLevel level, double x, double y, double z,
                             double radius, double unusedY, double unusedZ,
                             int c1, int c2, Entity entity) {
    super(level, x, y, z, entity);
    if (entity instanceof Player player) {
      hand = player.getUsedItemHand();
    } else {
      hand = InteractionHand.MAIN_HAND;
    }

    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;

    this.lifetime = 18 + random.nextInt(10); // ~1.5s
    this.radius = radius;
    this.angle = this.oAngle = random.nextDouble() * 2 * Math.PI;
    this.quadSize = 0.2f;
    this.alpha = 1f;
    this.hasPhysics = false;
    this.depthOffset = (random.nextDouble() - 0.5) * 0.2;

    updatePosition(0f);
    tick();
  }

  private void updatePosition(float partialTicks) {
    if (entity != null) {
      Vec3 eyePos = entity.getEyePosition(partialTicks);
      Vec3 lookDir = entity.getViewVector(partialTicks).normalize();
      Vec3 rightVec = lookDir.cross(new Vec3(0, 1, 0)).normalize();
      Vec3 upVec = rightVec.cross(lookDir).normalize();

      double angle = Mth.lerp(0, this.angle, this.oAngle);

      double dynamicRadius = radius + 0.02 * Math.sin((age + partialTicks) * 0.2 + radius * 10);
      double localX = Math.cos(angle) * dynamicRadius;
      double localY = Math.sin(angle) * dynamicRadius;

      Vec3 circleOffset = rightVec.scale(localX).add(upVec.scale(localY));

      // Hand-based horizontal offset (right for MAIN_HAND, left for OFF_HAND)
      double handOffset = hand == InteractionHand.MAIN_HAND ? 0.25 : -0.25;
      float t = (age + partialTicks) / lifetime;
      double smoothedDepth = Mth.lerp(t, 0.0, depthOffset);

      // Final particle base position, slightly in front of the face and offset to side
      Vec3 basePos = eyePos
          .add(lookDir.scale(0.6 + smoothedDepth))
          .add(rightVec.scale(handOffset));

      this.x = Mth.lerp(0, basePos.x + circleOffset.x, this.xo);
      this.y = Mth.lerp(0, basePos.y + circleOffset.y, this.yo);
      this.z = Mth.lerp(0, basePos.z + circleOffset.z, this.zo);
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.removed) {
      this.angle += 0.2 + random.nextDouble() * 0.1; // make an
    }
  }

  @Override
  protected void updateAlpha(float t) {
    this.alpha = 1.0f - (t * t); // fade out non-linearly
  }

  @Override
  public AABB getRenderBoundingBox(float partialTicks) {
    return AABB.INFINITE;
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    this.updatePosition(0f);
    super.render(buffer, renderInfo, partialTicks);
  }

  public static class Provider implements ParticleProvider<RootsParticleOptions> {
    private final SpriteSet sprite;

    public Provider(SpriteSet sprite) {
      this.sprite = sprite;
    }

    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double radius, double unusedY, double unusedZ) {
      Entity entity = Minecraft.getInstance().level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      ChannelCastParticle p = new ChannelCastParticle(level, x, y, z, radius, 0, 0, type.color1(), type.color2(), entity);
      p.pickSprite(sprite);
      return p;
    }
  }
}
