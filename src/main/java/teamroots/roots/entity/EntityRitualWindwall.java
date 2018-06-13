package teamroots.roots.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import teamroots.roots.RegistryManager;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageMoonlightBurstFX;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.recipe.MoonlightRecipe;
import teamroots.roots.recipe.RecipeRegistry;
import teamroots.roots.ritual.RitualRegistry;
import teamroots.roots.util.IEntityRenderingLater;
import teamroots.roots.util.NBTUtil;
import teamroots.roots.util.NoiseGenUtil;

public class EntityRitualWindwall extends Entity implements IRitualEntity {
	Random runeRand = new Random();
    public static final DataParameter<Integer> lifetime = EntityDataManager.<Integer>createKey(EntityRitualWindwall.class, DataSerializers.VARINT);
	public double x = 0;
	public double y = 0;
	public double z = 0;
    public EntityRitualWindwall(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
		this.setSize(1, 1);
		getDataManager().register(lifetime, RitualRegistry.ritual_windwall.duration+20);
		Random random = new Random();
	}
    
    @Override
    public void setPosition(double x, double y, double z){
    	super.setPosition(x, y, z);
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }
    
    @Override
    public boolean isEntityInsideOpaqueBlock(){
    	return false;
    }

	@Override
	protected void entityInit() {
		
	}
	
	@Override
	public void onUpdate(){
		ticksExisted ++;
		float alpha = (float)Math.min(40, (RitualRegistry.ritual_windwall.duration+20)-getDataManager().get(lifetime))/40.0f;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		getDataManager().set(lifetime, getDataManager().get(lifetime)-1);
		getDataManager().setDirty(lifetime);
		if (getDataManager().get(lifetime) < 0){
			setDead();
		}
		if (world.isRemote && getDataManager().get(lifetime) > 0){
			ParticleUtil.spawnParticleStar(world, (float)posX, (float)posY, (float)posZ, 0, 0, 0, 70, 70, 70, 0.5f*alpha, 20.0f, 40);
			for (float i = 0; i < 360; i += 120){
				float ang = (float)(ticksExisted % 360);
				float tx = (float)posX + 2.5f*(float)Math.sin(Math.toRadians(2.0f*(i+ang)));
				float ty = (float)posY + 0.5f*(float)Math.sin(Math.toRadians(4.0f*(i+ang)));
				float tz = (float)posZ + 2.5f*(float)Math.cos(Math.toRadians(2.0f*(i+ang)));
				ParticleUtil.spawnParticleStar(world, tx, ty, tz, 0, 0, 0, 70, 70, 70, 0.5f*alpha, 10.0f, 40);
			}
			if (rand.nextInt(5) == 0){
				ParticleUtil.spawnParticleSpark(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.0625f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 70, 70, 70, 1.0f*alpha, 1.0f+rand.nextFloat(), 160);
			}
		}
		if (this.ticksExisted % 5 == 0){
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-31.5,posY-31.5,posZ-31.5,posX+31.5,posY+31.5,posZ+31.5));
			for (EntityLivingBase e : entities){
				if (e instanceof EntityMob && Math.pow((posX-e.posX),2) + Math.pow((posY-e.posY),2) + Math.pow((posZ-e.posZ),2) < 1000){
					e.knockBack(this, 1.0f, posX-e.posX, posZ-e.posZ);
					if (world.isRemote){
						for (int i = 0; i < 10; i ++){
							ParticleUtil.spawnParticleSmoke(world, (float)e.posX, (float)e.posY, (float)e.posZ, (float)e.motionX*rand.nextFloat()*0.5f, (float)e.motionY*rand.nextFloat()*0.5f, (float)e.motionZ*rand.nextFloat()*0.5f, 0.65f, 0.65f, 0.65f, 0.15f, 12.0f+24.0f*rand.nextFloat(), 80, false);
						}
					}
				}
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.x = compound.getDouble("x");
		this.y = compound.getDouble("y");
		this.z = compound.getDouble("z");
		this.setPosition(x,y,z);
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
