package mysticmods.roots.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GrovePower;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Collections;
import java.util.List;

public class HudOverlay {
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
      } else if (state.is(RootsTags.Blocks.GROVE_STONE_HUD_RENDERER)) {
        renderGroveStone(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
      }
    } else {
      EntityHitResult trace = ((EntityHitResult) mc.hitResult);

      if (trace.getEntity().getType().is(RootsTags.Entities.SHOULD_RENDER_HUD)) {

      }
    }
  }

  // TODO: Handle conditions
  public static void renderPyre(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {
    Level level = mc.level;
    if (level.getBlockEntity(trace.getBlockPos()) instanceof PyreBlockEntity pyre) {
      boolean active = state.getValue(PyreBlock.BURNING);

      int x = (graphics.guiWidth() / 2); // + (graphics.guiWidth() / 4);
      int y = (graphics.guiHeight() / 2);// + (graphics.guiHeight() / 4);

      boolean empty = pyre.getInventory().isEmpty();

      if (!empty) {
        float angle = -90;
        int radius = 24;
        List<ItemStack> nonEmpty = pyre.getNonEmptyItems();
        float anglePer = 360f / nonEmpty.size();

        for (ItemStack stack : nonEmpty) {
          double xPos = x + Math.cos(angle * Math.PI / 180) * radius - 8;
          double yPos = y + Math.sin(angle * Math.PI / 180) * radius - 8;
          graphics.renderItem(stack, (int) xPos, (int) yPos, 0);
          graphics.renderItemDecorations(mc.font, stack, (int) xPos, (int) yPos);
          angle += anglePer;
        }
      }

      y += 10;
      x += 30;

      Ritual activeRitual = pyre.getCurrentRitual();
      PyreRecipe cachedRecipe = pyre.getCachedRecipe() == null ? null : pyre.getCachedRecipe().value();
      PyreRecipe lastRecipe = pyre.getLastRecipe() == null ? null : pyre.getLastRecipe().value();
      Ritual lastRitual = lastRecipe != null ? lastRecipe.getRitual() : null;
      Ritual nextRitual = cachedRecipe != null ? cachedRecipe.getRitual() : null;

      ItemStack output = ItemStack.EMPTY;
      Component comp1 = Component.empty();
      Component comp2 = Component.empty();
      Component comp3 = Component.empty();
      List<ChanceOutput> outputs = Collections.emptyList();

      if (cachedRecipe != null && activeRitual == null) {
        output = nextRitual != null ? nextRitual.getIcon() : cachedRecipe.getResultItem(mc.level.registryAccess());
        comp1 = Component.translatable("roots.hud.pyre.begin1");
        comp2 = Component.translatable(nextRitual != null ? "roots.hud.pyre.begin2" : "roots.hud.pyre.begin3", nextRitual != null ? nextRitual.getName() : output.getHoverName());
        outputs = cachedRecipe.getChanceOutputs();
      } else if (cachedRecipe != null && cachedRecipe == lastRecipe) {
        output = nextRitual != null ? nextRitual.getIcon() : cachedRecipe.getResultItem(mc.level.registryAccess());
        outputs = cachedRecipe.getChanceOutputs();
        if (active) {
          comp1 = nextRitual != null ? nextRitual.getName() : output.getHoverName();
          comp2 = Component.translatable("roots.hud.pyre.auto1");
        } else {
          comp1 = Component.translatable("roots.hud.pyre.begin1");
          comp2 = Component.translatable(nextRitual != null ? "roots.hud.pyre.begin2" : "roots.hud.pyre.begin3", nextRitual != null ? nextRitual.getName() : output.getHoverName());
        }
      } else if (lastRecipe != null) {
        output = lastRitual != null ? lastRitual.getIcon() : lastRecipe.getResultItem(mc.level.registryAccess());
        outputs = lastRecipe.getChanceOutputs();
        comp1 = Component.translatable("roots.hud.pyre.restart1");
        comp2 = Component.translatable("roots.hud.pyre.restart2");
        comp3 = nextRitual != null ? nextRitual.getName() : output.getHoverName();
      }

      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);

      int ny = y + 18;

      for (ChanceOutput chanceOutput : outputs) {
        graphics.renderItem(chanceOutput.getOutput(), x, ny, 0);
        graphics.renderItemDecorations(mc.font, chanceOutput.getOutput(), x, ny);
        Component comp4 = Component.translatable("roots.tooltip.chance", String.format("%.2f", chanceOutput.getChance() * 100));
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.drawString(mc.font, comp4, x - 15, ny, 16777215, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        ny += 18;
      }

      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, 16777215, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, 16777215, true);
      graphics.drawString(mc.font, comp3, x + 25, y + 24, 16777215, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }

  // TODO: In theory, pyre and grove crafter recipes can just unlock spells
  public static void renderGroveCrafter(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {
    Level level = mc.level;

    // TODO: Handle invalid recipes
    if (level.getBlockEntity(trace.getBlockPos()) instanceof GroveCrafterBlockEntity groveCrafter) {
      int x = (graphics.guiWidth() / 2); // + (graphics.guiWidth() / 4);
      int y = (graphics.guiHeight() / 2);// + (graphics.guiHeight() / 4);

      y += 10;
      x += 30;

      GroveRecipe cachedRecipe = groveCrafter.getRecipe() == null ? null : groveCrafter.getRecipe().value();

      ItemStack output = ItemStack.EMPTY;
      Component comp1 = Component.empty();
      Component comp2 = Component.empty();
      List<ChanceOutput> outputs = Collections.emptyList();

      if (cachedRecipe != null && !groveCrafter.isCrafting()) {
        output = cachedRecipe.getResultItem(mc.level.registryAccess());
        outputs = cachedRecipe.getChanceOutputs();
        comp1 = Component.translatable("roots.hud.grove_crafter");
        comp2 = output.getHoverName();
      }

      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);

      int ny = y + 18;

      for (ChanceOutput chanceOutput : outputs) {
        graphics.renderItem(chanceOutput.getOutput(), x, ny, 0);
        graphics.renderItemDecorations(mc.font, chanceOutput.getOutput(), x, ny);
        Component comp4 = Component.translatable("roots.tooltip.chance", String.format("%.2f", chanceOutput.getChance() * 100));
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.drawString(mc.font, comp4, x - 15, ny, 16777215, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();

        ny += 18;
      }

      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, 16777215, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, 16777215, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }

  public static void renderGroveStone(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {
    Level level = mc.level;

    BlockEntity blockEntity;

    if (!state.hasProperty(GroveStoneBlock.PART) && !state.hasProperty(GroveStoneBlock.ACTIVE)) {
      return;
    }

    if (!state.getValue(GroveStoneBlock.ACTIVE)) {
      return; // Render nothing for inactive grove stones
    }

    StateProperties.Part part = state.getValue(GroveStoneBlock.PART);

    BlockPos pos;

    if (part == StateProperties.Part.TOP) {
      pos = trace.getBlockPos();
    } else if (part == StateProperties.Part.BOTTOM) {
      pos = trace.getBlockPos().above(2);
    } else if (part == StateProperties.Part.MIDDLE) {
      pos = trace.getBlockPos().above();
    } else {
      return; // Somehow an invalid part? I'm not sure if this is even possible. I should make this a switch statement.
    }

    if (level.getBlockEntity(pos) instanceof IGroveInstance groveInstance) {
      int x = (graphics.guiWidth() / 2); // + (graphics.guiWidth() / 4);
      int y = (graphics.guiHeight() / 2);// + (graphics.guiHeight() / 4);

      y += 10;
      x += 30;

      GrovePower powerPower = groveInstance.getPower();
      Grove grove = groveInstance.asGrove();

      Component comp1 = Component.translatable("roots.hud.grove_power.grove", grove.getStyledName(), groveInstance.getRank(), groveInstance.getMaxRank());
      Component comp2 = Component.translatable("roots.hud.grove_power.power", powerPower.getUsedPower(), powerPower.getMaxPower());

/*      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);*/
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, 16777215, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, 16777215, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }

  private static Component getItemNameWithCount(ItemStack stack) {
    /*    if (stack.getCount() == 1) {*/
    return stack.getHoverName();
/*    } else {
      return Component.translatable("roots.hud.item_count", stack.getHoverName(), stack.getCount());
    }*/
  }

  public static void renderMortar(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {
    Level level = mc.level;
    Player player = mc.player;
    if (level.getBlockEntity(trace.getBlockPos()) instanceof MortarBlockEntity mortar) {
      int x = (graphics.guiWidth() / 2); // + (graphics.guiWidth() / 4);
      int y = (graphics.guiHeight() / 2);// + (graphics.guiHeight() / 4);

/*      y = y * 2;

      y = y + 20;
      x = x + 20;*/

      MortarRecipe cachedRecipe = mortar.getCachedRecipe() == null ? null : mortar.getCachedRecipe().value();
      MortarRecipe lastRecipe = mortar.getLastRecipe() == null ? null : mortar.getLastRecipe().value();

      boolean empty = mortar.getInventory().isEmpty();
      boolean pestle = player.getMainHandItem().is(RootsTags.Items.MORTAR_ACTIVATION) || player.getOffhandItem()
          .is(RootsTags.Items.MORTAR_ACTIVATION);
      boolean emptyHand = player.getMainHandItem().isEmpty();
      boolean pestleStored = mortar.getInventory().getStackInSlot(0).is(RootsTags.Items.MORTAR_ACTIVATION);

      if (!empty) {
        float angle = -90;
        int radius = 24;
        List<ItemStack> nonEmpty = mortar.getNonEmptyItems();
        float anglePer = 360f / nonEmpty.size();

        for (ItemStack stack : nonEmpty) {
          double xPos = x + Math.cos(angle * Math.PI / 180) * radius - 8;
          double yPos = y + Math.sin(angle * Math.PI / 180) * radius - 8;
          graphics.renderItem(stack, (int) xPos, (int) yPos, 0);
          graphics.renderItemDecorations(mc.font, stack, (int) xPos, (int) yPos);
          angle += anglePer;
        }
      }

      y += 10;
      x += 30;


      ItemStack output = ItemStack.EMPTY;
      Spell spell = null;
      Component comp1 = Component.empty();
      Component comp2 = Component.empty();
      Component comp3 = Component.empty();
      Component comp4 = Component.empty();
      List<ChanceOutput> outputs = Collections.emptyList();

      if (cachedRecipe != null) {
        Either<ItemStack, Spell> outputItemOrSpell = cachedRecipe.getOutputItemOrSpell(mc.level.registryAccess());
        if (outputItemOrSpell.left().isEmpty()) {
          spell = outputItemOrSpell.right().orElse(null);
        } else {
          output = outputItemOrSpell.left().orElse(ItemStack.EMPTY);
        }
        outputs = cachedRecipe.getChanceOutputs();
        comp1 = Component.translatable("roots.hud.mortar.crafting1");
        int val = (cachedRecipe.getTimes() - mortar.getUses());
        if (val == 1) {
          comp2 = Component.translatable("roots.hud.mortar.crafting2", val);
        } else {
          comp2 = Component.translatable("roots.hud.mortar.crafting3", val);
        }
        comp3 = spell != null ? spell.getStyledName() : getItemNameWithCount(output);
      } else if (lastRecipe != null && empty) {
        Either<ItemStack, Spell> outputItemOrSpell = lastRecipe.getOutputItemOrSpell(mc.level.registryAccess());
        outputs = lastRecipe.getChanceOutputs();
        if (outputItemOrSpell.left().isEmpty()) {
          spell = outputItemOrSpell.right().orElse(null);
        } else {
          output = outputItemOrSpell.left().orElse(ItemStack.EMPTY);
        }

        comp1 = Component.translatable("roots.hud.mortar.repeat1");
        comp2 = Component.translatable("roots.hud.mortar.repeat2");
        comp3 = spell == null ? getItemNameWithCount(output) : spell.getStyledName();
      }

      if (empty && pestle) {
        comp4 = Component.translatable("roots.hud.mortar.store_pestle");
      } else if (pestleStored && emptyHand) {
        comp4 = Component.translatable("roots.hud.mortar.remove_pestle");
      }

      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);

      int ny = y + 18;

      for (ChanceOutput chanceOutput : outputs) {
        graphics.renderItem(chanceOutput.getOutput(), x, ny, 0);
        graphics.renderItemDecorations(mc.font, chanceOutput.getOutput(), x, ny);
        String comp5 = String.format("%s%%", chanceOutput.getChance() * 100);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.drawString(mc.font, "~", x - 12, ny + 8, 16777215, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        ny += 16;
      }
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, 16777215, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, 16777215, true);
      graphics.drawString(mc.font, comp3, x + 25, y + 24, 16777215, true);
      graphics.drawString(mc.font, comp4, x + 25, y - 24, 16777215, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }
}
