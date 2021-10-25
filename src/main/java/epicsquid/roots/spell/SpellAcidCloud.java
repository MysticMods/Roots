package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageAcidCloudFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.DamageUtil;
import epicsquid.roots.util.EntityUtil;
import epicsquid.roots.util.types.RandomIterable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellAcidCloud extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(10);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("baffle_cap", 0.250));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(4f).setDescription("damage dealt each time to affected entities");
  public static Property<Integer> PROP_DAMAGE_COUNT = new Property<>("damage_count", 4).setDescription("maximum number of creatures that can be damaged per tick, -1 for infinite");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 80).setDescription("duration in ticks of the poison effect applied on the enemies");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 5).setDescription("duration in seconds of the fire effect applied on the enemies");
  public static Property<Integer> PROP_POISON_AMPLIFICATION = new Property<>("poison_amplification", 0).setDescription("the level of the poison effect applied on the enemies (0 is the first level)");
  public static Property<Integer> PROP_RADIUS_GENERAL = new Property<>("radius_general", 4).setDescription("default radius for the acid cloud");
  public static Property<Integer> PROP_RADIUS_BOOST = new Property<>("radius_boost", 2).setDescription("how much radius should be boosted by when Radius Boost modifier applied");
  public static Property<Float> PROP_NIGHT_LOWER = new Property<>("night_modifier_low", 0.05f).setDescription("the value to multiply damage by at dusk and dawn, rising to night_modifier_high at midnight and then down again");
  public static Property<Float> PROP_NIGHT_HIGHER = new Property<>("night_modifier_high", 0.6f).setDescription("the value to multiply damage by at midnight, increasing to this from dusk and decreasing from this at dawn");
  public static Property<Float> PROP_UNDEAD_DAMAGE = new Property<>("undead_damage", 2.0f).setDescription("additional damage done to undead entities with the spirit herb modifier");
  public static Property<Float> PROP_HEALING = new Property<>("healing", 1.0f).setDescription("how much healing is done by the cloud");
  public static Property<Integer> PROP_REGENERATION = new Property<>("regeneration", 40).setDescription("how long the duration of regen to apply (0 to not apply)");
  public static Property<Integer> PROP_REGEN_AMPLIFIER = new Property<>("regeneration_amplifier", 0).setDescription("what amplifier to use when applying the regen effect");
  public static Property<Integer> PROP_HEALING_COUNT = new Property<>("healing_count", 3).setDescription("maximum number of creatures that can be healed per tick, -1 for infinite");
  public static Property<Integer> PROP_SLOW_DURATION = new Property<>("slow_duration", 40).setDescription("how long to apply slowness for");
  public static Property<Integer> PROP_SLOW_AMPLIFIER = new Property<>("slow_amplifier", 1).setDescription("the amplifier to be applied to the slowing effect");
  public static Property<Float> PROP_UNDERWATER_BOOST = new Property<>("underwater_boost", 1.4f).setDescription("the multiplier given to damage and healing when underwater");
  public static Property<Integer> PROP_WEAKNESS_DURATION = new Property<>("weakness_duration", 4 * 20).setDescription("how long enemies should be weakened in place for");
  public static Property<Integer> PROP_WEAKNESS_AMPLIFIER = new Property<>("weakness_amplifier", 0).setDescription("the amplifier to be applied to the weakness effect");

  // TODO: Costs

  public static Modifier RADIUS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "radius_boost"), ModifierCores.PERESKIA, Cost.of(new Cost(CostType.ADDITIONAL_COST, 0.2, ModifierCores.PERESKIA))));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_cloud"), ModifierCores.WILDEWHEET, Cost.of(Cost.cost(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125), Cost.cost(CostType.ALL_COST_MULTIPLIER, -0.125))));
  public static Modifier WEAKNESS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "weakening_cloud"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.275)));
  public static Modifier NIGHT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "moonfall"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.375)));
  public static Modifier UNDEAD = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "unholy_vanquisher"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.425)));
  public static Modifier HEALING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "healing_cloud"), ModifierCores.TERRA_MOSS, Cost.of(new Cost(CostType.ADDITIONAL_COST, 0.275, ModifierCores.TERRA_MOSS), new Cost(CostType.ALL_COST_MULTIPLIER, -0.125))));
  public static Modifier SPEED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "increased_speed"), ModifierCores.CLOUD_BERRY, Cost.of(new Cost(CostType.ADDITIONAL_COST, 0.225, ModifierCores.CLOUD_BERRY))));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fire_cloud"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.275)));
  public static Modifier SLOWING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slowing"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.275)));
  public static Modifier UNDERWATER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "underwater_increase"), ModifierCores.DEWGONIA, Cost.of(new Cost(CostType.ADDITIONAL_COST, 0.175, ModifierCores.DEWGONIA))));

  static {
    // Conflicts
    HEALING.addConflicts(WEAKNESS, UNDEAD, FIRE);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_acid_cloud");
  public static SpellAcidCloud instance = new SpellAcidCloud(spellName);

  private float damage;
  private float night_low;
  private float night_high;
  private float undead_damage;
  private float healing;
  private float underwater_boost;
  private int poisonDuration, poisonAmplification, fireDuration, regen_duration, regen_amp, damage_count, heal_count, weakness_amplifier, weakness_duration, slow_duration, slow_amplifier;
  public int radius, radius_boost;

  public SpellAcidCloud(ResourceLocation name) {
    super(name, TextFormatting.DARK_GREEN, 80f / 255f, 160f / 255f, 40f / 255f, 64f / 255f, 96f / 255f, 32f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_DAMAGE_COUNT, PROP_POISON_DURATION, PROP_FIRE_DURATION, PROP_POISON_AMPLIFICATION, PROP_RADIUS_BOOST, PROP_RADIUS_GENERAL, PROP_NIGHT_LOWER, PROP_NIGHT_HIGHER, PROP_UNDEAD_DAMAGE, PROP_HEALING, PROP_REGEN_AMPLIFIER, PROP_REGENERATION, PROP_UNDERWATER_BOOST, PROP_HEALING_COUNT, PROP_SLOW_DURATION, PROP_SLOW_AMPLIFIER, PROP_UNDERWATER_BOOST, PROP_WEAKNESS_AMPLIFIER, PROP_WEAKNESS_DURATION);
    acceptModifiers(RADIUS, PEACEFUL, WEAKNESS, NIGHT, UNDEAD, HEALING, SPEED, FIRE, SLOWING, UNDERWATER);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("eye"),
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new OreIngredient("dyeLime"),
        new ItemStack(ModItems.runic_dust),
        new ItemStack(Items.ROTTEN_FLESH)
    );
    //setCastSound(ModSounds.Spells.ACID_CLOUD);
  }

  public static AxisAlignedBB boxGeneral, boxBoost;

  private double getMultiplier(double t, float min, float max) {
    int peak = 18000;
    int[] bounds = new int[]{12500, 23500};
    int threshold = peak / 3;
    if (t > bounds[0] && t < bounds[1]) {
      return max + Math.abs((t - peak) / threshold) * (min - max);
    } else {
      return 0;
    }
  }

  @Override
  public boolean cast(PlayerEntity player, StaffModifierInstanceList info, int ticks) {
    if (!player.world.isRemote) {
      List<LivingEntity> entities = player.world.getEntitiesWithinAABB(LivingEntity.class, info.has(RADIUS) ? boxBoost.offset(player.getPosition()) : boxGeneral.offset(player.getPosition()));
      int healed = 0;
      int damaged = 0;
      if (info.has(SPEED)) {
        player.addPotionEffect(new EffectInstance(Effects.SPEED, 1, 18, false, false));
        player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 1, 0, false, false));
      }
      float modifier = 1;
      if (info.has(NIGHT)) {
        double mult = getMultiplier(player.world.getWorldTime(), night_low, night_high);
        modifier += mult;
      }
      if (info.has(UNDERWATER)) {
        modifier += underwater_boost;
      }
      for (LivingEntity e : new RandomIterable<>(entities)) {
        // TODO: Visual effect of cloud (white or gold)
        // TODO: Enforce healing check
        // TODO: Additional visual effect

        if (info.has(HEALING)) {
          if (EntityUtil.isHostile(e, SpellAcidCloud.instance) || EntityUtil.isHostileTo(e, player, SpellAcidCloud.instance)) {
            continue;
          }
          if (regen_duration != -1) {
            e.addPotionEffect(new EffectInstance(Effects.REGENERATION, regen_duration, regen_amp));
          }
          // TODO: Particle effect to denote entities being healed
          if (healing > 0) {
            e.heal(healing * modifier);
          }
          healed++;
          if (heal_count != -1 && healed >= heal_count) {
            break;
          }
        } else {
          // TODO: Make instanceof check helper function
          if (e != player && !(e instanceof PlayerEntity && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
            if (e.hurtTime <= 0 && !e.isDead) {
              if (info.has(PEACEFUL) && EntityUtil.isFriendly(e, SpellAcidCloud.instance)) {
                continue;
              }
              if (info.has(WEAKNESS)) {
                e.addPotionEffect(new EffectInstance(Effects.WEAKNESS, weakness_duration, weakness_amplifier));
              }
              if (info.has(FIRE)) {
                DamageUtil.unhurt(e);
                e.attackEntityFrom(ModDamage.fireDamageFrom(player), (damage * modifier) / 2);
                DamageUtil.unhurt(e);
                e.attackEntityFrom(DamageSource.causeMobDamage(player), (damage * modifier) / 2);
                e.setFire(fireDuration);
              } else {
                DamageUtil.unhurt(e);
                e.attackEntityFrom(DamageSource.causeMobDamage(player), damage * modifier);
              }
              // TODO: Additional visual effect
              if (info.has(UNDEAD) && e.isEntityUndead()) {
                DamageUtil.unhurt(e);
                e.attackEntityFrom(ModDamage.radiantDamageFrom(player), undead_damage * modifier);
              }
              if (info.has(SLOWING)) {
                e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, slow_duration, slow_amplifier));
              }
              if (SpellConfig.spellFeaturesCategory.acidCloudPoisoningEffect) {
                e.addPotionEffect(new EffectInstance(Effects.POISON, poisonDuration, poisonAmplification));
              }
              e.setRevengeTarget(player);
              e.setLastAttackedEntity(player);
              damaged++;
              if (damage_count != -1 && damaged >= damage_count) {
                break;
              }
            }
          }
        }
      }
      PacketHandler.sendToAllTracking(new MessageAcidCloudFX(player.posX, player.posY + player.getEyeHeight(), player.posZ, info), player);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.damage = properties.get(PROP_DAMAGE);
    this.poisonAmplification = properties.get(PROP_POISON_AMPLIFICATION);
    this.poisonDuration = properties.get(PROP_POISON_DURATION);
    this.fireDuration = properties.get(PROP_FIRE_DURATION);
    this.radius = properties.get(PROP_RADIUS_GENERAL);
    this.radius_boost = properties.get(PROP_RADIUS_BOOST);
    boxGeneral = new AxisAlignedBB(-radius, -radius, -radius, radius + 1, radius + 1, radius + 1);
    boxBoost = boxGeneral.grow(radius_boost);
    this.night_low = properties.get(PROP_NIGHT_LOWER);
    this.night_high = properties.get(PROP_NIGHT_HIGHER);
    this.undead_damage = properties.get(PROP_UNDEAD_DAMAGE);
    this.healing = properties.get(PROP_HEALING);
    this.regen_amp = properties.get(PROP_REGEN_AMPLIFIER);
    this.regen_duration = properties.get(PROP_REGENERATION);
    this.damage_count = properties.get(PROP_DAMAGE_COUNT);
    this.heal_count = properties.get(PROP_HEALING_COUNT);
    this.underwater_boost = properties.get(PROP_UNDERWATER_BOOST);
    this.weakness_duration = properties.get(PROP_WEAKNESS_DURATION);
    this.weakness_amplifier = properties.get(PROP_WEAKNESS_AMPLIFIER);
    this.slow_duration = properties.get(PROP_SLOW_DURATION);
    this.slow_amplifier = properties.get(PROP_SLOW_AMPLIFIER);
  }
}
