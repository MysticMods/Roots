package epicsquid.roots.client.hud;

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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
@SuppressWarnings("Duplicates")
@Mod.EventBusSubscriber(modid = Roots.MODID, value = Side.CLIENT)
public class RenderHerbHUD {
  public static RenderHerbHUD INSTANCE = new RenderHerbHUD();

  private Deque<HerbAlert> slots = new ArrayDeque<>();
  private Map<Herb, HerbAlert> alerts = new HashMap<>();

  public double herbAmount(Herb herb) {
    HerbAlert alert = getAlert(herb);
    return alert.getAmount();
  }

  public void updateHerb(Herb herb, double amount) {
    HerbAlert alert = getAlert(herb);
    alert.setAmount(amount);
    if (alert.invalid()) {
      slots.addFirst(alert);
    }
    alert.show();
  }

  private HerbAlert getAlert(Herb herb) {
    HerbAlert alert = alerts.get(herb);
    if (alert == null) {
      alert = new HerbAlert(herb);
      alerts.put(herb, alert);
    }
    return alert;
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void renderHUD(RenderGameOverlayEvent.Post event) {
    if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
      ScaledResolution res = event.getResolution();
      float partial = event.getPartialTicks();
      int i = 0;
      for (HerbAlert alert : INSTANCE.slots) {
        alert.render(res, partial, i);
        i++;
      }
      INSTANCE.slots.removeIf(HerbAlert::invalid);
    }
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void clientTick(TickEvent.ClientTickEvent event) {
    INSTANCE.slots.forEach(HerbAlert::tick);
  }

  public void resolveSlots(EntityPlayer player, Herb herb, double amount) {
    if (!player.world.isRemote) {
      return;
    }

    Minecraft mc = Minecraft.getMinecraft();
    if (player.getUniqueID() != mc.player.getUniqueID()) {
      return;
    }

    updateHerb(herb, amount);
  }

  public class HerbAlert {
    private static final int TIME_VISIBLE = 8 * 20;
    private static final int MAX_TIME = TIME_VISIBLE;
    private static final int ANIM_TIME = 5;

    private int ticks = 0;
    private Herb herb;
    private ItemStack stack = null;
    private double amount;

    public HerbAlert(Herb herb) {
      this.herb = herb;
    }

    public void show () {
      this.ticks = TIME_VISIBLE;
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

    public boolean invalid () {
      return ticks <= 0;
    }

    public void tick() {
      if (ticks > 0) {
        ticks--;
      }
    }

    public void render(ScaledResolution res, float partialTicks, int slot) {
      Roots.logger.info(slot);
      if (ticks == 0) {
        return;
      }

      Minecraft mc = Minecraft.getMinecraft();

      float progress;

      int row = slot / 3;
      int col = slot % 3;

      int anim_time = ANIM_TIME * (row+1);

      if (ticks < anim_time) {
        progress = Math.max(0, ticks - partialTicks) / anim_time;
      } else {
        progress = Math.min(anim_time, (MAX_TIME - ticks) + partialTicks) / anim_time;
      }

      float anim = -progress * (progress - 2) * 20f;

      float x = res.getScaledWidth() / 2.0f;
      float y = res.getScaledHeight() - anim;

      if (row != 0) {
        y -= row * 20;
      }

      int barWidth = 190 + 58;
      if (!mc.player.getHeldItemOffhand().isEmpty()) {
        barWidth += 58;
      }
      x += ((barWidth / 2.0) * -1 + (col * 35)) - 75;

      ItemStack stack = getStack();

      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, 0);
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
      String s = String.format("%.1f", amount);
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.disableBlend();
      mc.fontRenderer.drawStringWithShadow(s, 18.0f, 3.5f, 16777215);
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      GlStateManager.enableBlend();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.popMatrix();
    }
  }
}
