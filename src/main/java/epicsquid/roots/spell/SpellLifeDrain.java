package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.RayCastUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageLifeDrainAbsorbFX;
import epicsquid.roots.network.fx.MessageTargetedLifeDrainFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpellLifeDrain extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("moonglow_leaf", 0.325));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("baffle_cap", 0.25));
  public static Property<Float> PROP_WITHER_DAMAGE = new Property<>("wither_damage", 3f).setDescription("wither damage dealt to the enemies (different from the damage dealt by the wither itself)");
  public static Property<Float> PROP_HEAL = new Property<>("heal", 1.5f).setDescription("health points restored to the player");
  public static Property<Integer> PROP_WITHER_DURATION = new Property<>("wither_duration", 70).setDescription("duration in ticks of the wither effect");
  public static Property<Integer> PROP_WITHER_AMPLIFICATION = new Property<>("wither_info.amplification", 0).setDescription("the level of the wither effect (0 is the first level)");
  public static Property<Integer> PROP_WITHER_CHANCE = new Property<>("wither_chance", 4).setDescription("chance for the enemies to be affected by a wither effect (the higher the number is the lower the chance is: 1/x) [default: 1/4]");
  public static Property<Float> PROP_ADDITIONAL_HEAL = new Property<>("additional_heal", 1.5f).setDescription("how much additional healing should be done");
  public static Property<Float> PROP_SPECTRAL_CHANCE = new Property<>("spectral_chance", 0.35f).setDescription("chance per cast of a spectral entity existing");
  public static Property<Double> PROP_DISTANCE = new Property<>("distance", 15d).setDescription("the distance that the targeted beam of life drain should extend for in blocks");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4).setDescription("the duration that relevant entities should be set aflame for in seconds");
  public static Property<Float> PROP_FIRE_DAMAGE = new Property<>("fire_damage", 2.5f).setDescription("the additional fire damage (that does not heal)");
  public static Property<Integer> PROP_SLOW_DURATION = new Property<>("slow_duration", 5 * 20).setDescription("the duration of the slow effect");
  public static Property<Integer> PROP_SLOW_AMPLIFIER = new Property<>("slow_amplifier", 0).setDescription("the info.amplifier to be applied to the slow effect");

  public static Modifier RATIO = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "amplified_healing"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.75)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_drain"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier DISTRIBUTE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "distributed_healing"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.375)));
  public static Modifier SPIRITS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "spectral_drain"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1.5)));
  public static Modifier TARGET = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "targeted_drain"), ModifierCores.TERRA_MOSS, Cost.of(Cost.cost(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.125), Cost.cost(CostType.ALL_COST_MULTIPLIER, -0.275))));
  public static Modifier DAMAGE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "lightened_load"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.25)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "brimstone"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.375)));
  public static Modifier SLOWING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slowing_drain"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.345)));
  public static Modifier CHTHONIC = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "chthonic"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.345)));

  static {
    TARGET.addConflicts(SPIRITS, DISTRIBUTE);
    SPIRITS.addConflicts(DISTRIBUTE);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_life_drain");
  public static SpellLifeDrain instance = new SpellLifeDrain(spellName);

  private float witherDamage, heal, additionalHeal, spectralChance, fire_damage;
  private int witherDuration, witherChance, witherAmplification, fire_duration, slow_duration, slow_amplifier;
  public double distance;

  public SpellLifeDrain(ResourceLocation name) {
    super(name, TextFormatting.DARK_GRAY, 144f / 255f, 32f / 255f, 64f / 255f, 255f / 255f, 196f / 255f, 240f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_WITHER_DAMAGE, PROP_HEAL, PROP_WITHER_DURATION, PROP_WITHER_AMPLIFICATION, PROP_WITHER_CHANCE, PROP_ADDITIONAL_HEAL, PROP_SPECTRAL_CHANCE, PROP_DISTANCE, PROP_FIRE_DAMAGE, PROP_FIRE_DURATION, PROP_SLOW_AMPLIFIER, PROP_SLOW_DURATION);
    acceptModifiers(RATIO, PEACEFUL, DISTRIBUTE, SPIRITS, TARGET, DAMAGE, FIRE, SLOWING, CHTHONIC);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.moonglow_seed),
        new ItemStack(Items.STONE_SWORD),
        new ItemStack(Blocks.IRON_BARS)
    );
    setCastSound(ModSounds.Spells.LIFE_DRAIN);
  }

  private boolean handleEntity(EntityPlayer player, EntityLivingBase e, StaffModifierInstanceList info, List<EntityLivingBase> peacefuls, float dam, float h) {
    if (e != player && !(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
      if (info.has(PEACEFUL) && EntityUtil.isFriendly(e, SpellLifeDrain.instance)) {
        return false;
      }
      if (e.hurtTime <= 0 && !e.isDead) {
        if (!player.world.isRemote) {
          if (info.has(CHTHONIC) && EntityUtil.isAquatic(e) && e.isInWater()) {
            dam *= 2;
          }
          e.attackEntityFrom(DamageSource.causeMobDamage(player), dam);
          if (e.rand.nextInt(witherChance) == 0) {
            e.addPotionEffect(new PotionEffect(MobEffects.WITHER, witherDuration, witherAmplification));
          }
          if (info.has(SLOWING)) {
            e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, slow_duration, slow_amplifier));
          }
          if (info.has(FIRE)) {
            e.setFire(fire_duration);
            e.attackEntityFrom(ModDamage.fireDamageFrom(player), fire_damage);
          }
          e.setRevengeTarget(player);
          e.setLastAttackedEntity(player);
          if (info.has(DISTRIBUTE)) {
            peacefuls.forEach(o -> o.heal(h));
          } else {
            player.heal(h);
          }
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    // TODO: Make this more elegant
    if (!player.world.isRemote) {
      boolean foundTarget = false;
      float heal = this.heal;
      if (info.has(RATIO)) {
        heal += additionalHeal;
      }
      float dam = witherDamage;
      if (info.has(DAMAGE)) {
        heal = heal / 2;
        dam = dam * 2;
      }
      final float h = heal;
      if (info.has(TARGET)) {
        List<EntityLivingBase> entitiesBeam = RayCastUtil.rayTraceEntities(EntityLivingBase.class, player, distance);
        EntityLivingBase targeted = null;
        for (EntityLivingBase target : entitiesBeam) {
          if (handleEntity(player, target, info, Collections.emptyList(), dam, h)) {
            targeted = target;
          }
        }
        if (targeted != null) {
          foundTarget = true;
          MessageTargetedLifeDrainFX packet = new MessageTargetedLifeDrainFX(player, targeted);
          PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) player);
        }
      } else {
        PacketHandler.sendToAllTracking(new MessageLifeDrainAbsorbFX(player.getUniqueID(), player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
        for (int i = 0; i < 4 && !foundTarget; i++) {
          double x = player.posX + player.getLookVec().x * 3.0 * (float) i;
          double y = player.posY + player.getEyeHeight() + player.getLookVec().y * 3.0 * (float) i;
          double z = player.posZ + player.getLookVec().z * 3.0 * (float) i;
          List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - 2.0, y - 2.0, z - 2.0, x + 2.0, y + 2.0, z + 2.0));
          List<EntityLivingBase> peacefuls = entities.stream().filter(o -> EntityUtil.isFriendly(o, SpellLifeDrain.instance)).collect(Collectors.toList());
          for (EntityLivingBase e : entities) {
            if (handleEntity(player, e, info, peacefuls, dam, h)) {
              foundTarget = true;
            }
          }
        }
      }
      if (!foundTarget && info.has(SPIRITS)) {
        if (Util.rand.nextFloat() < spectralChance) {
          player.heal(h);
        }
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.witherDamage = properties.get(PROP_WITHER_DAMAGE);
    this.heal = properties.get(PROP_HEAL);
    this.witherDuration = properties.get(PROP_WITHER_DURATION);
    this.witherAmplification = properties.get(PROP_WITHER_AMPLIFICATION);
    this.witherChance = properties.get(PROP_WITHER_CHANCE);
    this.additionalHeal = properties.get(PROP_ADDITIONAL_HEAL);
    this.spectralChance = properties.get(PROP_SPECTRAL_CHANCE);
    this.fire_damage = properties.get(PROP_FIRE_DAMAGE);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
    this.slow_amplifier = properties.get(PROP_SLOW_AMPLIFIER);
    this.slow_duration = properties.get(PROP_SLOW_DURATION);
    this.distance = properties.get(PROP_DISTANCE);
  }
}
