package epicsquid.roots.util;

import com.mojang.blaze3d.platform.GlStateManager;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.PouchItem;
import epicsquid.roots.item.SylvanArmorItem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class PowderInventoryUtil {
  private static Map<Herb, HerbAlert> alerts = new HashMap<>();
  private static HerbAlert slot1 = null;
  private static HerbAlert slot2 = null;

  public static ItemStack getPouch(PlayerEntity player) {
/*    if (Loader.isModLoaded("baubles")) {
      ItemStack stack = BaublePowderInventoryUtil.getPouch(player);
      if (!stack.isEmpty()) return stack;
    }*/

    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof PouchItem) {
        return player.inventory.getStackInSlot(i);
      }
    }

    return ItemStack.EMPTY;
  }

  public static double getPowderTotal(PlayerEntity player, Herb herb) {
    ItemStack pouch = getPouch(player);
    if (pouch.isEmpty()) return -1.0;

    // Hard-coding for creative pouch
    if (pouch.getItem() == ModItems.creative_pouch) return 999;

    return PouchItem.getHerbQuantity(pouch, herb);
  }

  public static void removePowder(PlayerEntity player, Herb herb, double amount) {
    ItemStack pouch = getPouch(player);
    if (pouch.isEmpty() || pouch.getItem() == ModItems.creative_pouch) return;

    // TODO: Cost reduction is calculated here
    amount -= amount * SylvanArmorItem.sylvanBonus(player);

    PouchItem.useQuantity(pouch, herb, amount);
    resolveSlots(player, herb);
  }

  public static void resolveSlots(PlayerEntity player, Herb herb) {
    if (!player.world.isRemote) return;

    Minecraft mc = Minecraft.getInstance();
    if (player.getUniqueID() != mc.player.getUniqueID()) return;

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

  public static HerbAlert getAlert(Herb herb) {
    if (!alerts.containsKey(herb)) {
      HerbAlert alert = new HerbAlert(herb);
      alerts.put(herb, alert);
    }

    return alerts.get(herb);
  }

  // TODO: THIS SHOULD NOT BE HERE
  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void renderHUD(RenderGameOverlayEvent.Post event) {
    if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
      MainWindow res = event.getWindow();
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
  @OnlyIn(Dist.CLIENT)
  public static void clientTick(TickEvent.ClientTickEvent event) {
    if (slot1 != null) {
      slot1.tick(event);
    }
    if (slot2 != null) {
      slot2.tick(event);
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

    public HerbAlert(Herb herb) {
      this.herb = herb;
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

    @OnlyIn(Dist.CLIENT)
    public void tick(TickEvent.ClientTickEvent event) {
      if (ticks == -1) return;

      ticks--;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(MainWindow res, float partialTicks) {
      if (ticks == 0) return;

      Minecraft mc = Minecraft.getInstance();

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
      GlStateManager.translated(x, y, 0);
      RenderHelper.enableGUIStandardItemLighting();
      mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
      String s = String.format("%.1f", getPowderTotal(mc.player, herb));
      GlStateManager.disableLighting();
      GlStateManager.disableDepthTest();
      GlStateManager.disableBlend();
      mc.fontRenderer.drawStringWithShadow(s, 18.0f, 3.5f, 16777215);
      GlStateManager.enableLighting();
      GlStateManager.enableDepthTest();
      // Fixes opaque cooldown overlay a bit lower
      // TODO: check if enabled blending still screws things up down the line.
      GlStateManager.enableBlend();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.popMatrix();
    }
  }
}