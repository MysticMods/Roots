package epicsquid.roots.util;

import epicsquid.roots.entity.mob.EntityHuskSlave;
import epicsquid.roots.entity.mob.EntityPigZombieSlave;
import epicsquid.roots.entity.mob.EntityZombieSlave;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SlaveUtil {
  private static Map<Class<?>, Function<World, EntityLivingBase>> slaveMap = new HashMap<>();
  private static Map<Class<?>, Function<World, EntityLivingBase>> reverseSlaveMap = new HashMap<>();

  static {
    slaveMap.put(EntityZombie.class, EntityZombieSlave::new);
    slaveMap.put(EntityHusk.class, EntityHuskSlave::new);
    slaveMap.put(EntityPigZombie.class, EntityPigZombieSlave::new);
    reverseSlaveMap.put(EntityZombieSlave.class, EntityZombie::new);
    reverseSlaveMap.put(EntityHuskSlave.class, EntityHusk::new);
    reverseSlaveMap.put(EntityPigZombieSlave.class, EntityPigZombie::new);
  }

  public static boolean canBecomeSlave(Entity entity) {
    return entity != null && slaveMap.keySet().contains(entity.getClass());
  }

  public static boolean isSlave(Entity entity) {
    return entity != null && reverseSlaveMap.keySet().contains(entity.getClass());
  }

  public static EntityLivingBase enslave(EntityLivingBase parent) {
    return convert(slaveMap.get(parent.getClass()), parent);
  }

  public static EntityLivingBase revert(EntityLivingBase parent) {
    return convert(reverseSlaveMap.get(parent.getClass()), parent);
  }

  @Nullable
  public static EntityLivingBase convert(Function<World, EntityLivingBase> builder, EntityLivingBase parent) {
    if (parent.world.isRemote) {
      return null;
    }

    EntityLivingBase slave = builder.apply(parent.world);

    for (PotionEffect pot : parent.getActivePotionEffects()) {
      slave.addPotionEffect(pot);
    }

    for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
      slave.setItemStackToSlot(slot, parent.getItemStackFromSlot(slot));
    }

    slave.setHealth(parent.getHealth());
    slave.prevPosX = parent.prevPosX;
    slave.prevPosY = parent.prevPosY;
    slave.prevPosZ = parent.prevPosZ;
    slave.posX = parent.posX;
    slave.posY = parent.posY;
    slave.posZ = parent.posZ;
    slave.motionX = parent.motionX;
    slave.motionY = parent.motionY;
    slave.motionZ = parent.motionZ;
    slave.rotationYaw = parent.rotationYaw;
    slave.rotationPitch = parent.rotationPitch;
    slave.prevRotationYaw = parent.prevRotationYaw;
    slave.prevRotationPitch = parent.prevRotationPitch;
    slave.onGround = parent.onGround;
    slave.collidedHorizontally = parent.collidedHorizontally;
    slave.collidedVertically = parent.collidedVertically;
    slave.collided = parent.collided;
    slave.width = parent.width;
    slave.height = parent.height;
    slave.prevDistanceWalkedModified = parent.prevDistanceWalkedModified;
    slave.distanceWalkedModified = parent.distanceWalkedModified;
    slave.distanceWalkedOnStepModified = parent.distanceWalkedOnStepModified;
    slave.fallDistance = parent.fallDistance;
    slave.lastTickPosX = parent.lastTickPosX;
    slave.lastTickPosY = parent.lastTickPosY;
    slave.lastTickPosZ = parent.lastTickPosZ;
    slave.stepHeight = parent.stepHeight;
    slave.entityCollisionReduction = parent.entityCollisionReduction;
    slave.ticksExisted = parent.ticksExisted;
    slave.hurtResistantTime = parent.hurtResistantTime;
    slave.chunkCoordX = parent.chunkCoordX;
    slave.chunkCoordY = parent.chunkCoordY;
    slave.chunkCoordZ = parent.chunkCoordZ;
    slave.timeUntilPortal = parent.timeUntilPortal;
    slave.dimension = parent.dimension;
    return slave;
  }
}
