package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.client.SpectatorHandler;
import epicsquid.roots.init.ModItems;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
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

  public static Modifier REACH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "reach"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier SPEED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "speed"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier SLOW_FALL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slow_fall"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
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

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "augment");
  public static SpellAugment instance = new SpellAugment(spellName);

  private int radius_x, radius_y, radius_z, reach_duration, speed_amplifier, speed_duration, slow_fall, drifter_duration, luck_amplifier, luck_duration;
  private double reach;

  private SpellAugment(ResourceLocation name) {
    super(name, TextFormatting.DARK_RED, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_REACH, PROP_REACH_DURATION, PROP_SPEED_AMPLIFIER, PROP_SPEED_DURATION, PROP_SLOW_FALL_DURATION, PROP_DRIFTER_DURATION, PROP_LUCK_AMPLIFIER, PROP_LUCK_DURATION);
    acceptsModifiers(REACH, SPEED, SLOW_FALL, LIGHT_DRIFTER, MAGNETISM, LUCK, JAUNT, STRENGTH, HASTE, SECOND_WIND);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.SHIELD),
        new OreIngredient("gemDiamond"),
        new OreIngredient("bone"),
        new ItemStack(Item.getItemFromBlock(Blocks.TRIPWIRE_HOOK)),
        new ItemStack(ModItems.moonglow_leaf)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
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
      count += Magnetize.pull(EntityXPOrb.class, player.world, player.getPosition(), ampInt(radius_x), ampInt(radius_y), ampInt(radius_z));

      if (!acted) {
        acted = count != 0;
      }
    }
    if (info.has(LUCK)) {
      acted = true;
      player.addPotionEffect(new PotionEffect(MobEffects.LUCK, ampInt(luck_duration), luck_amplifier, false, false));
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
    ModPotions.reach.loadComplete(this.reach);
  }
}
