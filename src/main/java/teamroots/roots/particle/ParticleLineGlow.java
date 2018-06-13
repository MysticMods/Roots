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

public class ParticleLineGlow extends Particle implements IRootsParticle {
	public float colorR = 0;
	public float colorG = 0;
	public float colorB = 0;
	public float initAlpha = 1.0f;
	public double targetPosX = 0;
	public double targetPosY = 0;
	public double targetPosZ = 0;
	public double initPosX = 0;
	public double initPosY = 0;
	public double initPosZ = 0;
	
	public float initScale = 0;
	public ResourceLocation texture = new ResourceLocation("roots:entity/particle_glow");
	public ParticleLineGlow(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime) {
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
		this.targetPosX = vx;
		this.targetPosY = vy;
		this.targetPosZ = vz;
		this.initPosX = x;
		this.initPosY = y;
		this.initPosZ = z;
		
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
		float lifeCoeff = (float)this.particleAge/(float)this.particleMaxAge;
		this.posX = ((1.0f-lifeCoeff*lifeCoeff)*initPosX + (lifeCoeff*lifeCoeff)*targetPosX);
		this.posY = ((1.0f-lifeCoeff*lifeCoeff)*initPosY + (lifeCoeff*lifeCoeff)*targetPosY);
		this.posZ = ((1.0f-lifeCoeff*lifeCoeff)*initPosZ + (lifeCoeff*lifeCoeff)*targetPosZ);
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
