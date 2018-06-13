package teamroots.roots.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.Constants;
import teamroots.roots.RegistryManager;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageMoonlightBurstFX;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.recipe.MoonlightRecipe;
import teamroots.roots.recipe.RecipeRegistry;
import teamroots.roots.spell.SpellRegistry;
import teamroots.roots.util.IEntityRenderingLater;
import teamroots.roots.util.NBTUtil;
import teamroots.roots.util.NoiseGenUtil;

public class EntityBoost extends Entity {
	public static final DataParameter<Integer> lifetime = EntityDataManager.<Integer>createKey(EntityBoost.class, DataSerializers.VARINT);
	public UUID playerId = null;
	
    public EntityBoost(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
		this.setSize(1, 1);
		getDataManager().register(lifetime, 20);
		Random random = new Random();
	}

    public void setPlayer(UUID id){
    	this.playerId = id;
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
		getDataManager().set(lifetime, getDataManager().get(lifetime)-1);
		getDataManager().setDirty(lifetime);
		if (getDataManager().get(lifetime) <= 0){
			setDead();
		}
		if (world.isRemote){
			for (int i = 0; i < 4; i ++){
				if (rand.nextBoolean()){
					ParticleUtil.spawnParticleStar(world, (float)posX+(rand.nextFloat())-0.5f, (float)posY+(rand.nextFloat())+0.5f, (float)posZ+(rand.nextFloat())-0.5f, -0.125f*(float)motionX, -0.125f*(float)motionY, -0.125f*(float)motionZ, SpellRegistry.spell_blue_orchid.red1, SpellRegistry.spell_blue_orchid.green1, SpellRegistry.spell_blue_orchid.blue1, 0.5f, 5.0f*rand.nextFloat()+5.0f, 40);
				}
				else {
					ParticleUtil.spawnParticleStar(world, (float)posX+(rand.nextFloat())-0.5f, (float)posY+(rand.nextFloat())+0.5f, (float)posZ+(rand.nextFloat())-0.5f, -0.125f*(float)motionX, -0.125f*(float)motionY, -0.125f*(float)motionZ, SpellRegistry.spell_blue_orchid.red2, SpellRegistry.spell_blue_orchid.green2, SpellRegistry.spell_blue_orchid.blue2, 0.5f, 5.0f*rand.nextFloat()+5.0f, 40);
				}
			}
		}
		if (playerId != null){
			EntityPlayer player = world.getPlayerEntityByUUID(playerId);
			if (player != null){
				this.posX = player.posX;
				this.posY = player.posY+1.0;
				this.posZ = player.posZ;
				player.motionX = player.getLookVec().x*0.8;
				player.motionY = player.getLookVec().y*0.8;
				player.motionZ = player.getLookVec().z*0.8;
				this.motionX = player.getLookVec().x;
				this.motionY = player.getLookVec().y;
				this.motionZ = player.getLookVec().z;
				player.fallDistance = 0;
				player.velocityChanged = true;
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.playerId = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("id"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("id",net.minecraft.nbt.NBTUtil.createUUIDTag(playerId));
	}

}
