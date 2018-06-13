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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.ritual.RitualRegistry;

public class EntityFlare extends Entity {
    public static final DataParameter<Float> value = EntityDataManager.<Float>createKey(EntityFlare.class, DataSerializers.FLOAT);
	BlockPos pos = new BlockPos(0,0,0);
	int lifetime = 320;
	public BlockPos dest = new BlockPos(0,0,0);
	public UUID id = null;
	
	public EntityFlare(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
		this.getDataManager().register(value, Float.valueOf(0));
	}
	
	public void initCustom(double x, double y, double z, double vx, double vy, double vz, double value){
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.motionX = vx;
		this.motionY = vy;
		this.motionZ = vz;
		setSize((float)value/10.0f,(float)value/10.0f);
		getDataManager().set(EntityFlare.value, (float)value);
		getDataManager().setDirty(EntityFlare.value);
		setSize((float)value/10.0f,(float)value/10.0f);
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		getDataManager().set(EntityFlare.value, compound.getFloat("value"));
		getDataManager().setDirty(EntityFlare.value);
		if (compound.hasKey("UUIDmost")){
			id = new UUID(compound.getLong("UUIDmost"),compound.getLong("UUIDleast"));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setFloat("value", getDataManager().get(value));
		if (id != null){
			compound.setLong("UUIDmost", id.getMostSignificantBits());
			compound.setLong("UUIDleast", id.getLeastSignificantBits());
		}
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		float alpha = (float)Math.min(40, (320.0f-(float)lifetime))/40.0f;
		lifetime --;
		if (lifetime <= 0){
			getEntityWorld().removeEntity(this);
			this.setDead();
		}
		getDataManager().set(value, getDataManager().get(value)-0.025f);
		if (getDataManager().get(value) <= 0){
			getEntityWorld().removeEntity(this);
			this.setDead();
		}
		
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		IBlockState state = getEntityWorld().getBlockState(getPosition());
		if (state.isFullCube() && state.isOpaqueCube()){
			if (getEntityWorld().isRemote){
				for (int i = 0; i < 40; i ++){
					ParticleUtil.spawnParticleFiery(getEntityWorld(), (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 255, 96, 32, 0.5f*alpha, getDataManager().get(value)+rand.nextFloat()*getDataManager().get(value), 40);
				}
			}
			List<EntityLivingBase> entities = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-getDataManager().get(value)*0.25,posY-getDataManager().get(value)*0.25,posZ-getDataManager().get(value)*0.25,posX+getDataManager().get(value)*0.25,posY+getDataManager().get(value)*0.25,posZ+getDataManager().get(value)*0.25));
			for (EntityLivingBase target : entities){
				DamageSource source = DamageSource.IN_FIRE;
				target.setFire(4);
				target.attackEntityFrom(source, getDataManager().get(value));
				target.knockBack(this, 0.5f, -motionX, -motionZ);
			}
			if (world.isAirBlock(getPosition().up())){
				world.setBlockState(getPosition().up(),Blocks.FIRE.getDefaultState());
			}
			this.setDead();
		}
		if (getEntityWorld().isRemote){
			for (double i = 0; i < 3; i ++){
				double coeff = i/3.0;
				ParticleUtil.spawnParticleFiery(getEntityWorld(), (float)(prevPosX+(posX-prevPosX)*coeff), (float)(prevPosY+(posY-prevPosY)*coeff), (float)(prevPosZ+(posZ-prevPosZ)*coeff), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 255, 96, 32, 0.5f*alpha, getDataManager().get(value)+rand.nextFloat()*getDataManager().get(value), 40);
			}
		}
		List<EntityLivingBase> entities = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-getDataManager().get(value)*0.125,posY-getDataManager().get(value)*0.125,posZ-getDataManager().get(value)*0.125,posX+getDataManager().get(value)*0.125,posY+getDataManager().get(value)*0.125,posZ+getDataManager().get(value)*0.125));
		if (entities.size() > 0){	
			for (EntityLivingBase target : entities){
				DamageSource source = DamageSource.IN_FIRE;
				target.setFire(4);
				target.attackEntityFrom(source, getDataManager().get(value));
				target.knockBack(this, 0.5f, -motionX, -motionZ);
			}
			if (getEntityWorld().isRemote){
				for (int i = 0; i < 40; i ++){
					ParticleUtil.spawnParticleFiery(getEntityWorld(), (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 255, 96, 32, 0.5f*alpha, getDataManager().get(value)+rand.nextFloat()*getDataManager().get(value), 40);
				}
			}
			this.setDead();
		}
	}
}
