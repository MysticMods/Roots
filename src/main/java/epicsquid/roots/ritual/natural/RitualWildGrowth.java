package epicsquid.roots.ritual.natural;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.world.tree.WorldGenWildwoodTree;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.List;
import java.util.Random;

public class RitualWildGrowth extends RitualBase {

    public RitualWildGrowth(String name, int duration) {
        super(name, duration);
        addIngredients(new ItemStack(ModItems.wildroot, 1), new ItemStack(ModItems.bark_oak, 1), new ItemStack(ModItems.bark_oak, 1),
                new ItemStack(ModItems.bark_oak, 1), new ItemStack(Items.DYE, 1, 15));
    }

    @Override
    public void doEffect(World world, BlockPos pos) {
        List<BlockPos> blockPosList = Util.getBlocksWithinRadius(world, pos, 6, 3, 6, ModBlocks.wildroot);
        if(!blockPosList.isEmpty()){
            generateTree(world, blockPosList.get(0), world.getBlockState(blockPosList.get(0)), new Random());
        }
    }

    private void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        WorldGenerator worldgenerator = new WorldGenWildwoodTree(true, false);
        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();
        worldIn.setBlockState(pos, iblockstate2, 4);
        if (!worldgenerator.generate(worldIn, rand, pos.add(0, 0, 0)))
        {
            worldIn.setBlockState(pos, state, 4);
        }
    }
}
