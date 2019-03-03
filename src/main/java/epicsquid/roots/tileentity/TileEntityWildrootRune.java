package epicsquid.roots.tileentity;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.rune.RuneBase;
import epicsquid.roots.rune.RuneRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityWildrootRune extends TileBase implements ITickable {

    private TileEntityIncenseBurner incenseBurner;
    private static final BlockPos[] INCENSE_POSITIONS = new BlockPos[]{new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0)};

    private RuneBase rune;

    public TileEntityWildrootRune(RuneBase rune){
        this.incenseBurner = null;
        this.rune = rune;
        System.out.println("Okay instantiated! " + rune);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if(rune != null){
            rune.saveToEntity(compound);
        }
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        System.out.println(compound.getString("rune"));
        System.out.println(RuneRegistry.getRune(compound));
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

        if(this.rune != null){
            if(rune.isCharged(this)){
                this.rune.update(getWorld(), getPos());
            }
        }
    }

    public TileEntityIncenseBurner getIncenseBurner() {
        return incenseBurner;
    }

    public RuneBase getRune() {

        return rune;
    }

}
