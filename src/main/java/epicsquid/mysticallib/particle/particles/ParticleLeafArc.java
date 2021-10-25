package epicsquid.mysticallib.particle.particles;

import epicsquid.mysticallib.particle.ParticleBase;
import epicsquid.mysticallib.util.Util;
import net.minecraft.world.World;

public class ParticleLeafArc extends ParticleBase {
  public float colorR;
  public float colorG;
  public float colorB;
  public float initScale;
  public float initAlpha;
  public float angularVelocity;
  public boolean additive;

  public ParticleLeafArc(World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
    super(world, x, y, z, vx, vy, vz, data);
    this.colorR = (float) data[1];
    this.colorG = (float) data[2];
    this.colorB = (float) data[3];
    if (this.colorR > 1.0) {
      this.colorR = this.colorR / 255.0f;
    }
    if (this.colorG > 1.0) {
      this.colorG = this.colorG / 255.0f;
    }
    if (this.colorB > 1.0) {
      this.colorB = this.colorB / 255.0f;
    }
    this.setRBGColorF(colorR, colorG, colorB);
    this.setAlphaF((float) data[4]);
    this.initAlpha = (float) data[4];
    this.particleScale = (float) data[5];
    this.initScale = (float) data[5];
    this.additive = data[6] == 1;
    this.angularVelocity = 0.0f;
    this.prevParticleAngle = particleAngle;
    this.particleAngle = Util.rand.nextFloat() * 2.0f * (float) Math.PI;
    this.particleGravity = 0.0f;
  }

  @Override
  public void onUpdate() {
    super.onUpdateNoMotion();
    this.prevParticleAngle = particleAngle;
    this.particleAngle += this.angularVelocity;
    float lifeCoeff = (float) this.particleAge / (float) this.particleMaxAge;
    this.particleAlpha = initAlpha * (Math.max(1.0f - lifeCoeff, 0.6f));
    this.particleGravity += 0.001f;
    if (particleGravity > 0.45f) {
      particleGravity = 0.45f;
    }
  }

  @Override
  public boolean isAdditive() {
    return additive;
  }
}
