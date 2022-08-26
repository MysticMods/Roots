package epicsquid.roots.ritual.natural;

import java.util.List;
import java.util.Random;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.world.tree.WorldGenWildwoodTree;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class RitualWildGrowth extends RitualBase {

    public RitualWildGrowth(String name, int duration) {
        super(name, duration);

        addCondition(new ConditionItems(
                new ItemStack(ModItems.wildroot),
                new ItemStack(ModItems.bark_oak),
                new ItemStack(ModItems.bark_oak),
                new ItemStack(ModItems.bark_dark_oak),
                new ItemStack(Items.DYE, 1, 15))
        );
        setIcon(ModItems.ritual_wild_growth);
        setColor(TextFormatting.DARK_GRAY);
        setBold(true);
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
