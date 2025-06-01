package mysticmods.roots.client.particle.screen.base;

import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;

public abstract class RootsScreenParticle extends TextureSheetScreenParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bCol2;
  protected float rollAmount;
  protected boolean defaultLight = true;
  protected boolean defaultMovement = true;
  protected boolean defaultAlpha = true;
  protected boolean defaultRoll = true;
  protected boolean defaultColor = true;

  protected boolean fastForwarding = false;

  protected RootsScreenParticle(ClientLevel level, RootsParticleOptions options, double x, double y, int col1, int col2) {
    super(level, x, y);
    unwrapColor(options.color1(), options.color2());
    if (options.fastForward() > 0) {
      this.fastForwarding = true;
      this.fastForward(options.fastForward());
      this.fastForwarding = false;
    }
  }

  protected RootsScreenParticle(ClientLevel level, RootsParticleOptions options, double x, double y, double xSpeed, double ySpeed) {
    super(level, x, y, xSpeed, ySpeed);
    unwrapColor(options.color1(), options.color2());
    if (options.fastForward() > 0) {
      this.fastForwarding = true;
      this.fastForward(options.fastForward());
      this.fastForwarding = false;
    }
  }

  protected void unwrapColor(int c1, int c2) {
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bCol2 = ((c2) & 0xFF) / 255.0f;
  }

  protected void updateQuadSize(float f) {

  }

  protected void updateAlpha(float f) {
    if (defaultAlpha) {
      this.alpha = 1f - f;
    }
  }

  protected void updateRoll(float f) {
    if (defaultRoll) {
      this.oRoll = this.roll;
      this.roll = this.roll + this.rollAmount;
    }
  }

  protected void updateColour(float f) {
    if (defaultColor) {
      if (this.oB1 != this.bCol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bCol2 - this.oB1) * f;
      }
    }
  }

  protected void updateSprite(float f) {

  }

  protected float generateF() {
    return (float) this.age / (float) this.lifetime;
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      if (defaultMovement) {
        this.yd = this.yd - 0.04 * (double) this.gravity;
        this.x += this.xd;
        this.y += this.yd;

        this.xd = this.xd * (double) this.friction;
        this.yd = this.yd * (double) this.friction;
      }
      float f = generateF();
      updateColour(f);
      updateAlpha(f);
      updateRoll(f);
      updateQuadSize(f);
      updateSprite(f);
      particleTick(f);
    }
  }

  public void fastForward(int ticks) {
    if (ticks > 0) {
      fastForwarding = true;
      for (int i = 0; i < ticks; i++) {
        this.tick();
      }
      fastForwarding = false;
    }
  }

  protected void particleTick(float f) {
  }

  @Override
  protected int getLightColor(float partialTick) {
    if (defaultLight) {
      return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
    } else {
      return super.getLightColor(partialTick);
    }
  }
}
