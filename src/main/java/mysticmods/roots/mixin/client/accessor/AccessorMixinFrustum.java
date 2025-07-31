package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Frustum.class)
public interface AccessorMixinFrustum {
  @Invoker("cubeInFrustum")
  boolean roots_1_21$cubeInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ);
}
