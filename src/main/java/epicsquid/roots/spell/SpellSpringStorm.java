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
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 3);
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(2.5f);
  public static Property<Float> PROP_WATER_MULTIPLIER = new Property<>("water_multiplier", 2.5f);
  public static Property<Float> PROP_ENDERMAN_MULTIPLIER = new Property<>("enderman_multiplier", 3.8f);
  public static Property<Float> PROP_FIRE_MULTIPLIER = new Property<>("fire_multiplier", 2.5f);
  public static Property<Float> PROP_LIGHTNING_CHANCE = new Property<>("lightning_chance", 0.25f);
  public static Property<Integer> PROP_LIGHTNING_STRIKES = new Property<>("lightning_strikes", 1);
  public static Property<Integer> PROP_FIRE_RESISTANCE = new Property<>("fire_resistance", 3);
  public static Property<Integer> PROP_TARGETS = new Property<>("targets", 1);
  public static Property<Integer> PROP_ADDITIONAL_TARGETS = new Property<>("additional_targets", 4);
  public static Property<Integer> PROP_DAMAGE_INTERVAL = new Property<>("damage_interval", 40);

  public int radius, duration, fire_resistance, targets, additional_targets, damage_interval, lightning_strikes;
  public float lightning_chance, damage, water_multiplier, enderman_multiplier, fire_multiplier;

  public static String spellName = "spell_spring_storm";
  public static SpellSpringStorm instance = new SpellSpringStorm(spellName);

  public SpellSpringStorm(String name) {
    super(name, TextFormatting.BLUE, 34 / 255F, 133 / 255F, 245 / 255F, 23 / 255F, 44 / 255F, 89 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION, PROP_RADIUS, PROP_DAMAGE, PROP_LIGHTNING_CHANCE, PROP_FIRE_RESISTANCE, PROP_LIGHTNING_STRIKES, PROP_TARGETS, PROP_ADDITIONAL_TARGETS, PROP_WATER_MULTIPLIER, PROP_DAMAGE_INTERVAL, PROP_ENDERMAN_MULTIPLIER, PROP_FIRE_MULTIPLIER);
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

    return spawnEntity(world, pos, EntitySpellSpringStorm.class, caster) != null;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
    this.radius = properties.get(PROP_RADIUS);
    this.fire_resistance = properties.get(PROP_FIRE_RESISTANCE);
    this.lightning_chance = properties.get(PROP_LIGHTNING_CHANCE);
    this.lightning_strikes = properties.get(PROP_LIGHTNING_STRIKES);
    this.damage = properties.get(PROP_DAMAGE);
    this.targets = properties.get(PROP_TARGETS);
    this.additional_targets = properties.get(PROP_ADDITIONAL_TARGETS);
    this.water_multiplier = properties.get(PROP_WATER_MULTIPLIER);
    this.damage_interval = properties.get(PROP_DAMAGE_INTERVAL);
    this.enderman_multiplier = properties.get(PROP_ENDERMAN_MULTIPLIER);
    this.fire_multiplier = properties.get(PROP_FIRE_MULTIPLIER);
  }
}
