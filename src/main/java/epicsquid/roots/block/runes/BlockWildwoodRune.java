package epicsquid.roots.block.runes;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityWildrootRune;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockWildwoodRune extends BlockTEBase {

    public BlockWildwoodRune(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
        super(mat, type, hardness, name, teClass);
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileEntityWildrootRune();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.wildwood_log);
    }
}
