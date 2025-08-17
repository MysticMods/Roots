package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.ArrayList;
import java.util.List;

public class DissolveBufferSource implements MultiBufferSource {
  private final MultiBufferSource.BufferSource delegate;
  private final List<RenderType> usedRenderTypes = new ArrayList<>();


  public DissolveBufferSource(BufferSource delegate) {
    this.delegate = delegate;
  }

  public List<RenderType> getUsedRenderTypes() {
    return usedRenderTypes;
  }

  @Override
  public VertexConsumer getBuffer(RenderType renderType) {
    if (!usedRenderTypes.contains(renderType)) {
      usedRenderTypes.add(renderType);
    }
    return delegate.getBuffer(RootsRenderTypes.DISSOLVE);
  }
}

