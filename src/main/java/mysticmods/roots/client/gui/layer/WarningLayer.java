package mysticmods.roots.client.gui.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class WarningLayer {
  public static final ResourceLocation WARNING_TEXTURE = RootsAPI.rl("textures/misc/warning_vignette.png");
  public static int warningTicks = 0;

  public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    warningTicks--;
    if (warningTicks < 0) {
      return;
    }
    RenderSystem.disableDepthTest();
    RenderSystem.depthMask(false);
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(
        GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
    );
    guiGraphics.setColor(0.8f * (warningTicks / 150f), 0f, 0f, 0.1f);

    guiGraphics.blit(WARNING_TEXTURE, 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
    RenderSystem.depthMask(true);
    RenderSystem.enableDepthTest();
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.defaultBlendFunc();
    RenderSystem.disableBlend();
  }
}
