package mysticmods.roots.client.particle;

import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class ChannelNoCastParticle extends TextureSheetParticle {
  private final float oR, oG, oB, rR, rG, rB;
  private double fallSpeed = 0;

  public ChannelNoCastParticle(ClientLevel level, double x, double y, double z,
                               double radius, double unusedY, double unusedZ,
                               int color1, int color2) {
    super(level, x, y, z);

    this.oR = ((color1 >> 16) & 0xFF) / 255.0f;
    this.oG = ((color1 >> 8) & 0xFF) / 255.0f;
    this.oB = ((color1) & 0xFF) / 255.0f;
    this.rR = ((color2 >> 16) & 0xFF) / 255.0f;
    this.rG = ((color2 >> 8) & 0xFF) / 255.0f;
    this.rB = ((color2) & 0xFF) / 255.0f;

    this.rCol = oR;
    this.gCol = oG;
    this.bCol = oB;

    this.lifetime = 15 + random.nextInt(10); // ~1.5s
    this.quadSize = 0.2f;
    this.alpha = 1f;
    this.hasPhysics = false;
  }

  @Override
  public void tick() {
    super.tick();
    if (!removed) {
      float t = (float) age / lifetime;

      this.rCol = oR + (rR - oR) * t;
      this.gCol = oG + (rG - oG) * t;
      this.bCol = oB + (rB - oB) * t;

      this.alpha = 1.0f - t; // non-linear fade

      // Begin falling after short delay
      if (age > 13) {
        fallSpeed += 0.06; // acceleration
        this.y -= fallSpeed;
      }
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
      ChannelNoCastParticle p = new ChannelNoCastParticle(level, x, y, z, radius, 0, 0, type.color1(), type.color2());
      p.pickSprite(sprite);
      return p;
    }
  }
}
