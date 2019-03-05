package epicsquid.roots.entity.ritual;

import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

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
        float alpha = (float) Math.min(40, (RitualRegistry.ritual_flower_growth.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
        getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
        getDataManager().setDirty(lifetime);
        if (getDataManager().get(lifetime) < 0) {
            setDead();
        }
        if (ticksExisted % 100 == 0)
        {
            generateFlower();
        }
    }

    private void generateFlower()
    {

    }

    @Override
    public DataParameter<Integer> getLifetime()
    {
        return lifetime;
    }
}
