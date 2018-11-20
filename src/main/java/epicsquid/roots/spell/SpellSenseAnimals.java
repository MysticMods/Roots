package epicsquid.roots.spell;

import java.util.List;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModItems;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

public class SpellSenseAnimals extends SpellBase {

  public SpellSenseAnimals(String name) {
    super(name, TextFormatting.WHITE, 255f / 255f, 255f / 255f, 255f / 255f, 10f / 255f, 196f / 255f, 10f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 100;

    addCost(ModItems.wildroot, 0.25f);
    addIngredients(
        new ItemStack(Items.CARROT, 1),
        new ItemStack(Blocks.RED_FLOWER, 1, 0),
        new ItemStack(ModItems.moonglow_seed, 1),
        new ItemStack(ModItems.moonglow_leaf, 1),
        new ItemStack(Items.GOLDEN_CARROT, 1)
    );
  }

  @Override
  public void cast(EntityPlayer caster) {
    List<EntityAnimal> animals = Util.getEntitiesWithinRadius(caster.getEntityWorld(), EntityAnimal.class, caster.getPosition(), 50, 10, 50);
    for(EntityAnimal animal : animals){
      animal.addPotionEffect( new PotionEffect(MobEffects.GLOWING, 20*20, 0));
    }
  }
}
