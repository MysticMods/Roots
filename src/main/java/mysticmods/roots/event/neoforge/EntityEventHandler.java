package mysticmods.roots.event.neoforge;

import mysticmods.roots.action.TameAnimalAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.EntityCooldowns;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.effect.SimpleEffect;
import mysticmods.roots.init.*;
import mysticmods.roots.integration.IntegrationUtil;
import mysticmods.roots.item.RunicShearsItem;
import mysticmods.roots.network.client.ClientboundSyncGeasPacket;
import mysticmods.roots.network.client.fx.AlertnessFXPacket;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.TradeWithVillagerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class EntityEventHandler {
  @SubscribeEvent
  public static void onSquidMilked(PlayerInteractEvent.EntityInteract event) {
    Player player = event.getEntity();
    ItemStack heldItem = player.getItemInHand(event.getHand());
    Level level = event.getLevel();
    if (!(event.getTarget() instanceof LivingEntity entity)) {
      return;
    }
    if (heldItem.is(RootsTags.Items.BOTTLES) && entity.getType().is(RootsTags.Entities.SQUID)) {
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.SUCCESS);
      MinecraftServer server = level.getServer();
      if (server == null) {
        return;
      }
      if (!level.isClientSide()) {
        // hasData is problematic here because it means only pre-milked squids will be true
        if (entity.getType().is(RootsTags.Entities.SQUID)) {
          if (EntityCooldowns.hasExpired(entity, ModAttachments.SQUID_MILKING_COOLDOWN)) {
            EntityCooldowns.setExpiresAt(entity, ModAttachments.SQUID_MILKING_COOLDOWN, server.getTickCount() + (20 * 15));
            level.playSound(null, player.blockPosition(), ModSounds.SQUID_MILK.get(), SoundSource.PLAYERS, 0.5f, level.getRandom()
                .nextFloat() * 0.25f + 0.6f);
            if (!player.isCreative()) {
              heldItem.shrink(1);
            }
            ItemStack result = new ItemStack(ModItems.INK_BOTTLE.get());
            if (!player.getInventory().add(result)) {
              ItemUtil.Spawn.spawnItem(level, player.blockPosition(), result);
            }
          } else {
            player.displayClientMessage(Component.translatable("roots.message.squid.cooldown")
                .setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLUE)).withBold(true)), true);
          }
        }
      }
    }
  }

  @SubscribeEvent
  public static void onPotionAdded (MobEffectEvent.Added event) {
    if (event.getEffectInstance().getEffect().is(RootsTags.MobEffects.GEAS)) {
      PacketDistributor.sendToPlayersTrackingEntity(event.getEntity(), new ClientboundSyncGeasPacket(event.getEntity().getId(), true));
    }
  }

  @SubscribeEvent
  public static void onPotionExpire(MobEffectEvent.Expired event) {
    if (event.getEffectInstance() == null) {
      return;
    }
    if (event.getEffectInstance().getEffect().is(RootsTags.MobEffects.GEAS)) {
      PacketDistributor.sendToPlayersTrackingEntity(event.getEntity(), new ClientboundSyncGeasPacket(event.getEntity().getId(), false));
    }
    if (event.getEffectInstance().getEffect().value() instanceof SimpleEffect simpleEffect) {
      if (simpleEffect.onEffectExpired(event.getEntity(), event.getEffectInstance().getAmplifier())) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public static void onPotionRemoved(MobEffectEvent.Remove event) {
    if (event.getEffectInstance() == null) {
      return;
    }
    if (event.getEffect().is(RootsTags.MobEffects.GEAS)) {
      PacketDistributor.sendToPlayersTrackingEntity(event.getEntity(), new ClientboundSyncGeasPacket(event.getEntity().getId(), false));
    }
    if (event.getEffectInstance().getEffect().value() instanceof SimpleEffect simpleEffect) {
      if (simpleEffect.onEffectRemoved(event.getEntity(), event.getEffectInstance().getAmplifier())) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public static void onEntityIncomingDamage(LivingIncomingDamageEvent event) {
    if (event.getEntity() instanceof Player player) {
      if (event.getSource().getEntity() instanceof LivingEntity living) {
        if (living.hasEffect(ModEffects.GEAS)) {
          event.setAmount(0);
          event.setCanceled(true);
        }
      } else if (event.getSource().getDirectEntity() instanceof LivingEntity living) {
        if (living.hasEffect(ModEffects.GEAS)) {
          event.setAmount(0);
          event.setCanceled(true);
        }
      }
    }
  }

  @SubscribeEvent
  public static void onEntityTarget(LivingChangeTargetEvent event) {
    if (event.getEntity().hasEffect(ModEffects.GEAS)) {
      event.setNewAboutToBeSetTarget(null);
      return;
    }

    if (event.getEntity() instanceof Mob mob && event.getOriginalAboutToBeSetTarget() instanceof Player player && mob.getTarget() != player && mob.getLastHurtByMob() != player) {
      if (ConfigManager.ALERTNESS_TAG.get() && !mob.getType().is(RootsTags.Entities.ALERTNESS)) {
        return;
      }
      List<ItemStack> charms = IntegrationUtil.getCharms(player);
      if (!charms.isEmpty()) {
        boolean doAlert = false;
        for (ItemStack stack : charms) {
          if (stack.is(RootsTags.Items.CHARM_ALERT)) {
            doAlert = true;
            break;
          }
        }
        if (doAlert) {
          PacketDistributor.sendToPlayer((ServerPlayer) player, new AlertnessFXPacket(event.getEntity().getId()));
          mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, false, false));
        }
      }
    }
  }

  @SubscribeEvent
  public static void onEntityDetection(LivingEvent.LivingVisibilityEvent event) {
    if (event.getEntity().hasEffect(ModEffects.NONDETECTION)) {
      event.modifyVisibility(0);
    }
  }

  @SubscribeEvent
  public static void onEntityStartTracking(PlayerEvent.StartTracking event) {
    if (event.getTarget() instanceof LivingEntity living) {
      PacketDistributor.sendToPlayer((ServerPlayer)event.getEntity(), new ClientboundSyncGeasPacket(living.getId(), living.hasEffect(ModEffects.GEAS)));
    }
  }

  @SubscribeEvent
  public static void onEntityInteract (PlayerInteractEvent.EntityInteractSpecific event) {
    InteractionHand hand = event.getHand();
    Player player = event.getEntity();
    Entity target = event.getTarget();
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.is(RootsTags.Items.RUNIC_SHEARS)) {
      if (target.getType().is(RootsTags.Entities.RUNIC_SHEARS_OVERRIDE) && target instanceof LivingEntity living) {
        event.setCancellationResult(heldItem.interactLivingEntity(player, living, hand));
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onEntityTame (AnimalTameEvent event) {
    if (!(event.getTamer() instanceof ServerPlayer player)) {
      return;
    }
    TameAnimalAction.Context context = new TameAnimalAction.Context(player.serverLevel(), player, event.getAnimal());
    ModActions.TAME_ANIMAL.get().accept(context);
  }

  // "Update" tick event handled in MixinLivingEntity
}
