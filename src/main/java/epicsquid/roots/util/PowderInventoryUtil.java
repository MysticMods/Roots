package epicsquid.roots.util;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.integration.baubles.pouch.BaublePowderInventoryUtil;
import epicsquid.roots.item.ItemPouch;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid= Roots.MODID)
public class PowderInventoryUtil {
  private static List<HerbAlert> alertRef = new ArrayList<>();
  private static Map<Herb, HerbAlert> alerts = new HashMap<>();
  private static Int2ObjectArrayMap<HerbAlert> slotMap = new Int2ObjectArrayMap<>();

  private static int MAX_SLOTS = 4;

  private static ItemStack getPouch (EntityPlayer player) {
    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        return player.inventory.getStackInSlot(i);
      }
    }
    if (Loader.isModLoaded("baubles")) {
      return BaublePowderInventoryUtil.getPouch(player);
    }
  }

  public static double getPowderTotal(EntityPlayer player, Herb herb) {
    ItemStack pouch = getPouch(player);
    if (pouch.isEmpty()) return 0.0;

    return ItemPouch.getHerbQuantity(pouch, herb);
  }

  public static void removePowder(EntityPlayer player, Herb herb, double amount) {
    ItemStack pouch = getPouch(player);
    if (pouch.isEmpty()) return;

    ItemPouch.useQuantity(pouch, herb, amount);
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void renderHUD (RenderGameOverlayEvent.Post event) {
    if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
      ScaledResolution res = event.getResolution();
      EntityPlayer player = Minecraft.getMinecraft().player;
      float partial = event.getPartialTicks();
    }
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void clientTick (TickEvent.ClientTickEvent event) {
    alerts.forEach((k, v) -> v.tick(event));
  }

  @SideOnly(Side.CLIENT)
  public static int getNextSlot () {
    List<HerbAlert> inUse = new ArrayList<>();


  }

  public class HerbAlert {
    private static final int MAX_TIME = 60;
    private static final int ANIM_TIME = 5;

    private int ticks = 0;
    private int slot = -1;
    private Herb herb;
    private double curCount;

    public HerbAlert (Herb herb) {
      this.herb = herb;
    }

    public boolean active () {
      return ticks > 0;
    }

    @SideOnly(Side.CLIENT)
    public void tick (TickEvent.ClientTickEvent event) {
      if (ticks == -1) return;

      ticks--;
    }

    @SideOnly(Side.CLIENT)
    public void render (ScaledResolution res, EntityPlayer player, boolean invert, float partialTicks) {
      Minecraft mc = Minecraft.getMinecraft();

      float progress;

      if (ticks < ANIM_TIME) {
        progress = Math.max(0, ticks - partialTicks) / ANIM_TIME;
      } else {
        progress = Math.min(ANIM_TIME, (MAX_TIME - ticks) + partialTicks) / ANIM_TIME;
      }

      EnumHandSide side = player.getPrimaryHand();

      int slots = 6;


      float x = res.getScaledWidth() / 2.0f;
      float y = res.getScaledHeight() -
    }
  }
}