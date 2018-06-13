package teamroots.roots.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
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

public class EntityRitualLife extends Entity implements IRitualEntity {
	Random runeRand = new Random();
    public static final DataParameter<Integer> lifetime = EntityDataManager.<Integer>createKey(EntityRitualLife.class, DataSerializers.VARINT);
	public double x = 0;
	public double y = 0;
	public double z = 0;
    public EntityRitualLife(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
		this.setSize(1, 1);
		getDataManager().register(lifetime, RitualRegistry.ritual_life.duration+20);
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
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		float alpha = (float)Math.min(40, (RitualRegistry.ritual_life.duration+20)-getDataManager().get(lifetime))/40.0f;
		getDataManager().set(lifetime, getDataManager().get(lifetime)-1);
		getDataManager().setDirty(lifetime);
		if (getDataManager().get(lifetime) < 0){
			setDead();
		}
		if (world.isRemote && getDataManager().get(lifetime) > 0){
			ParticleUtil.spawnParticleStar(world, (float)posX, (float)posY, (float)posZ, 0, 0, 0, 100, 255, 100, 0.5f*alpha, 20.0f, 40);
			if (rand.nextInt(5) == 0){
				ParticleUtil.spawnParticleSpark(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.0625f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 100, 255, 100, 1.0f*alpha, 1.0f+rand.nextFloat(), 160);
			}
			for (float i = 0; i < 360; i += 72.0f){
				double ang = ticksExisted % 360;
				float tx = (float)posX + 2.0f*(float)Math.sin(Math.toRadians(i+ang));
				float ty = (float)posY;
				float tz = (float)posZ + 2.0f*(float)Math.cos(Math.toRadians(i+ang));
				ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 100, 255, 100, 0.5f*alpha, 8.0f, 40);
			}
		}
		if (this.ticksExisted % 5 == 0){
			BlockPos pos = world.getTopSolidOrLiquidBlock(getPosition().add(rand.nextInt(19)-9, 0, rand.nextInt(19)-9));
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() instanceof BlockCrops){
				if (((BlockCrops)state.getBlock()).canGrow(world, pos, state, world.isRemote)){
					//if (state.getValue(BlockCrops.AGE) < 7){
						world.setBlockState(pos, state.getBlock().getStateFromMeta(state.getBlock().getMetaFromState(state)+1));
						world.notifyBlockUpdate(pos, state, state.getBlock().getStateFromMeta(state.getBlock().getMetaFromState(state)+1), 8);
						if (world.isRemote){
							for (float i = 0; i < 1; i += 0.125f){
								float coeff = i;
								ParticleUtil.spawnParticleSpark(world, (pos.getX()+0.5f), (pos.getY()+0.5f)+i, (pos.getZ()+0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.0625f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 100, 255, 100, 1.0f*(1.0f-coeff)*alpha, 3.0f*(1.0f-coeff), 40);
							}
						}
					//}
				}
			}
		}
		if (this.ticksExisted % 20 == 0){
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-15.5,posY-15.5,posZ-15.5,posX+15.5,posY+15.5,posZ+15.5));
			for (EntityLivingBase e : entities){
				if (e instanceof EntityPlayer){
					if (((EntityPlayer)e).getFoodStats().getFoodLevel() >= 18){
						e.heal(1.0f);
					}
					if (world.isRemote){
						for (float i = 0; i < 8; i ++){
							ParticleUtil.spawnParticleStar(world, (float)e.posX+0.5f*(rand.nextFloat()-0.5f), (float)e.posY+e.height/2.5f+(rand.nextFloat()-0.5f), (float)e.posZ+0.5f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.01875f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 100, 255, 100, 1.0f*alpha, 1.0f+2.0f*rand.nextFloat(), 40);
						}
					}
				}
				else {
					e.heal(1.0f);
					if (world.isRemote){
						for (float i = 0; i < 8; i ++){
							ParticleUtil.spawnParticleStar(world, (float)e.posX+0.5f*(rand.nextFloat()-0.5f), (float)e.posY+e.height/2.5f+(rand.nextFloat()-0.5f), (float)e.posZ+0.5f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.01875f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 100, 255, 100, 1.0f*alpha, 1.0f+2.0f*rand.nextFloat(), 40);
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
