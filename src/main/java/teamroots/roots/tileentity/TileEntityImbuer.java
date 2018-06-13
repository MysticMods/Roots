package teamroots.roots.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

public class TileEntityImbuer extends TileEntity implements ITileEntityBase, ITickable {
	public ItemStackHandler inventory = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityImbuer.this.markDirty();
        	if (!world.isRemote){
        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(TileEntityImbuer.this));
        	}
        }
	};
	int progress = 0;
	public float angle = 0;
	
	public TileEntityImbuer(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setInteger("progress", progress);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
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
		//if (!world.isRemote){
			if (!heldItem.isEmpty()){
				if (heldItem.getItem() == RegistryManager.petal_dust){
					if (inventory.getStackInSlot(0).isEmpty()){
						ItemStack toInsert = heldItem.copy();
						toInsert.setCount(1);
						ItemStack attemptedInsert = inventory.insertItem(0, toInsert, true);
						if (attemptedInsert.isEmpty()){
							inventory.insertItem(0, toInsert, false);
							player.getHeldItem(hand).shrink(1);
							if (player.getHeldItem(hand).getCount() == 0){
								player.setHeldItem(hand, ItemStack.EMPTY);
							}
							markDirty();
			        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
							return true;
						}
					}
				}
				else if (heldItem.getItem() == RegistryManager.staff){
					if (inventory.getStackInSlot(1).isEmpty()){
						ItemStack toInsert = heldItem.copy();
						toInsert.setCount(1);
						ItemStack attemptedInsert = inventory.insertItem(1, toInsert, true);
						if (attemptedInsert == ItemStack.EMPTY){
							inventory.insertItem(1, toInsert, false);
							player.getHeldItem(hand).shrink(1);
							if (player.getHeldItem(hand).getCount() == 0){
								player.setHeldItem(hand, ItemStack.EMPTY);
							}
							markDirty();
			        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
							return true;
						}
					}
				}
			}
			if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND){
				for (int i = 1; i >= 0; i --){
					if (!inventory.getStackInSlot(i).isEmpty()){
						ItemStack extracted = inventory.extractItem(i, 1, false);
						if (!world.isRemote){
							world.spawnEntity(new EntityItem(world,getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5,extracted));
						}
						markDirty();
		        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
						return true;
					}
				}
			}
		//}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!world.isRemote){
			Misc.spawnInventoryInWorld(world, getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5, inventory);
		}
	}

	@Override
	public void update() {
		angle ++;
		if (!inventory.getStackInSlot(0).isEmpty() && !inventory.getStackInSlot(1).isEmpty()){
			progress ++;
			angle += 2.0f;
			ItemStack spellDust = inventory.getStackInSlot(0);
			if (spellDust.hasTagCompound()){
				SpellBase spell = SpellRegistry.spellRegistry.get(spellDust.getTagCompound().getString("spell"));
				if (world.isRemote){
					if (Misc.random.nextInt(2) == 0){
						ParticleUtil.spawnParticleLineGlow(world, getPos().getX()+0.5f, getPos().getY()+0.125f, getPos().getZ()+0.5f, getPos().getX()+0.5f+0.5f*(Misc.random.nextFloat()-0.5f), getPos().getY()+1.0f, getPos().getZ()+0.5f+0.5f*(Misc.random.nextFloat()-0.5f), spell.red1, spell.green1, spell.blue1, 0.25f, 4.0f, 40);
					}
					else {
						ParticleUtil.spawnParticleLineGlow(world, getPos().getX()+0.5f, getPos().getY()+0.125f, getPos().getZ()+0.5f, getPos().getX()+0.5f+0.5f*(Misc.random.nextFloat()-0.5f), getPos().getY()+1.0f, getPos().getZ()+0.5f+0.5f*(Misc.random.nextFloat()-0.5f), spell.red2, spell.green2, spell.blue2, 0.25f, 4.0f, 40);
					}
				}
			}
			if (progress > 200){
        		progress = 0;
        		if (!world.isRemote){
        			ItemStack staff = inventory.getStackInSlot(1);
	        		if (spellDust.hasTagCompound()){
	    				if (SpellRegistry.spellRegistry.containsKey(spellDust.getTagCompound().getString("spell"))){
			        		ItemStaff.createData(staff, spellDust.getTagCompound().getString("spell"));
			        		world.spawnEntity(new EntityItem(world, getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5,staff));
			        		inventory.extractItem(0, 1, false);
			        		inventory.extractItem(1, 1, false);
							markDirty();
			        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
			        		PacketHandler.INSTANCE.sendToAll(new MessageImbueCompleteFX(spellDust.getTagCompound().getString("spell"),getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5));
	    				}
        			}
        		}
        	}
        	this.markDirty();
        	if (!world.isRemote){
        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
        	}
		}
		else {
			if (progress != 0){
				progress = 0;
	        	this.markDirty();
	        	if (!world.isRemote){
	        		PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
	        	}
			}
		}
	}

}
