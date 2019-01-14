package epicsquid.roots.block;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.tile.ITile;
import epicsquid.mysticallib.util.Util;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlockWildrootRune extends BlockWildrootLog  implements ITileEntityProvider {
    protected Class<? extends TileEntity> teClass;
    public static Set<Class<? extends TileEntity>> classes = new HashSet<>();

    public BlockWildrootRune(@Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
        super(name);
        this.teClass = teClass;
        attemptRegistry(teClass);
    }

    public static void attemptRegistry(@Nonnull Class<? extends TileEntity> c) {
        if (!classes.contains(c)) {
            String[] nameParts = c.getTypeName().split("\\.");
            String className = nameParts[nameParts.length - 1];
            String modid = LibRegistry.getActiveModid();
            GameRegistry.registerTileEntity(c, modid + ":" + Util.lowercase(className));
        }
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing face, float hitX, float hitY, float hitZ) {
        TileEntity t = world.getTileEntity(pos);
        if (t instanceof ITile) {
            return ((ITile) t).activate(world, pos, state, player, hand, face, hitX, hitY, hitZ);
        }
        return false;
    }

    @Override
    public void onBlockHarvested(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player) {
        TileEntity t = world.getTileEntity(pos);
        if (t instanceof ITile) {
            ((ITile) t).breakBlock(world, pos, state, player);
        }
    }

    @Override
    public void onBlockExploded(@Nonnull World world, @Nonnull BlockPos pos, Explosion e) {
        TileEntity t = world.getTileEntity(pos);
        if (t instanceof ITile) {
            ((ITile) t).breakBlock(world, pos, world.getBlockState(pos), null);
        }
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        try {
            return teClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
