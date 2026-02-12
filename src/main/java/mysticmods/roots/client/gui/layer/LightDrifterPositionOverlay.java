package mysticmods.roots.client.gui.layer;

import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.LightDrifterSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import mysticmods.roots.util.LightDrifterUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class LightDrifterPositionOverlay {
  public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    Minecraft minecraft = Minecraft.getInstance();
    // TODO: Migrate this to a proper overlay
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

    Gui gui = minecraft.gui;
    Font font = gui.getFont();
    Component overlayMessageString = Component.translatable("roots.gui.light_drifter_overlay", Mth.ceil(distance + 0.5), Mth.sqrt(snapshot.getMaxDistance()))
        .withStyle(ChatFormatting.BOLD);
    minecraft.getProfiler().push("overlayMessage");
    int yShift = Math.max(gui.leftHeight, gui.rightHeight) + (68 - 59);
    @SuppressWarnings("DataFlowIssue") int j = ChatFormatting.YELLOW.getColor();
    guiGraphics.pose().pushPose();
    guiGraphics.pose()
        .translate((float) (guiGraphics.guiWidth() / 2), (float) (guiGraphics.guiHeight() - Math.max(yShift, 68)), 0.0F);
    int k = font.width(overlayMessageString);
    guiGraphics.drawStringWithBackdrop(font, overlayMessageString, -k / 2, -20, k, j);
    guiGraphics.pose().popPose();
    minecraft.getProfiler().pop();
  }
}
