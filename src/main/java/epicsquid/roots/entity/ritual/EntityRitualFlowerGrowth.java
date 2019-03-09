package epicsquid.roots.entity.ritual;

import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityRitualFlowerGrowth extends EntityRitualBase {

    protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualFlowerGrowth.class, DataSerializers.VARINT);

    public EntityRitualFlowerGrowth(World worldIn)
    {
        super(worldIn);
        getDataManager().register(lifetime, RitualRegistry.ritual_flower_growth.getDuration() + 20);
    }

    @Override
    public void onUpdate()
    {
        ticksExisted++;
        getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
        getDataManager().setDirty(lifetime);
        if (getDataManager().get(lifetime) < 0) {
            setDead();
        }
        if (this.ticksExisted % 200 == 0)
        {
            for (int i = -10; i < 11; i++)
            {
                for (int j = -10; j < 11; j++)
                {

                }
            }
        }
    }

    private void generateFlower(BlockPos pos)
    {
        Block ground = world.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock();
        if (world.isAirBlock(pos) && (ground != Blocks.AIR && canPlaceFlower(ground)))
        {
            BlockFlower flower = new BlockFlower() {
                @Nonnull
                @Override
                public EnumFlowerColor getBlockType()
                {
                    return null;
                }
            };
        }
    }

    private boolean canPlaceFlower(Block block)
    {
        return block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.MYCELIUM;
    }

    @Override
    public DataParameter<Integer> getLifetime()
    {
        return lifetime;
    }
}
