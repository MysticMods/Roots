package teamroots.roots.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollow extends EntityAIBase {
	private EntityLivingBase target;
	private EntityLiving entity;
    World world;
    private final double followSpeed;
    private final PathNavigate pathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private float oldWaterCost;
    
    public EntityAIFollow(EntityLiving entity, EntityLivingBase target, double followSpeedIn, float minDistIn, float maxDistIn)
    {
        this.entity = entity;
        this.world = entity.world;
        this.followSpeed = followSpeedIn;
        this.pathfinder = entity.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(7);

        if (!(entity.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }
    
	@Override
	public boolean shouldExecute() {
		if (this.entity != null && this.target != null){
			if (this.entity.getDistanceSqToEntity(target) < (double)(this.minDist * this.minDist)){
	            return false;
	        }
			return true;
		}
		return false;
	}
	
    public boolean continueExecuting() {
        return !this.pathfinder.noPath() && this.entity.getDistanceSqToEntity(this.target) > (double)(this.maxDist * this.maxDist);
    }
    
    public void startExecuting()
    {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    public void resetTask()
    {
        this.target = null;
        this.pathfinder.clearPathEntity();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    private boolean isEmptyBlock(BlockPos pos)
    {
        IBlockState iblockstate = this.world.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
    }

    public void updateTask()
    {
    	this.entity.setHealth(0);
        this.entity.getLookHelper().setLookPositionWithEntity(this.target, 10.0F, (float)this.entity.getVerticalFaceSpeed());

        if (--this.timeToRecalcPath <= 0)
        {
            this.timeToRecalcPath = 10;

            if (!this.pathfinder.tryMoveToEntityLiving(this.target, this.followSpeed))
            {
                if (!this.entity.getLeashed())
                {
                    if (this.entity.getDistanceSqToEntity(this.target) >= 144.0D)
                    {
                        int i = MathHelper.floor(this.target.posX) - 2;
                        int j = MathHelper.floor(this.target.posZ) - 2;
                        int k = MathHelper.floor(this.target.getEntityBoundingBox().minY);

                        for (int l = 0; l <= 4; ++l)
                        {
                            for (int i1 = 0; i1 <= 4; ++i1)
                            {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.world.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isFullCube() && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1)))
                                {
                                    this.entity.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.entity.rotationYaw, this.entity.rotationPitch);
                                    this.pathfinder.clearPathEntity();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
