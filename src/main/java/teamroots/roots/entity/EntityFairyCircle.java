package teamroots.roots.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import teamroots.roots.RegistryManager;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageFairyDustBurstFX;
import teamroots.roots.network.message.MessageMoonlightBurstFX;
import teamroots.roots.network.message.MessageTEUpdate;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.recipe.MoonlightRecipe;
import teamroots.roots.recipe.RecipeRegistry;
import teamroots.roots.tileentity.TileEntityOffertoryPlate;
import teamroots.roots.util.IEntityRenderingLater;
import teamroots.roots.util.Misc;
import teamroots.roots.util.NoiseGenUtil;
import teamroots.roots.util.OfferingUtil;

public class EntityFairyCircle extends Entity implements IEntityRenderingLater {
	public int x = 0;
	public int y = 0;
	public int z = 0;
	public boolean initedPosition = false;
	public Map<UUID, Integer> reputation = new HashMap<UUID, Integer>();
	public static final DataParameter<Integer> time = EntityDataManager.<Integer>createKey(EntityFairy.class, DataSerializers.VARINT);
    public EntityFairyCircle(World worldIn) {
		super(worldIn);
		this.setInvisible(false);
		this.setSize(1, 1);
	}
    
    @Override
    public void setPosition(double x, double y, double z){
    	super.setPosition(x, y, z);
    	this.x = (int)x;
    	this.y = (int)y;
    	this.z = (int)z;
    	initedPosition = true;
    }
    
    @Override
    public boolean isEntityInsideOpaqueBlock(){
    	return false;
    }

	@Override
	protected void entityInit() {
		this.getDataManager().register(time, -1);
	}
	
	@Override
	public void onUpdate(){
		ticksExisted ++;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		if (ticksExisted % 80 == 0){
			List<EntityFairyCircle> circles = world.getEntitiesWithinAABB(EntityFairyCircle.class, new AxisAlignedBB(posX-32,posY-32,posZ-32,posX+32,posY+32,posZ+32));
			if (circles.size() > 0){
				for (EntityFairyCircle e : circles){
					if (e.getUniqueID().compareTo(getUniqueID()) != 0){
						world.removeEntity(e);
					}
				}
			}
		}
		if (ticksExisted % 40 == 0 && (world.getWorldTime() % 24000) > 12000 && !world.isRemote){
			List<EntityFairy> fairies = world.getEntitiesWithinAABB(EntityFairy.class, new AxisAlignedBB(posX-32,posY-32,posZ-32,posX+32,posY+32,posZ+32));
			if (fairies.size() < 10){
				if (rand.nextInt(20) == 0){
					EntityFairy fairy = new EntityFairy(world);
					fairy.setWorld(world);
					fairy.onInitialSpawn(world.getDifficultyForLocation(getPosition()), null);
					fairy.setPosition(posX+2.0f*(rand.nextFloat()-0.5f),posY,posZ+2.0f*(rand.nextFloat()-0.5f));
					world.spawnEntity(fairy);
				}
			}
		}
		if (world.isRemote && ticksExisted % 20 == 0 && (world.getWorldTime() % 24000) > 12000){
			if (rand.nextInt(800) == 0){
				BlockPos pos = world.getTopSolidOrLiquidBlock(getPosition().add(rand.nextInt(9)-4, 0, rand.nextInt(9)-4));
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleFloatingGlow(world, 
						pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f
						, 0, 0.125f, 0
						, 177, 255, 117, 1.0f
						, 7.5f, 320);
			}
			if (rand.nextInt(800) == 0){
				BlockPos pos = world.getTopSolidOrLiquidBlock(getPosition().add(rand.nextInt(9)-4, 0, rand.nextInt(9)-4));
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleFloatingGlow(world, 
						pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f
						, 0, 0.125f, 0
						, 255, 223, 163, 1.0f
						, 7.5f, 320);
			}
			if (rand.nextInt(800) == 0){
				BlockPos pos = world.getTopSolidOrLiquidBlock(getPosition().add(rand.nextInt(9)-4, 0, rand.nextInt(9)-4));
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleFloatingGlow(world, 
						pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f
						, 0, 0.125f, 0
						, 255, 163, 255, 1.0f
						, 7.5f, 320);
			}
			if (rand.nextInt(800) == 0){
				BlockPos pos = world.getTopSolidOrLiquidBlock(getPosition().add(rand.nextInt(9)-4, 0, rand.nextInt(9)-4));
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleFloatingGlow(world, 
						pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f
						, 0, 0.125f, 0
						, 219, 179, 255, 1.0f
						, 7.5f, 320);
			}
		}
		if (getDataManager().get(time) == -1){
			getDataManager().set(time, (int)world.getTotalWorldTime());
			getDataManager().setDirty(time);
		}
		else if (world.getTotalWorldTime() > getDataManager().get(time)+24000 || world.getTotalWorldTime() < getDataManager().get(time)){
			getDataManager().set(time, (int)world.getTotalWorldTime());
			getDataManager().setDirty(time);
			List<TileEntityOffertoryPlate> plates = Misc.getTileEntitiesWithin(world, TileEntityOffertoryPlate.class, 
					getPosition().getX()-32, getPosition().getY()-16, getPosition().getZ()-32, 
					getPosition().getX()+32, getPosition().getY()+16, getPosition().getZ()+32);
			for (TileEntityOffertoryPlate p : plates){
				int value = OfferingUtil.getValue(p.inventory.getStackInSlot(0));
				if (value > 0){
					float count = (float)value*0.25f;
					int size = 0;
					for (int i = 0; i < count; i ++){
						int mult = 1;
						if (count - (double)i < 1.0){
							mult = Math.round(1.0f/((count-(float)i)));
						}
						if (Misc.random.nextInt(4*mult) == 0){
							size ++;
						}
					}
					if (!world.isRemote){
						boolean charm = false;
						if (p.lastPlayer != null){
							if (!reputation.containsKey(p.lastPlayer)){
								reputation.put(p.lastPlayer, value);
							}
							else {
								reputation.replace(p.lastPlayer, reputation.get(p.lastPlayer)+value);
							}
							if (Misc.random.nextInt(9) == 0){
								charm = true;
							}
						}
						PacketHandler.INSTANCE.sendToAll(new MessageFairyDustBurstFX(p.getPos().getX()+0.5,p.getPos().getY()+1.0,p.getPos().getZ()+0.5));
						if (size > 0){
							ItemStack stack = new ItemStack(RegistryManager.fairy_dust,Math.min(64, size));
							if (charm){
								stack = new ItemStack(RegistryManager.fairy_charm,1);
							}
							p.inventory.setStackInSlot(0, stack);
							p.markDirty();
							PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(p));
						}
						else {
							p.inventory.setStackInSlot(0, ItemStack.EMPTY);
							p.markDirty();
							PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(p));
						}
					}
				}
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.x = compound.getInteger("x");
		this.y = compound.getInteger("y");
		this.z = compound.getInteger("z");
		getDataManager().set(time,compound.getInteger("time"));
		getDataManager().setDirty(time);
		this.initedPosition = compound.getBoolean("initedPosition");
		this.setPosition(x,y,z);
		NBTTagList ids = compound.getTagList("idList", Constants.NBT.TAG_COMPOUND);
		NBTTagList reps = compound.getTagList("repList", Constants.NBT.TAG_INT);
		for (int i = 0; i < ids.tagCount(); i ++){
			this.reputation.put(NBTUtil.getUUIDFromTag(ids.getCompoundTagAt(i)), reps.getIntAt(i));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("x", x);
		compound.setInteger("y", y);
		compound.setInteger("z", z);
		compound.setInteger("time", getDataManager().get(time));
		compound.setBoolean("initedPosition", initedPosition);
		
		NBTTagList ids = new NBTTagList();
		NBTTagList reps = new NBTTagList();
		for (Entry<UUID, Integer> e : reputation.entrySet()){
			ids.appendTag(NBTUtil.createUUIDTag(e.getKey()));
			reps.appendTag(new NBTTagInt(e.getValue()));
		}
		compound.setTag("idList", ids);
		compound.setTag("repList", reps);
	}

}
