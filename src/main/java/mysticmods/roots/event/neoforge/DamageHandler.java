package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModAdvancements;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
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
      event.setNewDamage(0);
      if (instance.getAmplifier() == 0) {
        entity.removeEffect(ModEffects.PETAL_SHELL);
      } else {
        MobEffectInstance newInstance = new MobEffectInstance(ModEffects.PETAL_SHELL, instance.getDuration(), instance.getAmplifier() - 1);
        entity.removeEffect(ModEffects.PETAL_SHELL);
        entity.addEffect(newInstance);
      }
    }
    if (event.getNewDamage() > 0 && entity.hasEffect(ModEffects.AQUA_BUBBLE)) {
      SnapshotHelper.applyLiving(entity, ModSerializers.AQUA_BUBBLE.get(), (e, snapshot) -> {
        DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.IS_FIRE)) {
          event.setNewDamage(event.getNewDamage() * snapshot.getFireResistance());
        } else if (source.is(RootsTags.DamageTypes.IS_LAVA)) {
          event.setNewDamage(event.getNewDamage() * snapshot.getLavaResistance());
        }
      });
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

    ModAdvancements.PACIFIST.get().trigger(player, event.getEntity());
  }
}
