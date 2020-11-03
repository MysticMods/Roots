package epicsquid.roots.potion;

import epicsquid.roots.init.ModDamage;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellGeas;
import epicsquid.roots.util.EntityUtil;
import epicsquid.roots.util.SlaveUtil;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class PotionGeas extends Potion {
  public PotionGeas() {
    super(false, 0xffe100);
    setPotionName("Geas");
    setIconIndex(0, 0);
    setBeneficial();
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

/*  @Override
  public void applyAttributesModifiersToEntity(EntityLivingBase target, AbstractAttributeMap attributeMapIn, int amplifier) {
    super.applyAttributesModifiersToEntity(target, attributeMapIn, amplifier);
    if (target instanceof EntityCreature && EntityUtil.isFriendly(target)) {
      EntityCreature entity = (EntityCreature) target;
      ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(target.getEntityData(), SpellGeas.instance);
      if (mods.has(SpellGeas.PEACEFUL)) {
        boolean hadAttack = false;
        for (EntityAITasks.EntityAITaskEntry task : entity.tasks.taskEntries) {
          if (task.action instanceof EntityAIAttackMelee) {
            hadAttack = true;
            break;
          }
        }
        if (!hadAttack) {
          entity.tasks.addTask(5, new EntityAIAttackMelee(entity, 1.0d, false));
        }
        boolean hadTarget = false;
        for (EntityAITasks.EntityAITaskEntry task : entity.targetTasks.taskEntries) {
          if (task.action instanceof EntityAINearestAttackableTarget) {
            hadTarget = true;
            break;
          }
        }
        if (!hadTarget) {
          entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(entity, EntityMob.class, 10, true, true, EntityUtil::isHostile));
        }
        boolean hadDamage = false;
        //noinspection ConstantConditions
        if (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) == null) {
          entity.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2);
        } else {
          hadDamage = true;
        }
        target.getEntityData().setBoolean("hadAttack", hadAttack);
        target.getEntityData().setBoolean("hadTarget", hadTarget);
        target.getEntityData().setBoolean("hadDamage", hadDamage);
      }
    }
  }*/

  @Override
  public void removeAttributesModifiersFromEntity(EntityLivingBase target, AbstractAttributeMap attributeMapIn, int amplifier) {
    super.removeAttributesModifiersFromEntity(target, attributeMapIn, amplifier);
    if (SlaveUtil.isSlave(target)) {
      EntityLivingBase slave = SlaveUtil.revert(target);
      target.world.spawnEntity(slave);
      target.setDropItemsWhenDead(false);
      target.setDead();
      slave.setPositionAndUpdate(slave.posX, slave.posY, slave.posZ);
    } else {/*if (target instanceof EntityCreature && EntityUtil.isFriendly(target)) {
      EntityCreature entity = (EntityCreature) target;
      ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(target.getEntityData(), SpellGeas.instance);
      if (mods.has(SpellGeas.PEACEFUL)) {
        EntityAIBase attack = null;
        for (EntityAITasks.EntityAITaskEntry task : entity.tasks.taskEntries) {
          if (task.action instanceof EntityAIAttackMelee) {
            attack = task.action;
            break;
          }
        }
        if (attack != null && !entity.getEntityData().getBoolean("hadAttack")) {
          entity.tasks.removeTask(attack);
        }
        EntityAIBase targAi = null;
        for (EntityAITasks.EntityAITaskEntry task : entity.targetTasks.taskEntries) {
          if (task.action instanceof EntityAINearestAttackableTarget) {
            targAi = task.action;
            break;
          }
        }
        if (targAi != null && !entity.getEntityData().getBoolean("hadTarget")) {
          entity.targetTasks.removeTask(targAi);
        }

        if (!entity.getEntityData().getBoolean("hadDamage")) {
          IAttributeInstance attr = entity.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
          if (attr != null) {
            attr.setBaseValue(0);
          }
        }

        target.getEntityData().removeTag("hadAttack");
        target.getEntityData().removeTag("hadTarget");
        target.getEntityData().removeTag("hadDamage");
      }*/
      ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(target.getEntityData(), SpellGeas.instance);
      if (!EntityUtil.isFriendly(target) || !mods.has(SpellGeas.PEACEFUL)) {
        if (mods.has(SpellGeas.WEAKNESS)) {
          target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, mods.ampInt(SpellGeas.instance.weakness_duration), SpellGeas.instance.weakness_amplifier));
        } else if (mods.has(SpellGeas.FIRE)) {
          target.setFire(mods.ampInt(SpellGeas.instance.fire_duration));
          target.attackEntityFrom(DamageSource.IN_FIRE, mods.ampFloat(SpellGeas.instance.fire_damage));
        } else if (mods.has(SpellGeas.PHYSICAL)) {
          target.attackEntityFrom(ModDamage.PHYSICAL_DAMAGE, mods.ampFloat(SpellGeas.instance.physical_damage));
        } else if (mods.has(SpellGeas.WATER)) {
          target.attackEntityFrom(ModDamage.waterDamageFrom(null), mods.ampFloat(SpellGeas.instance.water_damage));
        }
      }
    }
  }

  @Override
  public boolean shouldRender(PotionEffect effect) {
    return false;
  }
}
