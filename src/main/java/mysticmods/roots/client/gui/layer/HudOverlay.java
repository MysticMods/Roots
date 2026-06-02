package mysticmods.roots.client.gui.layer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.blockentity.ClearableBlockEntity;
import mysticmods.roots.api.blockentity.FakeMenuBlockEntity;
import mysticmods.roots.api.client.RootsClientAPI;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GrovePowerGenerator;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.FungalTransmuterBlockEntity;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.item.GramaryItem;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = RootsAPI.MODID)
public class HudOverlay {
  private static final int MENU_POS_TIME_OUT = 20 * 5;
  public static final int TEXT_COLOR = 16777215;

  private static int menuPosCooldown = -1;

  private static @Nullable BlockPos menu_pos = null;

  private static boolean storeBlockPos(@Nullable BlockPos pos) {
    menu_pos = pos;
    return pos != null;
  }

  @Nullable
  public static BlockPos getStoredBlockPos() {
    return menu_pos;
  }

  public static boolean isStoredBlock(BlockPos pos) {
    return menu_pos != null && menu_pos.equals(pos);
  }

  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post event) {
    if (menuPosCooldown-- <= 0) {
      menu_pos = null;
    }
    if (menu_pos != null) {
      Minecraft mc = Minecraft.getInstance();
      if (mc == null || mc.player == null) {
        return;
      }
      Player player = mc.player;
      if (player.distanceToSqr(Vec3.atCenterOf(menu_pos)) >= 4 * 4) {
        menu_pos = null;
      }
    }
  }

  public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    PoseStack stack = guiGraphics.pose();
    float partialTicks = deltaTracker.getRealtimeDeltaTicks();

    Minecraft mc = Minecraft.getInstance();

    // Copied from 1.12.2, I have no idea of the relevance of this.
    if (mc.screen instanceof ChatScreen || mc.hitResult == null || mc.level == null) {
      return;
    }

    if (mc.getOverlay() != null || mc.options.hideGui) {
      return;
    }

    if (mc.hitResult.getType() == HitResult.Type.BLOCK) {
      BlockHitResult trace = ((BlockHitResult) mc.hitResult);
      BlockState state = mc.level.getBlockState(trace.getBlockPos());
      boolean changed;

      if (state.is(RootsTags.Blocks.PYRE_HUD_RENDERER)) {
        renderPyre(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
        changed = storeBlockPos(trace.getBlockPos());
      } else if (state.is(RootsTags.Blocks.GROVE_CRAFTER_HUD_RENDERER)) {
        renderGroveCrafter(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
        changed = storeBlockPos(trace.getBlockPos());
      } else if (state.is(RootsTags.Blocks.MORTAR_HUD_RENDERER)) {
        renderMortar(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
        changed = storeBlockPos(trace.getBlockPos());
      } else if (state.is(RootsTags.Blocks.GROVE_STONE_HUD_RENDERER)) {
        renderGroveStone(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
        changed = false;
      } else if (state.is(RootsTags.Blocks.TRANSMUTER_HUD_RENDERER)) {
        renderTransmuter(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, state);
        changed = storeBlockPos(trace.getBlockPos());
      } else {
        changed = false;
      }

      if (changed) {
        menuPosCooldown = MENU_POS_TIME_OUT;
      }
    } else if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
      EntityHitResult trace = ((EntityHitResult) mc.hitResult);

      if (trace.getEntity().getType()
          .is(RootsTags.Entities.SHOULD_RENDER_HUD) && trace.getEntity() instanceof LivingEntity living) {
        renderEntity(guiGraphics, stack, partialTicks, deltaTracker, mc, trace, living);

      }
    }

    if (getStoredBlockPos() != null) {
      renderFakeMenu(guiGraphics, stack, partialTicks, deltaTracker, mc);
      renderClear(guiGraphics, stack, partialTicks, deltaTracker, mc);
    }
  }

  private static List<Holder<Attribute>> ATTRIBUTES_HOLDER = null;

  private static void renderEntity(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker deltaTracker, Minecraft mc, EntityHitResult trace, LivingEntity entity) {
    if (RootsClientAPI.isGramaryMode(GramaryItem.GramaryMode.ENTITY_INFO)) {

      Level level = mc.level;
      if (mc.player == null || level == null) {
        return;
      }

      int x = (graphics.guiWidth() / 2); // + (graphics.guiWidth() / 4);
      int y = (graphics.guiHeight() / 2);// + (graphics.guiHeight() / 4);

      y += 10;
      x += 30;

      List<Component> components = new ArrayList<>();
      if (ATTRIBUTES_HOLDER == null) {
        ATTRIBUTES_HOLDER = new ArrayList<>();
        BuiltInRegistries.ATTRIBUTE.getTag(RootsTags.Attributes.GRAMARY_ATTRIBUTES)
            .ifPresent(o -> o.forEach(v -> ATTRIBUTES_HOLDER.add(v)));
        ATTRIBUTES_HOLDER.sort(Comparator.comparing(o -> o.getKey().location().toString()));
      }

      for (Holder<Attribute> o : ATTRIBUTES_HOLDER) {
        AttributeInstance instance = entity.getAttribute(o);
        if (instance != null) {
          String visualValue = String.format("%.2f", instance.getValue());
          ChatFormatting style = o.value().getStyle(true);
          Component name = Component.translatable(o.value().getDescriptionId());
          name.getStyle().applyFormat(style);
          components.add(Component.translatable("roots.hud.attributes", name, visualValue));
        }
      }

      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      for (Component comp : components) {
        graphics.drawString(mc.font, comp, x + 25, y, TEXT_COLOR, true);
        y += 12;
      }
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }

  // TODO: Handle conditions
  public static void renderPyre(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {
    Level level = mc.level;
    if (level.getBlockEntity(trace.getBlockPos()) instanceof PyreBlockEntity pyre) {
      boolean active = state.getValue(PyreBlock.ACTIVE);

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
      Component comp1 = CommonComponents.EMPTY;
      Component comp2 = CommonComponents.EMPTY;
      Component comp3 = CommonComponents.EMPTY;
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
      } else if (lastRecipe != null && empty) {
        output = lastRitual != null ? lastRitual.getIcon() : lastRecipe.getResultItem(mc.level.registryAccess());
        outputs = lastRecipe.getChanceOutputs();
        comp1 = Component.translatable("roots.hud.pyre.restart1");
        comp2 = Component.translatable("roots.hud.pyre.restart2");
        comp3 = nextRitual != null ? nextRitual.getName() : output.getHoverName();
      }

      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);

      int baseX = x - 24;
      int baseY = y + 18;
      int columnSpacing = 25;
      int rowSpacing = 18;
      int itemsPerRow = 2;

      for (int i = 0; i < outputs.size(); i++) {
        ChanceOutput chanceOutput = outputs.get(i);
        int col = i % itemsPerRow;
        int row = i / itemsPerRow;

        int xPos = baseX + col * columnSpacing;
        int yPos = baseY + row * rowSpacing;

        graphics.renderItem(chanceOutput.output(), xPos, yPos, 0);
        graphics.renderItemDecorations(mc.font, chanceOutput.output(), xPos, yPos);

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.drawString(mc.font, "~", xPos - 8, yPos + 8, TEXT_COLOR, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
      }


      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp3, x + 25, y + 24, TEXT_COLOR, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }

  public static void renderTransmuter(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {
    Level level = mc.level;
    if (level.getBlockEntity(trace.getBlockPos()) instanceof FungalTransmuterBlockEntity transmuter) {
      int x = (graphics.guiWidth() / 2); // + (graphics.guiWidth() / 4);
      int y = (graphics.guiHeight() / 2);// + (graphics.guiHeight() / 4);

      boolean crafting = transmuter.isCrafting();

      boolean empty = transmuter.getInventory().isEmpty();

      if (!empty) {
        float angle = -90;
        int radius = 24;
        List<ItemStack> nonEmpty = transmuter.getNonEmptyItems();
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

      TransmutationRecipe cachedRecipe = transmuter.getCachedRecipe() == null ? null : transmuter.getCachedRecipe()
          .value();
      TransmutationRecipe lastRecipe = transmuter.getLastRecipe() == null ? null : transmuter.getLastRecipe().value();

      int requiredPower = 0;

      ItemStack output = ItemStack.EMPTY;
      Component comp1 = CommonComponents.EMPTY;
      Component comp2 = CommonComponents.EMPTY;
      Component comp3 = CommonComponents.EMPTY;
      List<ChanceOutput> outputs = Collections.emptyList();

      if (cachedRecipe != null) {
        output = cachedRecipe.getResultItem(mc.level.registryAccess());
        comp1 = Component.translatable("roots.hud.transmuter.begin1");
        comp2 = Component.translatable("roots.hud.transmuter.begin2");
        comp3 = Component.translatable("roots.hud.transmuter.begin3", output.getHoverName());
        requiredPower = cachedRecipe.getPower();
        outputs = cachedRecipe.getChanceOutputs();
      } else if (lastRecipe != null && empty) {
        output = lastRecipe.getResultItem(mc.level.registryAccess());
        outputs = lastRecipe.getChanceOutputs();
        if (crafting) {

        } else {

        }
        requiredPower = lastRecipe.getPower();
        comp1 = Component.translatable("roots.hud.transmuter.restart1");
        comp2 = Component.translatable("roots.hud.transmuter.restart2");
        comp3 = output.getHoverName();
      }

      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);

      int baseX = x - 24;
      int baseY = y + 18;
      int columnSpacing = 25;
      int rowSpacing = 18;
      int itemsPerRow = 2;

      Component comp4 = Component.translatable("roots.hud.transmuter.power", requiredPower, transmuter.getPower()/*, transmuter.getMaxPower()*/);

      for (int i = 0; i < outputs.size(); i++) {
        ChanceOutput chanceOutput = outputs.get(i);
        int col = i % itemsPerRow;
        int row = i / itemsPerRow;

        int xPos = baseX + col * columnSpacing;
        int yPos = baseY + row * rowSpacing;

        graphics.renderItem(chanceOutput.output(), xPos, yPos, 0);
        graphics.renderItemDecorations(mc.font, chanceOutput.output(), xPos, yPos);

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.drawString(mc.font, "~", xPos - 8, yPos + 8, TEXT_COLOR, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
      }


      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp4, x + 25, y - 24, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp1, x + 25, y, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp3, x + 25, y + 24, TEXT_COLOR, true);
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
      Component comp1 = CommonComponents.EMPTY;
      Component comp2 = CommonComponents.EMPTY;
      List<ChanceOutput> outputs = Collections.emptyList();

      if (cachedRecipe != null && !groveCrafter.isCrafting()) {
        output = cachedRecipe.getResultItem(mc.level.registryAccess());
        outputs = cachedRecipe.getChanceOutputs();
        comp1 = Component.translatable("roots.hud.grove_crafter");
        comp2 = output.getHoverName();
      }

      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);


      int baseX = x - 24;
      int baseY = y + 18;
      int columnSpacing = 25;
      int rowSpacing = 18;
      int itemsPerRow = 2;

      for (int i = 0; i < outputs.size(); i++) {
        ChanceOutput chanceOutput = outputs.get(i);
        int col = i % itemsPerRow;
        int row = i / itemsPerRow;

        int xPos = baseX + col * columnSpacing;
        int yPos = baseY + row * rowSpacing;

        graphics.renderItem(chanceOutput.output(), xPos, yPos, 0);
        graphics.renderItemDecorations(mc.font, chanceOutput.output(), xPos, yPos);

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.drawString(mc.font, "~", xPos - 8, yPos + 8, TEXT_COLOR, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
      }


      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, TEXT_COLOR, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }

  public static void renderGroveStone(GuiGraphics graphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft mc, BlockHitResult trace, BlockState state) {
    Level level = mc.level;

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

      GrovePowerGenerator powerPower = groveInstance.getPower();
      Grove grove = groveInstance.asGrove();

      Component comp1 = Component.translatable("roots.hud.grove_power.grove", grove.getStyledName(), groveInstance.getRank(), groveInstance.getMaxRank());
      Component comp2 = Component.translatable("roots.hud.grove_power.power", powerPower.getUsedPower(), powerPower.getMaxPower());
      if (groveInstance.getRank() == 0) {
        comp2 = Component.translatable("roots.hud.grove_power.invalid_rank");
      }

/*      graphics.renderItem(output, x, y, 0);
      graphics.renderItemDecorations(mc.font, output, x, y);*/
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, TEXT_COLOR, true);
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
      Component comp1 = CommonComponents.EMPTY;
      Component comp2 = CommonComponents.EMPTY;
      Component comp3 = CommonComponents.EMPTY;
      Component comp4 = CommonComponents.EMPTY;
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

      int baseX = x - 24;
      int baseY = y + 18;
      int columnSpacing = 25;
      int rowSpacing = 18;
      int itemsPerRow = 2;

      for (int i = 0; i < outputs.size(); i++) {
        ChanceOutput chanceOutput = outputs.get(i);
        int col = i % itemsPerRow;
        int row = i / itemsPerRow;

        int xPos = baseX + col * columnSpacing;
        int yPos = baseY + row * rowSpacing;

        graphics.renderItem(chanceOutput.output(), xPos, yPos, 0);
        graphics.renderItemDecorations(mc.font, chanceOutput.output(), xPos, yPos);

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.drawString(mc.font, "~", xPos - 8, yPos + 8, TEXT_COLOR, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
      }

      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, comp1, x + 25, y, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp2, x + 25, y + 12, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp3, x + 25, y + 24, TEXT_COLOR, true);
      graphics.drawString(mc.font, comp4, x + 25, y - 24, TEXT_COLOR, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }

  public static boolean shouldShowInsert(BlockPos pos) {
    Minecraft mc = Minecraft.getInstance();
    Level level = mc.level;
    return level != null && pos != null && level.getBlockEntity(pos) instanceof FakeMenuBlockEntity fake && fake.shouldShowInsert();
  }

  public static boolean shouldShowDelete(BlockPos pos) {
    Minecraft mc = Minecraft.getInstance();
    Level level = mc.level;
    return level != null && pos != null && level.getBlockEntity(pos) instanceof ClearableBlockEntity clearable && clearable.canClear();
  }

  public static void renderFakeMenu(GuiGraphics guiGraphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft minecraft) {
    Level level = minecraft.level;
    if (level != null && getStoredBlockPos() != null && shouldShowInsert(getStoredBlockPos()) && ConfigManager.SHOW_INSERT_MESSAGE.getAsBoolean()) {
      Gui gui = minecraft.gui;
      Font font = gui.getFont();
      minecraft.getProfiler().push("overlayMessage");
      int yShift = Math.max(gui.leftHeight, gui.rightHeight) + (68 - 59);
      @SuppressWarnings("DataFlowIssue") int j = ChatFormatting.YELLOW.getColor();
      guiGraphics.pose().pushPose();
      guiGraphics.pose()
          .translate((float) (guiGraphics.guiWidth() / 2), (float) (guiGraphics.guiHeight() - Math.max(yShift, 68)), 0.0F);
      Component overlayMessageString = Component.translatable("roots.hud.fake_menu", Component.keybind(KeyBindings.OPEN_FAKE_MENU.getName()), level.getBlockState(getStoredBlockPos())
          .getBlock().getName());

      int k = font.width(overlayMessageString);
      guiGraphics.drawStringWithBackdrop(font, overlayMessageString, -k / 2, -6, k, j);
      guiGraphics.pose().popPose();

      minecraft.getProfiler().pop();
    }
  }

  public static void renderClear(GuiGraphics guiGraphics, PoseStack pose, float partialTicks, DeltaTracker delta, Minecraft minecraft) {
    Level level = minecraft.level;
    if (level != null && getStoredBlockPos() != null && shouldShowDelete(getStoredBlockPos()) && ConfigManager.SHOW_DELETE_MESSAGE.getAsBoolean()) {
      Gui gui = minecraft.gui;
      Font font = gui.getFont();
      minecraft.getProfiler().push("overlayMessage");
      int yShift = Math.max(gui.leftHeight, gui.rightHeight) + (68 - 59);
      int j = TEXT_COLOR;
      guiGraphics.pose().pushPose();
      guiGraphics.pose()
          .translate((float) (guiGraphics.guiWidth() / 2), (float) (guiGraphics.guiHeight() - Math.max(yShift, 68)), 0.0F);
      Component overlayMessageString = Component.translatable("roots.hud.clear", Component.keybind(KeyBindings.CLEAR_CONTAINER.getName()), level.getBlockState(getStoredBlockPos())
          .getBlock().getName());

      int k = font.width(overlayMessageString);
      guiGraphics.drawStringWithBackdrop(font, overlayMessageString, -k / 2, 8, k, j);
      guiGraphics.pose().popPose();

      minecraft.getProfiler().pop();
    }
  }
}
