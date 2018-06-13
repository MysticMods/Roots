package teamroots.roots.entity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.ConfigManager;
import teamroots.roots.Roots;
import teamroots.roots.util.Misc;

public class EntitySprout extends EntityCreature {
	public static final DataParameter<Integer> variant = EntityDataManager.<Integer>createKey(EntitySprout.class, DataSerializers.VARINT);

	public SoundEvent ambientSound = new SoundEvent(new ResourceLocation("roots:darkoAmbient"));
	public EntitySprout(World world){
		super(world);
		setSize(0.5f,1.0f);
		this.experienceValue = 3;
	}
	
	@Override
	protected void entityInit(){
		super.entityInit();
		this.getDataManager().register(variant, rand.nextInt(3));
	}

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.5D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    public boolean isAIDisabled() {
        return false;
    }

	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }
	
	@Override
	public ResourceLocation getLootTable(){
		switch(getDataManager().get(EntitySprout.variant)){
			case 0: {return new ResourceLocation("roots:entity/sprout_green");}
			case 1: {return new ResourceLocation("roots:entity/sprout_tan");}
			case 2: {return new ResourceLocation("roots:entity/sprout_red");}
			default: {return new ResourceLocation("roots:entity/sprout_green");}
		}
	}
    
	@SideOnly(Side.CLIENT)
    @Override
    public void onUpdate(){
    	super.onUpdate();
    	if (world.isRemote){
    		if (Misc.random.nextInt(480) == 0 && ConfigManager.enableSilliness && Minecraft.getMinecraft().player.getGameProfile().getName().equalsIgnoreCase("Darkosto")){
    			world.playSound(Minecraft.getMinecraft().player, getPosition(), ambientSound, SoundCategory.NEUTRAL, 1.0f, 0.8f+0.4f*Misc.random.nextFloat());
    		}
        }
    	this.rotationYaw = this.rotationYawHead;
    }

    public float getEyeHeight()
    {
        return this.isChild() ? this.height : 1.3F;
    }

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		getDataManager().set(variant, compound.getInteger("variant"));
		getDataManager().setDirty(variant);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("variant", getDataManager().get(variant));
	}
}