package epicsquid.roots.entity.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellWildfire;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityIcicle extends EntityFireball {
  private StaffModifierInstanceList modifiers = null;

  public EntityIcicle (World worldIn) {
    super(worldIn);
  }

  public EntityIcicle(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
    super(worldIn, x, y, z, accelX, accelY, accelZ);
  }

  public EntityIcicle(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
    super(worldIn, shooter, accelX, accelY, accelZ);
  }

  @Override
  protected void entityInit() {
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
        Roots.logger.info("Icicle hit: " + result.entityHit);
      }
      setDead();
    }
  }
}
