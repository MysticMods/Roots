package epicsquid.roots.entity.ritual;

import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class EntityRitualFrost extends EntityRitualBase {

    protected static Random random = new Random();
    protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualFrost.class, DataSerializers.VARINT);

    public EntityRitualFrost(World worldIn) {
        super(worldIn);
        this.getDataManager().register(lifetime, RitualRegistry.ritual_frost.getDuration() + 20);
    }

    @Override
    public void onUpdate() {
        getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
        getDataManager().setDirty(lifetime);
        if (getDataManager().get(lifetime) < 0) {
            setDead();
        }
        if (this.ticksExisted % 400 == 0) {
            boolean blockTransformed = false;
            for (int i = -9; i < 10; i++) {
                for (int j = -9; j < 10; j++) {
                    BlockPos topBlockPos = world.getTopSolidOrLiquidBlock(new BlockPos(getPosition().getX() + i, getPosition().getY() - 20, getPosition().getZ() + j));
                    if(world.getBlockState(topBlockPos.add(0, -1, 0)).getBlock() == Blocks.SNOW_LAYER){
                        continue;
                    }
                    world.setBlockState(topBlockPos, Blocks.SNOW_LAYER.getDefaultState());
                    if (blockTransformed) {
                        break;
                    }
                }
                if (blockTransformed) {
                    break;
                }
            }
        }
    }

    @Override
    public DataParameter<Integer> getLifetime() {
        return lifetime;
    }

}

