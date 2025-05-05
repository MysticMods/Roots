package mysticmods.roots.client.gui.layer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class HudLayer {
  public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    PoseStack stack = guiGraphics.pose();
    float partialTicks = deltaTracker.getRealtimeDeltaTicks();

    Minecraft mc = Minecraft.getInstance();

    // Copied from 1.12.2, I have no idea of the relevance of this.
    if (mc.screen instanceof ChatScreen || mc.hitResult == null || mc.hitResult.getType() == HitResult.Type.MISS || mc.level == null) {
      return;
    }

    if (mc.getOverlay() != null || mc.options.hideGui) {
      return;
    }

    if (mc.hitResult.getType() == HitResult.Type.BLOCK) {
      BlockHitResult trace = ((BlockHitResult) mc.hitResult);
      BlockState state = mc.level.getBlockState(trace.getBlockPos());
      if (state.is(RootsTags.Blocks.PYRE_HUD_RENDERER)) {
        renderPyre(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
      } else if (state.is(RootsTags.Blocks.GROVE_CRAFTER_HUD_RENDERER)) {
        renderGroveCrafter(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
      } else if (state.is(RootsTags.Blocks.MORTAR_HUD_RENDERER)) {
        renderMortar(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
      }
    } else {
      EntityHitResult trace = ((EntityHitResult) mc.hitResult);

      if (trace.getEntity().getType().is(RootsTags.Entities.SHOULD_RENDER_HUD)) {

      }
    }
  }

  public static void renderPyre(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {
    Level level = mc.level;
    if (level.getBlockEntity(trace.getBlockPos()) instanceof PyreBlockEntity pyre) {
      int x = (graphics.guiWidth() / 2); // + (graphics.guiWidth() / 4);
      int y = (graphics.guiHeight() / 4);// + (graphics.guiHeight() / 4);

      y = y * 2;

      y = y + 20;
      x = x + 20;

      Ritual activeRitual = pyre.getCurrentRitual();
      PyreRecipe cachedRecipe = pyre.getCachedRecipe() == null ? null : pyre.getCachedRecipe().value();
      PyreRecipe lastRecipe = pyre.getLastRecipe() == null ? null : pyre.getLastRecipe().value();
      Ritual lastRitual = lastRecipe != null ? lastRecipe.getRitual() : null;
      Ritual nextRitual = cachedRecipe != null ? cachedRecipe.getRitual() : null;

      ItemStack output = ItemStack.EMPTY;
      Component comp1 = Component.empty();
      Component comp2 = Component.empty();
      Component comp3 = Component.empty();

      if (cachedRecipe != null && activeRitual == null) {
        output = nextRitual != null ? nextRitual.getIcon() : cachedRecipe.getResultItem(mc.level.registryAccess());
        comp1 = Component.translatable("roots.hud.pyre.begin1");
        comp2 = Component.translatable(nextRitual != null ? "roots.hud.pyre.begin2" : "roots.hud.pyre.begin3", nextRitual != null ? nextRitual.getName() : output.getHoverName());
      } else if (cachedRecipe != null && cachedRecipe == lastRecipe) {
        output = nextRitual != null ? nextRitual.getIcon() : cachedRecipe.getResultItem(mc.level.registryAccess());
        comp1 = nextRitual != null ? nextRitual.getName() : output.getHoverName();
        comp2 = Component.translatable("roots.hud.pyre.auto1");
      } else if (lastRecipe != null) {
        output = lastRitual != null ? lastRitual.getIcon() : lastRecipe.getResultItem(mc.level.registryAccess());
        comp1 = Component.translatable("roots.hud.pyre.restart1");
        comp2 = Component.translatable("roots.hud.pyre.restart2");
        comp3 = nextRitual != null ? nextRitual.getName() : output.getHoverName();
      }

      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, 16777215, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, 16777215, true);
      graphics.drawString(mc.font, comp3, x + 25, y + 24, 16777215, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }

  public static void renderGroveCrafter(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {

  }

  public static void renderGroveStone(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {

  }

  public static void renderMortar(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {

  }
}
