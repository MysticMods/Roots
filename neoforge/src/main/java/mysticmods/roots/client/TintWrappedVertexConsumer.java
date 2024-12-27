package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class TintWrappedVertexConsumer implements VertexConsumer {
  private final VertexConsumer wrapped;
  private final float red;
  private final float green;
  private final float blue;
  private final float alpha;

  public TintWrappedVertexConsumer(VertexConsumer wrapped, float red, float green, float blue, float alpha) {
    this.wrapped = wrapped;
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.alpha = alpha;
  }

  @Override
  public VertexConsumer vertex(double pX, double pY, double pZ) {
    return wrapped.vertex(pX, pY, pZ);
  }

  @Override
  public VertexConsumer color(int pRed, int pGreen, int pBlue, int pAlpha) {
    return wrapped.color((int) (pRed * this.red), (int) (pGreen * this.green), (int) (pBlue * this.blue), (int) (pAlpha * this.alpha));
  }

  @Override
  public VertexConsumer uv(float pU, float pV) {
    return wrapped.uv(pU, pV);
  }

  @Override
  public VertexConsumer overlayCoords(int pU, int pV) {
    return wrapped.overlayCoords(pU, pV);
  }

  @Override
  public VertexConsumer uv2(int pU, int pV) {
    return wrapped.uv2(pU, pV);
  }

  @Override
  public VertexConsumer normal(float pX, float pY, float pZ) {
    return wrapped.normal(pX, pY, pZ);
  }

  @Override
  public void endVertex() {
    wrapped.endVertex();
  }

  @Override
  public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA) {
    wrapped.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
  }

  @Override
  public void unsetDefaultColor() {
    wrapped.unsetDefaultColor();
  }
}
