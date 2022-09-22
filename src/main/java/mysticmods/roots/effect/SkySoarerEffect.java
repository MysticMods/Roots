package mysticmods.roots.effect;

import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.event.forge.ServerTickHandler;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

// Does nothing for non-player mobs
public class SkySoarerEffect extends MobEffect {
  public SkySoarerEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x03f0fc);
  }

  @Override
  public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    super.applyEffectTick(pLivingEntity, pAmplifier);
    if (pLivingEntity instanceof Player player) {
      player.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(snapshot -> snapshot.ifPresent(player, ModSerializers.SKY_SOARER.get(), (sky) -> {
        ServerTickHandler.nextTick(() -> {
          player.hasImpulse = true;
          Vec3 vec31 = player.getLookAngle();
          Vec3 vec32 = player.getDeltaMovement();
          player.setDeltaMovement(vec31);
        });
      }));
    }
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }

  @Override
  public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
    super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
  }
}
