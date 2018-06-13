package teamroots.roots.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.ritual.RitualRegistry;

public class EntityBlinkProjectile extends Entity {
    public static final DataParameter<Float> value = EntityDataManager.<Float>createKey(EntityBlinkProjectile.class, DataSerializers.FLOAT);
	BlockPos pos = new BlockPos(0,0,0);
	int lifetime = 40;
	public BlockPos dest = new BlockPos(0,0,0);
	public UUID playerId = null;
	
	public EntityBlinkProjectile(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
		this.getDataManager().register(value, Float.valueOf(0));
	}
	
	public void initCustom(double x, double y, double z, double vx, double vy, double vz, double value, UUID id){
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.motionX = vx;
		this.motionY = vy;
		this.motionZ = vz;
		this.playerId = id;
		setSize((float)value/10.0f,(float)value/10.0f);
		getDataManager().set(EntityBlinkProjectile.value, (float)value);
		getDataManager().setDirty(EntityBlinkProjectile.value);
		setSize((float)value/10.0f,(float)value/10.0f);
		lifetime = 40;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		getDataManager().set(EntityBlinkProjectile.value, compound.getFloat("value"));
		getDataManager().setDirty(EntityBlinkProjectile.value);
		if (compound.hasKey("id")){
			playerId = NBTUtil.getUUIDFromTag(compound.getCompoundTag("id"));
		}
		lifetime = compound.getInteger("lifetime");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		System.out.println("WRITING TAG!");
		compound.setFloat("value", getDataManager().get(value));
		if (playerId != null){
			compound.setTag("id",NBTUtil.createUUIDTag(playerId));
		}
		compound.setInteger("lifetime", lifetime);
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		this.motionX *= 0.9;
		this.motionY *= 0.9;
		this.motionZ *= 0.9;
		
		lifetime --;
		if (lifetime <= 0){
			setDead();
		}
		getDataManager().set(value, getDataManager().get(value)-0.025f);
		if (getDataManager().get(value) <= 0){
			setDead();
		}
		
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		IBlockState state = getEntityWorld().getBlockState(getPosition());
		if (state.isFullCube() && state.isOpaqueCube()){
			if (getEntityWorld().isRemote){
				for (int i = 0; i < 40; i ++){
					ParticleUtil.spawnParticleGlow(getEntityWorld(), (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 255, 163, 255, 0.5f, getDataManager().get(value)+rand.nextFloat()*getDataManager().get(value), 40);
				}
			}
		}
		if (getEntityWorld().isRemote){
			for (double i = 0; i < 3; i ++){
				double coeff = i/3.0;
				ParticleUtil.spawnParticleGlow(getEntityWorld(), (float)(prevPosX+(posX-prevPosX)*coeff), (float)(prevPosY+(posY-prevPosY)*coeff), (float)(prevPosZ+(posZ-prevPosZ)*coeff), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 255, 163, 255, 0.5f, getDataManager().get(value)+rand.nextFloat()*getDataManager().get(value), 40);
			}
		}
		List<EntityLivingBase> entities = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-getDataManager().get(value)*0.125,posY-getDataManager().get(value)*0.125,posZ-getDataManager().get(value)*0.125,posX+getDataManager().get(value)*0.125,posY+getDataManager().get(value)*0.125,posZ+getDataManager().get(value)*0.125));
		if (entities.size() > 0){	
			boolean hit = false;
			for (EntityLivingBase target : entities){
				if (playerId != null && target.getUniqueID().compareTo(playerId) != 0 || playerId == null){
					DamageSource source = DamageSource.MAGIC;
					target.attackEntityFrom(source, getDataManager().get(value));
					target.knockBack(this, 0.5f, -motionX, -motionZ);
					hit = true;
				}
			}
			if (hit){
				if (getEntityWorld().isRemote){
					for (int i = 0; i < 40; i ++){
						ParticleUtil.spawnParticleGlow(getEntityWorld(), (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 255, 163, 255, 0.5f, getDataManager().get(value)+rand.nextFloat()*getDataManager().get(value), 40);
					}
				}
				this.setDead();
			}
		}
	}
}
