package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ChannelJauntParticle extends TextureSheetParticle {
  private final Entity caster;
  private final float oR, oG, oB, rR, rG, rB;
  private final InteractionHand hand;
  private double fallSpeed;
  private final double depthOffset, jiggle;
  private final boolean motionUp;

  public ChannelJauntParticle(ClientLevel level, double x, double y, double z,
                              double motion, double unusedY, double unusedZ,
                              int color1, int color2, Entity caster) {
    super(level, x, y, z);
    this.caster = caster;
    if (caster instanceof Player player) {
      hand = player.getUsedItemHand();
    } else {
      hand = InteractionHand.MAIN_HAND;
    }

    motionUp = !(motion < 1);

    this.oR = ((color1 >> 16) & 0xFF) / 255.0f;
    this.oG = ((color1 >> 8) & 0xFF) / 255.0f;
    this.oB = ((color1) & 0xFF) / 255.0f;
    this.rR = ((color2 >> 16) & 0xFF) / 255.0f;
    this.rG = ((color2 >> 8) & 0xFF) / 255.0f;
    this.rB = ((color2) & 0xFF) / 255.0f;

    this.rCol = oR;
    this.gCol = oG;
    this.bCol = oB;

    this.yd = 0;
    this.xd = 0;
    this.zd = 0;

    this.lifetime = 8 + random.nextInt(5); // ~1.5s
    this.quadSize = 0.2f;
    this.alpha = 1f;
    this.hasPhysics = false;
    this.depthOffset = (random.nextDouble() - 0.5) * 0.2;
    this.jiggle = (random.nextDouble() - 0.5) * 0.1;

    tick();
  }

  private void updatePosition(float partialTicks) {
    if (caster != null) {
      Vec3 eyePos = caster.getEyePosition(partialTicks).subtract(0, 0.4, 0);
      Vec3 lookDir = caster.getViewVector(partialTicks).normalize();
      Vec3 rightVec = lookDir.cross(new Vec3(0, 1, 0)).normalize();

      // TODO: MAJOR TODO: Left-handedness
      // Hand-based horizontal offset (right for MAIN_HAND, left for OFF_HAND)
      double handOffset = hand == InteractionHand.MAIN_HAND ? 0.25 : -0.25;
      float t = (float) (age + partialTicks) / lifetime;
      double smoothedDepth = Mth.lerp(t, 0.0, depthOffset);

      // Final particle base position, slightly in front of the face and offset to side
      Vec3 basePos = eyePos
          .add(lookDir.scale(0.6 + smoothedDepth))
          .add(rightVec.scale(handOffset + (motionUp ? -0.1 : 0.1) + jiggle))
          .add(0, motionUp ? 0 : 0.7, 0);

      this.x = Mth.lerp(partialTicks, basePos.x, this.xo);
      this.y = Mth.lerp(partialTicks, basePos.y - fallSpeed, this.yo);
      this.z = Mth.lerp(partialTicks, basePos.z, this.zo);
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
      if (caster == null || caster.isRemoved()) {
        this.remove();
        return;
      }

      this.fallSpeed += motionUp ? -0.08 : 0.08;

      float t = (float) age / lifetime;

      this.rCol = oR + (rR - oR) * t;
      this.gCol = oG + (rG - oG) * t;
      this.bCol = oB + (rB - oB) * t;

      this.alpha = 1.0f - (t * t * t * t); // fade out non-linearly
    }
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
      Entity caster = Minecraft.getInstance().level.getEntity(type.entityId());
      if (caster == null) {
        return null;
      }
      ChannelJauntParticle p = new ChannelJauntParticle(level, x, y, z, radius, 0, 0, type.color1(), type.color2(), caster);
      p.pickSprite(sprite);
      return p;
    }
  }
}
