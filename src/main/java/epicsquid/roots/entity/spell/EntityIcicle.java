package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellWildfire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityIcicle extends EntitySpellModifiable<SpellWildfire> {
  private EntityLivingBase shootingEntity;
  private Entity target;
  private UUID shooterId, targetId;
  private StaffModifierInstanceList modifiers = null;
  private double accelX, accelY, accelZ;

  public EntityIcicle(World worldIn) {
    super(worldIn, SpellWildfire.instance, 90);
    this.setSize(0.3125F, 0.3125F);
  }

  @Override
  protected void entityInit() {
  }

  public EntityIcicle(World worldIn, EntityLivingBase shooter, Entity target) {
    this(worldIn);
    this.setSize(0.3125F, 0.3125F);
    this.shooterId = shooter.getUniqueID();
    this.targetId = target.getUniqueID();
    this.shootingEntity = shooter;
    this.target = target;
    double x = shooter.posX + Util.rand.nextDouble() - 0.5;
    double y = shooter.posY + shooter.getEyeHeight() + 0.5;
    double z = shooter.posZ + Util.rand.nextDouble() - 0.5;
    this.setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
    this.setPosition(x, y, z);
    this.motionY = 0;
    this.motionX = 0;
    this.motionZ = 0;
  }

  @Nullable
  public EntityLivingBase getShooter() {
    if (world.isRemote) {
      return null;
    }

    if (shootingEntity != null) {
      return shootingEntity;
    }

    if (shooterId != null) {
      shootingEntity = (EntityLivingBase) ((WorldServer) world).getEntityFromUuid(shooterId);
    }

    return shootingEntity;
  }

  @Nullable
  public Entity getTarget() {
    if (world.isRemote) {
      return null;
    }

    if (target != null) {
      return target;
    }

    if (targetId != null) {
      target = ((WorldServer) world).getEntityFromUuid(targetId);
    }

    return target;
  }

  public void setModifiers(StaffModifierInstanceList modifiers) {
    this.modifiers = modifiers;
  }

  @SideOnly(Side.CLIENT)
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

    if (Double.isNaN(d0)) {
      d0 = 4.0D;
    }

    d0 = d0 * 64.0D;
    return distance < d0 * d0;
  }

  @Override
  public boolean isBurning() {
    return false;
  }

  @Override
  public boolean canBeCollidedWith() {
    return false;
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.shooterId = compound.getUniqueId("shooter");
    this.targetId = compound.getUniqueId("target");
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setUniqueId("shooter", this.shooterId);
    compound.setUniqueId("target", this.targetId);
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return false;
  }

  @Override
  public void onUpdate() {
    if (world != null && !world.isRemote) {
      if (shootingEntity != null && !shootingEntity.isDead && target != null && !target.isDead && world.isBlockLoaded(getPosition())) {
        super.onUpdate();

        double f = 0.95;

        Entity target = getTarget();
        if (target == null) {
          setDead();
          return;
        }

        Vec3d accel = target.getPositionVector().subtract(getPositionVector());
        this.motionX += accel.x;
        this.motionY += accel.y;
        this.motionZ += accel.z;
        this.motionX *= 0.95;
        this.motionY *= 0.95;
        this.motionZ *= 0.95;
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        this.setPosition(this.posX, this.posY, this.posZ);
        ProjectileHelper.rotateTowardsMovement(this, 0.2F);

        if (this.isInWater()) {
          for (int i = 0; i < 4; ++i) {
            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
          }
        }



        RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, true, this.shootingEntity);

        if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
          this.onImpact(raytraceresult);
        }

        Roots.logger.info(getPositionVector());
      } else {
        setDead();
      }
    }
  }

  protected void onImpact(RayTraceResult result) {
    if (!world.isRemote) {
      if (result.entityHit != null) {
        DamageSource source;
        if (shootingEntity != null) {
          source = ModDamage.physicalDamageFrom(shootingEntity);
        } else {
          source = ModDamage.PHYSICAL_DAMAGE;
        }
        float dam = modifiers == null ? SpellWildfire.instance.ice_damage : modifiers.ampFloat(SpellWildfire.instance.ice_damage);
        result.entityHit.attackEntityFrom(source, dam);
        Roots.logger.info("Hit: " + result.entityHit);
      }
      setDead();
    }
  }

  public float getBrightness() {
    return 1.0F;
  }

  @SideOnly(Side.CLIENT)
  public int getBrightnessForRender() {
    return 15728880;
  }
}
