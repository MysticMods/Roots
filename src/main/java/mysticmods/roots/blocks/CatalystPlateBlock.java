package mysticmods.roots.blocks;

import mysticmods.roots.blocks.entities.CatalystPlateBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CatalystPlateBlock extends Block {
    public CatalystPlateBlock(Properties builder) {
        super(builder);
    }
/*
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CatalystPlateBlockEntity(ModBlockEntities.CATALYST_PLATE.get());
    }*/
}
