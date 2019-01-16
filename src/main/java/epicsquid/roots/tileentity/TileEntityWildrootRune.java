package epicsquid.roots.tileentity;

import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.rune.Rune;
import epicsquid.roots.rune.RuneRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileEntityWildrootRune extends TileBase implements ITickable {

    private TileEntityIncenseBurner incenseBurner;
    private static final BlockPos[] INCENSE_POSITIONS = new BlockPos[]{new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0)};

    private Rune rune;
    private ItemStack reagent;

    public TileEntityWildrootRune(){
        this.incenseBurner = null;
        this.rune = null;
        this.reagent = ItemStack.EMPTY;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if(reagent != ItemStack.EMPTY){
            NBTTagCompound stackCompound = new NBTTagCompound();
            compound.setTag("reagent", reagent.writeToNBT(stackCompound));
        }
        if(rune != null){
            rune.saveToEntity(compound);
        }
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.rune = RuneRegistry.getRune(compound);
        super.readFromNBT(compound);
    }

    @Override
    public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if(heldItem.isEmpty()){
            if(rune == null){
                return true;
            }
            rune.activate(this, player);
        }
        else{
            if(rune == null){
                Rune newRune = RuneRegistry.getRune(heldItem.getItem());
                System.out.println(newRune);
                if(newRune != null){
                    this.rune = newRune;
                    ItemStack toInsert = heldItem.copy();
                    toInsert.setCount(1);
                    this.reagent = toInsert;
                    heldItem.setCount(heldItem.getCount() - 1);
                    if (heldItem.getCount() <= 0) {
                        player.setHeldItem(hand, ItemStack.EMPTY);
                    } else {
                        player.setHeldItem(hand, heldItem);
                    }
                    markDirty();
                    PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
                }
            }
        }
        return true;
    }

    @Override
    public void update() {
        if(world.getWorldTime() % 20 == 0){
            if(this.incenseBurner != null){
                if(!this.incenseBurner.isLit()){
                    this.incenseBurner = null;
                }
            }
            else{
                for(BlockPos pos : INCENSE_POSITIONS){
                    if(world.getBlockState(this.getPos().add(pos)).getBlock() == ModBlocks.incense_burner){
                        TileEntityIncenseBurner incenseBurner = (TileEntityIncenseBurner) world.getTileEntity(this.getPos().add(pos));
                        if(incenseBurner == null){
                            continue;
                        }
                        if(incenseBurner.isLit()){
                            this.incenseBurner = incenseBurner;
                            break;
                        }
                    }
                }
            }
        }
    }

    public TileEntityIncenseBurner getIncenseBurner() {
        return incenseBurner;
    }

}
