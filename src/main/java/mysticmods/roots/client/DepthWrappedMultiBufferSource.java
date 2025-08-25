package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.mixin.client.accessor.AccessorMixinCompositeRenderType;
import mysticmods.roots.mixin.client.accessor.AccessorMixinCompositeState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class DepthWrappedMultiBufferSource implements MultiBufferSource {
  private final MultiBufferSource.BufferSource delegate;

  protected DepthWrappedMultiBufferSource(MultiBufferSource.BufferSource delegate) {
    this.delegate = delegate;
  }

  @Override
  public VertexConsumer getBuffer(RenderType renderType) {
    if (((AccessorMixinCompositeState)(Object)((AccessorMixinCompositeRenderType)renderType).rootsGetState()).rootsGetDepthTestState().equals(RenderType.EQUAL_DEPTH_TEST)) {
      return delegate.getBuffer(renderType);
    }

    // TODO: Properly handle incompatible render types
    return delegate.getBuffer(RootsRenderTypes.getDissolveDepth(renderType));
  }
}
