package epicsquid.roots.spell;

import java.util.List;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModItems;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

public class SpellSenseAnimals extends SpellBase {

  public SpellSenseAnimals(String name) {
    super(name, TextFormatting.WHITE, 255f / 255f, 255f / 255f, 255f / 255f, 10f / 255f, 196f / 255f, 10f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 100;
    addCost(ModItems.wildroot, 0.25f);
  }

  @Override
  public void cast(EntityPlayer caster) {
    List<EntityAnimal> animals = Util.getEntitiesWithinRadius(caster.getEntityWorld(), EntityAnimal.class, caster.getPosition(), 50, 10, 50);
    for(EntityAnimal animal : animals){
      animal.addPotionEffect( new PotionEffect(MobEffects.GLOWING, 20*20, 0));
    }
    super.cast(caster);
  }
}
