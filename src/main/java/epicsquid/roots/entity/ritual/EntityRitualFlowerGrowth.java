package epicsquid.roots.entity.ritual;

import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityRitualFlowerGrowth extends EntityRitualBase {

    protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualFlowerGrowth.class, DataSerializers.VARINT);

    private BlockPos ritualPos = getPosition();

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
                    generateFlower(ritualPos.add(i, 0, j));
                }
            }
        }
    }

    private void generateFlower(BlockPos pos)
    {
        IBlockState flower = getRandomFlower();
        if (world.isAirBlock(pos) && flower.getBlock().canPlaceBlockAt(world, pos) /*&& rand.nextInt(2) == 0*/)
        {
            world.setBlockState(pos, flower);
        }
    }

    private IBlockState getRandomFlower()
    {
        BlockFlower flower = new BlockFlower() {
            @Nonnull
            @Override
            public EnumFlowerColor getBlockType()
            {
                return rand.nextInt(9) == 0 ? EnumFlowerColor.YELLOW : EnumFlowerColor.RED;
            }
        };

        return flower.getDefaultState();
    }

    @Override
    public DataParameter<Integer> getLifetime()
    {
        return lifetime;
    }

}
