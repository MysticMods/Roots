package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.RenderTickHandler;
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
import org.joml.Quaternionf;

public class ChannelJauntParticle extends RootsEntityParticle {
  private final InteractionHand hand;
  private double fallSpeed, oFallSpeed;
  private final double depthOffset, jiggle;
  private final boolean motionUp;

  public ChannelJauntParticle(ClientLevel level, double x, double y, double z,
                              double motion, double unusedY, double unusedZ,
                              int c1, int c2, Entity entity) {
    super(level, x, y, z, entity);
    if (entity instanceof Player player) {
      hand = player.getUsedItemHand();
    } else {
      hand = InteractionHand.MAIN_HAND;
    }

    motionUp = !(motion < 1);
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bCol2 = ((c2) & 0xFF) / 255.0f;
    this.yd = 0;
    this.xd = 0;
    this.zd = 0;

    this.lifetime = 8 + random.nextInt(5); // ~1.5s
    this.quadSize = 0.2f;
    this.alpha = 1f;
    this.hasPhysics = false;
    this.depthOffset = (random.nextDouble() - 0.5) * 0.2;
    this.jiggle = (random.nextDouble() - 0.5) * 0.1;
    updatePosition(Minecraft.getInstance().gameRenderer.getMainCamera(), RenderTickHandler.getPartialTick());
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
  }

  private void updatePosition(Camera camera, float partialTicks) {
    if (entity != null) {
      Vec3 eye0 = entity.getEyePosition(0);
      Vec3 eye1 = entity.getEyePosition(1);
      Vec3 eyePos = eye0.lerp(eye1, partialTicks).subtract(0, 0.4, 0);

      Vec3 look0 = entity.getViewVector(0);
      Vec3 look1 = entity.getViewVector(1);
      Vec3 lookDir = look0.lerp(look1, partialTicks).normalize();
      Vec3 rightVec = lookDir.cross(new Vec3(0, 1, 0)).normalize();

      // TODO: MAJOR TODO: Left-handedness
      // Hand-based horizontal offset (right for MAIN_HAND, left for OFF_HAND)
      double handOffset = hand == InteractionHand.MAIN_HAND ? 0.25 : -0.25;
      float t = (float) age / (float) lifetime;
      double smoothedDepth = Mth.lerp(t, 0.0, depthOffset);

      // Final particle base position, slightly in front of the face and offset to side
      Vec3 basePos = eyePos
          .add(lookDir.scale(0.6 + smoothedDepth))
          .add(rightVec.scale(handOffset + (motionUp ? -0.08 : 0.08) + jiggle))
          .add(0, motionUp ? 0 : 0.7, 0);

      double fallSpeed = Mth.lerp(partialTicks, oFallSpeed, this.fallSpeed);

      this.x = basePos.x; //Mth.lerp(partialTicks, basePos.x, this.xo);
      this.y = basePos.y - fallSpeed; //Mth.lerp(partialTicks, basePos.y - fallSpeed, this.yo);
      this.z = basePos.z; //Mth.lerp(partialTicks, basePos.z, this.zo);
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.removed) {
      this.oFallSpeed = fallSpeed;
      this.fallSpeed += motionUp ? -0.03 : 0.03;
    }
  }

  @Override
  protected void updateAlpha(float t) {
    this.alpha = 1.0f - (t * t * t * t); // fade out non-linearly
  }

  @Override
  public AABB getRenderBoundingBox(float partialTicks) {
    return AABB.INFINITE;
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    if (shouldRender()) {
      this.updatePosition(renderInfo, partialTicks);

      Vec3 cam = renderInfo.getPosition();
      float rx = (float) (this.x - cam.x);
      float ry = (float) (this.y - cam.y);
      float rz = (float) (this.z - cam.z);

      Quaternionf quaternion = new Quaternionf();
      this.getFacingCameraMode().setRotation(quaternion, renderInfo, partialTicks);
      if (this.roll != 0.0f) {
        quaternion.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
      }

      renderRotatedQuad(buffer, quaternion, rx, ry, rz, partialTicks);
    }
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
      ChannelJauntParticle p = new ChannelJauntParticle(level, x, y, z, radius, 0, 0, type.color1(), type.color2(), entity);
      p.pickSprite(sprite);
      return p;
    }
  }
}
