package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public abstract class RootsParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rollAmount;
  protected boolean forceLight = true;
  protected boolean tickMovement = true;
  protected boolean delayedRender = true;

  protected RootsParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
  }

  protected RootsParticle(ClientLevel level, double x, double y, double z) {
    super(level, x, y, z);
  }

  protected void updateQuadSize(float f) {

  }

  protected void updateAlpha(float f) {
    this.alpha = 1f - f;
  }

  protected void updateRoll(float f) {
    this.oRoll = this.roll;
    this.roll = this.roll + this.rollAmount;
  }

  protected void updateColour(float f) {
    if (this.oB1 != this.bcol2) {
      this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
      this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
      this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
    }
  }

  protected void updateSprite(float f) {

  }

  protected boolean shouldRender () {
    if (!RenderTickHandler.isRenderingDelayedParticles() || delayedRender) {
      return true;
    }

    return false;
  }

  @Override
  public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
    if (shouldRender()) {
      super.render(buffer, renderInfo, partialTicks);
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
      if (tickMovement) {
        this.yd = this.yd - 0.04 * (double) this.gravity;
        this.move(this.xd, this.yd, this.zd);
        if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
          this.xd *= 1.1;
          this.zd *= 1.1;
        }

        this.xd = this.xd * (double) this.friction;
        this.yd = this.yd * (double) this.friction;
        this.zd = this.zd * (double) this.friction;
        if (this.onGround) {
          this.xd *= 0.7F;
          this.zd *= 0.7F;
        }
      }
      float f = (float) this.age / (float) this.lifetime;
      updateColour(f);
      updateAlpha(f);
      updateRoll(f);
      updateQuadSize(f);
      updateSprite(f);
    }
  }

  @Override
  protected int getLightColor(float partialTick) {
    if (forceLight) {
      return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
    } else {
      return super.getLightColor(partialTick);
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_TRANSLUCENT;
  }

}
