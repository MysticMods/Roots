package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.client.SpectatorHandler;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.MessageLightDrifterSync;
import epicsquid.roots.network.fx.MessageLightDrifterFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.Constants;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

public class SpellAugment extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(350);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("wildroot", 0.7));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("radius on the X axis of the area in which dropped items are magnetized to the player");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("radius on the Y axis of the area in which dropped items are magnetized to the player");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("radius on the Z axis of the area in which dropped items are magnetized to the player");

  public static Property<Integer> PROP_REACH_DURATION = new Property<>("reach_duration", 600).setDescription("duration for the reach potion effect");
  public static Property<Double> PROP_REACH = new Property<>("reach", 5.0).setDescription("the extended reach applied to the player during the effect of the spell");

  public static Property<Integer> PROP_SPEED_DURATION = new Property<>("speed_duration", 15 * 20).setDescription("duration for the speed potion effect");
  public static Property<Integer> PROP_SPEED_AMPLIFIER = new Property<>("speed_amplifier", 0).setDescription("amplifier for the speed potion effect");

  public static Property<Integer> PROP_SLOW_FALL_DURATION = new Property<>("slow_fall_duration", 18 * 20).setDescription("duration for the slow fall potion effect");

  public static Property<Integer> PROP_DRIFTER_DURATION = new Property<>("drifter_duration", 200).setDescription("the duration in ticks of the spell effect on the player");

  public static Property<Integer> PROP_LUCK_DURATION = new Property<>("luck_duration", 15 * 20).setDescription("duration for the luck potion effect");
  public static Property<Integer> PROP_LUCK_AMPLIFIER = new Property<>("luck_amplifier", 0).setDescription("amplifier for the luck potion effect");

  public static Property<Integer> PROP_STRENGTH_DURATION = new Property<>("strength_duration", 15 * 20).setDescription("duration for the strength potion effect");
  public static Property<Integer> PROP_STRENGTH_AMPLIFIER = new Property<>("strength_amplifier", 0).setDescription("amplifier for the strength potion effect");

  public static Property<Integer> PROP_HASTE_DURATION = new Property<>("haste_duration", 15 * 20).setDescription("duration for the haste potion effect");
  public static Property<Integer> PROP_HASTE_AMPLIFIER = new Property<>("haste_amplifier", 0).setDescription("amplifier for the haste potion effect");

  public static Property<Integer> PROP_ABSORPTION_DURATION = new Property<>("absorption_duration", 15 * 20).setDescription("duration for the haste potion effect");
  public static Property<Integer> PROP_ABSORPTION_AMPLIFIER = new Property<>("absorption_amplifier", 0).setDescription("amplifier for the haste potion effect");

  public static Property<Integer> PROP_AIR_AMOUNT = new Property<>("air_amount", 300).setDescription("the value to add to a user's air with the second wind effect");


  public static Modifier REACH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "reach"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.5)));
  public static Modifier SPEED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "speed"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.5)));
  public static Modifier SLOW_FALL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "augment_slow_fall"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.275)));
  public static Modifier LIGHT_DRIFTER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "light_drifter"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.5)));
  public static Modifier MAGNETISM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetism"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.125)));
  public static Modifier LUCK = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "bitter_luck"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.125)));
  public static Modifier ABSORPTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "absorption"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.75)));
  public static Modifier STRENGTH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "strength_of_flame"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.85)));
  public static Modifier HASTE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "haste"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.85)));
  public static Modifier SECOND_WIND = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "second_wind"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.125)));

  static {
    LIGHT_DRIFTER.addConflicts(SLOW_FALL, MAGNETISM);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_augment");
  public static SpellAugment instance = new SpellAugment(spellName);

  private int radius_x, radius_y, radius_z, reach_duration, speed_amplifier, speed_duration, slow_fall, drifter_duration, luck_amplifier, luck_duration, strength_amplifier, strength_duration, haste_amplifier, haste_duration, air_amount, absorption_duration, absorption_amplifier;
  private double reach;

  private SpellAugment(ResourceLocation name) {
    super(name, TextFormatting.AQUA, 69 / 255.0f, 209 / 255.0f, 127 / 255.0f, 28 / 255.0f, 28 / 255.0f, 148 / 255.0f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_REACH, PROP_REACH_DURATION, PROP_SPEED_AMPLIFIER, PROP_SPEED_DURATION, PROP_SLOW_FALL_DURATION, PROP_DRIFTER_DURATION, PROP_LUCK_AMPLIFIER, PROP_LUCK_DURATION, PROP_AIR_AMOUNT, PROP_STRENGTH_AMPLIFIER, PROP_STRENGTH_DURATION, PROP_HASTE_AMPLIFIER, PROP_HASTE_DURATION, PROP_ABSORPTION_AMPLIFIER, PROP_ABSORPTION_DURATION);
    acceptModifiers(REACH, SPEED, SLOW_FALL, LIGHT_DRIFTER, MAGNETISM, LUCK, ABSORPTION, STRENGTH, HASTE, SECOND_WIND);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Blocks.ANVIL),
        new ItemStack(Items.FISHING_ROD),
        new ItemStack(Items.SHIELD),
        new ItemStack(epicsquid.roots.init.ModItems.petals),
        new ItemStack(Items.BOOK)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    // TODO: Particle
    boolean acted = false;
    if (info.has(REACH)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(ModPotions.reach, reach_duration, 0, false, false));
    }
    if (info.has(SPEED)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.SPEED, speed_duration, speed_amplifier, false, false));
    }
    if (info.has(SLOW_FALL)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(ModPotions.slow_fall, slow_fall, 0, false, false));
    }
    if (info.has(LIGHT_DRIFTER)) {
      acted = true;
      if (!player.world.isRemote) {
        player.capabilities.disableDamage = true;
        player.capabilities.allowFlying = true;
        player.noClip = true;
        player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_TAG, drifter_duration);
        player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_X, player.posX);
        player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_Y, player.posY);
        player.getEntityData().setDouble(Constants.LIGHT_DRIFTER_Z, player.posZ);
        if (player.capabilities.isCreativeMode) {
          player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_MODE, GameType.CREATIVE.getID());
        } else {
          player.getEntityData().setInteger(Constants.LIGHT_DRIFTER_MODE, GameType.SURVIVAL.getID());
        }
        player.setGameType(GameType.SPECTATOR);
        PacketHandler.sendToAllTracking(new MessageLightDrifterSync(player.getUniqueID(), player.posX, player.posY, player.posZ, true, GameType.SPECTATOR.getID()), player);
        PacketHandler.sendToAllTracking(new MessageLightDrifterFX(player.posX, player.posY + 1.0f, player.posZ), player);
      } else {
        SpectatorHandler.setFake();
      }
    }
    if (info.has(MAGNETISM)) {
      int count = 0;
      count += Magnetize.pull(EntityItem.class, player.world, player.getPosition(), radius_x, radius_y, radius_z);
      if (SpellConfig.spellFeaturesCategory.shouldMagnetismAttractXP) {
        count += Magnetize.pull(EntityXPOrb.class, player.world, player.getPosition(), radius_x, radius_y, radius_z);
      }

      if (!acted) {
        acted = count != 0;
      }
    }
    if (info.has(LUCK)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.LUCK, luck_duration, luck_amplifier, false, false));
    }
    if (info.has(STRENGTH)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, strength_duration, strength_amplifier, false, false));
    }
    if (info.has(ABSORPTION)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, absorption_duration, absorption_amplifier, false, false));
    }
    if (info.has(SECOND_WIND)) {
      int air = player.getAir();
      if (air < 300) {
        acted = true;
        player.setAir(Math.min(300, air + air_amount));
        if (player.world.isRemote) {
          player.playSound(SoundEvents.ENTITY_BOAT_PADDLE_WATER, 1, 1);
        }
      }
    }
    if (info.has(HASTE)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.HASTE, haste_duration, haste_amplifier, false, false));
    }
    return acted;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.reach = properties.get(PROP_REACH);
    this.reach_duration = properties.get(PROP_REACH_DURATION);
    this.speed_duration = properties.get(PROP_SPEED_DURATION);
    this.speed_amplifier = properties.get(PROP_SPEED_AMPLIFIER);
    this.slow_fall = properties.get(PROP_SLOW_FALL_DURATION);
    this.drifter_duration = properties.get(PROP_DRIFTER_DURATION);
    this.luck_amplifier = properties.get(PROP_LUCK_AMPLIFIER);
    this.luck_duration = properties.get(PROP_LUCK_DURATION);
    this.strength_amplifier = properties.get(PROP_STRENGTH_AMPLIFIER);
    this.strength_duration = properties.get(PROP_STRENGTH_DURATION);
    this.haste_amplifier = properties.get(PROP_HASTE_AMPLIFIER);
    this.haste_duration = properties.get(PROP_HASTE_DURATION);
    this.air_amount = properties.get(PROP_AIR_AMOUNT);
    this.absorption_amplifier = properties.get(PROP_ABSORPTION_AMPLIFIER);
    this.absorption_duration = properties.get(PROP_ABSORPTION_DURATION);
    ModPotions.reach.loadComplete(this.reach);
  }
}
