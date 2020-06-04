package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellMagnetism extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(60);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 0.195));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("radius on the X axis of the area in which dropped items are magnetized to the player");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("radius on the Y axis of the area in which dropped items are magnetized to the player");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("radius on the Z axis of the area in which dropped items are magnetized to the player");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_magnetism");
  public static SpellMagnetism instance = new SpellMagnetism(spellName);

  private int radius_x, radius_y, radius_z;

  public SpellMagnetism(ResourceLocation name) {
    super(name, TextFormatting.DARK_GRAY, 255f / 255f, 130f / 255f, 130f / 255f, 130f / 255f, 130f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
  }

  @Override
  public void init () {
    addIngredients(
        new OreIngredient("ingotIron"),
        new OreIngredient("dustRedstone"),
        new ItemStack(Items.PAPER),
        new OreIngredient("wildroot"),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, BaseModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    // TODO: Check to see what the potential standard is for "unmagnetising" things
    int count = 0;
    count += Magnetize.pull(EntityItem.class, player.world, player.getPosition(), (int) (radius_x + radius_x * amplifier), (int) (radius_y + radius_y * amplifier), (int) (radius_z + radius_z * amplifier));
    count += Magnetize.pull(EntityXPOrb.class, player.world, player.getPosition(), (int) (radius_x + radius_x * amplifier), (int) (radius_y + radius_y * amplifier), (int) (radius_z + radius_z * amplifier));

    return count != 0;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
  }
}
