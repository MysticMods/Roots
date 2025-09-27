package mysticmods.roots.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class LightDrifterEffect extends SimpleEffect{
  public LightDrifterEffect(MobEffectCategory category, int color, boolean hiddenByDefault) {
    super(category, color, hiddenByDefault);
  }

  @Override
  public boolean onEffectExpired(LivingEntity entity, int amplifier) {
    if (entity instanceof ServerPlayer player) {
      player.setCamera(null);
    }
    return super.onEffectExpired(entity, amplifier);
  }

  @Override
  public boolean onEffectRemoved(LivingEntity entity, int amplifier) {
    if (entity instanceof ServerPlayer player) {
      player.setCamera(null);
    }
    return super.onEffectRemoved(entity, amplifier);
  }
}
