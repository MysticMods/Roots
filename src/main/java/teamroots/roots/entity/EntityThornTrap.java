package teamroots.roots.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
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
import net.minecraftforge.fml.common.FMLCommonHandler;
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

public class EntityThornTrap extends Entity {
	public static final DataParameter<Integer> lifetime = EntityDataManager.<Integer>createKey(EntityThornTrap.class, DataSerializers.VARINT);
	public UUID playerId = null;
	
    public EntityThornTrap(World worldIn) {
		super(worldIn);
		this.setInvisible(false);
		this.setSize(1, 1);
		getDataManager().register(lifetime, 600);
		Random random = new Random();
		this.setNoGravity(false);
		this.noClip = false;
		EntityItem item;
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
		super.onUpdate();
		this.move(MoverType.SELF, motionX, motionY, motionZ);
		this.motionY -= 0.04f;
		if (this.onGround){
			this.motionX = 0;
			this.motionZ = 0;
		}
		getDataManager().set(lifetime, getDataManager().get(lifetime)-1);
		getDataManager().setDirty(lifetime);
		if (getDataManager().get(lifetime) <= 0){
			setDead();
		}
		if (world.isRemote){
			if (onGround){
				if (rand.nextBoolean()){
					ParticleUtil.spawnParticleThorn(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), SpellRegistry.spell_rose.red1, SpellRegistry.spell_rose.green1, SpellRegistry.spell_rose.blue1, 0.5f, 2.5f, 12, rand.nextBoolean());
				}
				else {
					ParticleUtil.spawnParticleThorn(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), SpellRegistry.spell_rose.red2, SpellRegistry.spell_rose.green2, SpellRegistry.spell_rose.blue2, 0.5f, 2.5f, 12, rand.nextBoolean());
				}
			}
			else {
				if (rand.nextBoolean()){
					ParticleUtil.spawnParticleGlow(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), SpellRegistry.spell_rose.red1, SpellRegistry.spell_rose.green1, SpellRegistry.spell_rose.blue1, 0.5f, 5f, 12);
				}
				else {
					ParticleUtil.spawnParticleGlow(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), SpellRegistry.spell_rose.red2, SpellRegistry.spell_rose.green2, SpellRegistry.spell_rose.blue2, 0.5f, 5f, 12);
				}
			}
		}
		if (playerId != null){
			EntityPlayer player = world.getPlayerEntityByUUID(playerId);
			if (player != null){
				List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-1.5,posY-1.5,posZ-1.5,posX+1.5,posY+1.5,posZ+1.5));
				if (entities.size() > 0){
					if (entities.size() == 1){
						if (entities.get(0).getUniqueID().compareTo(player.getUniqueID()) == 0){
							return;
						}
					}
					setDead();
					for (int j = 0; j < entities.size(); j ++){
						if (!(entities.get(j) instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled()) &&
								entities.get(j).getUniqueID().compareTo(player.getUniqueID()) != 0){
							entities.get(j).attackEntityFrom((DamageSource.CACTUS).causeMobDamage(player), 6.0f);
							entities.get(j).setLastAttackedEntity(player);
							entities.get(j).setRevengeTarget(player);
						}
					}
					if (world.isRemote){
						for (int i = 0; i < 30; i ++){
							if (rand.nextBoolean()){
								ParticleUtil.spawnParticleThorn(world, (float)posX+0.25f*(rand.nextFloat()-0.5f), (float)posY+0.25f*(rand.nextFloat()-0.5f), (float)posZ+0.25f*(rand.nextFloat()-0.5f), 0.375f*rand.nextFloat()-0.1875f, 0.1f+0.125f*rand.nextFloat(), 0.375f*rand.nextFloat()-0.1875f, SpellRegistry.spell_rose.red1, SpellRegistry.spell_rose.green1, SpellRegistry.spell_rose.blue1, 0.5f, 4.0f, 24, rand.nextBoolean());
							}
							else {
								ParticleUtil.spawnParticleThorn(world, (float)posX+0.25f*(rand.nextFloat()-0.5f), (float)posY+0.25f*(rand.nextFloat()-0.5f), (float)posZ+0.25f*(rand.nextFloat()-0.5f), 0.375f*rand.nextFloat()-0.1875f, 0.1f+0.125f*rand.nextFloat(), 0.375f*rand.nextFloat()-0.1875f, SpellRegistry.spell_rose.red2, SpellRegistry.spell_rose.green2, SpellRegistry.spell_rose.blue2, 0.5f, 4.0f, 24, rand.nextBoolean());
							}
						}
					}
				}
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
