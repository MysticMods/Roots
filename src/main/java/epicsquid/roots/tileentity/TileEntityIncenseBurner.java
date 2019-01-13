package epicsquid.roots.tileentity;

import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntityIncenseBurner extends TileBase {



    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityIncenseBurner.this.markDirty();
            if (!world.isRemote) {
                PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(TileEntityIncenseBurner.this.getUpdateTag()));
            }
        }
    };

    private int burnTick;
    private boolean lit;

    public TileEntityIncenseBurner(){
        burnTick = 0;
        lit = false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("burnTick", this.burnTick);
        compound.setBoolean("lit", this.lit);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.burnTick = compound.getInteger("burnTick");
        this.lit = compound.getBoolean("lit");
        super.readFromNBT(compound);
    }

    @Override
    public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        if(hand != EnumHand.MAIN_HAND){
            return false;
        }
        ItemStack stack = player.getHeldItem(hand);
        ItemStack inventoryStack = this.inventory.getStackInSlot(0);
        if(stack.isEmpty()){
            return false;
        }
        if(stack.getItem() == Items.FLINT_AND_STEEL){
            if (!inventoryStack.isEmpty()) {
                this.lit = true;
                return true;
            }
        }
        if(!isHerb(stack)){
            return false;
        }
        if(!inventoryStack.isEmpty()){
            if(inventoryStack.getItem() == stack.getItem()){
                int remainingHerbs = 64 - inventoryStack.getCount();
                if(stack.getCount() <= remainingHerbs){
                    inventoryStack.setCount(inventoryStack.getCount() + stack.getCount());
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
                else{
                    inventoryStack.setCount(inventoryStack.getCount() + remainingHerbs);
                    stack.setCount(stack.getCount() - remainingHerbs);
                }
            }
        }
        else{
            this.inventory.insertItem(0, stack, false);
            player.setHeldItem(hand, ItemStack.EMPTY);
        }

        return false;
    }

    private boolean isHerb(@Nonnull ItemStack stack) {
        for (Herb herb : HerbRegistry.REGISTRY.getValuesCollection()) {
            if (stack.getItem() == herb.getItem()) {
                return true;
            }
        }
        return false;
    }
}
