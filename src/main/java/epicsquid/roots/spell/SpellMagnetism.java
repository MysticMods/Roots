package epicsquid.roots.spell;

import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellMagnetism extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(60);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 0.195));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);

  public static String spellName = "spell_magnetism";
  public static SpellMagnetism instance = new SpellMagnetism(spellName);

  private int radius_x, radius_y, radius_z;

  public SpellMagnetism(String name) {
    super(name, TextFormatting.DARK_GRAY, 255f / 255f, 130f / 255f, 130f / 255f, 130f / 255f, 130f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
  }

  @Override
  public void init() {
    addIngredients(
/*        new OreIngredient("ingotIron"),
        new OreIngredient("dustRedstone"),
        new ItemStack(Items.PAPER),
        new ItemStack(ModItems.wildroot),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine)*/
    );
  }

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    // TODO: Check to see what the potential standard is for "unmagnetising" things
    int count = 0;
    count += Magnetize.pull(ItemEntity.class, player.world, player.getPosition(), radius_x, radius_y, radius_z);
    count += Magnetize.pull(ExperienceOrbEntity.class, player.world, player.getPosition(), radius_x, radius_y, radius_z);

    return count != 0;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    this.radius_x = properties.getProperty(PROP_RADIUS_X);
    this.radius_y = properties.getProperty(PROP_RADIUS_Y);
    this.radius_z = properties.getProperty(PROP_RADIUS_Z);
  }
}
