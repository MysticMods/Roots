package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class DamageHandler {
  @SubscribeEvent
  public static void onDamageEvent(LivingDamageEvent.Pre event) {
    LivingEntity entity = event.getEntity();
    if (entity.hasEffect(ModEffects.PETAL_SHELL)) {
      MobEffectInstance instance = entity.getEffect(ModEffects.PETAL_SHELL);
      if (instance == null) {
        // TODO: This is problematic?
        return;
      }
      event.setNewDamage(0);
      if (instance.getAmplifier() == 0) {
        entity.removeEffect(ModEffects.PETAL_SHELL);
      } else {
        MobEffectInstance newInstance = new MobEffectInstance(ModEffects.PETAL_SHELL, instance.getDuration(), instance.getAmplifier() - 1);
        entity.removeEffect(ModEffects.PETAL_SHELL);
        entity.addEffect(newInstance);
      }
    }
  }

  @SubscribeEvent
  public static void onDeathEvent(LivingDeathEvent event) {
    ServerPlayer player;
    if (event.getSource().getEntity() instanceof ServerPlayer player1) {
      player = player1;
    } else if (event.getSource().getDirectEntity() instanceof ServerPlayer player2) {
      player = player2;
    } else {
      return;
    }
    // TODO:
    /*    Advancements.PACIFIST_TRIGGER.trigger(player, event);*/
  }
}
