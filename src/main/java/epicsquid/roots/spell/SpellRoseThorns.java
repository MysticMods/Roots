package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityThornTrap;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellRoseThorns extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(24);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("terra_moss", 0.325));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(4f);
  public static Property<Integer> PROP_SLOWNESS_DURATION = new Property<>("slowness_duration", 80).setDescription("duration in ticks of the slowness effect applied when the traps are triggered");
  public static Property<Integer> PROP_SLOWNESS_AMPLIFIER = new Property<>("slowness_amplifier", 0).setDescription("the level of the slowness effect (0 is the first level)");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 80).setDescription("duration in ticks of the poison effect applied when the traps are triggered");
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the level of the poison effect (0 is the first level)");
  public static Property<Integer> PROP_DURATION = new Property<>("trap_duration", 600).setDescription("duration in ticks of the trap before it disappears");
  public static Property<Integer> PROP_WEAKNESS_DURATION = new Property<>("weakness_duration", 4 * 20).setDescription("how long enemies should be weakened in place for");
  public static Property<Integer> PROP_WEAKNESS_AMPLIFIER = new Property<>("weakness_amplifier", 0).setDescription("the amplifier to be applied to the weakness effect");
  public static Property<Float> PROP_KNOCKBACK = new Property<>("knockback", 1.2f).setDescription("how much entities should be knocked back by");
  public static Property<Float> PROP_UNDEAD_DAMAGE = new Property<>("undead_damage", 1f).setDescription("how much additional damage against undead");
  public static Property<Float> PROP_KNOCKUP = new Property<>("knock_up", 1.2f).setDescription("how much a creature should be knocked up by");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4).setDescription("how long entities should be set aflame for");
  public static Property<Integer> PROP_STRENGTH_DURATION = new Property<>("strength_duration", 5 * 20).setDescription("how long the player should be empowered for when the trap triggers");
  public static Property<Integer> PROP_STRENGTH_AMPLIFIER = new Property<>("strength_amplifier", 0).setDescription("the amplifier to be applied to the strength potion effect");

  public float damage, undead_damage, knockback, knockup;
  public int slowness_duration, slowness_amplifier, poison_duration, poison_amplifier, duration, strength_duration, strength_amplifier, fire_duration, weakness_duration, weakness_amplifier;

  public static Modifier BIGGER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "larger_trap"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.475)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_trap"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier WEAKNESS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "weakening_trap"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.45)));
  public static Modifier KNOCKBACK = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "repelling_trap"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.375)));
  public static Modifier UNDEAD = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "ghostly_trap"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.275)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poisonous_trap"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.125)));
  public static Modifier BOOST = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "boosting_trap"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.345)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fire_trap"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.45)));
  public static Modifier STRENGTH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "empowering_trap"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.475)));
  public static Modifier SLOW = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slowing_trap"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.125)));

  static {
    // Conflicts
    WEAKNESS.addConflicts(KNOCKBACK, BOOST, SLOW); // Can't slow enemies to a stop & fling/levitate/slow them
    BOOST.addConflicts(KNOCKBACK); // Can't fling them up in the air and backwards at the same time
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_rose_thorns");
  public static SpellRoseThorns instance = new SpellRoseThorns(spellName);

  public SpellRoseThorns(ResourceLocation name) {
    super(name, TextFormatting.RED, 255f / 255f, 32f / 255f, 64f / 255f, 32f / 255f, 255f / 255f, 96f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_SLOWNESS_AMPLIFIER, PROP_SLOWNESS_DURATION, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_DURATION, PROP_WEAKNESS_DURATION, PROP_WEAKNESS_AMPLIFIER, PROP_KNOCKBACK, PROP_KNOCKUP, PROP_UNDEAD_DAMAGE, PROP_STRENGTH_AMPLIFIER, PROP_STRENGTH_DURATION, PROP_FIRE_DURATION);
    acceptModifiers(BIGGER, PEACEFUL, WEAKNESS, KNOCKBACK, UNDEAD, POISON, BOOST, FIRE, STRENGTH, SLOW);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("blockCactus"),
        new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.ROSE.getMeta()),
        new OreIngredient("stickWood"),
        new OreIngredient("dyeRed"),
        new ItemStack(ModItems.terra_moss)
    );
    setCastSound(ModSounds.Spells.ROSE_THORNS);
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    if (!player.world.isRemote) {
      EntityThornTrap trap = new EntityThornTrap(player.world);
      trap.setPlayer(player.getUniqueID());
      trap.setModifiers(info);
      trap.setPosition(player.posX + player.getLookVec().x, player.posY + player.getEyeHeight() + player.getLookVec().y, player.posZ + player.getLookVec().z);
      trap.motionX = player.getLookVec().x * 0.75f;
      trap.motionY = player.getLookVec().y * 0.75f;
      trap.motionZ = player.getLookVec().z * 0.75f;
      player.world.spawnEntity(trap);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.damage = properties.get(PROP_DAMAGE);
    this.poison_amplifier = properties.get(PROP_POISON_AMPLIFIER);
    this.poison_duration = properties.get(PROP_POISON_DURATION);
    this.slowness_amplifier = properties.get(PROP_SLOWNESS_AMPLIFIER);
    this.slowness_duration = properties.get(PROP_SLOWNESS_DURATION);
    this.duration = properties.get(PROP_DURATION);
    this.undead_damage = properties.get(PROP_UNDEAD_DAMAGE);
    this.knockback = properties.get(PROP_KNOCKBACK);
    this.knockup = properties.get(PROP_KNOCKUP);
    this.strength_amplifier = properties.get(PROP_STRENGTH_AMPLIFIER);
    this.strength_duration = properties.get(PROP_STRENGTH_DURATION);
    this.weakness_duration = properties.get(PROP_WEAKNESS_DURATION);
    this.weakness_amplifier = properties.get(PROP_WEAKNESS_AMPLIFIER);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
  }
}
