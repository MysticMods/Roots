package mysticmods.roots.effect;

import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

// Does nothing for non-player mobs
public class SkySoarerEffect extends MobEffect {
  public SkySoarerEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x03f0fc);
  }

  @Override
  public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    super.applyEffectTick(pLivingEntity, pAmplifier);
    SnapshotHelper.applyPlayerVehicle(pLivingEntity, ModSerializers.SKY_SOARER.get(), (vehicle, player, sky) -> {
      if (vehicle == null) {
        vehicle = player;
      } else {
        // TODO: Is hurt marked actually used or useful here?
        player.hurtMarked = true;
        player.fallDistance = 0f;
      }
      vehicle.hasImpulse = true;
      vehicle.hurtMarked = true;
      vehicle.fallDistance = 0f;
      vehicle.setDeltaMovement(player.getLookAngle().multiply(sky.getAmplifier(), sky.getAmplifier(), sky.getAmplifier()));
    });
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }

  @Override
  public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
    super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    SnapshotHelper.applyPlayerVehicle(pLivingEntity, ModSerializers.SKY_SOARER.get(), (vehicle, player, sky) -> {
      if (vehicle != null) {
        vehicle.hasImpulse = true;
        vehicle.hurtMarked = true;
        vehicle.fallDistance = 0f;
        vehicle.setDeltaMovement(sky.getVehicleOriginalMovement());
      }
      player.hasImpulse = true;
      player.hurtMarked = true;
      player.fallDistance = 0f;
      player.setDeltaMovement(sky.getOriginalMovement());
    });
  }
}
