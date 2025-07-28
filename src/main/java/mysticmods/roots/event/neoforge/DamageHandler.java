package mysticmods.roots.event.neoforge;

import mysticmods.roots.action.KillEntityAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModAdvancements;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class DamageHandler {
  // TODO: See Mob::~1498, this needs to handle setLastHurtMob and playAttackSound?
  @SubscribeEvent
  public static void onIncomingDamageEvent(LivingIncomingDamageEvent event) {
    LivingEntity entity = event.getEntity();
    if (entity.hasEffect(ModEffects.PETAL_SHELL)) {
      MobEffectInstance instance = entity.getEffect(ModEffects.PETAL_SHELL);
      if (instance == null) {
        return;
      }
      if (instance.getAmplifier() == 0) {
        entity.removeEffect(ModEffects.PETAL_SHELL);
      } else {
        MobEffectInstance newInstance = new MobEffectInstance(ModEffects.PETAL_SHELL, instance.getDuration(), instance.getAmplifier() - 1, false, false);
        entity.removeEffect(ModEffects.PETAL_SHELL);
        entity.addEffect(newInstance);
      }
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onDamageEvent(LivingDamageEvent.Pre event) {
    LivingEntity entity = event.getEntity();
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
    DamageSource source = event.getSource();
    Entity target = event.getEntity();
    Entity directEntity = source.getDirectEntity();
    Entity sourceEntity = source.getEntity();
    ServerPlayer player;
    if (directEntity instanceof ServerPlayer player1) {
      player = player1;
      directEntity = null;
    } else if (sourceEntity instanceof ServerPlayer player2) {
      player = player2;
      sourceEntity = null;
    } else {
      return;
    }

    if (directEntity == null) {
      directEntity = sourceEntity;
    }

    ModAdvancements.PACIFIST.get().trigger(player, target);

    KillEntityAction.Context context = new KillEntityAction.Context(player.serverLevel(), player, target, directEntity, source);
    ModActions.KILL_ENTITY.get().accept(context);
  }
}
