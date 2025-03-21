package mysticmods.roots.effect;

import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.vehicle.Boat;

// Does nothing for non-player mobs
public class SkySoarerEffect extends SimpleEffect {
  public SkySoarerEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x03f0fc);
  }

  @Override
  public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    super.applyEffectTick(pLivingEntity, pAmplifier);
    SnapshotHelper.applyLivingWithVehicle(pLivingEntity, ModSerializers.SKY_SOARER.get(), (vehicle, player, sky) -> {
      if (vehicle == null) {
        vehicle = player;
      } else {
        player.hurtMarked = true;
        player.fallDistance = 0f;
      }
      vehicle.hasImpulse = true;
      vehicle.hurtMarked = true;
      vehicle.fallDistance = 0f;
      if (vehicle instanceof Boat) {
        vehicle.setDeltaMovement(vehicle.getLookAngle()
            .multiply(sky.getAmplifier(), 0, sky.getAmplifier()));
      } else {
        vehicle.setDeltaMovement(player.getLookAngle()
            .multiply(sky.getAmplifier(), sky.getAmplifier(), sky.getAmplifier()));
      }
    });
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
    return true;
  }

  @Override
  public void removeAttributeModifiers(AttributeMap pAttributeMap) {
    super.removeAttributeModifiers(pAttributeMap);
  }

  @Override
  public boolean onEffectRemoved(LivingEntity pLivingEntity, int amplifier) {
    SnapshotHelper.applyLivingWithVehicle(pLivingEntity, ModSerializers.SKY_SOARER.get(), (vehicle, player, sky) -> {
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
    return false;
  }
}
