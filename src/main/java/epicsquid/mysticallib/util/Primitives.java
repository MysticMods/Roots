package epicsquid.mysticallib.util;

import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.struct.Vec4d;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class Primitives {

  public static void putVertex(@Nonnull World world, @Nonnull List<Float> data, @Nonnull List<Float> lmapData, float x, float y, float z) {
    data.add(x);
    data.add(y);
    data.add(z);
    int i = getBrightnessForPos(world, x, y, z);
    int j = i % 65536;
    int k = i / 65536;
    lmapData.add((float) j);
    lmapData.add((float) k);
  }

  public static void putUV(@Nonnull List<Float> data, float u, float v) {
    data.add(u);
    data.add(v);
  }

  public static void putColor(@Nonnull List<Float> data, float r, float g, float b, float a) {
    data.add(r);
    data.add(g);
    data.add(b);
    data.add(a);
  }

  public static void putNormal(@Nonnull List<Float> data, float nx, float ny, float nz) {
    data.add(nx);
    data.add(ny);
    data.add(nz);
  }

  public static int getBrightnessForPos(@Nonnull World world, float x, float y, float z) {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor(x), 0, MathHelper.floor(z));
    if (world.isBlockLoaded(pos)) {
      pos.setY(MathHelper.floor(y));
      return world.getCombinedLight(pos, 0);
    }
    return 0;
  }

  public static void addCubeToBuffer(@Nonnull BufferBuilder buff, double x1, double y1, double z1, double x2, double y2, double z2, @Nonnull Vec4d[] uv,
      float r, float g, float b, float a, boolean north, boolean south, boolean up, boolean down, boolean east, boolean west) {
    if (north) {
      buff.pos(x1, y1, z1).tex(uv[0].x, uv[0].y).color(r, g, b, a).normal(0, 0, -1.0f).endVertex();
      buff.pos(x2, y1, z1).tex(uv[0].x + uv[0].z, uv[0].y).color(r, g, b, a).normal(0, 0, -1.0f).endVertex();
      buff.pos(x2, y2, z1).tex(uv[0].x + uv[0].z, uv[0].y + uv[0].w).color(r, g, b, a).normal(0, 0, -1.0f).endVertex();
      buff.pos(x1, y2, z1).tex(uv[0].x, uv[0].y + uv[0].w).color(r, g, b, a).normal(0, 0, -1.0f).endVertex();
    }
    if (south) {
      buff.pos(x2, y1, z2).tex(uv[1].x, uv[1].y).color(r, g, b, a).normal(0, 0, 1.0f).endVertex();
      buff.pos(x1, y1, z2).tex(uv[1].x + uv[1].z, uv[1].y).color(r, g, b, a).normal(0, 0, 1.0f).endVertex();
      buff.pos(x1, y2, z2).tex(uv[1].x + uv[1].z, uv[1].y + uv[1].w).color(r, g, b, a).normal(0, 0, 1.0f).endVertex();
      buff.pos(x2, y2, z2).tex(uv[1].x, uv[1].y + uv[1].w).color(r, g, b, a).normal(0, 0, 1.0f).endVertex();
    }
    if (up) {
      buff.pos(x1, y2, z1).tex(uv[2].x, uv[2].y).color(r, g, b, a).normal(0, 1.0f, 0).endVertex();
      buff.pos(x2, y2, z1).tex(uv[2].x + uv[2].z, uv[2].y).color(r, g, b, a).normal(0, 1.0f, 0).endVertex();
      buff.pos(x2, y2, z2).tex(uv[2].x + uv[2].z, uv[2].y + uv[2].w).color(r, g, b, a).normal(0, 1.0f, 0).endVertex();
      buff.pos(x1, y2, z2).tex(uv[2].x, uv[2].y + uv[2].w).color(r, g, b, a).normal(0, 1.0f, 0).endVertex();
    }
    if (down) {
      buff.pos(x2, y1, z1).tex(uv[3].x, uv[3].y).color(r, g, b, a).normal(0, -1.0f, 0).endVertex();
      buff.pos(x1, y1, z1).tex(uv[3].x + uv[3].z, uv[3].y).color(r, g, b, a).normal(0, -1.0f, 0).endVertex();
      buff.pos(x1, y1, z2).tex(uv[3].x + uv[3].z, uv[3].y + uv[3].w).color(r, g, b, a).normal(0, -1.0f, 0).endVertex();
      buff.pos(x2, y1, z2).tex(uv[3].x, uv[3].y + uv[3].w).color(r, g, b, a).normal(0, -1.0f, 0).endVertex();
    }
    if (east) {
      buff.pos(x2, y1, z2).tex(uv[4].x, uv[4].y).color(r, g, b, a).normal(1.0f, 0, 0).endVertex();
      buff.pos(x2, y1, z1).tex(uv[4].x + uv[4].z, uv[4].y).color(r, g, b, a).normal(1.0f, 0, 0).endVertex();
      buff.pos(x2, y2, z1).tex(uv[4].x + uv[4].z, uv[4].y + uv[4].w).color(r, g, b, a).normal(1.0f, 0, 0).endVertex();
      buff.pos(x2, y2, z2).tex(uv[4].x, uv[4].y + uv[4].w).color(r, g, b, a).normal(1.0f, 0, 0).endVertex();
    }
    if (west) {
      buff.pos(x1, y1, z1).tex(uv[5].x, uv[5].y).color(r, g, b, a).normal(-1.0f, 0, 0).endVertex();
      buff.pos(x1, y1, z2).tex(uv[5].x + uv[5].z, uv[5].y).color(r, g, b, a).normal(-1.0f, 0, 0).endVertex();
      buff.pos(x1, y2, z2).tex(uv[5].x + uv[5].z, uv[5].y + uv[5].w).color(r, g, b, a).normal(-1.0f, 0, 0).endVertex();
      buff.pos(x1, y2, z1).tex(uv[5].x, uv[5].y + uv[5].w).color(r, g, b, a).normal(-1.0f, 0, 0).endVertex();
    }
  }

  public static void addCubeToBuffer(@Nonnull BufferBuilder buff, double x1, double y1, double z1, double x2, double y2, double z2, @Nonnull Vec4d[] uv,
      float r, float g, float b, float a, boolean north, boolean south, boolean up, boolean down, boolean east, boolean west, int lightx, int lighty) {
    if (north) {
      buff.pos(x1, y1, z1).tex(uv[0].x, uv[0].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y1, z1).tex(uv[0].x + uv[0].z, uv[0].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y2, z1).tex(uv[0].x + uv[0].z, uv[0].y + uv[0].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y2, z1).tex(uv[0].x, uv[0].y + uv[0].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
    }
    if (south) {
      buff.pos(x2, y1, z2).tex(uv[1].x, uv[1].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y1, z2).tex(uv[1].x + uv[1].z, uv[1].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y2, z2).tex(uv[1].x + uv[1].z, uv[1].y + uv[1].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y2, z2).tex(uv[1].x, uv[1].y + uv[1].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
    }
    if (up) {
      buff.pos(x1, y2, z1).tex(uv[2].x, uv[2].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y2, z1).tex(uv[2].x + uv[2].z, uv[2].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y2, z2).tex(uv[2].x + uv[2].z, uv[2].y + uv[2].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y2, z2).tex(uv[2].x, uv[2].y + uv[2].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
    }
    if (down) {
      buff.pos(x2, y1, z1).tex(uv[3].x, uv[3].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y1, z1).tex(uv[3].x + uv[3].z, uv[3].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y1, z2).tex(uv[3].x + uv[3].z, uv[3].y + uv[3].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y1, z2).tex(uv[3].x, uv[3].y + uv[3].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
    }
    if (east) {
      buff.pos(x2, y1, z2).tex(uv[4].x, uv[4].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y1, z1).tex(uv[4].x + uv[4].z, uv[4].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y2, z1).tex(uv[4].x + uv[4].z, uv[4].y + uv[4].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x2, y2, z2).tex(uv[4].x, uv[4].y + uv[4].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
    }
    if (west) {
      buff.pos(x1, y1, z1).tex(uv[5].x, uv[5].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y1, z2).tex(uv[5].x + uv[5].z, uv[5].y).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y2, z2).tex(uv[5].x + uv[5].z, uv[5].y + uv[5].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
      buff.pos(x1, y2, z1).tex(uv[5].x, uv[5].y + uv[5].w).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
    }
  }
}
