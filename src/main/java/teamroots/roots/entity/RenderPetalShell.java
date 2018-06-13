package teamroots.roots.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import teamroots.roots.spell.SpellRegistry;
import teamroots.roots.util.IRenderEntityLater;
import teamroots.roots.util.RenderUtil;

@SuppressWarnings("hiding")
public class RenderPetalShell<EntityPetalShell> extends RenderEntity implements IRenderEntityLater {
	public ResourceLocation texture = new ResourceLocation("roots:textures/entity/particle_petal.png");
	
	public RenderPetalShell(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks){
		//
	}
	
	public static float getColorCycle(float rads){
		return (MathHelper.sin(rads)+1.0f)/2.0f;
	}
	
	@Override
	public void renderLater(Entity entity, double dx, double dy, double dz, float entityYaw, float partialTicks){
		teamroots.roots.entity.EntityPetalShell shell = (teamroots.roots.entity.EntityPetalShell)entity;
		float r1 = SpellRegistry.spell_peony.red1;
		float r2 = SpellRegistry.spell_peony.red2;
		float g1 = SpellRegistry.spell_peony.green1;
		float g2 = SpellRegistry.spell_peony.green2;
		float b1 = SpellRegistry.spell_peony.blue1;
		float b2 = SpellRegistry.spell_peony.blue2;
		double x = entity.posX+dx;
		double y = entity.posY+dy;
		double z = entity.posZ+dz;
		if (shell.getDataManager().get(shell.charge) > 0){
			float yawRads = (float)Math.toRadians(3.0f*((float)(shell).ticksExisted + partialTicks));
			float pitchRads = (float)Math.sin(Math.toRadians(7.5f*((float)(shell).ticksExisted + partialTicks)))*(float)Math.toRadians(10.0f);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(false);
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
			int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
			float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
			GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
	        Tessellator tess = Tessellator.getInstance();
	        BufferBuilder buff = tess.getBuffer();
	        
	        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
	        for (float i = 0; i < 2.0f*Math.PI; i += (float)(Math.PI*2.0f)/(float)shell.getDataManager().get(shell.charge)){
	        	double posX1 = 1.25f*Math.sin(i+yawRads-Math.toRadians(25.0f))*Math.cos(pitchRads-Math.toRadians(25.0f));
	        	double posY1 = 1.25f*Math.sin(pitchRads-Math.toRadians(25.0f));
	        	double posZ1 = 1.25f*Math.cos(i+yawRads-Math.toRadians(25.0f))*Math.cos(pitchRads-Math.toRadians(25.0f));
	        	double posX2 = 1.25f*Math.sin(i+yawRads+Math.toRadians(25.0f))*Math.cos(pitchRads-Math.toRadians(25.0f));
	        	double posY2 = 1.25f*Math.sin(pitchRads-Math.toRadians(25.0f));
	        	double posZ2 = 1.25f*Math.cos(i+yawRads+Math.toRadians(25.0f))*Math.cos(pitchRads-Math.toRadians(25.0f));
	        	double posX3 = 1.25f*Math.sin(i+yawRads+Math.toRadians(25.0f))*Math.cos(pitchRads+Math.toRadians(25.0f));
	        	double posY3 = 1.25f*Math.sin(pitchRads+Math.toRadians(25.0f));
	        	double posZ3 = 1.25f*Math.cos(i+yawRads+Math.toRadians(25.0f))*Math.cos(pitchRads+Math.toRadians(25.0f));
	        	double posX4 = 1.25f*Math.sin(i+yawRads-Math.toRadians(25.0f))*Math.cos(pitchRads+Math.toRadians(25.0f));
	        	double posY4 = 1.25f*Math.sin(pitchRads+Math.toRadians(25.0f));
	        	double posZ4 = 1.25f*Math.cos(i+yawRads-Math.toRadians(25.0f))*Math.cos(pitchRads+Math.toRadians(25.0f));
	        	buff.pos(x+posX1, y+posY1, z+posZ1).tex(0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(getColorCycle(i+yawRads)*r1+(1.0f-getColorCycle(i+yawRads))*r2, getColorCycle(i+yawRads)*g1+(1.0f-getColorCycle(i+yawRads))*g2, getColorCycle(i+yawRads)*b1+(1.0f-getColorCycle(i+yawRads))*b2, 1f).endVertex();
	        	buff.pos(x+posX2, y+posY2, z+posZ2).tex(1.0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(getColorCycle(i+yawRads)*r1+(1.0f-getColorCycle(i+yawRads))*r2, getColorCycle(i+yawRads)*g1+(1.0f-getColorCycle(i+yawRads))*g2, getColorCycle(i+yawRads)*b1+(1.0f-getColorCycle(i+yawRads))*b2, 1f).endVertex();
	        	buff.pos(x+posX3, y+posY3, z+posZ3).tex(1.0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(getColorCycle(i+yawRads)*r1+(1.0f-getColorCycle(i+yawRads))*r2, getColorCycle(i+yawRads)*g1+(1.0f-getColorCycle(i+yawRads))*g2, getColorCycle(i+yawRads)*b1+(1.0f-getColorCycle(i+yawRads))*b2, 1f).endVertex();
	        	buff.pos(x+posX4, y+posY4, z+posZ4).tex(0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(getColorCycle(i+yawRads)*r1+(1.0f-getColorCycle(i+yawRads))*r2, getColorCycle(i+yawRads)*g1+(1.0f-getColorCycle(i+yawRads))*g2, getColorCycle(i+yawRads)*b1+(1.0f-getColorCycle(i+yawRads))*b2, 1f).endVertex();
	         }
	        tess.draw();
	        
			GlStateManager.enableCull();
			GlStateManager.alphaFunc(func, ref);
			GlStateManager.enableLighting();
	        GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.depthMask(true);
		}
	}
	
	@Override
	public boolean isMultipass(){
		return true;
	}
	
	@Override
	public boolean shouldRender(Entity livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return true;
    }
	
	public static class Factory implements IRenderFactory<teamroots.roots.entity.EntityPetalShell> {
		@Override
		public Render<? super teamroots.roots.entity.EntityPetalShell> createRenderFor(RenderManager manager) {
			return new RenderPetalShell(manager);
		}
	}
}
