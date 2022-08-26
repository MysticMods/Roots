package epicsquid.roots.entity.ai;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class EntityAIPassiveAttackMelee extends EntityAIAttackMelee {
  public EntityAIPassiveAttackMelee(EntityCreature creature, double speedIn, boolean useLongMemory) {
    super(creature, speedIn, useLongMemory);
  }

  @Override
  protected void checkAndPerformAttack(EntityLivingBase enemy, double distToEnemySqr) {
    double d0 = this.getAttackReachSqr(enemy);

    if (distToEnemySqr <= d0 && this.attackTick <= 0) {
      this.attackTick = 20;
      this.attacker.swingArm(EnumHand.MAIN_HAND);
      attackEntityAsMob(this.attacker, enemy);
    }
  }

  private boolean attackEntityAsMob(EntityLivingBase thisEntity, @Nonnull EntityLivingBase entityIn) {
    float f = 4.0f;
    int i = 0;

    f += EnchantmentHelper.getModifierForCreature(thisEntity.getHeldItemMainhand(), entityIn.getCreatureAttribute());
    i += EnchantmentHelper.getKnockbackModifier(thisEntity);

    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(thisEntity), f);

    if (flag) {
      if (i > 0) {
        entityIn.knockBack(thisEntity, (float) i * 0.5F, (double) MathHelper.sin(thisEntity.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(thisEntity.rotationYaw * 0.017453292F)));
        thisEntity.motionX *= 0.6D;
        thisEntity.motionZ *= 0.6D;
      }

      int j = EnchantmentHelper.getFireAspectModifier(thisEntity);

      if (j > 0) {
        entityIn.setFire(j * 4);
      }

      if (entityIn instanceof EntityPlayer) {
        EntityPlayer entityplayer = (EntityPlayer) entityIn;
        ItemStack itemstack = thisEntity.getHeldItemMainhand();
        ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

        if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, thisEntity) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
          float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(thisEntity) * 0.05F;

          if (thisEntity.rand.nextFloat() < f1) {
            entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
            thisEntity.world.setEntityState(entityplayer, (byte) 30);
          }
        }
      }

      EnchantmentHelper.applyThornEnchantments(entityIn, thisEntity);
      EnchantmentHelper.applyArthropodEnchantments(thisEntity, entityIn);
      thisEntity.setLastAttackedEntity(entityIn);
    }
    return flag;
  }
}
