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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import teamroots.roots.util.IRenderEntityLater;
import teamroots.roots.util.RenderUtil;

@SuppressWarnings("hiding")
public class RenderAuspiciousPoint<EntityAuspiciousPoint> extends RenderEntity implements IRenderEntityLater {
	public ResourceLocation texture = new ResourceLocation("roots:textures/entity/beam.png");
	public ResourceLocation texture2 = new ResourceLocation("roots:textures/entity/beam2.png");
	public ResourceLocation textureMinorBeam = new ResourceLocation("roots:textures/entity/lesserbeam_mark.png");
	public ResourceLocation textureMajorBeam = new ResourceLocation("roots:textures/entity/majorbeam_mark.png");
	
	public RenderAuspiciousPoint(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks){
	
	}
	
	public static float getColorCycle(Entity entity, float partialTicks){
		return (MathHelper.sin((float)Math.toRadians((float)entity.ticksExisted+partialTicks))+1.0f)/2.0f;
	}
	
	@Override
	public void renderLater(Entity entity, double dx, double dy, double dz, float entityYaw, float partialTicks){
		float solidity = 0;
		if (entity instanceof teamroots.roots.entity.EntityAuspiciousPoint){
			solidity = ((teamroots.roots.entity.EntityAuspiciousPoint)entity).getSolidity();
		}
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
		double x = entity.posX+dx;
		double y = entity.posY+dy;
		double z = entity.posZ+dz;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        RenderUtil.renderBeam(buff, x+0.5, y-(6.0f+(entity.posY-entity.getEntityWorld().getTopSolidOrLiquidBlock(entity.getPosition()).getY())), z+0.5, x+0.5, 256, z+0.5, getColorCycle(entity,partialTicks), getColorCycle(entity,partialTicks), 1, 0.4f*solidity, 3.0f, 0.5f*((float)entity.ticksExisted+partialTicks));
        RenderUtil.renderBeam(buff, x+0.5, y-(6.0f+(entity.posY-entity.getEntityWorld().getTopSolidOrLiquidBlock(entity.getPosition()).getY())), z+0.5, x+0.5, 256, z+0.5, 1.0f-getColorCycle(entity,partialTicks), 1.0f-getColorCycle(entity,partialTicks), 1, 0.4f*solidity, 3.0f, 45.0f+0.5f*((float)entity.ticksExisted+partialTicks));
        tess.draw();
        
        Minecraft.getMinecraft().renderEngine.bindTexture(texture2);
        if (entity instanceof teamroots.roots.entity.EntityAuspiciousPoint){
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            teamroots.roots.entity.EntityAuspiciousPoint point = (teamroots.roots.entity.EntityAuspiciousPoint)entity;
            RenderUtil.renderBeam(buff, 
            		dx+((double)point.getDataManager().get(point.rune1).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune1).getY()), 
            		dz+((double)point.getDataManager().get(point.rune1).getZ())+0.5, 
            		dx+((double)point.getDataManager().get(point.rune1).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune1).getY())+14.0, 
            		dz+((double)point.getDataManager().get(point.rune1).getZ())+0.5, 
            		getColorCycle(entity,partialTicks), getColorCycle(entity,partialTicks), 1, 0.05f*solidity, 1.2f, 0.5f*((float)entity.ticksExisted+partialTicks));
            RenderUtil.renderBeam(buff, 
            		dx+((double)point.getDataManager().get(point.rune1).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune1).getY()), 
            		dz+((double)point.getDataManager().get(point.rune1).getZ())+0.5, 
            		dx+((double)point.getDataManager().get(point.rune1).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune1).getY())+14.0, 
            		dz+((double)point.getDataManager().get(point.rune1).getZ())+0.5, 
            		1.0f-getColorCycle(entity,partialTicks), 1.0f-getColorCycle(entity,partialTicks), 1, 0.05f*solidity, 1.2f, 45.0f+0.5f*((float)entity.ticksExisted+partialTicks));

            RenderUtil.renderBeam(buff, 
            		dx+((double)point.getDataManager().get(point.rune2).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune2).getY()), 
            		dz+((double)point.getDataManager().get(point.rune2).getZ())+0.5, 
            		dx+((double)point.getDataManager().get(point.rune2).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune2).getY())+14.0, 
            		dz+((double)point.getDataManager().get(point.rune2).getZ())+0.5, 
            		getColorCycle(entity,partialTicks), getColorCycle(entity,partialTicks), 1, 0.05f*solidity, 1.2f, 0.5f*((float)entity.ticksExisted+partialTicks));
            RenderUtil.renderBeam(buff, 
            		dx+((double)point.getDataManager().get(point.rune2).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune2).getY()), 
            		dz+((double)point.getDataManager().get(point.rune2).getZ())+0.5, 
            		dx+((double)point.getDataManager().get(point.rune2).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune2).getY())+14.0, 
            		dz+((double)point.getDataManager().get(point.rune2).getZ())+0.5, 
            		1.0f-getColorCycle(entity,partialTicks), 1.0f-getColorCycle(entity,partialTicks), 1, 0.05f*solidity, 1.2f, 45.0f+0.5f*((float)entity.ticksExisted+partialTicks));

            RenderUtil.renderBeam(buff, 
            		dx+((double)point.getDataManager().get(point.rune3).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune3).getY()), 
            		dz+((double)point.getDataManager().get(point.rune3).getZ())+0.5, 
            		dx+((double)point.getDataManager().get(point.rune3).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune3).getY())+14.0, 
            		dz+((double)point.getDataManager().get(point.rune3).getZ())+0.5, 
            		getColorCycle(entity,partialTicks), getColorCycle(entity,partialTicks), 1, 0.05f*solidity, 1.2f, 0.5f*((float)entity.ticksExisted+partialTicks));
            RenderUtil.renderBeam(buff, 
            		dx+((double)point.getDataManager().get(point.rune3).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune3).getY()), 
            		dz+((double)point.getDataManager().get(point.rune3).getZ())+0.5, 
            		dx+((double)point.getDataManager().get(point.rune3).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune3).getY())+14.0, 
            		dz+((double)point.getDataManager().get(point.rune3).getZ())+0.5, 
            		1.0f-getColorCycle(entity,partialTicks), 1.0f-getColorCycle(entity,partialTicks), 1, 0.05f*solidity, 1.2f, 45.0f+0.5f*((float)entity.ticksExisted+partialTicks));

            RenderUtil.renderBeam(buff, 
            		dx+((double)point.getDataManager().get(point.rune4).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune4).getY()), 
            		dz+((double)point.getDataManager().get(point.rune4).getZ())+0.5, 
            		dx+((double)point.getDataManager().get(point.rune4).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune4).getY())+14.0, 
            		dz+((double)point.getDataManager().get(point.rune4).getZ())+0.5, 
            		getColorCycle(entity,partialTicks), getColorCycle(entity,partialTicks), 1, 0.05f*solidity, 1.2f, 0.5f*((float)entity.ticksExisted+partialTicks));
            RenderUtil.renderBeam(buff, 
            		dx+((double)point.getDataManager().get(point.rune4).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune4).getY()), 
            		dz+((double)point.getDataManager().get(point.rune4).getZ())+0.5, 
            		dx+((double)point.getDataManager().get(point.rune4).getX())+0.5, 
            		dy+((double)point.getDataManager().get(point.rune4).getY())+14.0, 
            		dz+((double)point.getDataManager().get(point.rune4).getZ())+0.5, 
            		1.0f-getColorCycle(entity,partialTicks), 1.0f-getColorCycle(entity,partialTicks), 1, 0.05f*solidity, 1.2f, 45.0f+0.5f*((float)entity.ticksExisted+partialTicks));
            
            tess.draw();

    		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Minecraft.getMinecraft().renderEngine.bindTexture(textureMinorBeam);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            drawRune(buff,point,point.getDataManager().get(point.rune1),dx,dy,dz);
            drawRune(buff,point,point.getDataManager().get(point.rune2),dx,dy,dz);
            drawRune(buff,point,point.getDataManager().get(point.rune3),dx,dy,dz);
            drawRune(buff,point,point.getDataManager().get(point.rune4),dx,dy,dz);
            tess.draw();
            
            Minecraft.getMinecraft().renderEngine.bindTexture(textureMajorBeam);
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            drawRune(buff,point,new BlockPos(point.getPosition().getX(),point.height,point.getPosition().getZ()),dx,dy,dz);
            tess.draw();
        }
        
		GlStateManager.alphaFunc(func, ref);
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
	}
	
	public void drawRune(BufferBuilder buff, Entity e, BlockPos pos, double dx, double dy, double dz){
		double dist = 0.001;
		if (Minecraft.getMinecraft().player != null){
			if (Minecraft.getMinecraft().player.getDistanceSqToEntity(e) > 256.0){
				dist = 0.01;
			}
		}
		buff.pos(dx+(double)pos.getX(), dy+(double)pos.getY()+dist, dz+(double)pos.getZ()).tex(0.0,0.0).endVertex();
		buff.pos(dx+(double)pos.getX()+1.0, dy+(double)pos.getY()+dist, dz+(double)pos.getZ()).tex(1.0,0.0).endVertex();
		buff.pos(dx+(double)pos.getX()+1.0, dy+(double)pos.getY()+dist, dz+(double)pos.getZ()+1.0).tex(1.0,1.0).endVertex();
		buff.pos(dx+(double)pos.getX(), dy+(double)pos.getY()+dist, dz+(double)pos.getZ()+1.0).tex(0.0,1.0).endVertex();
	}
	
	@Override
	public boolean shouldRender(Entity livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return true;
    }
	
	public static class Factory implements IRenderFactory<teamroots.roots.entity.EntityAuspiciousPoint> {
		@Override
		public Render<? super teamroots.roots.entity.EntityAuspiciousPoint> createRenderFor(RenderManager manager) {
			return new RenderAuspiciousPoint(manager);
		}
	}
}
