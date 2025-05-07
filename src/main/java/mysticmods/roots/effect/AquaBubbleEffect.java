package mysticmods.roots.effect;

import mysticmods.roots.network.client.fx.AquaBubbleFXPacket;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class AquaBubbleEffect extends SimpleEffect {
  public AquaBubbleEffect(MobEffectCategory pCategory, int pColor) {
    super(pCategory, pColor);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.tickCount % 18 == 0 && !livingEntity.level().isClientSide()) {
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, new AquaBubbleFXPacket(livingEntity.getId()));
    }

    return super.applyEffectTick(livingEntity, amplifier);
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return true;
  }
}
