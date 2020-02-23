package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.EntitySpellDrizzle;
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

public class SpellDrizzle extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(600);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.450));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("cloud_berry", 0.125));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(450);
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 3);
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(2.5f);
  public static Property<Float> PROP_WATER_MULTIPLIER = new Property<>("water_multiplier", 2.5f);
  public static Property<Double> PROP_LIGHTNING_CHANCE = new Property<>("lightning_chance", 0.45);
  public static Property<Integer> PROP_FIRE_RESISTANCE = new Property<>("fire_resistance", 3);
  public static Property<Integer> PROP_LIGHTNING_INTERVAL = new Property<>("lightning_interval", 60);
  public static Property<Integer> PROP_TARGETS = new Property<>("targets", 1);
  public static Property<Integer> PROP_ADDITIONAL_TARGETS = new Property<>("additiona_targets", 4);

  public int radius, duration, fire_resistance, lightning_interval, targets, additional_targets;
  public double lightning_chance;
  public float damage, water_multiplier;

  public static String spellName = "spell_drizzle";
  public static SpellDrizzle instance = new SpellDrizzle(spellName);

  public SpellDrizzle(String name) {
    super(name, TextFormatting.BLUE, 34 / 255F, 133 / 255F, 245 / 255F, 23 / 255F, 44 / 255F, 89 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION, PROP_RADIUS, PROP_DAMAGE, PROP_LIGHTNING_CHANCE, PROP_FIRE_RESISTANCE, PROP_LIGHTNING_INTERVAL, PROP_TARGETS, PROP_ADDITIONAL_TARGETS, PROP_WATER_MULTIPLIER);
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
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    World world = caster.world;
    BlockPos pos = caster.getPosition();

    return spawnEntity(world, pos, EntitySpellDrizzle.class, caster) != null;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
    this.radius = properties.get(PROP_RADIUS);
    this.fire_resistance = properties.get(PROP_FIRE_RESISTANCE);
    this.lightning_chance = properties.get(PROP_LIGHTNING_CHANCE);
    this.lightning_interval = properties.get(PROP_LIGHTNING_INTERVAL);
    this.damage = properties.get(PROP_DAMAGE);
    this.targets = properties.get(PROP_TARGETS);
    this.additional_targets = properties.get(PROP_ADDITIONAL_TARGETS);
    this.water_multiplier = properties.get(PROP_WATER_MULTIPLIER);
  }
}
