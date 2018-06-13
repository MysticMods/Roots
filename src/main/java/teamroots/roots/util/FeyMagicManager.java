package teamroots.roots.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.EventManager;
import teamroots.roots.Roots;
import teamroots.roots.capability.PlayerDataProvider;

public class FeyMagicManager {
	public static final String FEY_TAG = Roots.MODID+":fey_magic";
	public static final Float MAX_VALUE = 256.0f;
	public static void init(){
		MinecraftForge.EVENT_BUS.register(new FeyMagicManager());
	}
	
	public static void setFeyMagic(EntityPlayer player, float value){
		if (player.hasCapability(PlayerDataProvider.playerDataCapability,null)){
			NBTTagCompound tag = player.getCapability(PlayerDataProvider.playerDataCapability, null).getData();
			tag.setFloat(FEY_TAG, Math.min(MAX_VALUE, value));
			player.getCapability(PlayerDataProvider.playerDataCapability, null).markDirty();
		}
	}
	
	public static float getFeyMagic(EntityPlayer player){
		if (player.hasCapability(PlayerDataProvider.playerDataCapability,null)){
			NBTTagCompound tag = player.getCapability(PlayerDataProvider.playerDataCapability, null).getData();
			if (tag.hasKey(FEY_TAG)){
				return tag.getFloat(FEY_TAG);
			}
		}
		return 0;
	}
	
	public float getRed(float level){
		if (level >= 0 && level < 0.25f){
			float coeff = level * 4.0f;
			return 177f * (1.0f-coeff) + 219f * coeff;
		}
		if (level >= 0.25f && level < 0.5f){
			float coeff = (level-0.25f) * 4.0f;
			return 219f * (1.0f-coeff) + 255f * coeff;
		}
		if (level >= 0.5f && level < 0.75f){
			float coeff = (level-0.5f) * 4.0f;
			return 255f * (1.0f-coeff) + 255f * coeff;
		}
		if (level >= 0.75f && level < 1.0f){
			float coeff = (level-0.75f) * 4.0f;
			return 255f * (1.0f-coeff) + 177f * coeff;
		}
		return 0;
	}
	
	public float getGreen(float level){
		if (level >= 0 && level < 0.25f){
			float coeff = level * 4.0f;
			return 255f * (1.0f-coeff) + 179f * coeff;
		}
		if (level >= 0.25f && level < 0.5f){
			float coeff = (level-0.25f) * 4.0f;
			return 179f * (1.0f-coeff) + 163f * coeff;
		}
		if (level >= 0.5f && level < 0.75f){
			float coeff = (level-0.5f) * 4.0f;
			return 163f * (1.0f-coeff) + 223f * coeff;
		}
		if (level >= 0.75f && level < 1.0f){
			float coeff = (level-0.75f) * 4.0f;
			return 223f * (1.0f-coeff) + 255f * coeff;
		}
		return 0;
	}
	
	public float getBlue(float level){
		if (level >= 0 && level < 0.25f){
			float coeff = level * 4.0f;
			return 117f * (1.0f-coeff) + 255f * coeff;
		}
		if (level >= 0.25f && level < 0.5f){
			float coeff = (level-0.25f) * 4.0f;
			return 255f * (1.0f-coeff) + 255f * coeff;
		}
		if (level >= 0.5f && level < 0.75f){
			float coeff = (level-0.5f) * 4.0f;
			return 255f * (1.0f-coeff) + 163f * coeff;
		}
		if (level >= 0.75f && level < 1.0f){
			float coeff = (level-0.75f) * 4.0f;
			return 163f * (1.0f-coeff) + 117f * coeff;
		}
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGameOverlayRender(RenderGameOverlayEvent.Post e){
		EntityPlayer player = Minecraft.getMinecraft().player;
		float f = getFeyMagic(player);
		if (f > 0){
			int w = e.getResolution().getScaledWidth();
			int h = e.getResolution().getScaledHeight();
			
			int x = w/2;
			int y = h/2;
			
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder b = tess.getBuffer();
			GlStateManager.disableDepth();
			GlStateManager.disableCull();
			GlStateManager.enableBlend();
			//GlStateManager.depthMask(false);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
			GlStateManager.enableAlpha();
			int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
			float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
			GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);

			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			double x1 = x - 90;
			double x2 = x - 90 + (180.0 * f/MAX_VALUE);
			for (double j = 0; j < 50; j ++){
				float time = (float)(EventManager.ticks%20)/20f;
				double coeff = j/50.0;
				double coeff2 = (j+1.0)/50.0;
				float c = ((float)coeff + time);
				c -= (int)c;
				float c2 = ((float)coeff2 + time);
				c2 -= (int)c2;
				float r1 = getRed(c)/255f;
				float r2 = getRed(c2)/255f;
				float g1 = getGreen(c)/255f;
				float g2 = getGreen(c2)/255f;
				float b1 = getBlue(c)/255f;
				float b2 = getBlue(c2)/255f;
				for (double k = 0; k < 4; k += 0.5){
					float thick = (float)((k+0.5)/4.0);
					double p1 = x1*(1.0f-coeff)+x2*(coeff);
					double p2 = x1*(1.0f-coeff2)+x2*(coeff2);
					float a1 = 0.25f+0.75f*NoiseGenUtil.get2DNoise(0, (int)(EventManager.ticks+Minecraft.getMinecraft().getRenderPartialTicks()*5f) + (int)(400*coeff), 4)*(float)MathHelper.sqrt(Math.min(coeff*(f/MAX_VALUE), 1.0-coeff*(f/MAX_VALUE))*2.0f);
					float a2 = 0.25f+0.75f*NoiseGenUtil.get2DNoise(0, (int)(EventManager.ticks+Minecraft.getMinecraft().getRenderPartialTicks()*5f) + (int)(400*coeff2), 4)*(float)MathHelper.sqrt(Math.min(coeff2*(f/MAX_VALUE), 1.0-coeff2*(f/MAX_VALUE))*2.0f);
					RenderUtil.drawColorRectBatched(b, 
							p1, (h-29.5)+3.0f*(1.0f-thick), 0, p2-p1, 3.0f*thick, 
							r1, g1, b1, a1*(1.0f-thick)*0.25f, 
							r2, g2, b2, a2*(1.0f-thick)*0.25f,
							r2, g2, b2, 0f,
							r1, g1, b1, 0f);
					RenderUtil.drawColorRectBatched(b, 
							p1, (h-29.5)+3.0f*(1.0f-thick)+3.0f*thick, 0, p2-p1, 3.0f*thick, 
							r1, g1, b1, 0, 
							r2, g2, b2, 0,
							r2, g2, b2, a2*(1.0f-thick)*0.25f,
							r1, g1, b1, a1*(1.0f-thick)*0.25f);
				}
			}
			tess.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("roots:textures/gui/fairy_icon.png"));
			
			//Gui.drawModalRectWithCustomSizedTexture(x-16, h-40, 0, 0, 32, 32, 32, 32);

			GlStateManager.depthMask(true);
			GlStateManager.alphaFunc(func, ref);
			GlStateManager.disableBlend();
			GlStateManager.enableCull();
			GlStateManager.enableDepth();
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/gui/icons.png"));
	}
}
