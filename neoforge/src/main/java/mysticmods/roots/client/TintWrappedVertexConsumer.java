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
  public VertexConsumer addVertex(float pX, float pY, float pZ) {
    return wrapped.addVertex(pX, pY, pZ);
  }

  @Override
  public VertexConsumer setColor(int pRed, int pGreen, int pBlue, int pAlpha) {
    return wrapped.setColor((int) (pRed * this.red), (int) (pGreen * this.green), (int) (pBlue * this.blue), (int) (pAlpha * this.alpha));
  }

  @Override
  public VertexConsumer setUv(float pU, float pV) {
    return wrapped.setUv(pU, pV);
  }

  @Override
  public VertexConsumer setUv1(int pU, int pV) {
    return wrapped.setUv1(pU, pV);
  }

  @Override
  public VertexConsumer setUv2(int pU, int pV) {
    return wrapped.setUv2(pU, pV);
  }

  @Override
  public VertexConsumer setNormal(float pX, float pY, float pZ) {
    return wrapped.setNormal(pX, pY, pZ);
  }
/*
  @Override
  public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA) {
    wrapped.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
  }*/

/*  @Override
  public void unsetDefaultColor() {
    wrapped.unsetDefaultColor();
  }*/
}
