package epicsquid.roots.spell;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellSenseDanger extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(190);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 0.285));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 40);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 40);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 40);
  public static Property<Integer> PROP_GLOW_DURATION = new Property<>("glow_duration", 40 * 20);
  public static Property<Integer> PROP_NV_DURATION = new Property<>("night_vision_duration", 40 * 20);

  public static String spellName = "spell_sense_danger";
  public static SpellSenseDanger instance = new SpellSenseDanger(spellName);

  private int radius_x, radius_y, radius_z, glowDuration, nvDuration;

  public SpellSenseDanger(String name) {
    super(name, TextFormatting.DARK_RED, 255f / 255f, 0f / 255f, 0f / 255f, 60f / 255f, 0f / 255f, 60f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_NV_DURATION, PROP_GLOW_DURATION);
  }

  @Override
  public void init() {
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
    caster.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, nvDuration, 0, false, false));
    caster.addPotionEffect(new PotionEffect(ModPotions.danger_sense, glowDuration, 0, false, false));
    return true;
  }

  public int[] getRadius() {
    return new int[]{radius_x, radius_y, radius_z};
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.nvDuration = properties.get(PROP_NV_DURATION);
    this.glowDuration = properties.get(PROP_GLOW_DURATION);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
  }
}
