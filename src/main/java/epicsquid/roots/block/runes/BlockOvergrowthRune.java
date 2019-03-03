package epicsquid.roots.block.runes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.rune.RuneRegistry;
import epicsquid.roots.tileentity.TileEntityWildrootRune;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockOvergrowthRune extends BlockTEBase {

    public BlockOvergrowthRune(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
        super(mat, type, hardness, name, teClass);
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileEntityWildrootRune(RuneRegistry.getRune("overgrowth_rune"));
    }
}
