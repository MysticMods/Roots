package mysticmods.roots.client.particle;

import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class ShatterBeamParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float startScale;

  protected ShatterBeamParticle(ClientLevel level, double x, double y, double z, double scale, double unused, double unused2, int c1, int c2) {
    super(level, x, y, z, 0, 0, 0);
    this.lifetime = 40;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = this.startScale = (float) scale;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return super.getLightColor(partialTick);
    //return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.removed) {

      float f = (float) this.age / (float) this.lifetime;
      f *= f;
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      this.quadSize = startScale * (1 - f);
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new ShatterBeamParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
