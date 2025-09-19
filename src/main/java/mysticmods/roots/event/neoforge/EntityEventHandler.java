package mysticmods.roots.event.neoforge;

import mysticmods.roots.action.ArriveDimensionAction;
import mysticmods.roots.action.KillEntityAction;
import mysticmods.roots.action.TameAnimalAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.EntityCooldowns;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.effect.SimpleEffect;
import mysticmods.roots.init.*;
import mysticmods.roots.integration.IntegrationUtil;
import mysticmods.roots.network.client.ClientboundSyncGeasPacket;
import mysticmods.roots.network.client.fx.AlertnessFXPacket;
import mysticmods.roots.snapshot.SnapshotHelper;
import mysticmods.roots.util.ItemUtil;
import mysticmods.roots.util.QuiverUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
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
  public static void onPotionAdded(MobEffectEvent.Added event) {
    if (event.getEffectInstance().getEffect().is(RootsTags.MobEffects.GEAS)) {
      PacketDistributor.sendToPlayersTrackingEntity(event.getEntity(), new ClientboundSyncGeasPacket(event.getEntity()
          .getId(), true));
    }
  }

  @SubscribeEvent
  public static void onPotionExpire(MobEffectEvent.Expired event) {
    if (event.getEffectInstance() == null) {
      return;
    }
    if (event.getEffectInstance().getEffect().is(RootsTags.MobEffects.GEAS)) {
      PacketDistributor.sendToPlayersTrackingEntity(event.getEntity(), new ClientboundSyncGeasPacket(event.getEntity()
          .getId(), false));
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
      PacketDistributor.sendToPlayersTrackingEntity(event.getEntity(), new ClientboundSyncGeasPacket(event.getEntity()
          .getId(), false));
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
      PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new ClientboundSyncGeasPacket(living.getId(), living.hasEffect(ModEffects.GEAS)));
    }
  }

  @SubscribeEvent
  public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
    InteractionHand hand = event.getHand();
    Player player = event.getEntity();
    Entity target = event.getTarget();
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.is(RootsTags.Items.RUNIC_SHEARS)) {
      if (target.getType().is(RootsTags.Entities.RUNIC_SHEARS_OVERRIDE) && target instanceof LivingEntity living) {
        event.setCancellationResult(heldItem.interactLivingEntity(player, living, hand));
        event.setCanceled(true);
      }
    } else if (heldItem.is(RootsTags.Items.CASTING_TOOLS)) {
      if (!target.getType().is(RootsTags.Entities.ALLOW_CASTING_TOOL_RIGHT_CLICK)) {
        event.setCancellationResult(InteractionResult.FAIL);
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
    InteractionHand hand = event.getHand();
    Player player = event.getEntity();
    ItemStack heldItem = player.getItemInHand(hand);
    Entity target = event.getTarget();
    if (heldItem.is(RootsTags.Items.CASTING_TOOLS)) {
      if (!target.getType().is(RootsTags.Entities.ALLOW_CASTING_TOOL_RIGHT_CLICK)) {
        event.setCancellationResult(InteractionResult.FAIL);
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onEntityTame(AnimalTameEvent event) {
    if (!(event.getTamer() instanceof ServerPlayer player)) {
      return;
    }
    TameAnimalAction.Context context = new TameAnimalAction.Context(player.serverLevel(), player, event.getAnimal());
    ModActions.TAME_ANIMAL.get().accept(context);
  }

  @SubscribeEvent
  public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      ArriveDimensionAction.Context context = new ArriveDimensionAction.Context(player.serverLevel(), player);
      ModActions.ARRIVE_DIMENSION.get().accept(context);
      var data = player.getData(ModAttachments.HERB_STORAGE);
      data.setDirty(true);
      player.setData(ModAttachments.HERB_STORAGE, data);

      var data2 = player.getData(ModAttachments.GRANT_STORAGE);
      data2.setDirty(true);
      player.setData(ModAttachments.GRANT_STORAGE, data2);

      var data3 = player.getData(ModAttachments.REPUTATION_STORAGE);
      data3.setDirty(true);
      player.setData(ModAttachments.REPUTATION_STORAGE, data3);

      var data4 = player.getData(ModAttachments.SNAPSHOT_STORAGE);
      data4.setDirty(true);
      player.setData(ModAttachments.SNAPSHOT_STORAGE, data4);

      var data5 = player.getData(ModAttachments.COOLDOWN_STORAGE);
      data5.setDirty(true);
      player.setData(ModAttachments.COOLDOWN_STORAGE, data5);
    }
  }

  @SubscribeEvent
  public static void onVillagerTrades(VillagerTradesEvent event) {
    if (event.getType() == VillagerProfession.BUTCHER) {
      // Novice
      event.getTrades().get(1).add(
          new BasicItemListing(new ItemStack(ModItems.VENISON.get(), 7), new ItemStack(Items.EMERALD), 16, 2, 0.05f)
      );
      // Apprentice
      event.getTrades().get(1).add(
          new BasicItemListing(1, new ItemStack(ModItems.COOKED_VENISON.get(), 5), 16, 5, 0.05f)
      );
      event.getTrades().get(1).add(
          new BasicItemListing(1, new ItemStack(ModItems.COOKED_SQUID.get(), 4), 16, 5, 0.05f)
      );
      // Journeyman
      event.getTrades().get(3).add(
          new BasicItemListing(new ItemStack(ModItems.RAW_SQUID.get(), 5), new ItemStack(Items.EMERALD), 12, 5, 0.05f)
      );
      // Expert
      event.getTrades().get(4).add(
          new BasicItemListing(18, new ItemStack(ModItems.ANTLERS.get()), 12, 30, 0.05f)
      );
    } else if (event.getType() == VillagerProfession.FARMER) {
      // Novice
      event.getTrades().get(1).add(
          new BasicItemListing(new ItemStack(ModItems.AUBERGINE.get(), 15), new ItemStack(Items.EMERALD), 16, 2, 0.05f)
      );
    } else if (event.getType() == VillagerProfession.CLERIC) {
      // Novice
      event.getTrades().get(2).add(
          new BasicItemListing(new ItemStack(ModItems.SILVER_INGOT.get(), 3), new ItemStack(Items.EMERALD), 12, 10, 0.05f)
      );
      // Expert
      event.getTrades().get(4).add(
          new BasicItemListing(new ItemStack(ModItems.CARAPACE.get(), 18), new ItemStack(Items.EMERALD), 12, 30, 0.05f)
      );
    } else if (event.getType() == VillagerProfession.LEATHERWORKER) {
      // Journeyman
      event.getTrades().get(3).add(
          new BasicItemListing(new ItemStack(ModItems.PELT.get(), 9), new ItemStack(Items.EMERALD), 12, 20, 0.05f)
      );
    }
  }

  @SubscribeEvent
  public static void onWanderingTrade(WandererTradesEvent event) {
    event.getRareTrades().add(
        new BasicItemListing(18, new ItemStack(ModItems.ALERTNESS_CHARM), 1, 0, 0.05f)
    );
  }

  // TODO: See Mob::~1498, this needs to handle setLastHurtMob and playAttackSound?
  @SubscribeEvent
  public static void onIncomingDamageEvent(LivingIncomingDamageEvent event) {
    LivingEntity entity = event.getEntity();
    if (entity.hasEffect(ModEffects.PETAL_SHELL)) {
      MobEffectInstance instance = entity.getEffect(ModEffects.PETAL_SHELL);
      if (instance == null) {
        return;
      }
      if (!event.getSource().is(RootsTags.DamageTypes.PETAL_SHELL_IGNORES)) {
        if (instance.getAmplifier() == 0) {
          entity.removeEffect(ModEffects.PETAL_SHELL);
        } else {
          MobEffectInstance newInstance = new MobEffectInstance(ModEffects.PETAL_SHELL, instance.getDuration(), instance.getAmplifier() - 1, false, false);
          entity.removeEffect(ModEffects.PETAL_SHELL);
          entity.addEffect(newInstance);
        }
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

  // Decrementing is handled via MixinProjectileWeaponItem
  @SubscribeEvent
  public static void onFindProjectile(LivingGetProjectileEvent event) {
    if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
      ItemStack arrow = QuiverUtil.getArrow(player);
      if (!arrow.isEmpty()) {
        event.setProjectileItemStack(arrow);
      }
    }
  }


  // "Update" tick event handled in MixinLivingEntity
}
