package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellSenseDanger extends SpellBase {
  public static String spellName = "spell_sense_danger";
  public static SpellSenseDanger instance = new SpellSenseDanger(spellName);

  public SpellSenseDanger(String name) {
    super(name, TextFormatting.DARK_RED, 255f / 255f, 0f / 255f, 0f / 255f, 60f / 255f, 0f / 255f, 60f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 190;

    addCost(HerbRegistry.getHerbByName("wildroot"), 0.285f);
    addIngredients(
        new ItemStack(Items.GOLDEN_CARROT),
        new ItemStack(Items.COMPASS),
        new ItemStack(Items.SPIDER_EYE),
        new OreIngredient("rootsBark"),
        new ItemStack(ModItems.aubergine)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    List<EntityCreature> creatures = Util.getEntitiesWithinRadius(caster.getEntityWorld(), EntityCreature.class, caster.getPosition(), 40, 40, 40);
    caster.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 40*20));
    for(EntityCreature creature : creatures){
      if (!(creature instanceof IMob)) continue;
      creature.addPotionEffect( new PotionEffect(MobEffects.GLOWING, 40*20, 0));
    }
    return true;
  }
}
