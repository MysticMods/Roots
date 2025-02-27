package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public class RootsRenderType extends RenderType {
  private RootsRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
    super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
  }

  public static final RenderType LINES = create(
      "lines",
      DefaultVertexFormat.POSITION_COLOR_NORMAL,
      VertexFormat.Mode.LINES,
      1536,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_LINES_SHADER)
          .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .setOutputState(ITEM_ENTITY_TARGET)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
          .setCullState(NO_CULL)
          .createCompositeState(false)
  );
}
