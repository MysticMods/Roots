package epicsquid.roots.util;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
@SuppressWarnings("Duplicates")
@Mod.EventBusSubscriber(modid= Roots.MODID, value=Side.CLIENT)
public class HerbHud {
  private static HerbAlert slot1 = null;
  private static HerbAlert slot2 = null;
  private static Map<Herb, HerbAlert> alerts = new HashMap<>();

  public static double herbAmount (Herb herb) {
    HerbAlert alert = getAlert(herb);
    return alert.getAmount();
  }

  public static void updateHerb (Herb herb, double amount) {
    HerbAlert alert = getAlert(herb);
    alert.setAmount(amount);
  }

  private static HerbAlert getAlert(Herb herb) {
    if (!alerts.containsKey(herb)) {
      HerbAlert alert = new HerbAlert(herb);
      alerts.put(herb, alert);
    }

    return alerts.get(herb);
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void renderHUD(RenderGameOverlayEvent.Post event) {
    if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
      ScaledResolution res = event.getResolution();
      float partial = event.getPartialTicks();
      if (slot1 != null) {
        if (slot1.active()) {
          slot1.render(res, partial);
        } else {
          slot1.setSlot(-1);
          slot1 = null;
        }
      }
      if (slot2 != null) {
        if (slot2.active()) {
          slot2.render(res, partial);
        } else {
          slot2.setSlot(-1);
          slot2 = null;
        }
      }
    }
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void clientTick(TickEvent.ClientTickEvent event) {
    if (slot1 != null) {
      slot1.tick(event);
    }
    if (slot2 != null) {
      slot2.tick(event);
    }
  }

  public static void resolveSlots(EntityPlayer player, Herb herb, double amount) {
    if (!player.world.isRemote) return;

    Minecraft mc = Minecraft.getMinecraft();
    if (player.getUniqueID() != mc.player.getUniqueID()) return;

    updateHerb(herb, amount);

    if (slot1 != null && !slot1.active()) {
      slot1.setSlot(-1);
      slot1 = null;
    }

    if (slot2 != null && !slot2.active()) {
      slot2.setSlot(-1);
      slot2 = null;
    }

    if (slot1 == null && slot2 != null) {
      slot1 = slot2;
      slot2 = null;
      slot1.setSlot(1);
    }

    if (slot1 == getAlert(herb)) {
      slot1.refresh();
    } else if (slot2 == getAlert(herb)) {
      slot2.refresh();
    } else {
      if (slot1 == null) {
        slot1 = getAlert(herb);
        slot1.setSlot(1);
        slot1.enable();
      } else if (slot2 == null) {
        slot2 = getAlert(herb);
        slot2.setSlot(2);
        slot2.enable();
      }
    }
  }

  public static class HerbAlert {
    private static final int TIME_VISIBLE = 8 * 20;
    private static final int MAX_TIME = TIME_VISIBLE;
    private static final int ANIM_TIME = 5;

    private int ticks = 0;
    private int slot = 0;
    private Herb herb;
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

    public void refresh() {
      this.ticks = TIME_VISIBLE - ANIM_TIME;
    }

    public void setSlot(int slot) {
      this.slot = slot;
    }

    public void enable() {
      this.ticks = TIME_VISIBLE;
    }

    public ItemStack getStack() {
      if (stack == null) {
        stack = new ItemStack(herb.getItem());
      }

      return stack;
    }

    public boolean active() {
      return slot != -1 && ticks > 0;
    }

    @SideOnly(Side.CLIENT)
    public void tick(TickEvent.ClientTickEvent event) {
      if (ticks == -1) return;

      ticks--;
    }

    @SideOnly(Side.CLIENT)
    public void render(ScaledResolution res, float partialTicks) {
      if (ticks == 0) return;

      Minecraft mc = Minecraft.getMinecraft();

      float progress;

      if (ticks < ANIM_TIME) {
        progress = Math.max(0, ticks - partialTicks) / ANIM_TIME;
      } else {
        progress = Math.min(ANIM_TIME, (MAX_TIME - ticks) + partialTicks) / ANIM_TIME;
      }

      float anim = -progress * (progress - 2) * 20f;

      float x = res.getScaledWidth() / 2.0f;
      float y = res.getScaledHeight() - anim; //res.getScaledHeight() - progress;

      int barWidth = 190 + 58;
      if (!mc.player.getHeldItemOffhand().isEmpty()) {
        barWidth += 58;
      }
      x += ((barWidth / 2.0) * -1 + slot * 40) - 95;


      ItemStack stack = getStack();

      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, 0);
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
      String s = String.format("%.1f", amount); //ServerHerbUtil.getHerbAmount(mc.player, herb));
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.disableBlend();
      mc.fontRenderer.drawStringWithShadow(s, 18.0f, 3.5f, 16777215);
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      // Fixes opaque cooldownLeft overlay a bit lower
      // TODO: check if enabled blending still screws things up down the line.
      GlStateManager.enableBlend();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.popMatrix();
    }
  }
}
