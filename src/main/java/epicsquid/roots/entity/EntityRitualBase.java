package epicsquid.roots.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityRitualBase extends Entity implements IRitualEntity {

  public static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualWarden.class, DataSerializers.VARINT);

  public double x = 0;
  public double y = 0;
  public double z = 0;

  public EntityRitualBase(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
  }

  @Override
  public void setPosition(double x, double y, double z) {
    super.setPosition(x, y, z);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  @Override
  protected void entityInit() {

  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.x = compound.getDouble("x");
    this.y = compound.getDouble("y");
    this.z = compound.getDouble("z");
    this.setPosition(x, y, z);
    getDataManager().set(lifetime, compound.getInteger("lifetime"));
    getDataManager().setDirty(lifetime);
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setDouble("x", x);
    compound.setDouble("y", y);
    compound.setDouble("z", z);
    compound.setInteger("lifetime", getDataManager().get(lifetime));
  }
}
