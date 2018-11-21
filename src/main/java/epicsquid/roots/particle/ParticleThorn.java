package epicsquid.roots.particle;

import epicsquid.mysticallib.particle.ParticleBase;
import net.minecraft.world.World;

public class ParticleThorn extends ParticleBase {

  private float colorR;
  private float colorG;
  private float colorB;
  private float initAlpha;
  private float initScale;
  private boolean isAdditive = false;

  public ParticleThorn(World worldIn, double x, double y, double z, double vx, double vy, double vz, double[] data) {
    super(worldIn, x, y, z, 0, 0, 0, data);
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
    this.initAlpha = (float) data[4];
    this.setRBGColorF(colorR, colorG, colorB);
    this.particleMaxAge = (int) data[0];
    this.particleScale = (float) data[5];
    this.initScale = (float) data[5];
    this.motionX = vx;
    this.motionY = vy;
    this.motionZ = vz;
    this.particleAngle = rand.nextFloat() * 2.0f * (float) Math.PI;
    this.particleGravity = 0.2f;
  }

  @Override
  public int getBrightnessForRender(float pTicks) {
    return 255;
  }

  @Override
  public boolean shouldDisableDepth() {
    return true;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float lifeCoeff = (float) this.particleAge / (float) this.particleMaxAge;
    this.particleScale = initScale - initScale * lifeCoeff;
    this.particleAlpha = (1.0f - lifeCoeff) * initAlpha;
  }

  @Override
  public boolean isAdditive() {
    return true;
  }

}
