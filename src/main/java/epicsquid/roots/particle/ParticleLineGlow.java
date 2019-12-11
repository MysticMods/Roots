package epicsquid.roots.particle;

import epicsquid.mysticallib.particle.ParticleBase;
import net.minecraft.world.World;

public class ParticleLineGlow extends ParticleBase {
  private float colorR;
  private float colorG;
  private float colorB;
  private float initAlpha;
  private double targetPosX;
  private double targetPosY;
  private double targetPosZ;
  private double initPosX;
  private double initPosY;
  private double initPosZ;

  private float initScale;

  public ParticleLineGlow(World world, double x, double y, double z, double vx, double vy, double vz, float[] data) {
    super(world, x, y, z, vx, vy, vz, data);
    this.colorR = data[1];
    this.colorG = data[2];
    this.colorB = data[3];
    if (this.colorR > 1.0) {
      this.colorR = this.colorR / 255.0f;
    }
    if (this.colorG > 1.0) {
      this.colorG = this.colorG / 255.0f;
    }
    if (this.colorB > 1.0) {
      this.colorB = this.colorB / 255.0f;
    }
    this.initAlpha = data[4];
    this.setColor(colorR, colorG, colorB);
    this.setAlphaF(0);
    this.setMaxAge((int) data[0]);
    this.particleScale = data[5];
    this.initScale = data[5];
    this.targetPosX = vx;
    this.targetPosY = vy;
    this.targetPosZ = vz;
    this.initPosX = x;
    this.initPosY = y;
    this.initPosZ = z;

    this.particleAngle = 2.0f * (float) Math.PI;
  }

  @Override
  public int getBrightnessForRender(float pTicks) {
    return 255;
  }

/*  @Override
  public boolean shouldDisableDepth() {
    return true;
  }*/

  @Override
  public void tick() {
    super.tick();

    float lifeCoeff = (float) this.age / (float) this.maxAge;
    this.posX = ((1.0f - lifeCoeff * lifeCoeff) * initPosX + (lifeCoeff * lifeCoeff) * targetPosX);
    this.posY = ((1.0f - lifeCoeff * lifeCoeff) * initPosY + (lifeCoeff * lifeCoeff) * targetPosY);
    this.posZ = ((1.0f - lifeCoeff * lifeCoeff) * initPosZ + (lifeCoeff * lifeCoeff) * targetPosZ);
    this.particleScale = initScale - initScale * lifeCoeff;
    this.particleAlpha = initAlpha * (1.0f - lifeCoeff);
    prevParticleAngle = particleAngle;
    particleAngle += 1.0f;
  }

/*  @Override
  public boolean isAdditive() {
    return true;
  }*/
}
