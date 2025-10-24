package mysticmods.roots.client.gui.layer;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.client.KeyHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class CancelEffectOverlay {
  public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    Minecraft minecraft = Minecraft.getInstance();

    Player player = minecraft.player;
    if (player == null) {
      return;
    }

    Holder<MobEffect> cancelEffect = null;

    for (MobEffectInstance instance : player.getActiveEffects()) {
      if (instance.getEffect().is(RootsTags.MobEffects.CANCELLABLE_EFFECTS)) {
        cancelEffect = instance.getEffect();
        break;
      }
    }

    if (cancelEffect == null) {
      return;
    }

    Gui gui = minecraft.gui;
    Font font = gui.getFont();
    minecraft.getProfiler().push("overlayMessage");
    int yShift = Math.max(gui.leftHeight, gui.rightHeight) + (68 - 59);
    @SuppressWarnings("DataFlowIssue") int j = ChatFormatting.YELLOW.getColor();
    guiGraphics.pose().pushPose();
    guiGraphics.pose()
        .translate((float) (guiGraphics.guiWidth() / 2), (float) (guiGraphics.guiHeight() - Math.max(yShift, 68)), 0.0F);
    Component overlayMessageString = Component.translatable(KeyHandler.isCancelingEffect() ? "roots.gui.effect_continue_canceling" : "roots.gui.effect_cancel", KeyBindings.CANCEL_EFFECT.getTranslatedKeyMessage(), Component.translatable(cancelEffect.value().getDescriptionId()))
        .withStyle(ChatFormatting.BOLD);
    int k = font.width(overlayMessageString);
    guiGraphics.drawStringWithBackdrop(font, overlayMessageString, -k / 2, -34, k, j);
    guiGraphics.pose().popPose();

    minecraft.getProfiler().pop();
  }
}
