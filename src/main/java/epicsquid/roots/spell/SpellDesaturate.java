package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageDesaturationFX;
import epicsquid.roots.network.fx.MessageRampantLifeInfusionFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellDesaturate extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(500);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("spirit_herb", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("terra_moss", 0.5));

  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 8).setDescription("radius on the X axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 2).setDescription("radius on the Y axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 8).setDescription("radius on the Z axis within which entities are affected by the spell");

  public static Property<Double> PROP_MULTIPLIER = new Property<>("multiplier", 0.70).setDescription("amount of health points restored by each food point");
  public static Property<Double> PROP_AMPLIFIED_MULTIPLIER = new Property<>("multiplier", 0.95).setDescription("amount of health points restored by each food point when using the amplified bonus");

  public static Property<Integer> PROP_SHIELD_BASE = new Property<>("shield_base_duration", 20).setDescription("the base duration for the shield, multiplied by overflow");
  public static Property<Integer> PROP_SHIELD_AMPLIFIER = new Property<>("shield_amplifier", 0).setDescription("the amplifier value for the shield");

  public static Property<Integer> PROP_RESISTANCE_BASE = new Property<>("resistance_base_duration", 20).setDescription("the base duration for the resistance buff, multiplied by overflow");
  public static Property<Integer> PROP_RESISTANCE_AMPLIFIER = new Property<>("resistance_amplifier", 0).setDescription("the amplifier value for the resistance buff");

  public static Property<Integer> PROP_GROWTH_TICKS = new Property<>("growth_ticks", 10).setDescription("the number of additional growth ticks to apply multiplied by overflow");
  public static Property<Integer> PROP_GROWTH_COUNT = new Property<>("growth_count", 3).setDescription("the number of random plants that should be considered for growth");

  public static Property<Integer> PROP_HEAL_AMOUNT = new Property<>("heal_targets", 3).setDescription("the number of peaceful creatures nearby that should be healed");

  public static Property<Integer> PROP_DAMAGE_AMOUNT = new Property<>("damage_targets", 4).setDescription("the number of hostile creatures nearby that should be damaged");

  public static Property<Integer> PROP_LEVITATION_DURATION = new Property<>("levitation_duration", 20).setDescription("the duration of the levitation effect multiplied by the overflow");
  public static Property<Integer> PROP_LEVITATE_TARGETS = new Property<>("levitation_targets", 3).setDescription("the number of hostile nearby creatures that should be levitated");

  public static Modifier RATIO = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "amplified_saturation"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.6)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_heal"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier GROWTH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_growth"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.125)));
  public static Modifier SHIELD = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_absorb"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.45)));
  public static Modifier DAMAGE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_damage"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.65)));
  public static Modifier LEVITATE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_levitation"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.275)));
  public static Modifier THOUGHTFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "thoughtful"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.3)));
  public static Modifier RESISTANCE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_stone_skin"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.4)));
  public static Modifier PURIFY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_purification"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.7)));

  static {
    THOUGHTFUL.addConflicts(PEACEFUL, GROWTH, SHIELD, DAMAGE, LEVITATE, RESISTANCE, PURIFY);
    PEACEFUL.addConflicts(GROWTH, SHIELD, DAMAGE, LEVITATE, RESISTANCE, PURIFY);
    GROWTH.addConflicts(SHIELD, DAMAGE, LEVITATE, RESISTANCE, PURIFY);
    SHIELD.addConflicts(LEVITATE, RESISTANCE, PURIFY);
    LEVITATE.addConflicts(RESISTANCE, PURIFY);
    RESISTANCE.addConflict(PURIFY);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_desaturate");
  public static SpellDesaturate instance = new SpellDesaturate(spellName);

  private AxisAlignedBB box;
  private double multiplier, amplified_multiplier;
  private int shield_base, shield_amplifier, resistance_base, resistance_amplifier, growth_ticks, growth_count, heal_amount, damage_amount, levitation_duration, levitation_targets, radius_x, radius_y, radius_z;

  public SpellDesaturate(ResourceLocation name) {
    super(name, TextFormatting.DARK_PURPLE, 184F / 255F, 232F / 255F, 42F / 255F, 109F / 255F, 32F / 255F, 168F / 255F);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_MULTIPLIER, PROP_AMPLIFIED_MULTIPLIER, PROP_SHIELD_BASE, PROP_SHIELD_AMPLIFIER, PROP_LEVITATE_TARGETS, PROP_LEVITATION_DURATION, PROP_RESISTANCE_AMPLIFIER, PROP_RESISTANCE_BASE, PROP_GROWTH_COUNT, PROP_GROWTH_TICKS, PROP_HEAL_AMOUNT, PROP_DAMAGE_AMOUNT, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
    acceptsModifiers(RATIO, PEACEFUL, GROWTH, SHIELD, DAMAGE, LEVITATE, THOUGHTFUL, RESISTANCE, PURIFY);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.BOWL),
        new ItemStack(ModItems.petals),
        new ItemStack(Items.ROTTEN_FLESH),
        new OreIngredient("bone"),
        new ItemStack(ModItems.spirit_herb)
    );
    setCastSound(ModSounds.Spells.DESATURATE);
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
    if (!caster.shouldHeal()) {
      return false;
    }
    FoodStats stats = caster.getFoodStats();
    int food = stats.getFoodLevel();
    if (food <= 1) {
      return false;
    }
    double multiplier = this.multiplier;
    if (info.has(RATIO)) {
      multiplier = this.amplified_multiplier;
    }
    multiplier = (float) multiplier;
    float missing = caster.getMaxHealth() - caster.getHealth();
    float healed = 0;

    float required;
    if (info.has(THOUGHTFUL)) {
      required = caster.getMaxHealth() - caster.getHealth();
    } else {
      required = caster.getMaxHealth();
    }

    for (int i = 0; i <= required; i++) {
      if (food > 1) {
        food--;
        healed += 1f * multiplier;
      } else {
        break;
      }
    }

    float overheal = (float) Math.floor((healed - missing) * 10) / 10.0f;

    World world = caster.world;

    if (!world.isRemote) {
      caster.heal(healed);
      stats.setFoodLevel(food);
      stats.foodSaturationLevel = Math.min(stats.foodSaturationLevel, food);

      if (!info.has(THOUGHTFUL) && overheal > 0) {
        if (info.has(PEACEFUL)) {
          List<EntityLivingBase> entities = caster.world.getEntitiesWithinAABB(EntityLivingBase.class, box.offset(caster.getPosition()), o -> EntityUtil.isFriendly(o, SpellDesaturate.instance));
          int count = 0;
          while (entities.size() > 0 && count < heal_amount) {
            int targ = Util.rand.nextInt(entities.size());
            EntityLivingBase target = entities.remove(targ);
            // TODO: Visual
            target.heal(overheal);
            count++;
          }
        } else if (info.has(GROWTH)) {
          List<BlockPos> positions = Growth.collect(caster.world, caster.getPosition(), radius_x, radius_y, radius_z);
          int count = 0;
          while (positions.size() > 0 && count < growth_count) {
            int targ = Util.rand.nextInt(positions.size());
            BlockPos pos = positions.remove(targ);
            for (int i = 0; i < (growth_ticks * overheal); i++) {
              IBlockState state = caster.world.getBlockState(pos);
              state.getBlock().randomTick(caster.world, pos, state, Util.rand);
            }
            PacketHandler.sendToAllTracking(new MessageRampantLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), caster);
            count++;
          }
        } else if (info.has(SHIELD)) {
          // TODO: Visual
          caster.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, (int) (shield_base * overheal), shield_amplifier, false, false));
          // Add a shield
        } else if (info.has(DAMAGE)) {
          List<EntityLivingBase> entities = caster.world.getEntitiesWithinAABB(EntityLivingBase.class, box.offset(caster.getPosition()), o -> EntityUtil.isHostile(o, SpellDesaturate.instance));
          int count = 0;
          while (entities.size() > 0 && count < damage_amount) {
            int targ = Util.rand.nextInt(entities.size());
            EntityLivingBase target = entities.remove(targ);
            // TODO: Visual
            target.attackEntityFrom(DamageSource.causeMobDamage(caster), overheal);
            count++;
          }
        } else if (info.has(LEVITATE)) {
          List<EntityLivingBase> entities = caster.world.getEntitiesWithinAABB(EntityLivingBase.class, box.offset(caster.getPosition()), o->EntityUtil.isHostile(o, SpellDesaturate.instance));
          int count = 0;
          while (entities.size() > 0 && count < levitation_targets) {
            int targ = Util.rand.nextInt(entities.size());
            EntityLivingBase target = entities.remove(targ);
            // TODO: Visual
            target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, (int) (levitation_duration * overheal), 0));
            count++;
          }
        } else if (info.has(RESISTANCE)) {
          // TODO: Visual
          caster.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, (int) (resistance_base * overheal), resistance_amplifier, false, false));
          // Grant resistance
        } else if (info.has(PURIFY)) {
          Potion pot = null;
          for (PotionEffect ef : caster.getActivePotionEffects()) {
            if (ef.getPotion().isBadEffect()) {
              pot = ef.getPotion();
              break;
            }
          }
          if (pot != null) {
            caster.removePotionEffect(pot);
          }
        }
      }

      ((EntityPlayerMP) caster).connection.sendPacket(new SPacketUpdateHealth(caster.getHealth(), stats.getFoodLevel(), stats.getSaturationLevel()));

      MessageDesaturationFX message = new MessageDesaturationFX(caster);
      PacketHandler.sendToAllTracking(message, caster);
    }

    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.multiplier = properties.get(PROP_MULTIPLIER);
    this.amplified_multiplier = properties.get(PROP_AMPLIFIED_MULTIPLIER);
    this.shield_base = properties.get(PROP_SHIELD_BASE);
    this.shield_amplifier = properties.get(PROP_SHIELD_AMPLIFIER);
    this.resistance_base = properties.get(PROP_RESISTANCE_BASE);
    this.resistance_amplifier = properties.get(PROP_RESISTANCE_AMPLIFIER);
    this.growth_ticks = properties.get(PROP_GROWTH_TICKS);
    this.growth_count = properties.get(PROP_GROWTH_COUNT);
    this.heal_amount = properties.get(PROP_HEAL_AMOUNT);
    this.damage_amount = properties.get(PROP_DAMAGE_AMOUNT);
    this.levitation_duration = properties.get(PROP_LEVITATION_DURATION);
    this.levitation_targets = properties.get(PROP_LEVITATE_TARGETS);
    int[] radius = properties.getRadius();
    this.radius_x = radius[0];
    this.radius_y = radius[1];
    this.radius_z = radius[2];
    this.box = new AxisAlignedBB(-radius_x, -radius_y, -radius_z, radius_x + 1, radius_y + 1, radius_z + 1);
  }
}
