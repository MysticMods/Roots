package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.EntityCooldowns;
import mysticmods.roots.effect.SimpleEffect;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


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
  public static void onPotionExpire(MobEffectEvent.Expired event) {
    if (event.getEffectInstance() == null) {
      return;
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
    }
  }

  @SubscribeEvent
  public static void onEntityDetection (LivingEvent.LivingVisibilityEvent event) {
    if (event.getEntity().hasEffect(ModEffects.NONDETECTION)) {
      event.modifyVisibility(0);
    }
  }
}
