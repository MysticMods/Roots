package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellAquaBubble extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(1200);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 1.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.5));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Double> PROP_ABSORPTION = new Property<>("absorption", 20.0).setDescription("the amount of health absorption granted");
  public static Property<Float> PROP_FIRE_REDUCTION = new Property<>("fire_reduction", 0.5f).setDescription("how much fire damage is multiplied by");
  public static Property<Float> PROP_LAVA_REDUCTION = new Property<>("fire_reduction", 0.0f).setDescription("how much lava damage is multiplied by");
  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_aqua_bubble");
  public static SpellAquaBubble instance = new SpellAquaBubble(spellName);

  public int duration;
  public double absorption;
  public float fire_reduction, lava_reduction;

  public SpellAquaBubble(ResourceLocation name) {
    super(name, TextFormatting.AQUA, 255f / 255f, 0f / 255f, 0f / 255f, 60f / 255f, 0f / 255f, 60f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION, PROP_ABSORPTION, PROP_FIRE_REDUCTION, PROP_LAVA_REDUCTION);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("dyeBlue"),
        new ItemStack(ModItems.dewgonia),
        new OreIngredient("snowball"),
        new OreIngredient("blockGlass"),
        new OreIngredient("gemQuartz")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    caster.addPotionEffect(new PotionEffect(ModPotions.aqua_bubble, (int) (duration + duration * amplifier), 0, false, false));
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
    this.absorption = properties.get(PROP_ABSORPTION);
    this.fire_reduction = properties.get(PROP_FIRE_REDUCTION);
    this.lava_reduction = properties.get(PROP_LAVA_REDUCTION);
    ModPotions.aqua_bubble.finalise(this);
  }
}
