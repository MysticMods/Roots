package mysticmods.roots.mixin.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface AccessorMixinEntity {
  @Invoker("readAdditionalSaveData")
  void roots$ReadAdditionalSaveData (CompoundTag tag);
}
