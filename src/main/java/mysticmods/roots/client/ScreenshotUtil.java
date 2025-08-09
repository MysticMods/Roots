package mysticmods.roots.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

  public static void dump(RenderTarget rt) {
    RenderSystem.assertOnRenderThread();

    final Minecraft mc = Minecraft.getInstance();
    final Path screenshotsDir = mc.gameDirectory.toPath().resolve("screenshots");
    try {
      Files.createDirectories(screenshotsDir);
    } catch (IOException ignored) {
    }
    final String ts = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());

    // ----- COLOR -----
    NativeImage colorImg = readColorRGBA(rt);
    try {
      colorImg.writeToFile(screenshotsDir.resolve(ts + "_color.png"));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      colorImg.close();
    }

    // ----- DEPTH -----
    NativeImage depthImg = readDepthGrayscale(rt);
    try {
      depthImg.writeToFile(screenshotsDir.resolve(ts + "_depth.png"));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      depthImg.close();
    }
  }

  // Reads RGBA8 from the RT color texture and writes to NativeImage via setPixelRGBA (ABGR int)
  private static NativeImage readColorRGBA(RenderTarget rt) {
    final int w = rt.width, h = rt.height;
    final int bpp = 4;

    ByteBuffer tmp = MemoryUtil.memAlloc(w * h * bpp);

    int prevTex = GL11C.glGetInteger(GL11C.GL_TEXTURE_BINDING_2D);
    int prevPack = GL11C.glGetInteger(GL11C.GL_PACK_ALIGNMENT);

    try {
      GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, rt.getColorTextureId());
      GL11C.glPixelStorei(GL11C.GL_PACK_ALIGNMENT, 1);

      // Read back as RGBA8
      GL11C.glGetTexImage(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, tmp);

      NativeImage img = new NativeImage(NativeImage.Format.RGBA, w, h, false);

      // Flip Y: GL origin bottom-left, NativeImage top-left
      for (int y = 0; y < h; y++) {
        int srcRow = (h - 1 - y) * w * bpp;
        for (int x = 0; x < w; x++) {
          int i = srcRow + x * bpp;
          int r = tmp.get(i) & 0xFF;
          int g = tmp.get(i + 1) & 0xFF;
          int b = tmp.get(i + 2) & 0xFF;
          int a = tmp.get(i + 3) & 0xFF;

          // NativeImage#setPixelRGBA expects ABGR layout in the int
          int abgr = (a << 24) | (b << 16) | (g << 8) | r;
          img.setPixelRGBA(x, y, abgr);
        }
      }
      return img;
    } finally {
      GL11C.glPixelStorei(GL11C.GL_PACK_ALIGNMENT, prevPack);
      GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, prevTex);
      MemoryUtil.memFree(tmp);
    }
  }

  // Reads depth as GL_FLOAT and writes an 8-bit grayscale preview via setPixelRGBA
  private static NativeImage readDepthGrayscale(RenderTarget rt) {
    final int w = rt.width, h = rt.height;

    FloatBuffer tmp = MemoryUtil.memAllocFloat(w * h);

    int prevTex = GL11C.glGetInteger(GL11C.GL_TEXTURE_BINDING_2D);
    int prevPack = GL11C.glGetInteger(GL11C.GL_PACK_ALIGNMENT);

    try {
      int depthTex = rt.getDepthTextureId();
      GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, depthTex);
      GL11C.glPixelStorei(GL11C.GL_PACK_ALIGNMENT, 1);

      // If it's a combined depth-stencil texture, ensure depth read mode:
      // GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL30C.GL_DEPTH_STENCIL_TEXTURE_MODE, GL11C.GL_DEPTH_COMPONENT);

      GL11C.glGetTexImage(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_DEPTH_COMPONENT, GL11C.GL_FLOAT, tmp);

      NativeImage img = new NativeImage(NativeImage.Format.RGBA, w, h, false);

      for (int y = 0; y < h; y++) {
        int srcRow = (h - 1 - y) * w; // flip Y
        for (int x = 0; x < w; x++) {
          float z = tmp.get(srcRow + x);

          // Clamp and map non-linear depth to 0..255; for linear eye-space you’d convert here
          int g = (int) (Math.max(0f, Math.min(1f, z)) * 255f + 0.5f);

          // ABGR int with grayscale replicated and A=255
          int abgr = (0xFF << 24) | (g << 16) | (g << 8) | g;
          img.setPixelRGBA(x, y, abgr);
        }
      }
      return img;
    } finally {
      GL11C.glPixelStorei(GL11C.GL_PACK_ALIGNMENT, prevPack);
      GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, prevTex);
      MemoryUtil.memFree(tmp);
    }
  }
}
