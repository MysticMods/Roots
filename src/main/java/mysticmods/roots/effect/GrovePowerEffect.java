package mysticmods.roots.effect;

import mysticmods.roots.api.attachment.GrovePowerEntityFlag;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GrovePowerEffect extends SimpleEffect {
  private final GrovePowerEntityFlag flag;

  public GrovePowerEffect(boolean eligible, boolean powered) {
    super(MobEffectCategory.BENEFICIAL, 0x000000, true);
    this.flag = new GrovePowerEntityFlag(eligible, powered);
  }

  @Override
  public boolean onEffectRemoved(LivingEntity entity, int amplifier) {
    var result = super.onEffectRemoved(entity, amplifier);
    entity.removeData(ModAttachments.GROVE_POWER_ENTITY_FLAG);
    return result;
  }

  @Override
  public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
    livingEntity.setData(ModAttachments.GROVE_POWER_ENTITY_FLAG, flag);
  }
}
