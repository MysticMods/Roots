package epicsquid.roots.particle;

import epicsquid.mysticallib.particle.ParticleBase;
import epicsquid.mysticallib.util.Util;
import net.minecraft.world.World;

public class ParticlePyreLeaf extends ParticleBase {
	public float colorR;
	public float colorG;
	public float colorB;
	public float initScale;
	public float initAlpha;
	public float angularVelocity;
	public boolean additive;
	
	public double destX, destY, destZ;
	public double distance;
	
	public ParticlePyreLeaf(World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
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
		this.destX = data[7];
		this.destY = data[8];
		this.destZ = data[9];
		this.distance = data[10];
		this.angularVelocity = 0.0f;
		this.prevParticleAngle = particleAngle;
		this.particleAngle = Util.rand.nextFloat() * 2.0f * (float) Math.PI;
		this.particleGravity = 0.0f;
	}
	
	private void standardUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}
		
		if (this.distance() <= distance) {
			this.particleMaxAge = this.particleAge;
			this.initAlpha = 0;
			this.setExpired();
		}
		
		this.motionY -= 0.04D * (double) this.particleGravity;
		this.move(this.motionX, this.motionY, this.motionZ);
		
		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}
	
	public double distance() {
		double a = this.posX - this.destX;
		double b = this.posY - this.destY;
		double c = this.posZ - this.destZ;
		return (a * a + b * b + c * c);
	}
	
	@Override
	public void onUpdate() {
		standardUpdate();
		lifetime--;
		this.prevParticleAngle = particleAngle;
		this.particleAngle += this.angularVelocity;
		float lifeCoeff = (float) this.particleAge / (float) this.particleMaxAge;
		this.particleAlpha = initAlpha * (Math.max(1.0f - lifeCoeff, 0.6f));
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
