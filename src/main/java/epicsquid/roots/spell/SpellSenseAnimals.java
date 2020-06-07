package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
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

public class SpellSenseAnimals extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(100);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildewheet", 0.25));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 50).setDescription("radius on the X axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 25).setDescription("radius on the Y axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 50).setDescription("radius on the Z axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_DURATION = new Property<>("duration", 40 * 20).setDescription("duration in ticks of the glowing effect applied to animals");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_sense_animals");
  public static SpellSenseAnimals instance = new SpellSenseAnimals(spellName);

  private int radius_x, radius_y, radius_z, duration;

  public SpellSenseAnimals(ResourceLocation name) {
    super(name, TextFormatting.WHITE, 255f / 255f, 255f / 255f, 255f / 255f, 10f / 255f, 196f / 255f, 10f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_DURATION);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("cropCarrot"),
        new ItemStack(Items.LEAD),
        new ItemStack(ModItems.wildewheet),
        new OreIngredient("cropWheat"),
        new OreIngredient("nuggetGold")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    caster.addPotionEffect(new PotionEffect(ModPotions.animal_sense, (int) (duration + duration * amplifier), 0, false, false));
    return true;
  }

  public int[] getRadius() {
    return new int[]{radius_x, radius_y, radius_z};
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
  }
}
