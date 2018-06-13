package teamroots.roots.util;

import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderUtil {
	public static final float root2over2 = (float)Math.sqrt(2.0f)/2.0f;
	public static int lightx = 0xF000F0;
	public static int lighty = 0xF000F0;
    public static void renderBeam(BufferBuilder buf, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a, float radius, double angle){
		double yaw = Misc.yawDegreesBetweenPoints(x1, y1, z1, x2, y2, z2);
        double pitch = Misc.pitchDegreesBetweenPoints(x1, y1, z1, x2, y2, z2);
        
        float yawCos = MathHelper.cos((float)Math.toRadians(yaw));
        float yawSin = MathHelper.sin((float)Math.toRadians(yaw));
        float pitchCos = MathHelper.cos((float)Math.toRadians(pitch));
        float pitchSin = MathHelper.sin((float)Math.toRadians(pitch));
        float angCos = MathHelper.cos((float)Math.toRadians(angle));
        float angSin = MathHelper.sin((float)Math.toRadians(angle));
        
        float dxh = radius*yawCos*pitchSin;
        float dyh = radius*pitchCos;
        float dzh = radius*yawSin*pitchSin;
        
        float dxv = radius*yawSin*pitchSin;
        float dyv = radius*pitchCos;
        float dzv = radius*yawCos*pitchSin;
        
        float dx = dxh * angCos + dxv * angSin;
        float dy = dyh * angCos + dyv * angSin;
        float dz = dzh * angCos + dzv * angSin;
        float dx2 = dxh * -angSin + dxv * angCos;
        float dy2 = dyh * -angSin + dyv * angCos;
        float dz2 = dzh * -angSin + dzv * angCos;
        
        double distX = x2-x1;
        double distY = y2-y1;
        double distZ = z2-z1;
        
        buf.pos(x1-dx, y1-dy, z1-dz).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+dx, y1+dy, z1+dz).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x2+dx, y2+dy, z2+dz).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x2-dx, y2-dy, z2-dz).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        
        buf.pos(x1-dx2, y1-dy2, z1-dz2).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+dx2, y1+dy2, z1+dz2).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x2+dx2, y2+dy2, z2+dz2).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x2-dx2, y2-dy2, z2-dz2).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
	
	}
    
    public static void drawCenteredString(FontRenderer font, String string, int x, int y, int color){
    	font.drawString(string, x-font.getStringWidth(string)/2, y, color);
    }

    public static void drawCenteredStringWithShadow(FontRenderer font, String string, int x, int y, int color){
    	font.drawStringWithShadow(string, x-font.getStringWidth(string)/2, y, color);
    }
	
	public static void renderAlchemyCircle(BufferBuilder buf, double x, double y, double z, float r, float g, float b, float a, double radius, double angle){
		double sign = 1;
		if (Minecraft.getMinecraft().player.posY+Minecraft.getMinecraft().player.getEyeHeight() < y){
			sign = -1;
		}
		int lightx = 0xF000F0;
        int lighty = 0xF000F0;
        for (double i = 0; i < 360; i += 10){
			double tx = Math.sin(Math.toRadians(i+angle));
			double tz = Math.cos(Math.toRadians(i+angle));
			double tx2 = Math.sin(Math.toRadians(i+angle+10));
			double tz2 = Math.cos(Math.toRadians(i+angle+10));
			buf.pos(x+radius*tx, y, z+radius*tz).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
			buf.pos(x+(radius+0.25)*tx, y, z+(radius+0.25)*tz).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
			buf.pos(x+(radius+0.25)*tx2, y, z+(radius+0.25)*tz2).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
			buf.pos(x+radius*tx2, y, z+radius*tz2).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		}
		double ax = (radius+0.24)*Math.sin(Math.toRadians(0+angle));
		double az = (radius+0.24)*Math.cos(Math.toRadians(0+angle));
		double adx = (0.1875)*Math.cos(Math.toRadians(0+angle));
		double adz = (0.1875)*-Math.sin(Math.toRadians(0+angle));
		double bx = (radius+0.24)*Math.sin(Math.toRadians(120+angle));
		double bz = (radius+0.24)*Math.cos(Math.toRadians(120+angle));
		double bdx = (0.1875)*Math.cos(Math.toRadians(120+angle));
		double bdz = (0.1875)*-Math.sin(Math.toRadians(120+angle));
		double cx = (radius+0.24)*Math.sin(Math.toRadians(240+angle));
		double cz = (radius+0.24)*Math.cos(Math.toRadians(240+angle));
		double cdx = (0.1875)*Math.cos(Math.toRadians(240+angle));
		double cdz = (0.1875)*-Math.sin(Math.toRadians(240+angle));
		buf.pos(x+(ax-adx), y+0.00005*sign, z+(az-adz)).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(ax+adx), y+0.00005*sign, z+(az+adz)).tex(0, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(bx-bdx), y+0.00005*sign, z+(bz-bdz)).tex(1, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(bx+bdx), y+0.00005*sign, z+(bz+bdz)).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();

		buf.pos(x+(bx-bdx), y+0.0001*sign, z+(bz-bdz)).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(bx+bdx), y+0.0001*sign, z+(bz+bdz)).tex(0, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(cx-cdx), y+0.0001*sign, z+(cz-cdz)).tex(1, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(cx+cdx), y+0.0001*sign, z+(cz+cdz)).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();

		buf.pos(x+(ax-adx), y+0.00015*sign, z+(az-adz)).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(ax+adx), y+0.00015*sign, z+(az+adz)).tex(0, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(cx-cdx), y+0.00015*sign, z+(cz-cdz)).tex(1, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(cx+cdx), y+0.00015*sign, z+(cz+cdz)).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param textures
	 * 
	 * Order the textures and inversions like so: up (pos Y), down (neg Y), north (neg Z), south (pos Z), west (neg X), east (pos X)
	 */
	public static void addBox(BufferBuilder b, double x1, double y1, double z1, double x2, double y2, double z2, StructUV[] textures, int[] inversions){
		//BOTTOM FACE
		b.pos(x1, y1, z1).tex(textures[0].minU,textures[0].minV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x1, y1, z2).tex(textures[0].maxU,textures[0].minV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x2, y1, z2).tex(textures[0].maxU,textures[0].maxV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x2, y1, z1).tex(textures[0].minU,textures[0].maxV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		//TOP FACE
		b.pos(x1, y2, z1).tex(textures[1].minU,textures[1].minV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x1, y2, z2).tex(textures[1].maxU,textures[1].minV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x2, y2, z2).tex(textures[1].maxU,textures[1].maxV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x2, y2, z1).tex(textures[1].minU,textures[1].maxV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		//NORTH FACE
		b.pos(x1, y1, z1).tex(textures[2].minU,textures[2].minV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x2, y1, z1).tex(textures[2].maxU,textures[2].minV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x2, y2, z1).tex(textures[2].maxU,textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x1, y2, z1).tex(textures[2].minU,textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		//SOUTH FACE
		b.pos(x1, y1, z2).tex(textures[3].minU,textures[3].minV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x2, y1, z2).tex(textures[3].maxU,textures[3].minV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x2, y2, z2).tex(textures[3].maxU,textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x1, y2, z2).tex(textures[3].minU,textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		//WEST FACE
		b.pos(x1, y1, z1).tex(textures[4].minU,textures[4].minV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y1, z2).tex(textures[4].maxU,textures[4].minV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y2, z2).tex(textures[4].maxU,textures[4].maxV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y2, z1).tex(textures[4].minU,textures[4].maxV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		//EAST FACE
		b.pos(x2, y1, z1).tex(textures[5].minU,textures[5].minV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y1, z2).tex(textures[5].maxU,textures[5].minV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y2, z2).tex(textures[5].maxU,textures[5].maxV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y2, z1).tex(textures[5].minU,textures[5].maxV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
	}
	
	public static void addBoxWithSprite(BufferBuilder b, double x1, double y1, double z1, double x2, double y2, double z2, TextureAtlasSprite sprite, StructUV[] textures, int[] inversions){
		float spriteW = sprite.getMaxU()-sprite.getMinU();
		float spriteH = sprite.getMaxV()-sprite.getMinV();
		
		//BOTTOM FACE
		b.pos(x1, y1, z1).tex(sprite.getMinU()+textures[0].minU*spriteW,sprite.getMinV()+textures[0].minV*spriteH).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x1, y1, z2).tex(sprite.getMinU()+textures[0].maxU*spriteW,sprite.getMinV()+textures[0].minV*spriteH).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x2, y1, z2).tex(sprite.getMinU()+textures[0].maxU*spriteW,sprite.getMinV()+textures[0].maxV*spriteH).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x2, y1, z1).tex(sprite.getMinU()+textures[0].minU*spriteW,sprite.getMinV()+textures[0].maxV*spriteH).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		//TOP FACE
		b.pos(x1, y2, z1).tex(sprite.getMinU()+textures[1].minU*spriteW,sprite.getMinV()+textures[1].minV*spriteH).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x1, y2, z2).tex(sprite.getMinU()+textures[1].maxU*spriteW,sprite.getMinV()+textures[1].minV*spriteH).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x2, y2, z2).tex(sprite.getMinU()+textures[1].maxU*spriteW,sprite.getMinV()+textures[1].maxV*spriteH).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x2, y2, z1).tex(sprite.getMinU()+textures[1].minU*spriteW,sprite.getMinV()+textures[1].maxV*spriteH).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		//NORTH FACE
		b.pos(x1, y1, z1).tex(sprite.getMinU()+textures[2].minU*spriteW,sprite.getMinV()+textures[2].minV*spriteH).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x2, y1, z1).tex(sprite.getMinU()+textures[2].maxU*spriteW,sprite.getMinV()+textures[2].minV*spriteH).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x2, y2, z1).tex(sprite.getMinU()+textures[2].maxU*spriteW,sprite.getMinV()+textures[2].maxV*spriteH).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x1, y2, z1).tex(sprite.getMinU()+textures[2].minU*spriteW,sprite.getMinV()+textures[2].maxV*spriteH).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		//SOUTH FACE
		b.pos(x1, y1, z2).tex(sprite.getMinU()+textures[3].minU*spriteW,sprite.getMinV()+textures[3].minV*spriteH).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x2, y1, z2).tex(sprite.getMinU()+textures[3].maxU*spriteW,sprite.getMinV()+textures[3].minV*spriteH).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x2, y2, z2).tex(sprite.getMinU()+textures[3].maxU*spriteW,sprite.getMinV()+textures[3].maxV*spriteH).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x1, y2, z2).tex(sprite.getMinU()+textures[3].minU*spriteW,sprite.getMinV()+textures[3].maxV*spriteH).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		//WEST FACE
		b.pos(x1, y1, z1).tex(sprite.getMinU()+textures[4].minU*spriteW,sprite.getMinV()+textures[4].minV*spriteH).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y1, z2).tex(sprite.getMinU()+textures[4].maxU*spriteW,sprite.getMinV()+textures[4].minV*spriteH).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y2, z2).tex(sprite.getMinU()+textures[4].maxU*spriteW,sprite.getMinV()+textures[4].maxV*spriteH).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y2, z1).tex(sprite.getMinU()+textures[4].minU*spriteW,sprite.getMinV()+textures[4].maxV*spriteH).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		//EAST FACE
		b.pos(x2, y1, z1).tex(sprite.getMinU()+textures[5].minU*spriteW,sprite.getMinV()+textures[5].minV*spriteH).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y1, z2).tex(sprite.getMinU()+textures[5].maxU*spriteW,sprite.getMinV()+textures[5].minV*spriteH).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y2, z2).tex(sprite.getMinU()+textures[5].maxU*spriteW,sprite.getMinV()+textures[5].maxV*spriteH).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y2, z1).tex(sprite.getMinU()+textures[5].minU*spriteW,sprite.getMinV()+textures[5].maxV*spriteH).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
	}

	public static void addRune(BufferBuilder buf, double x, double y, double z, double minU, double minV, double maxU, double maxV, float r, float g, float b, float a){
		int lightx = 0xF000F0;
        int lighty = 0xF000F0;
		buf.pos(x, y, z).tex(minU,minV).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+1, y, z).tex(maxU,minV).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+1, y, z+1).tex(maxU,maxV).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x, y, z+1).tex(minU,maxV).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
	}

	public static void addBoxExt(BufferBuilder b, double x1, double y1, double z1, double x2, double y2, double z2, StructUV[] textures, int[] inversions, boolean[] faceToggles){
		//BOTTOM FACE
		if (faceToggles[0]){
			b.pos(x1, y1, z1).tex(textures[0].minU,textures[0].minV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
			b.pos(x1, y1, z2).tex(textures[0].maxU,textures[0].minV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
			b.pos(x2, y1, z2).tex(textures[0].maxU,textures[0].maxV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
			b.pos(x2, y1, z1).tex(textures[0].minU,textures[0].maxV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		}
		//TOP FACE
		if (faceToggles[1]){
			b.pos(x1, y2, z1).tex(textures[1].minU,textures[1].minV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
			b.pos(x1, y2, z2).tex(textures[1].maxU,textures[1].minV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
			b.pos(x2, y2, z2).tex(textures[1].maxU,textures[1].maxV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
			b.pos(x2, y2, z1).tex(textures[1].minU,textures[1].maxV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		}
		//NORTH FACE
		if (faceToggles[2]){
			b.pos(x1, y1, z1).tex(textures[2].minU,textures[2].minV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
			b.pos(x2, y1, z1).tex(textures[2].maxU,textures[2].minV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
			b.pos(x2, y2, z1).tex(textures[2].maxU,textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
			b.pos(x1, y2, z1).tex(textures[2].minU,textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		}
		//SOUTH FACE
		if (faceToggles[3]){
			b.pos(x1, y1, z2).tex(textures[3].minU,textures[3].minV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
			b.pos(x2, y1, z2).tex(textures[3].maxU,textures[3].minV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
			b.pos(x2, y2, z2).tex(textures[3].maxU,textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
			b.pos(x1, y2, z2).tex(textures[3].minU,textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		}
		//WEST FACE
		if (faceToggles[4]){
			b.pos(x1, y1, z1).tex(textures[4].minU,textures[4].minV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
			b.pos(x1, y1, z2).tex(textures[4].maxU,textures[4].minV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
			b.pos(x1, y2, z2).tex(textures[4].maxU,textures[4].maxV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
			b.pos(x1, y2, z1).tex(textures[4].minU,textures[4].maxV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		}
		//EAST FACE
		if (faceToggles[5]){
			b.pos(x2, y1, z1).tex(textures[5].minU,textures[5].minV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
			b.pos(x2, y1, z2).tex(textures[5].maxU,textures[5].minV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
			b.pos(x2, y2, z2).tex(textures[5].maxU,textures[5].maxV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
			b.pos(x2, y2, z1).tex(textures[5].minU,textures[5].maxV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void drawQuadGui(BufferBuilder vertexbuffer, float z, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float minU, float minV, float maxU, float maxV, float r, float g, float b, float a){
		float f = 0.00390625F;
        float f1 = 0.00390625F;
        vertexbuffer.pos(x1 + 0.0F, y1 + 0.0F, z).tex(minU,maxV).color(r, g, b, a).endVertex();
        vertexbuffer.pos(x2 + 0.0F, y2 + 0.0F, z).tex(maxU, maxV).color(r, g, b, a).endVertex();
        vertexbuffer.pos(x3 + 0.0F, y3 + 0.0F, z).tex(maxU, minV).color(r, g, b, a).endVertex();
        vertexbuffer.pos(x4 + 0.0F, y4 + 0.0F, z).tex(minU, minV).color(r, g, b, a).endVertex();
    }
	
	@SideOnly(Side.CLIENT)
	public static void drawQuadGuiExt(BufferBuilder vertexbuffer, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int minU, int minV, int maxU, int maxV, int texW, int texH){
		float mU = (float)minU / (float)texW;
		float mV = (float)minV / (float)texH;
		float xU = (float)maxU / (float)texW;
		float xV = (float)maxV / (float)texH;
        vertexbuffer.pos(x1 + 0.0F, y1 + 0.0F, 0).tex(mU,xV).endVertex();
        vertexbuffer.pos(x2 + 0.0F, y2 + 0.0F, 0).tex(xU, xV).endVertex();
        vertexbuffer.pos(x3 + 0.0F, y3 + 0.0F, 0).tex(xU, mV).endVertex();
        vertexbuffer.pos(x4 + 0.0F, y4 + 0.0F, 0).tex(mU, mV).endVertex();
    }
	
	public static void renderChunkUniforms(RenderChunk c){
		if (ShaderUtil.currentProgram == ShaderUtil.lightProgram){
			BlockPos pos = c.getPosition();
			int chunkX = GL20.glGetUniformLocation(ShaderUtil.lightProgram, "chunkX");
			int chunkY = GL20.glGetUniformLocation(ShaderUtil.lightProgram, "chunkY");
			int chunkZ = GL20.glGetUniformLocation(ShaderUtil.lightProgram, "chunkZ");
			GL20.glUniform1i(chunkX, pos.getX());
			GL20.glUniform1i(chunkY, pos.getY());
			GL20.glUniform1i(chunkZ, pos.getZ());
		}
	}

	@SideOnly(Side.CLIENT)
	public static void drawColorRectBatched(BufferBuilder vertexbuffer, double x, double y, double zLevel, double widthIn, double heightIn,
			float r1, float g1, float b1, float a1,
			float r2, float g2, float b2, float a2,
			float r3, float g3, float b3, float a3,
			float r4, float g4, float b4, float a4)
    {
        vertexbuffer.pos((double)(x + 0), (double)(y + heightIn), (double)zLevel).color(r1, g1, b1, a1).endVertex();
        vertexbuffer.pos((double)(x + widthIn), (double)(y + heightIn), (double)zLevel).color(r2, g2, b2, a2).endVertex();
        vertexbuffer.pos((double)(x + widthIn), (double)(y + 0), (double)zLevel).color(r3, g3, b3, a3).endVertex();
        vertexbuffer.pos((double)(x + 0), (double)(y + 0), (double)zLevel).color(r4, g4, b4, a4).endVertex();
    }
}
