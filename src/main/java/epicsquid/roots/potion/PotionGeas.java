package epicsquid.roots.potion;

import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellGeas;
import epicsquid.roots.util.SlaveUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

public class PotionGeas extends Effect {
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
  public void applyAttributesModifiersToEntity(LivingEntity target, AbstractAttributeMap attributeMapIn, int amplifier) {
    super.applyAttributesModifiersToEntity(target, attributeMapIn, amplifier);
  }

  @Override
  public void removeAttributesModifiersFromEntity(LivingEntity target, AbstractAttributeMap attributeMapIn, int amplifier) {
    super.removeAttributesModifiersFromEntity(target, attributeMapIn, amplifier);
    if (SpellGeas.instance.shouldPlaySound()) {
      target.playSound(ModSounds.Spells.GEAS_EFFECT_END, SpellGeas.instance.getSoundVolume(), 1);
    }
    if (SlaveUtil.isSlave(target)) {
      LivingEntity slave = SlaveUtil.revert(target);
      target.world.spawnEntity(slave);
      target.setDropItemsWhenDead(false);
      target.setDead();
      slave.setPositionAndUpdate(slave.posX, slave.posY, slave.posZ);
    } else {
      ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(target.getEntityData(), SpellGeas.instance);
      if (mods.has(SpellGeas.WEAKNESS)) {
        target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, SpellGeas.instance.weakness_duration, SpellGeas.instance.weakness_amplifier));
      } else if (mods.has(SpellGeas.FIRE)) {
        target.setFire(SpellGeas.instance.fire_duration);
        target.attackEntityFrom(DamageSource.IN_FIRE, SpellGeas.instance.fire_damage);
      } else if (mods.has(SpellGeas.PHYSICAL)) {
        // TODO: VISUAL EFFECT
        target.attackEntityFrom(ModDamage.PHYSICAL_DAMAGE, SpellGeas.instance.physical_damage);
      } else if (mods.has(SpellGeas.WATER)) {
        // TODO: ROCK FALL EVERYONE DIE
        target.attackEntityFrom(ModDamage.waterDamageFrom(null), SpellGeas.instance.water_damage);
      }
    }
  }

  @Override
  public boolean shouldRender(EffectInstance effect) {
    return false;
  }
}
