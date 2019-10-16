package epicsquid.roots.spell;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.spell.modules.SpellModule;
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
        new OreIngredient("nuggetGold"),
        new ItemStack(Items.COMPASS),
        new ItemStack(Items.SPIDER_EYE),
        new OreIngredient("rootsBark"),
        new ItemStack(ModItems.aubergine)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    caster.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 40 * 20 + 5, 0, false, false));
    caster.addPotionEffect(new PotionEffect(ModPotions.danger_sense, 40 * 20, 0, false, false));
    return true;
  }

  public int[] getRadius() {
    return new int[]{40, 40, 40};
  }
}
