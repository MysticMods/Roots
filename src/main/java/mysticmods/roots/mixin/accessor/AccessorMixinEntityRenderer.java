package mysticmods.roots.mixin.accessor;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface AccessorMixinEntityRenderer {
  @Accessor("entityRenderDispatcher")
  EntityRenderDispatcher rootsGetEntityRenderDispatcher();
}
