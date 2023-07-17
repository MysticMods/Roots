package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ModSounds;
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
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.libs.noobutil.util.ItemUtil;

@Mod.EventBusSubscriber(modid= RootsAPI.MODID)
public class EntityEventHandler {
  @SubscribeEvent
  public static void onSquidMilked (PlayerInteractEvent.EntityInteract event) {
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
        entity.getCapability(Capabilities.SQUID_MILKING_CAPABILITY).ifPresent(cap -> {
          if (cap.hasExpired(server)) {
            cap.setExpiresAt(server, 20 * 15);
            level.playSound(null, player.blockPosition(), ModSounds.SQUID_MILK.get(), SoundSource.PLAYERS, 0.5f, level.getRandom().nextFloat() * 0.25f + 0.6f);
            if (!player.isCreative()) {
              heldItem.shrink(1);
            }
            ItemStack result = new ItemStack(ModItems.INK_BOTTLE.get());
            if (!player.getInventory().add(result)) {
              ItemUtil.Spawn.spawnItem(level, player.blockPosition(), result);
            }
          } else {
            player.displayClientMessage(Component.translatable("roots.message.squid.cooldown").setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLUE)).withBold(true)), true);
          }
        });
      }
    }
  }
}
