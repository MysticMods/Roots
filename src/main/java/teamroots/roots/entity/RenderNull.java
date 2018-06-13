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
import teamroots.roots.util.IRenderEntityLater;
import teamroots.roots.util.RenderUtil;

public class RenderNull extends RenderEntity {
	
	public RenderNull(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks){
		//
	}
	
	@Override
	public boolean shouldRender(Entity livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return false;
    }
	
	public static class Factory implements IRenderFactory {
		@Override
		public Render createRenderFor(RenderManager manager) {
			return new RenderNull(manager);
		}
	}
}
