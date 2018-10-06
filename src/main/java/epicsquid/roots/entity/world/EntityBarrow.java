package epicsquid.roots.entity.world;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityBarrow extends Entity {
  public int x = 0;
  public int y = 0;
  public int z = 0;
  public boolean initedPosition = false;
  public EntityBarrow(World worldIn) {
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

  }

  @Override
  public void onUpdate(){
    ticksExisted ++;
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    if (ticksExisted % 70 == 0 && (world.getWorldTime() % 24000) > 12000 && !world.isRemote){
      List<EntityMob> nearbyMobs = world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(posX-16,posY-16,posZ-16,posX+16,posY+16,posZ+16));
      if (nearbyMobs.size() < 10){
        if (rand.nextInt(10) == 0){
          EntityZombie zombie = new EntityZombie(world);
          zombie.setWorld(world);
          zombie.onInitialSpawn(world.getDifficultyForLocation(getPosition()), null);
          zombie.setPosition(posX+2.0f*(rand.nextFloat()-0.5f),posY,posZ+2.0f*(rand.nextFloat()-0.5f));
          world.spawnEntity(zombie);
        }
        else if (rand.nextInt(9) == 0){
          EntitySkeleton skeleton = new EntitySkeleton(world);
          skeleton.setWorld(world);
          skeleton.onInitialSpawn(world.getDifficultyForLocation(getPosition()), null);
          skeleton.setPosition(posX+2.0f*(rand.nextFloat()-0.5f),posY,posZ+2.0f*(rand.nextFloat()-0.5f));
          world.spawnEntity(skeleton);
        }
      }
    }
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.x = compound.getInteger("x");
    this.y = compound.getInteger("y");
    this.z = compound.getInteger("z");
    this.initedPosition = compound.getBoolean("initedPosition");
    this.setPosition(x,y,z);
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setInteger("x", x);
    compound.setInteger("y", y);
    compound.setInteger("z", z);
    compound.setBoolean("initedPosition", initedPosition);
  }

}