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
import teamroots.roots.RegistryManager;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageMoonlightBurstFX;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.recipe.MoonlightRecipe;
import teamroots.roots.recipe.RecipeRegistry;
import teamroots.roots.util.IEntityRenderingLater;
import teamroots.roots.util.NBTUtil;
import teamroots.roots.util.NoiseGenUtil;

public class EntityPetalShell extends Entity {
	public UUID playerId = null;
	public static final DataParameter<Integer> charge = EntityDataManager.<Integer>createKey(EntityPetalShell.class, DataSerializers.VARINT);
	
    public EntityPetalShell(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
		this.setSize(1, 1);
		getDataManager().register(charge, 1);
		Random random = new Random();
		this.noClip = true;
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
		this.ticksExisted ++;
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
		if (this.playerId != null){
			EntityPlayer player = world.getPlayerEntityByUUID(this.playerId);
			if (player != null){
				this.setPosition(player.posX, player.posY+1.0f, player.posZ);
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.getDataManager().set(charge, compound.getInteger("charge"));
		getDataManager().setDirty(charge);
		this.playerId = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("id"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("charge", getDataManager().get(charge));
		compound.setTag("id",net.minecraft.nbt.NBTUtil.createUUIDTag(playerId));
	}

}
