package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockWildwoodRune extends BlockBase {
	
	public BlockWildwoodRune(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
		super(mat, type, hardness, name);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.wildwood_log);
	}
}

