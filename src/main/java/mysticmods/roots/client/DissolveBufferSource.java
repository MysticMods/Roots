package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class DissolveBufferSource implements MultiBufferSource {
  private final MultiBufferSource.BufferSource delegate;

  public DissolveBufferSource(BufferSource delegate) {
    this.delegate = delegate;
  }

  @Override
  public VertexConsumer getBuffer(RenderType renderType) {
    return delegate.getBuffer(RootsRenderTypes.DISSOLVE);
  }
}

