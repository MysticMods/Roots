package mysticmods.roots.effect;

import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.PetalShellFXPacket;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class PetalShellEffect extends SimpleEffect {
  public PetalShellEffect(MobEffectCategory pCategory, int pColor) {
    super(pCategory, pColor);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.level().isClientSide()) {
      livingEntity.level().addParticle(new RootsParticleOptions(ModParticles.PETAL_SHELL, ModSpells.PETAL_SHELL.get().getColor1(), ModSpells.PETAL_SHELL.get().getColor1(), livingEntity.getId()), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, 0, 0);
    }

    return super.applyEffectTick(livingEntity, amplifier);
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return true;
  }
}
