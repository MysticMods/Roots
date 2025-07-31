package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.CompositeState.class)
public interface AccessorMixinCompositeState {
  @Accessor("textureState")
  RenderStateShard.EmptyTextureStateShard rootsGetTextureState();

  @Accessor("shaderState")
  RenderStateShard.ShaderStateShard rootsGetShaderState();

  @Accessor("transparencyState")
  RenderStateShard.TransparencyStateShard rootsGetTransparencyState();

  @Accessor("depthTestState")
  RenderStateShard.DepthTestStateShard rootsGetDepthTestState();

  @Accessor("cullState")
  RenderStateShard.CullStateShard rootsGetCullState();

  @Accessor("lightmapState")
  RenderStateShard.LightmapStateShard rootsGetLightmapState();

  @Accessor("overlayState")
  RenderStateShard.OverlayStateShard rootsGetOverlayState();

  @Accessor("layeringState")
  RenderStateShard.LayeringStateShard rootsGetLayeringState();

  @Accessor("outputState")
  RenderStateShard.OutputStateShard rootsGetOutputState();

  @Accessor("texturingState")
  RenderStateShard.TexturingStateShard rootsGetTexturingState();

  @Accessor("writeMaskState")
  RenderStateShard.WriteMaskStateShard rootsGetWriteMaskState();

  @Accessor("lineState")
  RenderStateShard.LineStateShard rootsGetLineState();

  @Accessor("colorLogicState")
  RenderStateShard.ColorLogicStateShard rootsGetColorLogicState();

  @Accessor("outlineProperty")
  RenderType.OutlineProperty rootsGetOutlineProperty();

  @Invoker("<init>")
  static RenderType.CompositeState rootsCreateCompositeState(
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
