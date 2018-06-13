package teamroots.roots.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.World;
import teamroots.roots.RegistryManager;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageMoonlightBurstFX;
import teamroots.roots.network.message.MessageMoonlightSparkleFX;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.recipe.MoonlightRecipe;
import teamroots.roots.recipe.RecipeRegistry;
import teamroots.roots.util.IEntityRenderingLater;
import teamroots.roots.util.NBTUtil;
import teamroots.roots.util.NoiseGenUtil;

public class EntityAuspiciousPoint extends Entity implements IEntityRenderingLater {
	Random runeRand = new Random();
    public static final DataParameter<Float> progress = EntityDataManager.<Float>createKey(EntityAuspiciousPoint.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> solidity = EntityDataManager.<Float>createKey(EntityAuspiciousPoint.class, DataSerializers.FLOAT);
    public static final DataParameter<BlockPos> rune1 = EntityDataManager.<BlockPos>createKey(EntityAuspiciousPoint.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<BlockPos> rune2 = EntityDataManager.<BlockPos>createKey(EntityAuspiciousPoint.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<BlockPos> rune3 = EntityDataManager.<BlockPos>createKey(EntityAuspiciousPoint.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<BlockPos> rune4 = EntityDataManager.<BlockPos>createKey(EntityAuspiciousPoint.class, DataSerializers.BLOCK_POS);
	public int x = 0;
	public int y = 0;
	public int z = 0;
	public int height = 0;
	public boolean initedPosition = false;
    public EntityAuspiciousPoint(World worldIn) {
		super(worldIn);
		this.setInvisible(false);
		this.setSize(1, 1);
		getDataManager().register(progress, Float.valueOf(0));
		getDataManager().register(solidity, Float.valueOf(0));
		Random random = new Random();
		getDataManager().register(rune1, new BlockPos(getPosition().getX()+random.nextInt(4)+1,-1,getPosition().getZ()+random.nextInt(4)+1));
		getDataManager().register(rune2, new BlockPos(getPosition().getX()-random.nextInt(4)-1,-1,getPosition().getZ()+random.nextInt(4)+1));
		getDataManager().register(rune3, new BlockPos(getPosition().getX()-random.nextInt(4)-1,-1,getPosition().getZ()-random.nextInt(4)-1));
		getDataManager().register(rune4, new BlockPos(getPosition().getX()+random.nextInt(4)+1,-1,getPosition().getZ()-random.nextInt(4)-1));
	}
    
    @Override
    public void setPosition(double x, double y, double z){
    	super.setPosition(x, y, z);
    	this.x = (int)x;
    	this.y = (int)y;
    	this.z = (int)z;
    	initedPosition = true;
    }
    
    public float getSolidity(){
    	return getDataManager().get(solidity);
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
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		if (world.isRemote && this.getSolidity() > 0){
			if (rand.nextInt(2) == 0 && !world.isAirBlock(getDataManager().get(rune1))){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleGlow(world, 
						getDataManager().get(rune1).getX()+0.5f, getDataManager().get(rune1).getY()+0.125f, getDataManager().get(rune1).getZ()+0.5f
						, 0, 0.0625f, 0
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 5.5f, 20);
			}
			if (rand.nextInt(2) == 0 && !world.isAirBlock(getDataManager().get(rune2))){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleGlow(world, 
						getDataManager().get(rune2).getX()+0.5f, getDataManager().get(rune2).getY()+0.125f, getDataManager().get(rune2).getZ()+0.5f
						, 0, 0.0625f, 0
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 5.5f, 20);
			}
			if (rand.nextInt(2) == 0 && !world.isAirBlock(getDataManager().get(rune3))){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleGlow(world, 
						getDataManager().get(rune3).getX()+0.5f, getDataManager().get(rune3).getY()+0.125f, getDataManager().get(rune3).getZ()+0.5f
						, 0, 0.0625f, 0
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 5.5f, 20);
			}
			if (rand.nextInt(2) == 0 && !world.isAirBlock(getDataManager().get(rune4))){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleGlow(world, 
						getDataManager().get(rune4).getX()+0.5f, getDataManager().get(rune4).getY()+0.125f, getDataManager().get(rune4).getZ()+0.5f
						, 0, 0.0625f, 0
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 5.5f, 20);
			}
			if (rand.nextInt(100) == 0){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleFloatingGlow(world, 
						getDataManager().get(rune1).getX()+0.5f, getDataManager().get(rune1).getY()+0.5f, getDataManager().get(rune1).getZ()+0.5f
						, 0, 0.125f, 0
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 7.5f, 320);
			}
			if (rand.nextInt(100) == 0){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleFloatingGlow(world, 
						getDataManager().get(rune2).getX()+0.5f, getDataManager().get(rune2).getY()+0.5f, getDataManager().get(rune2).getZ()+0.5f
						, 0, 0.125f, 0
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 7.5f, 320);
			}
			if (rand.nextInt(100) == 0){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleFloatingGlow(world, 
						getDataManager().get(rune3).getX()+0.5f, getDataManager().get(rune3).getY()+0.5f, getDataManager().get(rune3).getZ()+0.5f
						, 0, 0.125f, 0
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 7.5f, 320);
			}
			if (rand.nextInt(100) == 0){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleFloatingGlow(world, 
						getDataManager().get(rune4).getX()+0.5f, getDataManager().get(rune4).getY()+0.5f, getDataManager().get(rune4).getZ()+0.5f
						, 0, 0.125f, 0
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 7.5f, 320);
			}
			if (getDataManager().get(progress) > 0){
			}
		}
		BlockPos rune1Pos = getDataManager().get(rune1);
		BlockPos rune2Pos = getDataManager().get(rune2);
		BlockPos rune3Pos = getDataManager().get(rune3);
		BlockPos rune4Pos = getDataManager().get(rune4);
		if (rune1Pos.getY() < 0 && initedPosition){
			runeRand.setSeed((long)(NoiseGenUtil.getNoise(world.getSeed(),getPosition().getX(),getPosition().getZ())*Long.MAX_VALUE));
			int r1X = getPosition().getX()+(runeRand.nextInt(3)+1);
			int r1Z = getPosition().getZ()+(runeRand.nextInt(3)+1);
			int r2X = getPosition().getX()-(runeRand.nextInt(3)+1);
			int r2Z = getPosition().getZ()+(runeRand.nextInt(3)+1);
			int r3X = getPosition().getX()-(runeRand.nextInt(3)+1);
			int r3Z = getPosition().getZ()-(runeRand.nextInt(3)+1);
			int r4X = getPosition().getX()+(runeRand.nextInt(3)+1);
			int r4Z = getPosition().getZ()-(runeRand.nextInt(3)+1);
			getDataManager().set(rune1, new BlockPos(r1X, world.getTopSolidOrLiquidBlock(new BlockPos(r1X,64,r1Z)).getY(),r1Z));
			getDataManager().set(rune2, new BlockPos(r2X, world.getTopSolidOrLiquidBlock(new BlockPos(r2X,64,r2Z)).getY(),r2Z));
			getDataManager().set(rune3, new BlockPos(r3X, world.getTopSolidOrLiquidBlock(new BlockPos(r3X,64,r3Z)).getY(),r3Z));
			getDataManager().set(rune4, new BlockPos(r4X, world.getTopSolidOrLiquidBlock(new BlockPos(r4X,64,r4Z)).getY(),r4Z));
			getDataManager().setDirty(rune1);
			getDataManager().setDirty(rune2);
			getDataManager().setDirty(rune3);
			getDataManager().setDirty(rune4);
			rune1Pos = getDataManager().get(rune1);
			rune2Pos = getDataManager().get(rune2);
			rune3Pos = getDataManager().get(rune3);
			rune4Pos = getDataManager().get(rune4);
		}
		int rune1Height = world.getTopSolidOrLiquidBlock(rune1Pos).getY();
		int rune2Height = world.getTopSolidOrLiquidBlock(rune2Pos).getY();
		int rune3Height = world.getTopSolidOrLiquidBlock(rune3Pos).getY();
		int rune4Height = world.getTopSolidOrLiquidBlock(rune4Pos).getY();
		if (rune1Pos.getY() != rune1Height){
			getDataManager().set(rune1, new BlockPos(rune1Pos.getX(),rune1Height,rune1Pos.getZ()));
			getDataManager().setDirty(rune1);
		}
		if (rune2Pos.getY() != rune2Height){
			getDataManager().set(rune2, new BlockPos(rune2Pos.getX(),rune2Height,rune2Pos.getZ()));
			getDataManager().setDirty(rune2);
		}
		if (rune3Pos.getY() != rune3Height){
			getDataManager().set(rune3, new BlockPos(rune3Pos.getX(),rune3Height,rune3Pos.getZ()));
			getDataManager().setDirty(rune3);
		}
		if (rune4Pos.getY() != rune4Height){
			getDataManager().set(rune4, new BlockPos(rune4Pos.getX(),rune4Height,rune4Pos.getZ()));
			getDataManager().setDirty(rune4);
		}
		height = getEntityWorld().getTopSolidOrLiquidBlock(getPosition()).getY();
		if (world.isRemote && getDataManager().get(progress) > 0){
			float partY = NoiseGenUtil.interpolate(256.0f, (float)height, getDataManager().get(progress)/200.0f);

			if (rand.nextInt(5) == 0){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleLineGlow(world, 
						getDataManager().get(rune1).getX()+0.5f, getDataManager().get(rune1).getY()+0.5f, getDataManager().get(rune1).getZ()+0.5f
						, x+0.5f, height+1.5f, z+0.5f
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 7.5f, 40);
			}
			if (rand.nextInt(5) == 0){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleLineGlow(world, 
						getDataManager().get(rune2).getX()+0.5f, getDataManager().get(rune2).getY()+0.5f, getDataManager().get(rune2).getZ()+0.5f
						, x+0.5f, height+1.5f, z+0.5f
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 7.5f, 40);
			}
			if (rand.nextInt(5) == 0){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleLineGlow(world, 
						getDataManager().get(rune3).getX()+0.5f, getDataManager().get(rune3).getY()+0.5f, getDataManager().get(rune3).getZ()+0.5f
						, x+0.5f, height+1.5f, z+0.5f
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 7.5f, 40);
			}
			if (rand.nextInt(5) == 0){
				float colorRand = rand.nextFloat();
				ParticleUtil.spawnParticleLineGlow(world, 
						getDataManager().get(rune4).getX()+0.5f, getDataManager().get(rune4).getY()+0.5f, getDataManager().get(rune4).getZ()+0.5f
						, x+0.5f, height+1.5f, z+0.5f
						, MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), MessageMoonlightBurstFX.getColorCycle(colorRand*360.0f), 255.0f, this.getSolidity()
						, 7.5f, 40);
			}
		}
		boolean isShining = false;
		//if (!world.isRemote){
			List<EntityItem> stacks = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x,height,z,x+1,height+256,z+1));
			for (int i = 0; i < stacks.size(); i ++){
				if (Block.getBlockFromItem(stacks.get(i).getItem().getItem()) == Blocks.STONE){
					isShining = true;
				}
			}
			if (this.ticksExisted % (rand.nextInt(20)+1) == 0){
				if (stacks.size() > 0){
					boolean changed = false;
					for (int i = 0; i < stacks.size() && !changed; i ++){
						if (Block.getBlockFromItem(stacks.get(i).getItem().getItem()) == Blocks.STONE){
							isShining = true;
							if (!world.isRemote){
								ItemStack stack = stacks.get(i).getItem().copy();
								stack.shrink(1);
								stacks.get(i).setItem(stack);
								world.spawnEntity(new EntityItem(world,stacks.get(i).posX,stacks.get(i).posY,stacks.get(i).posZ,new ItemStack(RegistryManager.runestone,1)));
								PacketHandler.INSTANCE.sendToAll(new MessageMoonlightSparkleFX(stacks.get(i).posX,stacks.get(i).posY+stacks.get(i).height/2.0f,stacks.get(i).posZ));
							}
						}
					}
				}
			}
			if (true){
				boolean valid = false;
				IBlockState core = world.getBlockState(new BlockPos(x,height,z));
				IBlockState outer1 = world.getBlockState(getDataManager().get(rune1));
				IBlockState outer2 = world.getBlockState(getDataManager().get(rune2));
				IBlockState outer3 = world.getBlockState(getDataManager().get(rune3));
				IBlockState outer4 = world.getBlockState(getDataManager().get(rune4));
				MoonlightRecipe recipe = RecipeRegistry.getMoonlightRecipe(core, outer1, outer2, outer3, outer4);
				if (recipe != null){
					valid = true;
				}
				if (valid){
					isShining = true;
					if (!world.isRemote){
						getDataManager().set(progress, getDataManager().get(progress)+1);
						getDataManager().setDirty(progress);
						if (getDataManager().get(progress) > 200 && !world.isRemote){
							getDataManager().set(progress, 0f);
							getDataManager().setDirty(progress);
		
							world.setBlockState(new BlockPos(x,height,z), recipe.resultState, 8);
							world.notifyBlockUpdate(new BlockPos(x,height,z), core, recipe.resultState, 8);
							
							PacketHandler.INSTANCE.sendToAll(new MessageMoonlightBurstFX(x+0.5,height+0.5,z+0.5));
							
							world.destroyBlock(getDataManager().get(rune1), false);
							world.notifyBlockUpdate(getDataManager().get(rune1), outer1, Blocks.AIR.getDefaultState(), 8);
							world.destroyBlock(getDataManager().get(rune2), false);
							world.notifyBlockUpdate(getDataManager().get(rune2), outer2, Blocks.AIR.getDefaultState(), 8);
							world.destroyBlock(getDataManager().get(rune3), false);
							world.notifyBlockUpdate(getDataManager().get(rune3), outer3, Blocks.AIR.getDefaultState(), 8);
							world.destroyBlock(getDataManager().get(rune4), false);
							world.notifyBlockUpdate(getDataManager().get(rune4), outer4, Blocks.AIR.getDefaultState(), 8);
						}
					}
				}
				else if (!world.isRemote){
					if (getDataManager().get(progress) != 0){
						getDataManager().set(progress, 0f);
						getDataManager().setDirty(progress);
					}
				}
			}
		//}
		if (isShining && getDataManager().get(solidity) < 1.0f){
			getDataManager().set(solidity, Math.min(1.0f, getDataManager().get(solidity)+0.05f));
			getDataManager().setDirty(solidity);
		}
		else if (!isShining && getDataManager().get(solidity) > 0.0f){
			getDataManager().set(solidity, Math.max(0.0f, getDataManager().get(solidity)-0.05f));
			getDataManager().setDirty(solidity);
		}
		//ParticleUtil.spawnParticleGlow(getEntityWorld(), (float)posX+0.5f, (float)getEntityWorld().getTopSolidOrLiquidBlock(getPosition()).getY()+0.25f, (float)posZ+0.5f, 0, 0, 0, 1, 1, 1, 0.25f, 10.0f+10.0f*rand.nextFloat(), 40);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.x = compound.getInteger("x");
		this.y = compound.getInteger("y");
		this.z = compound.getInteger("z");
		this.initedPosition = compound.getBoolean("initedPosition");
		this.setPosition(x,y,z);
		getDataManager().set(progress, compound.getFloat("progress"));
		getDataManager().setDirty(progress);
		getDataManager().set(solidity, compound.getFloat("solidity"));
		getDataManager().setDirty(solidity);
		getDataManager().set(rune1, new BlockPos(compound.getInteger("rune1_x"), compound.getInteger("rune1_y"), compound.getInteger("rune1_z")));
		getDataManager().set(rune2, new BlockPos(compound.getInteger("rune2_x"), compound.getInteger("rune2_y"), compound.getInteger("rune2_z")));
		getDataManager().set(rune3, new BlockPos(compound.getInteger("rune3_x"), compound.getInteger("rune3_y"), compound.getInteger("rune3_z")));
		getDataManager().set(rune4, new BlockPos(compound.getInteger("rune4_x"), compound.getInteger("rune4_y"), compound.getInteger("rune4_z")));
		getDataManager().setDirty(rune1);
		getDataManager().setDirty(rune2);
		getDataManager().setDirty(rune3);
		getDataManager().setDirty(rune4);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("x", x);
		compound.setInteger("y", y);
		compound.setInteger("z", z);
		compound.setBoolean("initedPosition", initedPosition);
		compound.setFloat("progress", getDataManager().get(progress));
		compound.setFloat("solidity", getDataManager().get(solidity));
		compound.setInteger("rune1_x", getDataManager().get(rune1).getX());
		compound.setInteger("rune1_y", getDataManager().get(rune1).getY());
		compound.setInteger("rune1_z", getDataManager().get(rune1).getZ());
		compound.setInteger("rune2_x", getDataManager().get(rune2).getX());
		compound.setInteger("rune2_y", getDataManager().get(rune2).getY());
		compound.setInteger("rune2_z", getDataManager().get(rune2).getZ());
		compound.setInteger("rune3_x", getDataManager().get(rune3).getX());
		compound.setInteger("rune3_y", getDataManager().get(rune3).getY());
		compound.setInteger("rune3_z", getDataManager().get(rune3).getZ());
		compound.setInteger("rune4_x", getDataManager().get(rune4).getX());
		compound.setInteger("rune4_y", getDataManager().get(rune4).getY());
		compound.setInteger("rune4_z", getDataManager().get(rune4).getZ());
	}

}
