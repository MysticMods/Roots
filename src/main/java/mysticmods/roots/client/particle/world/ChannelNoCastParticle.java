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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ChannelNoCastParticle extends SortedEntityParticle {
  private static final int threshold = 13;

  private double fallSpeed = 0;
  private final double angle, radius, hand;

  public ChannelNoCastParticle(ClientLevel level, double x, double y, double z,
                               double radius, double angle, double hand,
                               int c1, int c2, Entity entity) {
    super(level, x, y, z, entity);
    this.angle = angle;
    this.radius = radius;
    this.hand = hand;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.lifetime = 11 + random.nextInt(10); // ~1.5s
    this.quadSize = 0.2f;
    this.alpha = 1f;
    this.hasPhysics = false;
    this.tickMovement = false;
    this.autoUpdateDistance = false;
    updatePosition(Minecraft.getInstance().gameRenderer.getMainCamera(), RenderTickHandler.getPartialTick());
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
  }

  private void updatePosition(Camera camera, float partialTicks) {
    if (entity != null) {
      Vec3 lookDir = entity.getViewVector(partialTicks).normalize();
      Vec3 rightVec = lookDir.cross(new Vec3(0, 1, 0)).normalize();
      Vec3 upVec = rightVec.cross(lookDir).normalize();

      double localX = Math.cos(angle) * radius;
      double localY = Math.sin(angle) * radius;
      Vec3 circleOffset = rightVec.scale(localX).add(upVec.scale(localY));

      Vec3 eyePos = entity.getEyePosition();
      Vec3 start = eyePos.add(lookDir.scale(0.6)).add(circleOffset).add(rightVec.scale(hand));

      // Renderer should automatically lerp this
      this.x = start.x; //Mth.lerp(partialTicks, start.x, this.xo);
      if (age > threshold) {
        this.y = start.y - fallSpeed; //Mth.lerp(partialTicks, start.y - fallSpeed, this.yo);
      } else {
        this.y = start.y; //Mth.lerp(partialTicks, start.y, this.yo);
      }
      this.z = start.z; //Mth.lerp(partialTicks, start.z, this.zo);

      Vec3 newPos = new Vec3(this.x, this.y, this.z);
      Vec3 oldPos = new Vec3(this.xo, this.yo, this.zo);
      Vec3 pos = oldPos.lerp(newPos, partialTicks);
      Vec3 camPos = camera.getPosition();
      this.distanceToCamera = pos.distanceTo(camPos);
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.removed) {
      if (age > threshold) {
        fallSpeed += 0.03; // acceleration
      }
    }
  }

  @Override
  protected void updateAlpha(float f) {
    this.alpha = 1.0f - (float) Math.pow(f, 9); // non-linear fade
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    updatePosition(renderInfo, partialTicks);
    super.render(buffer, renderInfo, partialTicks);
  }

  public static class Provider implements ParticleProvider<RootsParticleOptions> {
    private final SpriteSet sprite;

    public Provider(SpriteSet sprite) {
      this.sprite = sprite;
    }

    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double radius, double unusedY, double unusedZ) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      ChannelNoCastParticle p = new ChannelNoCastParticle(level, x, y, z, radius, unusedY, unusedZ, type.color1(), type.color2(), entity);
      p.pickSprite(sprite);
      return p;
    }
  }
}
