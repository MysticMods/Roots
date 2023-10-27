package epicsquid.roots.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityLifetimeBase extends Entity {
	public static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityLifetimeBase.class, DataSerializers.VARINT);
	
	protected static final UUID FakePlayer = UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77");
	
	protected UUID player;
	
	protected double x = 0;
	protected double y = 0;
	protected double z = 0;
	
	public EntityLifetimeBase(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
		this.setSize(1, 1);
	}
	
	public UUID getPlayer() {
		if (player == null) {
			return FakePlayer;
		}
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
			this.setPlayer(compound.getUniqueId("player"));
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
			compound.setUniqueId("player", player);
		}
	}
	
	@Override
	public void onUpdate() {
		getDataManager().set(lifetime, Integer.valueOf(getDataManager().get(lifetime) - 1));
		getDataManager().setDirty(lifetime);
		if (getDataManager().get(lifetime) < 0) {
			setDead();
		}
	}
}
