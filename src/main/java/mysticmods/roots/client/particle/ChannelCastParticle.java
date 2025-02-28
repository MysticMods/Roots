package mysticmods.roots.client.particle;

import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ChannelCastParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;

  protected Vec3 stop;

  protected ChannelCastParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 30;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.stop = new Vec3(xSpeed, ySpeed, zSpeed);
    Vec3 direction = stop.subtract(new Vec3(x, y, z)).normalize().scale(0.3);
    this.xd = direction.x;
    this.yd = direction.y;
    this.zd = direction.z;
    this.hasPhysics = true;
    this.x += (random.nextDouble() - 0.5) * 0.2;
    this.xo = this.x;
    this.y += (random.nextDouble() - 0.5) * 0.2;
    this.yo = this.y;
    this.z += (random.nextDouble() - 0.5) * 0.2;
    this.zo = this.z;
    this.setPos(this.x, this.y, this.z);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      // Here is where we determine if we're close to the stop location
      if (this.getPos().distanceTo(this.stop) < 0.1) {
        double newXd = -this.zd;
        double newZd = this.xd;

        // Add slight randomness for spreading out
        double spread = 0.05; // Adjust for more or less spread
        newXd += (random.nextDouble() - 0.5) * spread;
        newZd += (random.nextDouble() - 0.5) * spread;
        this.xd = newXd;
        this.zd = newZd;

        // Set a downward motion to simulate gravity or dispersal
        this.yd = -0.05;

        // Shorten lifetime for quicker dispersal
        this.age = 0;
        this.lifetime = 15;
      }

      f *= f;

/*      // Height control
      if (this.age < 8) {
        this.yd = 0; // Stay at the same height
      } else {
        // Start dropping slowly in the last few ticks
        this.yd -= (0.2 * f) * 0.1f;
      }*/
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<ColorGravityParticleOptions> {
    @Override
    public Particle createParticle(ColorGravityParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new ChannelCastParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
