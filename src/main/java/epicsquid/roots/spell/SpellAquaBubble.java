package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellAquaBubble extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(1200);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("dewgonia", 1.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("terra_moss", 0.5));
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Double> PROP_ABSORPTION = new Property<>("absorption", 20.0).setDescription("the amount of health absorption granted");
  public static Property<Float> PROP_FIRE_REDUCTION = new Property<>("fire_reduction", 0.5f).setDescription("how much fire damage is multiplied by");
  public static Property<Float> PROP_LAVA_REDUCTION = new Property<>("fire_reduction", 0.0f).setDescription("how much lava damage is multiplied by");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 5).setDescription("the radius to search for familiars and additional players within");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("the radius to search for familiars and additional players within");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 5).setDescription("the radius to search for familiars and additional players within");
  public static Property<Float> PROP_KNOCKBACK = new Property<>("knockback", 0.5f).setDescription("the amount of knockback to be applied");
  public static Property<Integer> PROP_SLOW_DURATION = new Property<>("slowness_duration", 60).setDescription("the duration of time that the slowness effect is applied for");
  public static Property<Integer> PROP_SLOW_AMPLIFIER = new Property<>("slowness_amplifier", 0).setDescription("the amplifier to be applied to the slowness effect");
  public static Property<Float> PROP_UNDEAD_REDUCTION = new Property<>("undead_reduction", 0.5f).setDescription("the percentage of damage (as a float) that you take from undead");
  public static Property<Integer> PROP_RESISTANCE_DURATION = new Property<>("resistance_duration", 500).setDescription("how long the resistance buff should be added for");
  public static Property<Integer> PROP_RESISTANCE_AMPLIFIER = new Property<>("resistance_amplifier", 0).setDescription("how much to amplify the resistance buff by");
  public static Property<Float> PROP_MAGIC_RESIST = new Property<>("magic_resistance", 0.25f).setDescription("how much magic damage is reduced by");

  // TODO: Costs

  public static Modifier DUO = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "bubble_duumvirate"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.75)));
  public static Modifier FAMILIARS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "faithful_sharing"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.475)));
  public static Modifier SLOW = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "barkskin"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.35)));
  public static Modifier MAGIC_RESIST = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "moon_bubble"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.725)));
  public static Modifier UNDEAD = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "spirit_bubble"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.475)));
  public static Modifier POISON_RESIST = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poison_protection"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.375)));
  public static Modifier KNOCKBACK = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "bubble_knockback"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.275)));
  public static Modifier REFLECT_FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "reflective_bubble"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.125)));
  public static Modifier RESISTANCE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "stoneskin"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.475)));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_aqua_bubble");
  public static SpellAquaBubble instance = new SpellAquaBubble(spellName);

  public int duration, radius_x, radius_y, radius_z, slow_duration, slow_amplifier, resistance_duration, resistance_amplifier;
  public double absorption;
  public float fire_reduction, lava_reduction, knockback, undead_reduction, magic_reduction;

  private AxisAlignedBB radius;

  public SpellAquaBubble(ResourceLocation name) {
    super(name, TextFormatting.AQUA, 255f / 255f, 0f / 255f, 0f / 255f, 60f / 255f, 0f / 255f, 60f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION, PROP_ABSORPTION, PROP_FIRE_REDUCTION, PROP_LAVA_REDUCTION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_KNOCKBACK, PROP_SLOW_AMPLIFIER, PROP_SLOW_DURATION, PROP_UNDEAD_REDUCTION, PROP_RESISTANCE_AMPLIFIER, PROP_RESISTANCE_DURATION, PROP_MAGIC_RESIST);
    acceptsModifiers(DUO, FAMILIARS, SLOW, MAGIC_RESIST, UNDEAD, POISON_RESIST, KNOCKBACK, REFLECT_FIRE, RESISTANCE);
    setCastSound(ModSounds.Spells.AQUA_BUBBLE);
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
    setCastSound(ModSounds.Spells.AQUA_BUBBLE);
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
    if (!caster.world.isRemote) {
      caster.addPotionEffect(new PotionEffect(ModPotions.aqua_bubble, info.ampInt(duration), 0, false, false));
      caster.getEntityData().setIntArray(getCachedName(), info.toArray());
      if (info.has(RESISTANCE)) {
        caster.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, info.ampInt(resistance_duration), resistance_amplifier));
      }
      if (info.has(DUO)) {
        List<EntityPlayer> players = caster.world.getEntitiesWithinAABB(EntityPlayer.class, radius.offset(caster.getPosition()));
        if (!players.isEmpty()) {
          EntityPlayer other = players.get(Util.rand.nextInt(players.size()));
          other.addPotionEffect(new PotionEffect(ModPotions.aqua_bubble, info.ampInt(duration), 0, false, false));
          other.getEntityData().setIntArray(getCachedName(), info.toArray());
          if (info.has(RESISTANCE)) {
            other.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, info.ampInt(resistance_duration), resistance_amplifier));
          }
        }
      }
    }
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
    int[] radius = properties.getRadius();
    this.radius_x = radius[0];
    this.radius_y = radius[1];
    this.radius_z = radius[2];
    this.radius = new AxisAlignedBB(-radius_x, -radius_y, -radius_z, radius_x + 1, radius_y + 1, radius_z + 1);
    this.knockback = properties.get(PROP_KNOCKBACK);
    this.slow_amplifier = properties.get(PROP_SLOW_AMPLIFIER);
    this.slow_duration = properties.get(PROP_SLOW_DURATION);
    this.undead_reduction = properties.get(PROP_UNDEAD_REDUCTION);
    this.resistance_amplifier = properties.get(PROP_RESISTANCE_AMPLIFIER);
    this.resistance_duration = properties.get(PROP_RESISTANCE_DURATION);
    this.magic_reduction = properties.get(PROP_MAGIC_RESIST);
  }
}
