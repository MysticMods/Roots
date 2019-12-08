package epicsquid.roots.entity.ritual;

import epicsquid.roots.block.BlockBonfire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityRitualBase extends Entity implements IRitualEntity {
  public static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualBase.class, DataSerializers.VARINT);

  private UUID player;

  private double x = 0;
  private double y = 0;
  private double z = 0;

  public EntityRitualBase(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
  }

  public UUID getPlayer() {
    return player;
  }

  public void setPlayer(UUID player) {
    this.player = player;
  }

  @Nullable
  public EntityPlayer getPlayerEntity() {
    if (player == null || world.isRemote) {
      return null;
    }

    MinecraftServer server = world.getMinecraftServer();
    if (server == null) {
      return null;
    }
    return server.getPlayerList().getPlayerByUUID(getPlayer());
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
    if (compound.hasUniqueId("player")) {
      this.setPlayer(NBTUtil.getUUIDFromTag(compound.getCompoundTag("player")));
    }
    this.setEntityId(compound.getInteger("entity_id"));
    this.setPosition(x, y, z);
    getDataManager().set(lifetime, compound.getInteger("lifetime"));
    getDataManager().setDirty(lifetime);
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setDouble("x", x);
    compound.setDouble("y", y);
    compound.setDouble("z", z);
    compound.setInteger("entity_id", getEntityId());
    compound.setInteger("lifetime", getDataManager().get(lifetime));
    if (player != null) {
      compound.setTag("player", NBTUtil.createUUIDTag(player));
    }
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }

    if (!world.isRemote && !(world.getBlockState(getPosition()).getBlock() instanceof BlockBonfire)) {
      setDead();
    }
  }
}
