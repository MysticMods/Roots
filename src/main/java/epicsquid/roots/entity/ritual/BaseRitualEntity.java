package epicsquid.roots.entity.ritual;

import epicsquid.roots.block.BonfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class BaseRitualEntity extends Entity implements IRitualEntity {
  public static final DataParameter<Integer> lifetime = EntityDataManager.createKey(BaseRitualEntity.class, DataSerializers.VARINT);

  private double x = 0;
  private double y = 0;
  private double z = 0;

  public BaseRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
  }

/*  public BaseRitualEntity(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
  }*/

  @Override
  protected abstract void registerData();

  @Override
  public void setPosition(double x, double y, double z) {
    super.setPosition(x, y, z);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    this.x = compound.getDouble("x");
    this.y = compound.getDouble("y");
    this.z = compound.getDouble("z");
    this.setEntityId(compound.getInt("entity_id"));
    this.setPosition(x, y, z);
    getDataManager().set(lifetime, compound.getInt("lifetime"));
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    compound.putDouble("x", x);
    compound.putDouble("y", y);
    compound.putDouble("z", z);
    compound.putInt("entity_id", getEntityId());
    compound.putInt("lifetime", getDataManager().get(lifetime));
  }

  @Nonnull
  @Override
  public BlockPos getPosition() {
    return new BlockPos(x, y, z);
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return null;
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  // TODO: What does this do -> how to replicate

/*  @Override
  protected void entityInit() {
    this.posY = y;
    this.posX = x;
    this.posZ = z;
  }*/

  @Override
  public void tick() {
    super.tick();

    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    if (getDataManager().get(lifetime) < 0) {
      remove();
    }

    if (!world.isRemote && !(world.getBlockState(getPosition()).getBlock() instanceof BonfireBlock)) {
      remove();
    }
  }
}
