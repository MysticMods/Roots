package mysticmods.roots.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.RootsRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Iterator;
import java.util.Set;

public class BeamManager {
  private static final Set<Beam> BEAMS = new ObjectLinkedOpenHashSet<>();

  public static void addBeam(Beam beam) {
    synchronized (BEAMS) {
      BEAMS.add(beam);
    }
  }

  public static void tick() {
    synchronized (BEAMS) {
      Iterator<Beam> iterator = BEAMS.iterator();
      while (iterator.hasNext()) {
        Beam beam = iterator.next();
        beam.tick();
        if (beam.isRemoved()) {
          iterator.remove();
        }
      }
    }
  }

  public static void render(float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn) {
    render(partialTicks, matrixStack, bufferIn, null);
  }

  private static final ResourceLocation BEAM_TEXTURE = RootsAPI.rl("textures/misc/beam_gold.png");

  public static void render(float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, @Nullable Vec3 cameraPos) {
    VertexConsumer buffer = bufferIn.getBuffer(RootsRenderTypes.ROOTS_BEAM.apply(BEAM_TEXTURE)); // Let's just go with this
    Matrix4f matrix = matrixStack.last().pose();
    synchronized (BEAMS) {
      for (Beam beam : BEAMS) {
        renderBeam(buffer, beam, matrix, cameraPos, partialTicks);
      }
    }
  }

  public static void renderBeam(VertexConsumer consumer, Beam beam, Matrix4f matrix, @Nullable Vec3 cameraPos, float partialTicks) {
    Beam.BeamColor style = beam.getStyle();

    Vec3 start = cameraPos == null ? beam.getStart(partialTicks) : beam.getStart(partialTicks).subtract(cameraPos);
    Vec3 stop = cameraPos == null ? beam.getStop(partialTicks) : beam.getStop(partialTicks).subtract(cameraPos);
    Vec3 subtracted = start.subtract(stop);
    double d0 = subtracted.length();
    double d22 = -0.5;
    double d23 = d0 * 5.0d + d22;
    double r = 0.08d; // get width from the beam

    double separation = 0.001;
    Vec3 offset = subtracted.normalize().scale(separation);

    // Get two stable perpendicular vectors to the beam
    Vec3 base1 = subtracted.cross(new Vec3(0, 1, 0));
    if (base1.lengthSqr() == 0) {
      base1 = subtracted.cross(new Vec3(1, 0, 0));
    }
    base1 = base1.normalize();
    Vec3 base2 = subtracted.cross(base1).normalize();

    // Scale both
    base1 = base1.multiply(r, r, r);
    base2 = base2.multiply(r, r, r);

    // Rotate both by ±45° around beam axis (combine to get X shape)
    Vec3 cross1 = base1.add(base2).normalize().multiply(r * Math.sqrt(2), r * Math.sqrt(2), r * Math.sqrt(2));
    Vec3 cross2 = base1.subtract(base2).normalize().multiply(r * Math.sqrt(2), r * Math.sqrt(2), r * Math.sqrt(2));

    Vec3 a = start.add(cross1).add(offset);
    Vec3 b = stop.add(cross1).add(offset);
    Vec3 c = stop.subtract(cross1).add(offset);
    Vec3 d = start.subtract(cross1).add(offset);

    consumer.addVertex(matrix, (float) a.x, (float) a.y, (float) a.z)
        .setColor(style.getRed(), style.getGreen(), style.getBlue(), 0)
        .setUv(1, (float) d22)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, 1, 0);
    consumer.addVertex(matrix, (float) b.x, (float) b.y, (float) b.z)
        .setColor(style.getRed(), style.getGreen(), style.getBlue(), style.getAlpha()).setUv(1, (float) d22)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, 1, 0);

    consumer.addVertex(matrix, (float) c.x, (float) c.y, (float) c.z)
        .setColor(style.getRed(), style.getGreen(), style.getBlue(), style.getAlpha()).setUv(0, (float) d23)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, 1, 0);
    consumer.addVertex(matrix, (float) d.x, (float) d.y, (float) d.z)
        .setColor(style.getRed(), style.getGreen(), style.getBlue(), 0).setUv(0, (float) d23)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, 1, 0);

    Vec3 e = start.add(cross2).subtract(offset);
    Vec3 f = stop.add(cross2).subtract(offset);
    Vec3 g = stop.subtract(cross2).subtract(offset);
    Vec3 h = start.subtract(cross2).subtract(offset);

    consumer.addVertex(matrix, (float) e.x, (float) e.y, (float) e.z)
        .setColor(style.getRed(), style.getGreen(), style.getBlue(), 0).setUv(1, (float) d22)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, 1, 0);
    consumer.addVertex(matrix, (float) f.x, (float) f.y, (float) f.z)
        .setColor(style.getRed(), style.getGreen(), style.getBlue(), style.getAlpha()).setUv(1, (float) d22)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, 1, 0);

    consumer.addVertex(matrix, (float) g.x, (float) g.y, (float) g.z)
        .setColor(style.getRed(), style.getGreen(), style.getBlue(), style.getAlpha()).setUv(0, (float) d23)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, 1, 0);
    consumer.addVertex(matrix, (float) h.x, (float) h.y, (float) h.z)
        .setColor(style.getRed(), style.getGreen(), style.getBlue(), 0).setUv(0, (float) d23)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, 1, 0);
  }
}
