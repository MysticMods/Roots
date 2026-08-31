package mysticmods.roots.client.particle.world.block;


import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class GroveStoneParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rollAmount;

  protected GroveStoneParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.lifetime = 50;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = xSpeed;
    this.yd = ySpeed;
    this.zd = zSpeed;
    this.hasPhysics = false;
    this.quadSize = 0.05f;
    this.gravity = 0.0f;
    this.rollAmount = 0.1f + random.nextFloat() * 0.2f; // randomized roll amount
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    //return RootsParticleRenderTypes.GLOW;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.removed) {
      float f = (float) this.age / (float) this.lifetime;

      // Fade from color1 to color2
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      this.oRoll = this.roll;
      this.roll += rollAmount;

      float fadeStart = 0.9f;
      if (f < fadeStart) {
        this.alpha = 1f;
      } else {
        float fadeProgress = (f - fadeStart) / (1f - fadeStart);
        this.alpha = 1f - (fadeProgress * fadeProgress * fadeProgress); // cubic fade
      }
    }
  }

  public static class Provider implements ParticleProvider<RootsParticleOptions> {
    private final SpriteSet sprite;

    public Provider(SpriteSet sprite) {
      this.sprite = sprite;
    }

    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      TextureSheetParticle portalParticle = new GroveStoneParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      portalParticle.pickSprite(this.sprite);
      return portalParticle;
    }
  }
}

