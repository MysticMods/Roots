package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.CompositeState.class)
public interface AccessorMixinCompositeState {
  @Accessor("textureState")
  RenderStateShard.EmptyTextureStateShard roots$GetTextureState();

  @Accessor("shaderState")
  RenderStateShard.ShaderStateShard roots$GetShaderState();

  @Accessor("transparencyState")
  RenderStateShard.TransparencyStateShard roots$GetTransparencyState();

  @Accessor("depthTestState")
  RenderStateShard.DepthTestStateShard roots$GetDepthTestState();

  @Accessor("cullState")
  RenderStateShard.CullStateShard roots$GetCullState();

  @Accessor("lightmapState")
  RenderStateShard.LightmapStateShard roots$GetLightmapState();

  @Accessor("overlayState")
  RenderStateShard.OverlayStateShard roots$GetOverlayState();

  @Accessor("layeringState")
  RenderStateShard.LayeringStateShard roots$GetLayeringState();

  @Accessor("outputState")
  RenderStateShard.OutputStateShard roots$GetOutputState();

  @Accessor("texturingState")
  RenderStateShard.TexturingStateShard roots$GetTexturingState();

  @Accessor("writeMaskState")
  RenderStateShard.WriteMaskStateShard roots$GetWriteMaskState();

  @Accessor("lineState")
  RenderStateShard.LineStateShard roots$GetLineState();

  @Accessor("colorLogicState")
  RenderStateShard.ColorLogicStateShard roots$GetColorLogicState();

  @Accessor("outlineProperty")
  RenderType.OutlineProperty roots$GetOutlineProperty();

  @Invoker("<init>")
  static RenderType.CompositeState roots$CreateCompositeState(
      RenderStateShard.EmptyTextureStateShard textureState,
      RenderStateShard.ShaderStateShard shaderState,
      RenderStateShard.TransparencyStateShard transparencyState,
      RenderStateShard.DepthTestStateShard depthState,
      RenderStateShard.CullStateShard cullState,
      RenderStateShard.LightmapStateShard lightmapState,
      RenderStateShard.OverlayStateShard overlayState,
      RenderStateShard.LayeringStateShard layeringState,
      RenderStateShard.OutputStateShard outputState,
      RenderStateShard.TexturingStateShard texturingState,
      RenderStateShard.WriteMaskStateShard writeMaskState,
      RenderStateShard.LineStateShard lineState,
      RenderStateShard.ColorLogicStateShard colorLogicState,
      RenderType.OutlineProperty outlineProperty) {
    return null;
  }
}
