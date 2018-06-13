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
import teamroots.roots.effect.EffectManager;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageMoonlightBurstFX;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.recipe.MoonlightRecipe;
import teamroots.roots.recipe.RecipeRegistry;
import teamroots.roots.spell.SpellRegistry;
import teamroots.roots.util.IEntityRenderingLater;
import teamroots.roots.util.NBTUtil;
import teamroots.roots.util.NoiseGenUtil;

public class EntityTimeStop extends Entity {
	public static final DataParameter<Integer> lifetime = EntityDataManager.<Integer>createKey(EntityTimeStop.class, DataSerializers.VARINT);
	public UUID playerId = null;
	
    public EntityTimeStop(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
		this.setSize(1, 1);
		getDataManager().register(lifetime, 200);
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
			for (float i = 0; i < 360; i += 24.0f){
				if (rand.nextInt(4) == 0){
					if (rand.nextBoolean()){
						ParticleUtil.spawnParticleSmoke(world, (float)posX+rand.nextFloat()*rand.nextFloat()*8.0f*(float)Math.sin(Math.toRadians(i)), (float)posY-0.5f, (float)posZ+rand.nextFloat()*rand.nextFloat()*8.0f*(float)Math.cos(Math.toRadians(i)), 0, rand.nextFloat()*0.05f, 0, SpellRegistry.spell_oxeye_daisy.red1, SpellRegistry.spell_oxeye_daisy.green1, SpellRegistry.spell_oxeye_daisy.blue1, rand.nextFloat()*0.25f, rand.nextFloat()*24f, 120, true);
					}
					else {
						ParticleUtil.spawnParticleSmoke(world, (float)posX+rand.nextFloat()*rand.nextFloat()*8.0f*(float)Math.sin(Math.toRadians(i)), (float)posY-0.5f, (float)posZ+rand.nextFloat()*rand.nextFloat()*8.0f*(float)Math.cos(Math.toRadians(i)), 0, rand.nextFloat()*0.05f, 0, SpellRegistry.spell_oxeye_daisy.red2, SpellRegistry.spell_oxeye_daisy.green2, SpellRegistry.spell_oxeye_daisy.blue2, rand.nextFloat()*0.25f, rand.nextFloat()*24f, 120, true);
					}
				}
			}
		}
		//if (!world.isRemote){
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-9.0,posY-1.0,posZ-9.0,posX+9.0,posY+19.0,posZ+9.0));
			for (EntityLivingBase e : entities){
				if (playerId != null){
					if (e.getUniqueID().compareTo(playerId) != 0){
						EffectManager.assignEffect(e, EffectManager.effect_time_stop.name, 40, new NBTTagCompound());
					}
				}
			}
		//}
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
