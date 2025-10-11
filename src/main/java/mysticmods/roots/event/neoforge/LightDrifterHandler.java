package mysticmods.roots.event.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.client.ClientLightDrifterUtil;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.LightDrifterSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import mysticmods.roots.util.LightDrifterUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class LightDrifterHandler {
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onMousePre(InputEvent.MouseButton.Pre event) {
    Minecraft mc = Minecraft.getInstance();
    // TODO: Is this enough?
    if (mc.screen != null && mc.screen.isPauseScreen()) {
      return;
    }
    if (mc.player != null && mc.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
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

    if (event.getName().equals(VanillaGuiLayers.EXPERIENCE_BAR)) {
      event.setCanceled(true);
    } else if (event.getName().equals(VanillaGuiLayers.OVERLAY_MESSAGE)) {

      LightDrifterEntity entity = LightDrifterUtil.getLightDrifterEntity(minecraft.player);
      if (entity == null) {
        return;
      }
      LightDrifterSnapshot snapshot = SnapshotHelper.getSnapshot(entity, ModSerializers.LIGHT_DRIFTER.get());
      // TODO: ???
      if (snapshot == null || snapshot.isExpired(entity)) {
        return;
      }

      double distance = entity.position().distanceTo(minecraft.player.position());

      GuiGraphics guiGraphics = event.getGuiGraphics();
      Gui gui = minecraft.gui;
      Font font = gui.getFont();
      Component overlayMessageString = Component.translatable("roots.gui.light_drifter_overlay", Mth.ceil(distance + 0.5), Mth.sqrt(snapshot.getMaxDistance())).withStyle(ChatFormatting.BOLD);
      minecraft.getProfiler().push("overlayMessage");
      int yShift = Math.max(gui.leftHeight, gui.rightHeight) + (68 - 59);
      int j = ChatFormatting.YELLOW.getColor().intValue();
      guiGraphics.pose().pushPose();
      guiGraphics.pose()
          .translate((float) (guiGraphics.guiWidth() / 2), (float) (guiGraphics.guiHeight() - Math.max(yShift, 68)), 0.0F);
      int k = font.width(overlayMessageString);
      guiGraphics.drawStringWithBackdrop(font, overlayMessageString, -k / 2, -20, k, j);
      overlayMessageString = Component.translatable("roots.gui.light_drifter_cancel", KeyBindings.CANCEL_LIGHT_DRIFTER.getTranslatedKeyMessage());
      k = font.width(overlayMessageString);
      guiGraphics.drawStringWithBackdrop(font, overlayMessageString, -k / 2, -34, k, j);
      guiGraphics.pose().popPose();

      minecraft.getProfiler().pop();
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
