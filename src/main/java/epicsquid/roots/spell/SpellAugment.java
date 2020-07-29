package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellAugment extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(350);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 1.0));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 2).setDescription("radius on the X axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 2).setDescription("radius on the Y axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 2).setDescription("radius on the Z axis within which entities are affected by the spell");
  public static Property<Integer> PROP_DROP_CHANCE = new Property<>("drop_chance", 4).setDescription("chance for mobs to drop their equipment and weapons (the higher the number is the lower the chance is: 1/x) [default: 1/4]");

  public static Property<Integer> PROP_REACH_DURATION = new Property<>("reach_duration", 600).setDescription("duration for the reach potion effect");
  public static Property<Double> PROP_REACH = new Property<>("reach", 5.0).setDescription("the extended reach applied to the player during the effect of the spell");

  public static Property<Integer> PROP_SPEED_DURATION = new Property<>("speed_duration", 15*20).setDescription("duration for the speed potion effect");
  public static Property<Integer> PROP_SPEED_AMPLIFIER = new Property<>("speed_amplifier", 0).setDescription("amplifier for the speed potion effect");

  public static Property<Integer> PROP_SLOW_FALL_DURATION = new Property<>("slow_fall_duration", 18*20).setDescription("duration for the slow fall potion effect");

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

  private int radius_x, radius_y, radius_z, drop_chance, reach_duration, speed_amplifier, speed_duration, slow_fall;
  private double reach;

  private SpellAugment(ResourceLocation name) {
    super(name, TextFormatting.DARK_RED, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_REACH, PROP_REACH_DURATION, PROP_SPEED_AMPLIFIER, PROP_SPEED_DURATION, PROP_SLOW_FALL_DURATION);
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
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList modifiers, int ticks) {
    if (has(REACH, modifiers)) {
      caster.addPotionEffect(new PotionEffect(ModPotions.reach, ampInt(reach_duration), 0, false, false));
    }
    if (has(SPEED, modifiers)) {
      caster.addPotionEffect(new PotionEffect(MobEffects.SPEED, ampInt(speed_duration), speed_amplifier, false, false));
    }
    if (has(SLOW_FALL, modifiers)) {
      // TODO: Slow Fall???
      //caster.addPotionEffect(new PotionEffect(MobEffects.
    }
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.drop_chance = properties.get(PROP_DROP_CHANCE);
    this.reach = properties.get(PROP_REACH);
    this.reach_duration = properties.get(PROP_REACH_DURATION);
    this.speed_duration = properties.get(PROP_SPEED_DURATION);
    this.speed_amplifier = properties.get(PROP_SPEED_AMPLIFIER);
    this.slow_fall = properties.get(PROP_SLOW_FALL_DURATION);
    ModPotions.reach.loadComplete(this.reach);
  }
}
