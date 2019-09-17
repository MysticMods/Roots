package epicsquid.roots.entity.ritual;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.util.types.PropertyTable;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class EntityRitualBase extends Entity implements IRitualEntity {
  public static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualBase.class, DataSerializers.VARINT);

  private double x = 0;
  private double y = 0;
  private double z = 0;
  private PropertyTable props;

  public EntityRitualBase (World worldIn) {
    this(worldIn, null);
  }

  public EntityRitualBase(World worldIn, PropertyTable props) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    this.props = props;
  }

  public void setProps(PropertyTable props) {
    this.props = props;
  }

  @Override
  public void setPosition(double x, double y, double z) {
    super.setPosition(x, y, z);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Nonnull
  @Override
  public BlockPos getPosition() {
    return new BlockPos(x, y, z);
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  @Override
  protected void entityInit() {
    this.posY = y;
    this.posX = x;
    this.posZ = z;
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.x = compound.getDouble("x");
    this.y = compound.getDouble("y");
    this.z = compound.getDouble("z");
    this.setEntityId(compound.getInteger("id"));
    this.setPosition(x, y, z);
    getDataManager().set(lifetime, compound.getInteger("lifetime"));
    getDataManager().setDirty(lifetime);
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setDouble("x", x);
    compound.setDouble("y", y);
    compound.setDouble("z", z);
    compound.setInteger("id", getEntityId());
    compound.setInteger("lifetime", getDataManager().get(lifetime));
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }

    if (!world.isRemote && world.getBlockState(getPosition()).getBlock() != ModBlocks.bonfire) {
      setDead();
    }
  }
}
