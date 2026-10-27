package mysticmods.roots.mixin.accessor;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.world.level.block.SkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SkullBlockRenderer.class)
public interface AccessorMixinSkullBlockRenderer {
  @Accessor("modelByType")
  Map<SkullBlock.Type, SkullModelBase> roots$getModelByType();
}
