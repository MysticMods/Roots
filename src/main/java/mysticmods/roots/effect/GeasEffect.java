package mysticmods.roots.effect;

import mysticmods.roots.network.client.fx.GeasFXPacket;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class GeasEffect extends SimpleEffect {
  public GeasEffect(MobEffectCategory pCategory, int pColor) {
    super(pCategory, pColor);
  }

  public GeasEffect(MobEffectCategory category, int color, boolean hiddenByDefault) {
    super(category, color, hiddenByDefault);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    // TODO: Better visual!
    PacketDistributor.sendToPlayersTrackingEntity(livingEntity, new GeasFXPacket(livingEntity.getId()));
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return duration % 20 == 0;
  }
}
