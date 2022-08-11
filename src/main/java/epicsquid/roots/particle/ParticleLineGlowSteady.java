package epicsquid.roots.particle;

import epicsquid.mysticallib.particle.ParticleBase;
import net.minecraft.world.World;

public class ParticleLineGlowSteady extends ParticleBase {
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
	
	public ParticleLineGlowSteady(World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
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
		this.initAlpha = (float) data[4];
		this.setRBGColorF(colorR, colorG, colorB);
		this.setAlphaF(0);
		this.particleMaxAge = (int) data[0];
		this.particleScale = (float) data[5];
		this.initScale = (float) data[5];
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
	
	@Override
	public boolean shouldDisableDepth() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		float lifeCoeff = (float) this.particleAge / (float) this.particleMaxAge;
		this.posX = ((1.0f - lifeCoeff * lifeCoeff) * initPosX + (lifeCoeff * lifeCoeff) * targetPosX);
		this.posY = ((1.0f - lifeCoeff * lifeCoeff) * initPosY + (lifeCoeff * lifeCoeff) * targetPosY);
		this.posZ = ((1.0f - lifeCoeff * lifeCoeff) * initPosZ + (lifeCoeff * lifeCoeff) * targetPosZ);
		this.particleScale = initScale;
		this.particleAlpha = initAlpha;
		prevParticleAngle = particleAngle;
		particleAngle += 1.0f;
	}
	
	@Override
	public boolean isAdditive() {
		return true;
	}
	
}