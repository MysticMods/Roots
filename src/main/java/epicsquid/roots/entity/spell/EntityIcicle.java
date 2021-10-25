package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModDamage;
import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.spell.SpellStormCloud;
import epicsquid.roots.spell.SpellWildfire;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityIcicle extends DamagingProjectileEntity {
  private ISnapshot modifiers = null;
  private SpellType type = null;

  public EntityIcicle(World worldIn) {
    super(worldIn);
    this.setSize(0.25F, 0.25F);
  }

  public EntityIcicle(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, SpellType type) {
    super(worldIn, shooter, accelX, accelY, accelZ);
    this.setSize(0.25F, 0.25F);
    this.type = type;
    this.motionY = this.accelerationY;
    this.motionX = this.accelerationX;
    this.motionZ = this.accelerationZ;
    ProjectileHelper.rotateTowardsMovement(this, 5f);
  }

  @Override
  protected void entityInit() {
  }

  public void setModifiers(ISnapshot modifiers) {
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
        float dam;
        switch (type) {
          default:
          case WILDFIRE:
            dam = SpellWildfire.instance.ice_damage;
            break;
          case STORM_CLOUD:
            dam = SpellStormCloud.instance.icicle_damage;
            break;
        }
        dam = modifiers == null ? dam : dam;
        result.entityHit.attackEntityFrom(source, dam);
      }
      setDead();
    }
  }

  public enum SpellType {
    WILDFIRE, STORM_CLOUD
  }
}
