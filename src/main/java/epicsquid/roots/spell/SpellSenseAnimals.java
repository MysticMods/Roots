package epicsquid.roots.spell;

import java.util.List;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

public class SpellSenseAnimals extends SpellBase {
  public static String spellName = "spell_sense_animals";
  public static SpellSenseAnimals instance = new SpellSenseAnimals(spellName);

  public SpellSenseAnimals(String name) {
    super(name, TextFormatting.WHITE, 255f / 255f, 255f / 255f, 255f / 255f, 10f / 255f, 196f / 255f, 10f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 100;

    addCost(HerbRegistry.getHerbByName("wildewheet"), 0.25f);
    addIngredients(
        new ItemStack(Items.CARROT),
        new ItemStack(Blocks.RED_FLOWER),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(Items.WHEAT),
        new ItemStack(Items.GOLD_NUGGET)
    );
  }

  @Override
  public void cast(EntityPlayer caster, List<SpellModule> modules) {
    List<EntityAnimal> animals = Util.getEntitiesWithinRadius(caster.getEntityWorld(), EntityAnimal.class, caster.getPosition(), 50, 10, 50);
    for(EntityAnimal animal : animals){
      animal.addPotionEffect( new PotionEffect(MobEffects.GLOWING, 20*20, 0));
    }
  }
}
