package epicsquid.roots.spell;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellSenseAnimals extends SpellBase {
  public static String spellName = "spell_sense_animals";
  public static SpellSenseAnimals instance = new SpellSenseAnimals(spellName);

  public SpellSenseAnimals(String name) {
    super(name, TextFormatting.WHITE, 255f / 255f, 255f / 255f, 255f / 255f, 10f / 255f, 196f / 255f, 10f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 100;

    addCost(HerbRegistry.getHerbByName("wildewheet"), 0.25f);

    addIngredients(
        new OreIngredient("cropCarrot"),
        new ItemStack(Items.LEAD),
        new ItemStack(ModItems.wildewheet),
        new OreIngredient("cropWheat"),
        new OreIngredient("nuggetGold")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    caster.addPotionEffect(new PotionEffect(ModPotions.animal_sense, 40 * 20, 0, false, false));
    return true;
  }

  public int[] getRadius() {
    return new int[]{50, 25, 50};
  }
}
