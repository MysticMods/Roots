package epicsquid.roots.block;

import epicsquid.mysticallib.block.Block;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockWildwoodRune extends Block {

  public BlockWildwoodRune(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
  }

  @Override
  public Item getItemDropped(BlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(ModBlocks.wildwood_log);
  }
}

