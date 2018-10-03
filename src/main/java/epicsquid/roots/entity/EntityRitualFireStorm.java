package epicsquid.roots.entity;

import java.util.Random;

import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityRitualFireStorm  extends Entity implements IRitualEntity {
  Random runeRand = new Random();
  public static final DataParameter<Integer> lifetime = EntityDataManager.<Integer>createKey(EntityRitualFireStorm.class, DataSerializers.VARINT);
  public double x = 0;
  public double y = 0;
  public double z = 0;
  public EntityRitualFireStorm(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, RitualRegistry.ritual_fire_storm.duration+20);
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
    float alpha = (float)Math.min(40, (RitualRegistry.ritual_light.duration+20)-getDataManager().get(lifetime))/40.0f;
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    getDataManager().set(lifetime, getDataManager().get(lifetime)-1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0){
      setDead();
    }
    if (world.isRemote && getDataManager().get(lifetime) > 0){
      //todo: fix particle when available | ParticleUtil.spawnParticleStar(world, (float)posX, (float)posY, (float)posZ, 0, 0, 0, 255, 96, 32, 0.5f*alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0){
        //todo: fix particle when available | ParticleUtil.spawnParticleSpark(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.0625f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 255, 96, 32, 1.0f*alpha, 1.0f+rand.nextFloat(), 160);
      }
      if (rand.nextInt(2) == 0){
        for (float i = 0; i < 360; i += rand.nextFloat()*45.0f){
          float tx = (float)posX + 2f*(float)Math.sin(Math.toRadians(i));
          float ty = (float)posY;
          float tz = (float)posZ + 2f*(float)Math.cos(Math.toRadians(i));
          //todo: fix particle when available | ParticleUtil.spawnParticleFiery(world, tx, ty, tz, 0, 0, 0, 255, 96, 32, 0.5f*alpha, 6.0f+6.0f*rand.nextFloat(), 40);
        }
      }
    }
    if (this.ticksExisted % 10 == 0){
      //todo: make entity flare
//      List<EntityFlare> projectiles = world.getEntitiesWithinAABB(EntityFlare.class, new AxisAlignedBB(posX-15.5f,posY-15.5,posZ-15.5,posX+15.5,posY+15.5,posZ+15.5));
//      if (projectiles.size() < 20 && !world.isRemote){
//        EntityFlare flare = new EntityFlare(world);
//        flare.initCustom(posX+21.0f*(rand.nextFloat()-0.5f),posY+43.0f,posZ+21.0f*(rand.nextFloat()-0.5f),0.125f*(rand.nextFloat()-0.5f),-0.5f-rand.nextFloat()*0.5f,0.125f*(rand.nextFloat()-0.5f),4.0f+8.0f*rand.nextFloat());
//        world.spawnEntity(flare);
//      }
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