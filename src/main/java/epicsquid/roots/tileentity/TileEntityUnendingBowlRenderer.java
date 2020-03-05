/*
package epicsquid.roots.tileentity;

import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.util.ColorLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityUnendingBowlRenderer extends TileEntitySpecialRenderer<TileEntityUnendingBowl> {
  private static FluidStack fluidStack = null;

  private static Vec3d corner1 = new Vec3d(3, 4.5, 3);

  @Override
  public void render(TileEntityUnendingBowl te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    if (te != null) {
      if (this.rendererDispatcher == null) {
        this.setRendererDispatcher(TileEntityRendererDispatcher.instance);
      }

      if (fluidStack == null) {
        Fluid fluid = GeneralConfig.getFluid();
        if (fluid == null) {
          fluid = FluidRegistry.getFluid("water");
        }
        fluidStack = new FluidStack(fluid, Integer.MAX_VALUE);
      }

      GlStateManager.pushMatrix();
      GlStateManager.pushMatrix();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

*/
/*      if (Minecraft.isAmbientOcclusionEnabled()) {
        GL11.glShadeModel(GL11.GL_SMOOTH);
      } else {
        GL11.glShadeModel(GL11.GL_FLAT);
      }*//*


      Minecraft mc = Minecraft.getMinecraft();
      TextureAtlasSprite top = mc.getTextureMapBlocks().getTextureExtry(fluidStack.getFluid().getStill(fluidStack).toString());
      TextureAtlasSprite side = mc.getTextureMapBlocks().getTextureExtry(fluidStack.getFluid().getFlowing(fluidStack).toString());

      if (top == null && side == null) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        return;
      } else if (top == null) {
        top = side;
      }

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.getBuffer();
      BlockPos pos = te.getPos();
      int light = mc.world.getCombinedLight(pos, fluidStack.getFluid().getLuminosity());
      int color = fluidStack.getFluid().getColor(fluidStack);

      double sx = (pos.getX() - TileEntityRendererDispatcher.staticPlayerX);
      double sy = (pos.getY() - TileEntityRendererDispatcher.staticPlayerY);
      double sz = (pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);

      //GlStateManager.translate(sx, sy, sz);

      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
      mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

      ColorLight colour = new ColorLight(color, light);
      int r = colour.red;
      int g = colour.green;
      int b = colour.blue;
      int a = colour.alpha;
      int l1 = colour.light1;
      int l2 = colour.light2;

      double minU = top.getInterpolatedU(0);
      double maxU = top.getInterpolatedU(0);
      double minV = top.getInterpolatedV(10);
      double maxV = top.getInterpolatedV(10);

      buffer.setTranslation(sx, sy, sz);
      buffer.pos(3 / 16.0, 4.5 / 16.0, 3 / 16.0).color(r, g, b, a).tex(minU, minV).lightmap(l1, l2).endVertex();
      buffer.pos(3 / 16.0, 4.5 / 16.0, 13 / 16.0).color(r, g, b, a).tex(maxU, minV).lightmap(l1, l2).endVertex();
      buffer.pos(13 / 16.0, 4.5 / 16.0, 13 / 16.0).color(r, g, b, a).tex(minU, maxV).lightmap(l1, l2).endVertex();
      buffer.pos(13 / 16.0, 4.5 / 16.0, 3 / 16.0).color(r, g, b, a).tex(maxU, maxV).lightmap(l1, l2).endVertex();
      buffer.setTranslation(0, 0, 0);

      tessellator.draw();

      GlStateManager.color(1f, 1f, 1f, 1f);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
      RenderHelper.enableStandardItemLighting();
      GlStateManager.popMatrix();
    }
  }
}*/
