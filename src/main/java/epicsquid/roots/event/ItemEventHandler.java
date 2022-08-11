package epicsquid.roots.event;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.roots.Roots;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.entity.item.EntityItemMagmaticSoil;
import epicsquid.roots.handler.PouchHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemRunicShears;
import epicsquid.roots.util.ServerHerbUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

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
		
		ItemRunicShears item = (ItemRunicShears) ModItems.runic_shears;
		
		Vec3d hit = event.getHitVec();
		EnumActionResult result = item.onItemUse(player, event.getWorld(), event.getPos(), event.getHand(), event.getFace(), (float) hit.x, (float) hit.y, (float) hit.z);
		if (result == EnumActionResult.SUCCESS) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onEntityItemPickup(EntityItemPickupEvent event) {
		if (GeneralConfig.AutoRefillPouches) {
			EntityPlayer player = event.getEntityPlayer();
			EntityItem entity = event.getItem();
			if (!entity.world.isRemote) {
				ItemStack stack = entity.getItem().copy();
				Item item = stack.getItem();
				int original = stack.getCount();
				if (HerbRegistry.isHerb(item)) {
					boolean modified = false;
					List<ItemStack> pouches = ServerHerbUtil.getPouches(player);
					for (ItemStack pouch : pouches) {
						PouchHandler handler = PouchHandler.getHandler(pouch);
						int refill = handler.refill(stack);
						if (refill < original) {
							modified = true;
							stack.setCount(refill);
							original = refill;
						}
					}
					if (modified) {
						event.setCanceled(true);
						entity.setDead();
						entity.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.6f, 0.5f);
						if (!stack.isEmpty()) {
							EntityItem newEntity = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, stack);
							newEntity.motionX = entity.motionX;
							newEntity.motionY = entity.motionY;
							newEntity.motionZ = entity.motionZ;
							newEntity.setPickupDelay(0);
							entity.world.spawnEntity(newEntity);
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
