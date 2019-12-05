package epicsquid.roots.event.handlers;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.roots.Roots;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.entity.item.MagmaticSoilEntityItem;
import epicsquid.roots.handler.PouchHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.RunicShearsItem;
import epicsquid.roots.util.PowderInventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class ItemEventHandler {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
    if (event.getHand() != Hand.MAIN_HAND)
      return;

    PlayerEntity player = event.getEntityPlayer();
    ItemStack main = player.getHeldItemMainhand();
    if (main.getItem() != ModItems.runic_shears)
      return;

    RunicShearsItem item = (RunicShearsItem) ModItems.runic_shears;

    Vec3d hit = event.getHitVec();
    ActionResultType result = item.onItemUse(player, event.getWorld(), event.getPos(), event.getHand(), event.getFace(), (float) hit.x, (float) hit.y, (float) hit.z);
    if (result == ActionResultType.SUCCESS) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onEntityItemPickup (EntityItemPickupEvent event) {
    if (GeneralConfig.AutoRefillPouches) {
      PlayerEntity player = event.getEntityPlayer();
      ItemEntity entity = event.getItem();
      if (!entity.world.isRemote) {
        ItemStack stack = entity.getItem().copy();
        Item item = stack.getItem();
        int original = stack.getCount();
        if (HerbRegistry.isHerb(item)) {
          ItemStack pouch = PowderInventoryUtil.getPouch(player);
          if (!pouch.isEmpty()) {
            PouchHandler handler = PouchHandler.getHandler(pouch);
            PouchHandler.PouchHerbHandler herbs = handler.getHerbs();
            int refill = herbs.refill(stack);
            if (refill < original) {
              event.setCanceled(true);
              entity.setDead();
              if (refill != 0) {
                stack.setCount(refill);
                ItemEntity newEntity = new ItemEntity(entity.world, entity.posX, entity.posY, entity.posZ, stack);
                newEntity.motionX = entity.motionX;
                newEntity.motionY = entity.motionY;
                newEntity.motionZ = entity.motionZ;
                newEntity.setPickupDelay(0);
                entity.world.spawnEntity(newEntity);
              }
              entity.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1, 0.5f);
            }
          }
        }
      }
    }
  }

  public static Item MAGMATIC_SOIL = null;

  @SubscribeEvent
  public static void onEntityItemJoinWorld(EntityJoinWorldEvent event) {
    if (MAGMATIC_SOIL == null) {
      MAGMATIC_SOIL = ((BlockBase) ModBlocks.elemental_soil_fire).getItemBlock();
    }
    Entity entity = event.getEntity();
    if (event.getWorld().isRemote) return;

    if (entity instanceof ItemEntity && !(entity instanceof MagmaticSoilEntityItem)) {
      ItemEntity entityItem = (ItemEntity) entity;
      ItemStack stack = entityItem.getItem();
      if (stack.getItem() == MAGMATIC_SOIL) {
        MagmaticSoilEntityItem soil = new MagmaticSoilEntityItem(event.getWorld(), entity.posX, entity.posY, entity.posZ, stack);
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
