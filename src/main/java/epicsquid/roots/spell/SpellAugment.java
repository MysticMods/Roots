package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.client.SpectatorHandler;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.mechanics.Magnetize;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.MessageLightDrifterSync;
import epicsquid.roots.network.fx.MessageLightDrifterFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.oredict.OreIngredient;

public class SpellAugment extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(350);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 1.0));
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

  public static Property<Integer> PROP_AIR_AMOUNT = new Property<>("air_amount", 300).setDescription("the value to add to a user's air with the second wind effect");

  public static Property<Integer> PROP_JAUNT_DISTANCE = new Property<>("jaunt_distance", 5).setDescription("the number of blocks forward to jaunt");

  public static Modifier REACH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "reach"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier SPEED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "speed"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier SLOW_FALL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "augment_slow_fall"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier LIGHT_DRIFTER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "light_drifter"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier MAGNETISM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetism"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier LUCK = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "bitter_luck"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier JAUNT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "jaunt"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier STRENGTH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "strength_of_flame"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier HASTE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "haste"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier SECOND_WIND = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "second_wind"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    LIGHT_DRIFTER.addConflicts(SLOW_FALL, MAGNETISM);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_augment");
  public static SpellAugment instance = new SpellAugment(spellName);

  private int radius_x, radius_y, radius_z, reach_duration, speed_amplifier, speed_duration, slow_fall, drifter_duration, luck_amplifier, luck_duration, jaunt_distance, strength_amplifier, strength_duration, haste_amplifier, haste_duration, air_amount;
  private double reach;

  private SpellAugment(ResourceLocation name) {
    super(name, TextFormatting.AQUA, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_REACH, PROP_REACH_DURATION, PROP_SPEED_AMPLIFIER, PROP_SPEED_DURATION, PROP_SLOW_FALL_DURATION, PROP_DRIFTER_DURATION, PROP_LUCK_AMPLIFIER, PROP_LUCK_DURATION, PROP_JAUNT_DISTANCE, PROP_AIR_AMOUNT, PROP_STRENGTH_AMPLIFIER, PROP_STRENGTH_DURATION, PROP_HASTE_AMPLIFIER, PROP_HASTE_DURATION);
    acceptsModifiers(REACH, SPEED, SLOW_FALL, LIGHT_DRIFTER, MAGNETISM, LUCK, JAUNT, STRENGTH, HASTE, SECOND_WIND);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(ModItems.spirit_herb_seed),
        new ItemStack(Items.EXPERIENCE_BOTTLE),
        new ItemStack(Items.CARROT_ON_A_STICK),
        new ItemStack(Items.GOLDEN_APPLE),
        new ItemStack(Items.IRON_HELMET)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    // TODO: Particle
    boolean acted = false;
    if (info.has(REACH)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(ModPotions.reach, ampInt(reach_duration), 0, false, false));
    }
    if (info.has(SPEED)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.SPEED, ampInt(speed_duration), speed_amplifier, false, false));
    }
    if (info.has(SLOW_FALL)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(ModPotions.slow_fall, ampInt(slow_fall), 0, false, false));
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
      count += Magnetize.pull(EntityItem.class, player.world, player.getPosition(), ampInt(radius_x), ampInt(radius_y), ampInt(radius_z));
      if (SpellConfig.spellFeaturesCategory.shouldMagnetismAttractXP) {
        count += Magnetize.pull(EntityXPOrb.class, player.world, player.getPosition(), ampInt(radius_x), ampInt(radius_y), ampInt(radius_z));
      }

      if (!acted) {
        acted = count != 0;
      }
    }
    if (info.has(LUCK)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.LUCK, ampInt(luck_duration), luck_amplifier, false, false));
    }
    if (info.has(JAUNT)) {
      Vec3d realPos = new Vec3d(player.posX, player.posY, player.posZ).add(Vec3d.fromPitchYaw(0, player.rotationYaw).scale(jaunt_distance));
      BlockPos pos = player.world.getHeight(new BlockPos(realPos.x, realPos.y, realPos.z));
      IBlockState state = player.world.getBlockState(pos);
      if (state.getBlock().isPassable(player.world, pos)) {
        acted = true;
        if (!player.world.isRemote) {
          player.setPositionAndUpdate(realPos.x, pos.getY() + 0.01, realPos.z);
          player.fallDistance = 0f;
        }
      }
    }
    if (info.has(STRENGTH)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, ampInt(strength_duration), strength_amplifier, false, false));
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
      player.addPotionEffect(new PotionEffect(MobEffects.HASTE, ampInt(haste_duration), haste_amplifier, false, false));
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
    this.jaunt_distance = properties.get(PROP_JAUNT_DISTANCE);
    this.air_amount = properties.get(PROP_AIR_AMOUNT);
    ModPotions.reach.loadComplete(this.reach);
  }
}
