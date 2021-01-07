package epicsquid.roots.potion;

import epicsquid.roots.entity.ai.EntityAIPassiveAttackMelee;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellGeas;
import epicsquid.roots.util.EntityUtil;
import epicsquid.roots.util.SlaveUtil;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
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

  @Override
  public void applyAttributesModifiersToEntity(EntityLivingBase target, AbstractAttributeMap attributeMapIn, int amplifier) {
    super.applyAttributesModifiersToEntity(target, attributeMapIn, amplifier);
    if (target instanceof EntityCreature && EntityUtil.isFriendly(target)) {
      EntityCreature entity = (EntityCreature) target;
      ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(target.getEntityData(), SpellGeas.instance);
      if (mods.has(SpellGeas.PEACEFUL)) {
        boolean hadAttack = entity.tasks.taskEntries.stream().anyMatch(o -> o.action instanceof EntityAIAttackMelee);
        if (!hadAttack) {
          entity.tasks.addTask(5, new EntityAIPassiveAttackMelee(entity, 1.0d, false));
        }
        boolean hadTarget = entity.targetTasks.taskEntries.stream().anyMatch(o -> o.action instanceof EntityAINearestAttackableTarget);
        if (!hadTarget) {
          entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(entity, EntityMob.class, 10, false, false, o -> !SlaveUtil.isSlave(o)));
        }
        target.getEntityData().setBoolean("hadAttack", hadAttack);
        target.getEntityData().setBoolean("hadTarget", hadTarget);
      }
    }
  }

  @Override
  public void removeAttributesModifiersFromEntity(EntityLivingBase target, AbstractAttributeMap attributeMapIn, int amplifier) {
    super.removeAttributesModifiersFromEntity(target, attributeMapIn, amplifier);
    if (SlaveUtil.isSlave(target)) {
      EntityLivingBase slave = SlaveUtil.revert(target);
      target.world.spawnEntity(slave);
      target.setDropItemsWhenDead(false);
      target.setDead();
      slave.setPositionAndUpdate(slave.posX, slave.posY, slave.posZ);
    } else {
      ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(target.getEntityData(), SpellGeas.instance);
      if (mods.has(SpellGeas.WEAKNESS)) {
        target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, mods.ampInt(SpellGeas.instance.weakness_duration), SpellGeas.instance.weakness_amplifier));
      } else if (mods.has(SpellGeas.FIRE)) {
        target.setFire(mods.ampInt(SpellGeas.instance.fire_duration));
        target.attackEntityFrom(DamageSource.IN_FIRE, mods.ampFloat(SpellGeas.instance.fire_damage));
      } else if (mods.has(SpellGeas.PHYSICAL)) {
        // TODO: VISUAL EFFECT
        target.attackEntityFrom(ModDamage.PHYSICAL_DAMAGE, mods.ampFloat(SpellGeas.instance.physical_damage));
      } else if (mods.has(SpellGeas.WATER)) {
        // TODO: ROCK FALL EVERYONE DIE
        target.attackEntityFrom(ModDamage.waterDamageFrom(null), mods.ampFloat(SpellGeas.instance.water_damage));
      }
    }
  }

  @Override
  public boolean shouldRender(PotionEffect effect) {
    return false;
  }
}
