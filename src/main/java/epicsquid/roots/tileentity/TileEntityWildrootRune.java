package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileEntityWildrootRune extends TileBase implements ITickable {

    private TileEntityIncenseBurner incenseBurner;
    private static final BlockPos[] INCENSE_POSITIONS = new BlockPos[]{new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0)};

    public TileEntityWildrootRune(){
        this.incenseBurner = null;
    }

    @Override
    public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {

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
}
