package mysticmods.roots.event.forge;

import mysticmods.roots.advancements.Advancements;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class DamageHandler {
  @SubscribeEvent
  public static void onDamageEvent(LivingHurtEvent event) {
    LivingEntity entity = event.getEntity();
    if (entity.hasEffect(ModEffects.PETAL_SHELL.get())) {
      MobEffectInstance instance = entity.getEffect(ModEffects.PETAL_SHELL.get());
      if (instance == null) {
        // TODO: This is problematic?
        return;
      }
      event.setCanceled(true);
      if (instance.getAmplifier() == 0) {
        entity.removeEffect(ModEffects.PETAL_SHELL.get());
      } else {
        MobEffectInstance newInstance = new MobEffectInstance(ModEffects.PETAL_SHELL.get(), instance.getDuration(), instance.getAmplifier() - 1);
        entity.removeEffect(ModEffects.PETAL_SHELL.get());
        entity.addEffect(newInstance);
      }
    }
  }

  @SubscribeEvent
  public static void onDeathEvent(LivingDeathEvent event) {
    if (event.getSource() instanceof EntityDamageSource source) {
      if (source.getEntity() instanceof ServerPlayer player) {
        Advancements.PACIFIST_TRIGGER.trigger(player, event);
      }
    }
  }
}
