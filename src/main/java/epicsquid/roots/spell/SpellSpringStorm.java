package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.EntitySpellSpringStorm;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class SpellSpringStorm extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(600);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.8));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("cloud_berry", 0.3));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(400);
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 3).setDescription("the radius of the area covered by the spring storm");
  public static Property<Integer> PROP_LIGHTNING_CHANCE = new Property<>("lightning_chance", 4).setDescription("chance for each lightning to spawn (the higher the number is the lower the chance becomes: 1/x) [default: 1/4]");
  public static Property<Integer> PROP_LIGHTNING_STRIKES = new Property<>("lightning_strikes", 3).setDescription("number of lightning strikes attempts each time");
  public static Property<Integer> PROP_FIRE_RESISTANCE = new Property<>("fire_resistance", 3).setDescription("the level of fire resistance given to the player for 60 seconds");
  public static Property<Integer> PROP_RESISTANCE = new Property<>("resistance", 0).setDescription("the level of resistance given to the player for 60 seconds");

  public int radius, duration, fire_resistance, lightning_strikes, resistance, lightning_chance;

  public static String spellName = "spell_spring_storm";
  public static SpellSpringStorm instance = new SpellSpringStorm(spellName);

  public SpellSpringStorm(String name) {
    super(name, TextFormatting.BLUE, 34 / 255F, 133 / 255F, 245 / 255F, 23 / 255F, 44 / 255F, 89 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION, PROP_RADIUS, PROP_LIGHTNING_CHANCE, PROP_FIRE_RESISTANCE, PROP_LIGHTNING_STRIKES, PROP_RESISTANCE);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.WATER_BUCKET),
        new ItemStack(ModItems.dewgonia),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(Item.getItemFromBlock(Blocks.WATERLILY)),
        new ItemStack(Items.CLAY_BALL)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules, int ticks) {
    World world = caster.world;
    BlockPos pos = caster.getPosition();

    // Only 1 cloud per person
    if (EntitySpellSpringStorm.hasCloud(caster)) {
      return false;
    }

    return spawnEntity(world, pos, EntitySpellSpringStorm.class, caster) != null;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
    this.radius = properties.get(PROP_RADIUS);
    this.fire_resistance = properties.get(PROP_FIRE_RESISTANCE);
    this.resistance = properties.get(PROP_RESISTANCE);
    this.lightning_chance = properties.get(PROP_LIGHTNING_CHANCE);
    this.lightning_strikes = properties.get(PROP_LIGHTNING_STRIKES);
  }
}
