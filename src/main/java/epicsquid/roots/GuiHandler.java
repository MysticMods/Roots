/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots;

import epicsquid.roots.client.gui.*;
import epicsquid.roots.container.*;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import epicsquid.roots.tileentity.TileEntityImposer;
import epicsquid.roots.util.PlayerSyncUtil;
import epicsquid.roots.util.SpellUtil;
import epicsquid.roots.world.data.SpellLibraryRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GuiHandler implements IGuiHandler {
	
	public static final int POUCH_ID = 16;
	public static final int QUIVER_ID = 17;
	public static final int CRAFTER_ID = 18;
	public static final int IMPOSER_ID = 19;
	public static final int LIBRARY_ID = 20;
	
	@Nullable
	private static Supplier<ItemStack> getStaff(EntityPlayer player) {
		ItemStack staff = player.getHeldItemMainhand();
		if (!staff.isEmpty() && staff.getItem().equals(ModItems.staff)) {
			return player::getHeldItemMainhand;
		}
		
		staff = player.getHeldItemOffhand();
		if (!staff.isEmpty() && staff.getItem().equals(ModItems.staff)) {
			return player::getHeldItemOffhand;
		}

/*    IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
    if (handler != null) {
      for (int i = 0; i < handler.getSlots(); i++) {
        staff = handler.getStackInSlot(i);
        if (!staff.isEmpty() && staff.getItem().equals(ModItems.staff)) {
          final int x = i;
          return () -> handler.getStackInSlot(x);
        }
      }
    }*/
		
		return null;
	}
	
	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te;
		switch (id) {
			case POUCH_ID:
				return new ContainerPouch(player, true);
			case QUIVER_ID:
				return new ContainerQuiver(player);
			case CRAFTER_ID:
				te = world.getTileEntity(new BlockPos(x, y, z));
				if (te instanceof TileEntityFeyCrafter) {
					return new ContainerFeyCrafter(player, (TileEntityFeyCrafter) te);
				} else {
					return null;
				}
			case IMPOSER_ID:
				te = world.getTileEntity(new BlockPos(x, y, z));
				if (te instanceof TileEntityImposer) {
					((TileEntityImposer) te).updateInSlot(player);
					return new ContainerImposer(player, (TileEntityImposer) te);
				} else {
					return null;
				}
			case LIBRARY_ID:
				Supplier<ItemStack> staff = getStaff(player);
				if (staff != null) {
					SpellUtil.updateModifiers(staff.get(), player);
					PlayerSyncUtil.syncPlayer(player);
					return new ContainerLibrary(player, staff, SpellLibraryRegistry.getData(player));
				} else {
					player.sendStatusMessage(new TextComponentTranslation("roots.message.hold_staff").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE).setBold(true)), true);
					return null;
				}
		}
		return null;
	}
	
	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
			case POUCH_ID:
				return new GuiPouch(new ContainerPouch(player, false));
			case QUIVER_ID:
				return new GuiQuiver(new ContainerQuiver(player));
			case CRAFTER_ID:
				TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
				if (te instanceof TileEntityFeyCrafter) {
					return new GuiFeyCrafter(new ContainerFeyCrafter(player, (TileEntityFeyCrafter) te));
				}
				break;
			case IMPOSER_ID:
				te = world.getTileEntity(new BlockPos(x, y, z));
				if (te instanceof TileEntityImposer) {
					return new GuiImposer(new ContainerImposer(player, (TileEntityImposer) te));
				}
				break;
			case LIBRARY_ID:
				Supplier<ItemStack> staff = getStaff(player);
				if (staff != null) {
					return new GuiLibrary(new ContainerLibrary(player, staff, null));
				}
				break;
		}
		return null;
	}
}
