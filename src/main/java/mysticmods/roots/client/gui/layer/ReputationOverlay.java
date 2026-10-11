package mysticmods.roots.client.gui.layer;

import com.google.common.collect.MapMaker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveValue;
import mysticmods.roots.api.grove.ReputationProgress;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentMap;

public class ReputationOverlay {
  public static final Deque<ReputationAlert> slots = new ArrayDeque<>();
  private static final ConcurrentMap<GroveInternKey, ReputationAlert> VALUES = (new MapMaker()).weakValues().makeMap();

  public static ReputationAlert getOrCreate(Grove grove, int amount) {
    IGroveValue.Type type = amount <= 0 ? IGroveValue.Type.REPUTATION_LOSS : IGroveValue.Type.REPUTATION_GAIN;
    return getOrCreate(grove, type);
  }

  public static ReputationAlert getOrCreate(Grove grove, IGroveValue.Type type) {
    return VALUES.computeIfAbsent(new GroveInternKey(grove, type), (key) -> new ReputationAlert(key.getGrove(), key.type));
  }

  public static void updateReputationAlert(Grove grove, int amount, ReputationProgress progress) {
    ReputationAlert alert = getOrCreate(grove, amount);
    alert.setProgress(progress);

    if (alert.invalid()) {
      alert.setAmount(amount);
      slots.addFirst(alert);
    } else {
      alert.addAmount(amount);
    }
    alert.show();
  }

  public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    PoseStack stack = guiGraphics.pose();
    float partialTicks = deltaTracker.getRealtimeDeltaTicks();
    int i = 0;
    for (ReputationAlert alert : slots) {
      alert.render(guiGraphics, stack, partialTicks, i++);
    }
    slots.removeIf(ReputationAlert::invalid);
  }

  public static void tick() {
    for (ReputationAlert alert : slots) {
      alert.tick();
    }
  }

  record GroveInternKey(ResourceLocation grove, IGroveValue.Type type) {
    public GroveInternKey(Grove grove, IGroveValue.Type type) {
      this(grove.builtInRegistryHolder().getKey().location(), type);
    }

    public Grove getGrove() {
      return RootsRegistries.GROVES.get(grove);
    }
  }

  public static class ReputationAlert {
    private static final int TIME_VISIBLE = 10 * 20;
    private static final int MAX_TIME = TIME_VISIBLE;
    private static final int ANIMATION_TIME = 5;
    private static final int COL_WIDTH = 65;

    private int ticks = 0;
    private final ItemStack stack;
    private final IGroveValue.Type type;
    private int amount;
    private ReputationProgress progress;

    public ReputationAlert(Grove grove, IGroveValue.Type type) {
      this.stack = grove.getIcon();
      this.type = type;
    }

    public void setProgress (ReputationProgress progress) {
      this.progress = progress;
    }

    public ReputationProgress getProgress () {
      return this.progress;
    }

    public int getAmount() {
      return amount;
    }

    public void setAmount(int amount) {
      this.amount = amount;
    }

    public boolean invalid() {
      return ticks <= 0;
    }

    public void tick() {
      if (ticks > 0) {
        ticks--;
      }
    }

    public void show() {
      if (this.ticks > 0) {
        this.ticks = TIME_VISIBLE - ANIMATION_TIME;
      } else {
        this.ticks = TIME_VISIBLE;
      }
    }

    private static int getStartX(Minecraft mc, GuiGraphics graphics) {
      int x = graphics.guiWidth() / 2 + 91 + 5; // right edge of hotbar + padding
      if (mc.player.getMainArm() == HumanoidArm.LEFT) {
        // offhand slot is drawn on the right for left-handed players
        if (!mc.player.getOffhandItem().isEmpty()) {
          x += 29;
        }
      } else if (mc.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
        // hotbar attack indicator sits on the right for right-handed players
        x += 24;
      }
      return x;
    }

    public void render(GuiGraphics graphics, PoseStack pose, float partialTicks, int slot) {
      if (ticks == 0) {
        return;
      }

      Minecraft mc = Minecraft.getInstance();
      if (mc == null || mc.player == null) {
        return;
      }

      float progress;

      int startX = getStartX(mc, graphics);
      int available = graphics.guiWidth() - startX - 5;
      int maxColumns = Math.min(2, Math.max(1, available / COL_WIDTH));
      int row = slot / maxColumns;
      int col = slot % maxColumns;

      int anim_time = ANIMATION_TIME * (row + 1);

      if (ticks < anim_time) {
        progress = Math.max(0, ticks - partialTicks) / anim_time;
      } else {
        progress = Math.min(anim_time, (MAX_TIME - ticks) + partialTicks) / anim_time;
      }

      float anim = -progress * (progress - 2) * 20f;

      float y = graphics.guiHeight() - anim - row * 20;
      float x = startX + col * COL_WIDTH;

      var atlas = Minecraft.getInstance().getTextureAtlas(RootsAPI.OVERLAYS_ATLAS_FILE);
      var overlaySprite = atlas.apply(type == IGroveValue.Type.REPUTATION_GAIN ? RootsAPI.rl("gui/grove_reputation_gain_symbol") : RootsAPI.rl("gui/grove_reputation_loss_symbol"));

      pose.pushPose();
      pose.translate(x, y, 0);
      graphics.renderItem(stack, 0, 0, 0);
      pose.translate(0.0F, 0.0F, 200.0F);
      graphics.blit(0, 0, 0, 16, 16, overlaySprite);
      FormattedCharSequence number = Component.literal(String.valueOf(amount))
          .setStyle(Style.EMPTY.withColor(type.color())).getVisualOrderText();
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, number, 18.0f, 0 /*3.5f*/, 16777215, true);
      pose.popPose();
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }

    public void addAmount(int amount) {
      this.amount += amount;
    }
  }
}
