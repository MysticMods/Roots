package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface AccessorMixinParticle {
  @Accessor("removed")
  boolean roots_1_21$isRemoved();
}
