package mysticmods.roots.mixin.client.accessor;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(RenderType.CompositeRenderType.class)
public interface AccessorMixinCompositeRenderType {
  @Accessor("state")
  RenderType.CompositeState rootsGetState();

  @Accessor("outline")
  Optional<RenderType> rootsGetOutline();

  @Accessor("isOutline")
  boolean rootsIsOutline();

  @Invoker("<init>")
  static RenderType.CompositeRenderType rootsCreateCompositeRenderType(
      String name,
      VertexFormat format,
      VertexFormat.Mode mode,
      int bufferSize,
      boolean affectsCrumbling,
      boolean sortOnUpload,
      RenderType.CompositeState state) {
    throw new AssertionError("Mixin should have replaced this method");
  }
}
