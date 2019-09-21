package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.roots.particle.ParticleUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockMark extends BlockBase {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockMark(@Nonnull String name) {
        super(Material.GRASS, SoundType.CLOTH, 0F, name);
        this.setFlammable(false);
        this.setResistance(0);
        this.setItemBlock(null);
    }

    // TODO: 21/09/2019 Create Bounding Box
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return super.getBoundingBox(state, world, pos);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return null;
    }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) {
        return false;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    // TODO: 21/09/2019 TBD
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        ParticleUtil.spawnParticlePetal(worldIn, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0F, 0F, 0F, 1, 1, 1, 0.75F, 3.0F, 240);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        switch (state.getValue(FACING))
        {
            case EAST:
                i = i | 1;
                break;
            case WEST:
                i = i | 2;
                break;
            case SOUTH:
                i = i | 3;
                break;
            case NORTH:
                i = i | 4;
                break;
            case DOWN:
                i = i | 5;
                break;
            case UP:
                i = i | 6;
                break;
        }
        return i;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();

        switch (meta)
        {
            case 1:
                state = state.withProperty(FACING, EnumFacing.EAST);
                break;
            case 2:
                state = state.withProperty(FACING, EnumFacing.WEST);
                break;
            case 3:
                state = state.withProperty(FACING, EnumFacing.SOUTH);
                break;
            case 4:
                state = state.withProperty(FACING, EnumFacing.NORTH);
                break;
            case 5:
                state = state.withProperty(FACING, EnumFacing.DOWN);
                break;
            case 6:
                state = state.withProperty(FACING, EnumFacing.UP);
                break;
        }

        return state;
    }
}
