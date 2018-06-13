package teamroots.roots.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import teamroots.roots.util.Misc;

public class ParticleFloatingGlow extends Particle implements IRootsParticle {
	public float colorR = 0;
	public float colorG = 0;
	public float colorB = 0;
	public float initAlpha = 1.0f;
	public double targetMotionX = 0;
	public double targetMotionY = 0;
	public double targetMotionZ = 0;
	
	public float initScale = 0;
	public ResourceLocation texture = new ResourceLocation("roots:entity/particle_glow");
	public ParticleFloatingGlow(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime) {
		super(worldIn, x,y,z,0,0,0);
		this.colorR = r;
		this.colorG = g;
		this.colorB = b;
		if (this.colorR > 1.0){
			this.colorR = this.colorR/255.0f;
		}
		if (this.colorG > 1.0){
			this.colorG = this.colorG/255.0f;
		}
		if (this.colorB > 1.0){
			this.colorB = this.colorB/255.0f;
		}
		this.initAlpha = a;
		this.setRBGColorF(colorR, colorG, colorB);
		this.setAlphaF(0);
		this.particleMaxAge = lifetime;
		this.particleScale = scale;
		this.initScale = scale;
		this.motionX = vx;
		this.motionY = vy;
		this.motionZ = vz;
		this.targetMotionX = motionX;
		this.targetMotionY = motionY;
		this.targetMotionZ = motionZ;
		
		this.particleAngle = 2.0f*(float)Math.PI;
	    TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
	    this.setParticleTexture(sprite);
	}
	/*
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ){
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		super.renderParticle(buffer, entity, partialTicks, rotX, rotZ, rotYZ, rotXY, rotXZ);
	}*/
	
	@Override
	public int getBrightnessForRender(float pTicks){
		return 255;
	}
	
	@Override
	public boolean shouldDisableDepth(){
		return true;
	}
	
	@Override
	public int getFXLayer(){
		return 1;
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		if (Misc.random.nextInt(6) == 0){
			this.particleAge ++;
		}
		if (this.particleAge % 5 == 0){
			targetMotionX = this.motionX*0.5f+0.125f*(rand.nextFloat()-0.5f);
			targetMotionZ = this.motionZ*0.5f+0.125f*(rand.nextFloat()-0.5f);
		}
		this.motionX = motionX*0.5f+targetMotionX*0.5f;
		this.motionY = targetMotionY;
		this.motionZ = motionX*0.5f+targetMotionZ*0.5f;
		float lifeCoeff = (float)this.particleAge/(float)this.particleMaxAge;
		this.particleScale = initScale*(1.0f-(Math.abs(0.5f-lifeCoeff)/0.5f));
		this.particleAlpha = initAlpha*(1.0f-(Math.abs(0.5f-lifeCoeff)/0.5f));
		prevParticleAngle = particleAngle;
		particleAngle += 1.0f;
	}

	@Override
	public boolean alive() {
		return this.particleAge < this.particleMaxAge;
	}

	@Override
	public boolean isAdditive() {
		return true;
	}

	@Override
	public boolean ignoreDepth() {
		return false;
	}
}
