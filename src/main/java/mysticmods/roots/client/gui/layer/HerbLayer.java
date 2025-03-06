package mysticmods.roots.client.gui.layer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.client.RenderUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class HerbLayer {
  public static final Deque<HerbAlert> slots = new ArrayDeque<>();
  private static final Map<Herb, HerbAlert> alerts = new HashMap<>();

  public static double herbAmount(Herb herb) {
    HerbAlert alert = getAlert(herb);
    return alert.getAmount();
  }

  public static void updateHerb(Herb herb, double amount) {
    HerbAlert alert = getAlert(herb);
      alert.setAmount(amount);
      if (alert.invalid()) {
        slots.addFirst(alert);
      }
      alert.show();
  }

  public static HerbAlert getAlert(Herb herb) {
    HerbAlert alert = alerts.get(herb);
    if (alert == null) {
      alert = new HerbAlert(herb);
      alerts.put(herb, alert);
    }
    return alert;
  }

  public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    PoseStack stack = guiGraphics.pose();
    float partialTicks = deltaTracker.getRealtimeDeltaTicks();
    int i = 0;
    for (HerbAlert alert : slots) {
      alert.render(guiGraphics, stack, partialTicks, i++);
    }
    slots.removeIf(HerbAlert::invalid);
  }

  public static void tick() {
    for (HerbAlert alert : slots) {
      alert.tick();
    }
  }

  public static class HerbAlert {
    private static final int TIME_VISIBLE = 8 * 20;
    private static final int MAX_TIME = TIME_VISIBLE;
    private static final int ANIMATION_TIME = 5;

    private int ticks = 0;
    private final Herb herb;
    private ItemStack stack = null;
    private double amount;

    public HerbAlert(Herb herb) {
      this.herb = herb;
    }

    public double getAmount() {
      return amount;
    }

    public void setAmount(double amount) {
      this.amount = amount;
    }

    public ItemStack getStack() {
      if (stack == null) {
        stack = new ItemStack(herb.getItem());
      }

      return stack;
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
      this.ticks = TIME_VISIBLE;
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

      int row = slot / 3;
      int col = slot % 3;

      int anim_time = ANIMATION_TIME * (row + 1);

      if (ticks < anim_time) {
        progress = Math.max(0, ticks - partialTicks) / anim_time;
      } else {
        progress = Math.min(anim_time, (MAX_TIME - ticks) + partialTicks) / anim_time;
      }

      float anim = -progress * (progress - 2) * 20f;


      float x = graphics.guiWidth() / 2.0f;
      float y = graphics.guiHeight() - anim;

      if (row != 0) {
        y -= row * 20;
      }

      int barWidth = 90 + 58;
      if (!mc.player.getOffhandItem().isEmpty()) {
        barWidth += 58;
      }
      x += ((barWidth / 2.0) * -1 + (col * 35)) - 75;

      ItemStack stack = getStack();

      pose.pushPose();
      pose.translate(x, y, 0);
      graphics.renderItem(stack, 0, 0, 0);
      String s = String.format("%.1f", amount);
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      graphics.drawString(mc.font, s, 19.0f, 3.5f, 16777215, true);
      pose.popPose();
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
    }
  }
}
