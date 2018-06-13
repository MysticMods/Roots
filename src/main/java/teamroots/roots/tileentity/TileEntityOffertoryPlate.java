package teamroots.roots.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.roots.RegistryManager;
import teamroots.roots.item.ItemPetalDust;
import teamroots.roots.item.ItemStaff;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageImbueCompleteFX;
import teamroots.roots.network.message.MessageTEUpdate;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.recipe.RecipeRegistry;
import teamroots.roots.recipe.SpellRecipe;
import teamroots.roots.spell.SpellBase;
import teamroots.roots.spell.SpellRegistry;
import teamroots.roots.util.Misc;
import teamroots.roots.util.OfferingUtil;

public class TileEntityOffertoryPlate extends TileEntity implements ITileEntityBase {
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityOffertoryPlate.this.markDirty();
        	if (!world.isRemote){
        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(TileEntityOffertoryPlate.this));
        	}
        }
	};
	public UUID lastPlayer = null;
	int progress = 0;
	public float angle = 0;
	
	public TileEntityOffertoryPlate(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setInteger("progress", progress);
		if (lastPlayer != null){
			tag.setTag("lastPlayer",NBTUtil.createUUIDTag(lastPlayer));
		}
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		if (tag.hasKey("lastPlayer")){
			lastPlayer = NBTUtil.getUUIDFromTag(tag.getCompoundTag("lastPlayer"));
		}
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		progress = tag.getInteger("progress");
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!heldItem.isEmpty() && OfferingUtil.getValue(heldItem) != 0){
			if (inventory.getStackInSlot(0).isEmpty()){
				ItemStack toInsert = heldItem.copy();
				ItemStack attemptedInsert = inventory.insertItem(0, toInsert, true);
				if (attemptedInsert.isEmpty()){
					inventory.insertItem(0, toInsert, false);
					player.getHeldItem(hand).shrink(toInsert.getCount());
					if (player.getHeldItem(hand).getCount() == 0){
						player.setHeldItem(hand, ItemStack.EMPTY);
					}
					markDirty();
					return true;
				}
			}
		}
		if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND){
			if (!inventory.getStackInSlot(0).isEmpty()){
				ItemStack extracted = inventory.extractItem(0, inventory.getStackInSlot(0).getCount(), false);
				if (!world.isRemote){
					world.spawnEntity(new EntityItem(world,getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5,extracted));
				}
				markDirty();
        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
				return true;
			}
		}
		if (!world.isRemote){
			lastPlayer = player.getUniqueID();
			markDirty();
    		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!world.isRemote){
			Misc.spawnInventoryInWorld(world, getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5, inventory);
		}
	}
}
