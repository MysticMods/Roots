package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.mixin.client.accessor.AccessorMixinCompositeRenderType;
import mysticmods.roots.mixin.client.accessor.AccessorMixinCompositeState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.HashSet;
import java.util.Set;

public class DepthWrappedMultiBufferSource implements MultiBufferSource {
  private static final Set<RenderType> EQUAL_DEPTH = new HashSet<>();
  private static final Set<RenderType> USE_DELEGATE = new HashSet<>();

  private final MultiBufferSource.BufferSource delegate;

  protected DepthWrappedMultiBufferSource(MultiBufferSource.BufferSource delegate) {
    this.delegate = delegate;
  }

  @Override
  public VertexConsumer getBuffer(RenderType renderType) {
    if (EQUAL_DEPTH.contains(renderType)) {
      return delegate.getBuffer(renderType);
    } else if (USE_DELEGATE.contains(renderType)) {
      return delegate.getBuffer(RootsRenderTypes.getDissolveDepth(renderType));
    } else {
      if (((AccessorMixinCompositeState) (Object) ((AccessorMixinCompositeRenderType) renderType).rootsGetState()).rootsGetDepthTestState()
          .equals(RenderType.EQUAL_DEPTH_TEST)) {
        EQUAL_DEPTH.add(renderType);
        return delegate.getBuffer(renderType);
      } else {
        USE_DELEGATE.add(renderType);
        return delegate.getBuffer(RootsRenderTypes.getDissolveDepth(renderType));
      }
    }
  }
}
