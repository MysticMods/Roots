package epicsquid.roots.particle;

import epicsquid.mysticallib.particle.ParticleBase;
import epicsquid.mysticallib.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleWhirlwindLeaf extends ParticleBase {
  public float colorR;
  public float colorG;
  public float colorB;
  public float initScale;
  public float initAlpha;
  public float angularVelocity;
  public boolean additive;
  public float initAngle;

  public double centerX, centerY, centerZ;
  public double radius;

  public boolean inverse = false;

  public ParticleWhirlwindLeaf(World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
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
    this.centerX = data[7];
    this.centerY = data[8];
    this.centerZ = data[9];
    this.radius = data[10];
    this.angularVelocity = 0.0f;
    this.prevParticleAngle = particleAngle;
    this.particleAngle = Util.rand.nextFloat() * 2.0f * (float) Math.PI;
    this.particleGravity = 0.0f;
    this.initAngle = Util.rand.nextFloat() * (2.0f * (float) Math.PI * 2);
    this.inverse = data[11] == 1;
  }

  private void standardUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;

    if (this.particleAge++ >= this.particleMaxAge) {
      this.setExpired();
    }

    float thisAngle = (float) particleAge / particleMaxAge * (2 * (float) Math.PI * 2) + initAngle;
    this.posY = centerY + MathHelper.sin(0.0073123123f * thisAngle);
    if (!inverse) {
      this.posX = centerX + radius * MathHelper.sin(thisAngle);
      this.posZ = centerZ + radius * MathHelper.cos(thisAngle);
    } else {
      this.posX = centerX + radius * MathHelper.cos(thisAngle);
      this.posZ = centerZ + radius * MathHelper.sin(thisAngle);
    }
  }

  @Override
  public void onUpdate() {
    standardUpdate();
    lifetime--;
    this.prevParticleAngle = particleAngle;
    //this.particleAngle += this.angularVelocity;
    float lifeCoeff = (float) this.particleAge / (float) this.particleMaxAge;
    this.particleAlpha = initAlpha * (Math.max(1.0f - lifeCoeff, 0.1f));
  }

  @Override
  protected void onUpdateNoMotion() {
    standardUpdate();
    lifetime--;
  }

  @Override
  public boolean isAdditive() {
    return this.additive;
  }
}
