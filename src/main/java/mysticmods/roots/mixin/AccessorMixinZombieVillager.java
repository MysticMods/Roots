package mysticmods.roots.mixin;

import net.minecraft.world.entity.monster.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ZombieVillager.class)
public interface AccessorMixinZombieVillager {
  @Accessor
  int getVillagerConversionTime ();

  @Accessor
  void setVillagerConversionTime (int time);
}
