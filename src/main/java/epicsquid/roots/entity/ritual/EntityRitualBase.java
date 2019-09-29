package epicsquid.roots.entity.ritual;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class EntityRitualBase extends Entity implements IRitualEntity {

  private double x = 0;
  private double y = 0;
  private double z = 0;

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
    this.setEntityId(compound.getInteger("entity_id"));
    this.setPosition(x, y, z);
    getDataManager().set(getLifetime(), compound.getInteger("lifetime"));
    getDataManager().setDirty(getLifetime());
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setDouble("x", x);
    compound.setDouble("y", y);
    compound.setDouble("z", z);
    compound.setInteger("entity_id", getEntityId());
    compound.setInteger("lifetime", getDataManager().get(getLifetime()));
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (!world.isRemote && world.getBlockState(getPosition()).getBlock() != ModBlocks.bonfire) {
      setDead();
    }
  }

  public abstract DataParameter<Integer> getLifetime();
}
