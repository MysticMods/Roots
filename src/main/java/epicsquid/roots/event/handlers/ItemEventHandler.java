package epicsquid.roots.event.handlers;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemRunicShears;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class ItemEventHandler {
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
    if (event.getHand() != EnumHand.MAIN_HAND) return;

    EntityPlayer player = event.getEntityPlayer();
    ItemStack main = player.getHeldItemMainhand();
    if (main.getItem() != ModItems.runic_shears) return;

    event.setCanceled(true);
    ItemRunicShears item = (ItemRunicShears) ModItems.runic_shears;

    Vec3d hit = event.getHitVec();
    item.onItemUse(player, event.getWorld(), event.getPos(), event.getHand(), event.getFace(), (float) hit.x, (float) hit.y, (float) hit.z);
  }
}
