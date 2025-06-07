package mysticmods.roots.client.gui.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

public class AquaBubbleLayer {
  public static final ResourceLocation WARNING_TEXTURE = RootsAPI.rl("textures/misc/aqua_bubble_vignette.png");

  public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    if (!ConfigManager.AQUA_BUBBLE_OVERLAY.getAsBoolean()) {
      return;
    }
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player == null) {
      return;
    }
    MobEffectInstance instance = minecraft.player.getEffect(ModEffects.AQUA_BUBBLE);
    if (instance == null) {
      return;
    }
    if (!minecraft.options.getCameraType().isFirstPerson()) {
      return;
    }
    RenderSystem.disableDepthTest();
    RenderSystem.depthMask(false);
    RenderSystem.enableBlend();
/*    RenderSystem.defaultBlendFunc();*/
    RenderSystem.blendFuncSeparate(
        GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
        GlStateManager.SourceFactor.ONE,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
    );
    float alpha = 0.25f;

    if (instance.getDuration() < 20) {
      alpha *= instance.getDuration() / 20.0f;
    }

    guiGraphics.setColor(1f, 1f, 1f, alpha);

    guiGraphics.blit(WARNING_TEXTURE, 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
    RenderSystem.depthMask(true);
    RenderSystem.enableDepthTest();
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.defaultBlendFunc();
    RenderSystem.disableBlend();
  }
}
