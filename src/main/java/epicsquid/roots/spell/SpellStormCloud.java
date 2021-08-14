package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class SpellStormCloud extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(100);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("cloud_berry", 0.250));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(600).setDescription("the duration of the spell effect on the player");
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 2).setDescription("the radius of the area covered by the spring storm");
  public static Property<Integer> PROP_RADIUS_EXTENDED = new Property<>("extended_radius", 2).setDescription("the additional radius of the storm");
  public static Property<Float> PROP_LIGHTNING_CHANCE = new Property<>("lightning_chance", 0.25f).setDescription("chance for each lightning to spawn");
  public static Property<Integer> PROP_LIGHTNING_INTERVAL = new Property<>("lightning_interval", 3 * 20).setDescription("frequency that checks for lightning strikes are made");
  public static Property<Integer> PROP_FIRE_RESISTANCE = new Property<>("fire_resistance", 2).setDescription("the level of fire resistance given entities for the duration");
  public static Property<Float> PROP_LIGHTNING_DAMAGE = new Property<>("lightning_damage", 2.5f).setDescription("the amount of damage done when struck while in the storm");
  public static Property<Integer> PROP_HEAL_INTERVAL = new Property<>("heal_interval", 5 * 20).setDescription("the interval between each heal");
  public static Property<Float> PROP_HEAL_AMOUNT = new Property<>("heal_amount", 1f).setDescription("how much healing should be done");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 3 * 20).setDescription("the duration of the poison effect when applied to creatures");
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the amplifier that should be used for the poison effect");
  public static Property<Float> PROP_ICICLE_DAMAGE = new Property<>("icicle_damage", 2.5f).setDescription("how much damage icicles should do to an entity");
  public static Property<Float> PROP_ICICLE_CHANCE = new Property<>("icicle_chance", 0.25f).setDescription("the chance of a icicle spawning every icicle interval");
  public static Property<Integer> PROP_ICICLE_INTERVAL = new Property<>("icicle_interval", 70).setDescription("the interval between attempting to spawn icicles");

  public static Modifier RADIUS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "spread"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.275)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_storm"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier MAGNETISM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_storm"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.345)));
  public static Modifier LIGHTNING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "lightning_strikes"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.65)));
  // TODO: Update documentation
  public static Modifier JOLT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "reactive_jolt"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.345)));
  public static Modifier HEALING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "healing_rain"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.345)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poison_storm"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.345)));
  public static Modifier OBSIDIAN = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "ignification"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.345)));
  public static Modifier ICICLES = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "icicles"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.45)));
  public static Modifier ICE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "freezing_rain"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.275)));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_storm_cloud");
  public static SpellStormCloud instance = new SpellStormCloud(spellName);

  public float lightning_damage, heal_amount, icicle_damage, lightning_chance, icicle_chance;
  public int radius, duration, fire_resistance, lightning_interval, radius_extended, heal_interval, poison_duration, poison_amplifier, icicle_interval;

  public SpellStormCloud(ResourceLocation name) {
    super(name, TextFormatting.DARK_AQUA, 22f / 255f, 142f / 255f, 255f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DURATION, PROP_RADIUS, PROP_RADIUS_EXTENDED, PROP_LIGHTNING_CHANCE, PROP_LIGHTNING_DAMAGE, PROP_LIGHTNING_INTERVAL, PROP_FIRE_RESISTANCE, PROP_HEAL_AMOUNT, PROP_HEAL_INTERVAL, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_ICICLE_DAMAGE, PROP_ICICLE_CHANCE, PROP_ICICLE_INTERVAL);
    acceptModifiers(RADIUS, PEACEFUL, MAGNETISM, LIGHTNING, JOLT, HEALING, POISON, OBSIDIAN, ICICLES, ICE);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(ModItems.dewgonia),
        new ItemStack(Items.WATER_BUCKET),
        new OreIngredient("sugarcane"),
        new ItemStack(Items.CLAY_BALL),
        new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta())
    );
    setCastSound(ModSounds.Spells.STORM_CLOUD);
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    World world = player.world;
    if (!world.isRemote) {
      player.addPotionEffect(new PotionEffect(ModPotions.storm_cloud, duration, 0, false, false));
      player.getEntityData().setIntArray(getCachedName(), info.toArray());
    }
    return true;
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
    this.radius_extended = properties.get(PROP_RADIUS_EXTENDED);
    this.lightning_damage = properties.get(PROP_LIGHTNING_DAMAGE);
    this.heal_amount = properties.get(PROP_HEAL_AMOUNT);
    this.poison_amplifier = properties.get(PROP_POISON_AMPLIFIER);
    this.poison_duration = properties.get(PROP_POISON_DURATION);
    this.icicle_damage = properties.get(PROP_ICICLE_DAMAGE);
    this.icicle_interval = properties.get(PROP_ICICLE_INTERVAL);
    this.heal_interval = properties.get(PROP_HEAL_INTERVAL);
    this.icicle_chance = properties.get(PROP_ICICLE_CHANCE);
  }
}

