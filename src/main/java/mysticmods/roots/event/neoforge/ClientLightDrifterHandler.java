package mysticmods.roots.event.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class ClientLightDrifterHandler {
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onMousePre(InputEvent.MouseButton.Pre event) {
    Minecraft mc = Minecraft.getInstance();
    // TODO: Is this enough?
    if (mc.screen != null && mc.screen.isPauseScreen()) {
      return;
    }
    if (mc.player != null && mc.player.hasEffect(ModEffects.LIGHT_DRIFTER) && event.getAction() != InputConstants.RELEASE) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.screen != null) {
      return;
    }
    if (mc.player != null && mc.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onMouseInteract(InputEvent.InteractionKeyMappingTriggered event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.screen != null) {
      return;
    }
    if (mc.player != null && mc.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      event.setCanceled(true);
      event.setSwingHand(false);
    }
  }

  @SubscribeEvent
  public static void onLayerRender(RenderGuiLayerEvent.Pre event) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player == null || !minecraft.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      return;
    }

    if (ConfigManager.shouldSkipLayer(event.getName())) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onTeleportDimension(EntityTeleportEvent event) {
    // Light Drifters cannot change dimensions
    if (event.getEntity().getType().is(RootsTags.Entities.LIGHT_DRIFTER)) {
      event.setCanceled(true);
    }
  }
}
