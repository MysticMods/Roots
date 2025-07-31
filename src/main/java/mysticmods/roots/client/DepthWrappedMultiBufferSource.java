package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class DepthWrappedMultiBufferSource implements MultiBufferSource {
  private final MultiBufferSource.BufferSource delegate;

  protected DepthWrappedMultiBufferSource(MultiBufferSource.BufferSource delegate) {
    this.delegate = delegate;
  }

  @Override
  public VertexConsumer getBuffer(RenderType renderType) {
    return delegate.getBuffer(RootsRenderTypes.getDissolveDepth(renderType));
  }
}
