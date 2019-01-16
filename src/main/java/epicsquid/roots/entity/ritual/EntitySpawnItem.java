package epicsquid.roots.entity.ritual;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntitySpawnItem extends EntityRitualBase {

    protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualLife.class, DataSerializers.VARINT);
    private ItemStack spawnStack;
    private int dropTime, ticksTillDrop;

    public EntitySpawnItem(World worldIn) {
        super(worldIn);
        this.spawnStack = ItemStack.EMPTY;
        this.dropTime = 0;
        this.ticksTillDrop = 100;
        getDataManager().register(lifetime, ticksTillDrop + 20);
    }

    public EntitySpawnItem(World worldIn, ItemStack stack, int ticksTillDrop) {
        super(worldIn);
        this.spawnStack = stack;
        this.dropTime = 0;
        this.ticksTillDrop = ticksTillDrop;
        getDataManager().register(lifetime, ticksTillDrop + 20);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.spawnStack.deserializeNBT(compound.getCompoundTag("spawnStack"));
        this.dropTime = compound.getInteger("dropTime");
        this.ticksTillDrop = compound.getInteger("ticksTillDrop");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("spawnStack", this.spawnStack.serializeNBT());
        compound.setInteger("dropTime", this.dropTime);
        compound.setInteger("ticksTillDrop", this.ticksTillDrop);
        return super.writeToNBT(compound);
    }

    @Override
    public void onUpdate() {
        if(this.dropTime == ticksTillDrop){
            if (!world.isRemote) {
                world.spawnEntity(new EntityItem(world, this.posX , this.posY, this.posZ, this.spawnStack));
            }
        }
        dropTime++;
        super.onUpdate();
    }

    @Override
    public DataParameter<Integer> getLifetime() {
        return lifetime;
    }
}
