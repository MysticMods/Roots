package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextureAtlas.class)
public interface AccessorMixinTextureAtlas {
  @Invoker("getWidth")
  int roots$GetWidth();

  @Invoker("getHeight")
  int roots$GetHeight();
}
