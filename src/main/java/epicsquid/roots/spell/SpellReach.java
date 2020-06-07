package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellReach extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(800);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.750));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("stalicripe", 0.750));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(600);
  public static Property<Double> PROP_REACH = new Property<>("reach", 5.0).setDescription("the extended reach applied to the player during the effect of the spell");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_reach");
  public static SpellReach instance = new SpellReach(spellName);

  public int duration;
  public double reach;

  public SpellReach(ResourceLocation name) {
    super(name, TextFormatting.DARK_GREEN, 255f / 255f, 0f / 255f, 0f / 255f, 60f / 255f, 0f / 255f, 60f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION, PROP_REACH);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("dustRedstone"),
        new ItemStack(Items.LEAD),
        new ItemStack(Items.CARROT_ON_A_STICK),
        new OreIngredient("vine"),
        new OreIngredient("stairWood")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    caster.addPotionEffect(new PotionEffect(ModPotions.reach, (int) (duration + duration * amplifier), 0, false, false));
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
    this.reach = properties.get(PROP_REACH);
    ModPotions.reach.loadComplete(this.reach);
  }
}
