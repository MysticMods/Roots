package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModDamage;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellWildfire;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityIcicle extends EntityFireball {
  private StaffModifierInstanceList modifiers = null;

  public EntityIcicle(World worldIn) {
    super(worldIn);
    this.setSize(0.3125F, 0.3125F);
  }

  public EntityIcicle(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
    super(worldIn, shooter, accelX, accelY, accelZ);
    this.setSize(0.3125F, 0.3125F);
  }

  @SideOnly(Side.CLIENT)
  public EntityIcicle(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
    super(worldIn, x, y, z, accelX, accelY, accelZ);
    this.setSize(0.3125F, 0.3125F);
  }

  public void setModifiers(StaffModifierInstanceList modifiers) {
    this.modifiers = modifiers;
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
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return false;
  }

  @SideOnly(Side.CLIENT)
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    this.setPosition(x, y, z);
    this.setRotation(yaw, pitch);
  }

  @SideOnly(Side.CLIENT)
  public void setVelocity(double x, double y, double z) {
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;

    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      float f = MathHelper.sqrt(x * x + z * z);
      this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (180D / Math.PI));
      this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
      this.prevRotationPitch = this.rotationPitch;
      this.prevRotationYaw = this.rotationYaw;
      this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }
  }

  @Override
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
      }
      setDead();
    }
  }
}
