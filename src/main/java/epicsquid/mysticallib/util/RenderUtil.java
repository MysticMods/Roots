package epicsquid.mysticallib.util;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.MysticalLib;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderUtil {

  public static ResourceLocation beam_texture = new ResourceLocation(MysticalLib.MODID + ":textures/effect/beam.png");
  public static ResourceLocation glow_texture = new ResourceLocation(MysticalLib.MODID + ":textures/effect/glow.png");
  public static int maxLightX = 0xF000F0;
  public static int maxLightY = 0xF000F0;

  public static void renderBeam(@Nonnull BufferBuilder buf, double x1, double y1, double z1, double x2, double y2, double z2, float r1, float g1, float b1,
      float a1, float r2, float g2, float b2, float a2, double width, double angle) {
    float rads = (float) Math.toRadians(angle);
    double ac = MathHelper.cos(rads);
    double as = MathHelper.sin(rads);
    float yaw = (float) Math.atan2(x2 - x1, z2 - z1);
    float pitch = (float) Math.atan2(y2 - y1, (double) MathHelper.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2)));

    double tX1 = width * (double) MathHelper.cos(yaw);
    double tY1 = 0;
    double tZ1 = -width * (double) MathHelper.sin(yaw);

    double tX2 = width * (double) MathHelper.sin(yaw) * -(double) MathHelper.sin(pitch);
    double tY2 = width * (double) MathHelper.cos(pitch);
    double tZ2 = width * (double) MathHelper.cos(yaw) * -(double) MathHelper.sin(pitch);

    double tXc = tX1 * ac + tX2 * as;
    double tYc = tY1 * ac + tY2 * as;
    double tZc = tZ1 * ac + tZ2 * as;
    double tXs = tX1 * -as + tX2 * ac;
    double tYs = tY1 * -as + tY2 * ac;
    double tZs = tZ1 * -as + tZ2 * ac;

    buf.pos((x1 - tXs), (y1 - tYs), (z1 - tZs)).tex(0.0, 0.0).lightmap(maxLightX, maxLightY)
        .color(r1, g1, b1, a1).endVertex();
    buf.pos((x2 - tXs), (y2 - tYs), (z2 - tZs)).tex(1.0, 0.0).lightmap(maxLightX, maxLightY)
        .color(r2, g2, b2, a2).endVertex();
    buf.pos((x2 + tXs), (y2 + tYs), (z2 + tZs)).tex(1.0, 1.0).lightmap(maxLightX, maxLightY)
        .color(r2, g2, b2, a2).endVertex();
    buf.pos((x1 + tXs), (y1 + tYs), (z1 + tZs)).tex(0.0, 1.0).lightmap(maxLightX, maxLightY)
        .color(r1, g1, b1, a1).endVertex();

    buf.pos((x1 - tXc), (y1 - tYc), (z1 - tZc)).tex(0.0, 0.0).lightmap(maxLightX, maxLightY)
        .color(r1, g1, b1, a1).endVertex();
    buf.pos((x2 - tXc), (y2 - tYc), (z2 - tZc)).tex(1.0, 0.0).lightmap(maxLightX, maxLightY)
        .color(r2, g2, b2, a2).endVertex();
    buf.pos((x2 + tXc), (y2 + tYc), (z2 + tZc)).tex(1.0, 1.0).lightmap(maxLightX, maxLightY)
        .color(r2, g2, b2, a2).endVertex();
    buf.pos((x1 + tXc), (y1 + tYc), (z1 + tZc)).tex(0.0, 1.0).lightmap(maxLightX, maxLightY)
        .color(r1, g1, b1, a1).endVertex();
  }

  public static void renderSlash(@Nonnull BufferBuilder buf, double x0, double y0, double z0, float r, float g, float b, float a, float radius, float width,
      float angleRange) {
    for (float i = -angleRange / 2.0f; i < angleRange / 2.0f; i += angleRange / 16.0f) {
      float coeff1 = 1.0f - Math.abs(i) / (angleRange / 2.0f);
      float coeff2 = 1.0f - Math.abs(i + angleRange / 16.0f) / (angleRange / 2.0f);
      double x1 = x0 + radius * Math.sin(Math.toRadians(i));
      double z1 = z0 + radius * Math.cos(Math.toRadians(i));
      double x2 = x0 + (radius + 0.5f * coeff1 * width) * Math.sin(Math.toRadians(i));
      double z2 = z0 + (radius + 0.5f * coeff1 * width) * Math.cos(Math.toRadians(i));
      double x3 = x0 + (radius + 0.5f * coeff2 * width) * Math.sin(Math.toRadians(i + angleRange / 16.0f));
      double z3 = z0 + (radius + 0.5f * coeff2 * width) * Math.cos(Math.toRadians(i + angleRange / 16.0f));
      double x4 = x0 + radius * Math.sin(Math.toRadians(i + angleRange / 16.0f));
      double z4 = z0 + radius * Math.cos(Math.toRadians(i + angleRange / 16.0f));
      buf.pos(x1, y0, z1 - radius / 2.0f).tex(1, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x2, y0, z2 - radius / 2.0f).tex(1, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x3, y0, z3 - radius / 2.0f).tex(0, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      buf.pos(x4, y0, z4 - radius / 2.0f).tex(0, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      x1 = x0 + radius * Math.sin(Math.toRadians(i));
      z1 = z0 + radius * Math.cos(Math.toRadians(i));
      x2 = x0 + (radius - 0.5f * coeff1 * width) * Math.sin(Math.toRadians(i));
      z2 = z0 + (radius - 0.5f * coeff1 * width) * Math.cos(Math.toRadians(i));
      x3 = x0 + (radius - 0.5f * coeff2 * width) * Math.sin(Math.toRadians(i + angleRange / 16.0f));
      z3 = z0 + (radius - 0.5f * coeff2 * width) * Math.cos(Math.toRadians(i + angleRange / 16.0f));
      x4 = x0 + radius * Math.sin(Math.toRadians(i + angleRange / 16.0f));
      z4 = z0 + radius * Math.cos(Math.toRadians(i + angleRange / 16.0f));
      buf.pos(x1, y0, z1 - radius / 2.0f).tex(1, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x2, y0, z2 - radius / 2.0f).tex(1, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x3, y0, z3 - radius / 2.0f).tex(0, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      buf.pos(x4, y0, z4 - radius / 2.0f).tex(0, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      buf.pos(x1, y0, z1 - radius / 2.0f).tex(1, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x2, y0, z2 - radius / 2.0f).tex(1, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x3, y0, z3 - radius / 2.0f).tex(0, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      buf.pos(x4, y0, z4 - radius / 2.0f).tex(0, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      x1 = x0 + radius * Math.sin(Math.toRadians(i));
      z1 = z0 + radius * Math.cos(Math.toRadians(i));
      x2 = x0 + radius * Math.sin(Math.toRadians(i));
      z2 = z0 + radius * Math.cos(Math.toRadians(i));
      x3 = x0 + radius * Math.sin(Math.toRadians(i + angleRange / 16.0f));
      z3 = z0 + radius * Math.cos(Math.toRadians(i + angleRange / 16.0f));
      x4 = x0 + radius * Math.sin(Math.toRadians(i + angleRange / 16.0f));
      z4 = z0 + radius * Math.cos(Math.toRadians(i + angleRange / 16.0f));
      buf.pos(x1, y0, z1 - radius / 2.0f).tex(1, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x2, y0 - width * 0.5f * coeff1, z2 - radius / 2.0f).tex(1, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x3, y0 - width * 0.5f * coeff2, z3 - radius / 2.0f).tex(0, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      buf.pos(x4, y0, z4 - radius / 2.0f).tex(0, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      buf.pos(x1, y0, z1 - radius / 2.0f).tex(1, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x2, y0 + width * 0.5f * coeff1, z2 - radius / 2.0f).tex(1, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff1).endVertex();
      buf.pos(x3, y0 + width * 0.5f * coeff2, z3 - radius / 2.0f).tex(0, 0).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
      buf.pos(x4, y0, z4 - radius / 2.0f).tex(0, 1).lightmap(maxLightX, maxLightY).color(r, g, b, a * coeff2).endVertex();
    }
  }
}
