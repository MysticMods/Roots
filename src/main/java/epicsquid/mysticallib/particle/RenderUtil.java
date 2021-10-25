package epicsquid.mysticallib.particle;

import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.struct.Vec4f;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// This code originally derived from the Public Domain code of EnderIO for 1.12.2
// https://github.com/SleepyTrousers/EnderIO

@Mod.EventBusSubscriber(modid = MysticalLib.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class RenderUtil {
  private static TextureAtlasSprite whiteTexture;

  public static void renderBoundingBox(@Nonnull AxisAlignedBB bb, @Nonnull Vec4f color) {
    final float minU = whiteTexture.getMinU();
    final float maxU = whiteTexture.getMaxU();
    final float minV = whiteTexture.getMinV();
    final float maxV = whiteTexture.getMaxV();
    final BufferBuilder tes = Tessellator.getInstance().getBuffer();
    tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    for (EnumFacing e : EnumFacing.values()) {
      for (Vertex v : getCornersWithUvForFace(bb, e, minU, maxU, minV, maxV)) {
        tes.pos(v.x(), v.y(), v.z()).tex(v.u(), v.v()).color(color.x, color.y, color.z, color.w).endVertex();
      }
    }
    Tessellator.getInstance().draw();
  }

  private static List<Vertex> getCornersWithUvForFace(@Nonnull AxisAlignedBB bb, @Nonnull EnumFacing face, float minU, float maxU, float minV, float maxV) {
    List<Vertex> result = new ArrayList<>();
    switch (face) {
      case NORTH:
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.minZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.minZ), new Vec2f(maxU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.minZ), new Vec2f(maxU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.minZ), new Vec2f(minU, maxV)));
        break;
      case SOUTH:
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.maxZ), new Vec2f(maxU, minV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.maxZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.maxZ), new Vec2f(maxU, maxV)));
        break;
      case EAST:
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.minZ), new Vec2f(maxU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.maxZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.minZ), new Vec2f(maxU, minV)));
        break;
      case WEST:
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.minZ), new Vec2f(maxU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.maxZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.minZ), new Vec2f(maxU, maxV)));
        break;
      case UP:
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.minZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.minZ), new Vec2f(maxU, maxV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.maxZ), new Vec2f(maxU, minV)));
        break;
      case DOWN:
      default:
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.minZ), new Vec2f(maxU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.minZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.maxZ), new Vec2f(maxU, minV)));
        break;
    }
    return result;
  }

  public static class Vertex {

    private Vec3d xyz;
    private Vec2f uv;

    public Vertex(Vec3d xyz, Vec2f uv) {
      this.xyz = xyz;
      this.uv = uv;
    }

    public double x() {
      return xyz.x;
    }

    public double y() {
      return xyz.y;
    }

    public double z() {
      return xyz.z;
    }

    public float u() {
      return uv.x;
    }

    public float v() {
      return uv.y;
    }
  }


  @SubscribeEvent
  public static void onIconLoad(TextureStitchEvent.Pre event) {
    final TextureMap map = event.getMap();
    if (map != null) {
      whiteTexture = map.registerSprite(new ResourceLocation(MysticalLib.MODID, "white"));
    }
  }

  public interface IRanged {

    @SideOnly(Side.CLIENT)
    boolean isShowingRange();

    @SideOnly(Side.CLIENT)
    void toggleShowRange();

    @SideOnly(Side.CLIENT)
    void setShowingRange(boolean showingRange);

    @Nonnull
    AxisAlignedBB getBounds();
  }
}
