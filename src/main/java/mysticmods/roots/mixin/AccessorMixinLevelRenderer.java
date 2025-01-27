package mysticmods.roots.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface AccessorMixinLevelRenderer {
  @Invoker
  void callRenderHitOutline(
      PoseStack poseStack,
      VertexConsumer consumer,
      Entity entity,
      double camX,
      double camY,
      double camZ,
      BlockPos pos,
      BlockState state
  );
}
