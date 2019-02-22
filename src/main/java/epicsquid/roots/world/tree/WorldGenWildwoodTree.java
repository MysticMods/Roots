package epicsquid.roots.world.tree;

import java.util.Random;

import epicsquid.mysticalworld.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenWildwoodTree extends WorldGenAbstractTree
{
    private static final IBlockState LOG = ModBlocks.wildwoodLog.getDefaultState();
    private final boolean useExtraRandomHeight;

    public WorldGenWildwoodTree(boolean notify, boolean useExtraRandomHeightIn)
    {
        super(notify);
        this.useExtraRandomHeight = useExtraRandomHeightIn;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for(int i = 0; i < 6; i++){
            worldIn.setBlockState(position.add(0, i, 0), ModBlocks.wildwoodLog.getDefaultState());
        }

        return true;
    }
}