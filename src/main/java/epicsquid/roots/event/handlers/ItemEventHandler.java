package epicsquid.roots.event.handlers;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.item.EntityItemMagmaticSoil;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemRunicShears;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class ItemEventHandler {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
    if (event.getHand() != EnumHand.MAIN_HAND)
      return;

    EntityPlayer player = event.getEntityPlayer();
    ItemStack main = player.getHeldItemMainhand();
    if (main.getItem() != ModItems.runic_shears)
      return;

    event.setCanceled(true);
    ItemRunicShears item = (ItemRunicShears) ModItems.runic_shears;

    Vec3d hit = event.getHitVec();
    item.onItemUse(player, event.getWorld(), event.getPos(), event.getHand(), event.getFace(), (float) hit.x, (float) hit.y, (float) hit.z);
  }

  //@SubscribeEvent
  public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
  }

  public static Item MAGMATIC_SOIL = null;

  @SubscribeEvent
  public static void onEntityItemJoinWorld(EntityJoinWorldEvent event) {
    if (MAGMATIC_SOIL == null) {
      MAGMATIC_SOIL = ((BlockBase) ModBlocks.elemental_soil_fire).getItemBlock();
    }
    Entity entity = event.getEntity();
    if (event.getWorld().isRemote) return;

    if (entity instanceof EntityItem && !(entity instanceof EntityItemMagmaticSoil)) {
      EntityItem entityItem = (EntityItem) entity;
      ItemStack stack = entityItem.getItem();
      if (stack.getItem() == MAGMATIC_SOIL) {
        EntityItemMagmaticSoil soil = new EntityItemMagmaticSoil(event.getWorld(), entity.posX, entity.posY, entity.posZ, stack);
        soil.setPickupDelay(40);
        soil.motionX = entity.motionX;
        soil.motionY = entity.motionY;
        soil.motionZ = entity.motionZ;
        entity.setDead();
        event.getWorld().spawnEntity(soil);
      }
    }
  }
}
