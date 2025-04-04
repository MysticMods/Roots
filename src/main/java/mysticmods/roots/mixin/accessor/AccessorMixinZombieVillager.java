package mysticmods.roots.mixin.accessor;

import net.minecraft.world.entity.monster.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ZombieVillager.class)
public interface AccessorMixinZombieVillager {
  @Accessor("villagerConversionTime")
  int rootsGetVillagerConversionTime();

  @Accessor("villagerConversionTime")
  void rootsSetVillagerConversionTime(int time);
}
