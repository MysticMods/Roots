package mysticmods.roots.effect;

import mysticmods.roots.network.client.fx.AquaBubbleFXPacket;
import mysticmods.roots.network.client.fx.PetalShellFXPacket;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class PetalShellEffect extends SimpleEffect {
  public PetalShellEffect(MobEffectCategory pCategory, int pColor) {
    super(pCategory, pColor);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.tickCount % 10 == 0 && !livingEntity.level().isClientSide()) {
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, new PetalShellFXPacket(livingEntity.getId()));
    }

    return super.applyEffectTick(livingEntity, amplifier);
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return true;
  }
}
