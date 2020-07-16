package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityThornTrap;
import epicsquid.roots.init.ModItems;
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
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("terra_moss", 0.25));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(8f);
  public static Property<Integer> PROP_SLOWNESS_DURATION = new Property<>("slowness_duration", 80).setDescription("duration in ticks of the slowness effect applied when the traps are triggered");
  public static Property<Integer> PROP_SLOWNESS_AMPLIFIER = new Property<>("slowness_amplifier", 0).setDescription("the level of the slowness effect (0 is the first level)");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 80).setDescription("duration in ticks of the poison effect applied when the traps are triggered");
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the level of the poison effect (0 is the first level)");
  public static Property<Integer> PROP_DURATION = new Property<>("trap_duration", 600).setDescription("duration in ticks of the trap before it disappears");

  public static float damage;
  public static int slownessDuration, slownessAmplifier, poisonDuration, poisonAmplifier, duration;

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "larger_trap"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier WILDEWHEET = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_trap"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "rooting_trap"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "repelling_trap"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "ghostly_trap"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poisonous_trap"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier CLOUD_BERRY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "boosting_trap"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier INFERNAL_BULB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fire_trap"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "empowering_trap"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "paralysis_trap"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    // Conflicts
    WILDROOT.addConflicts(MOONGLOW_LEAF, CLOUD_BERRY, DEWGONIA); // Can't slow enemies to a stop & fling/levitate/slow them
    CLOUD_BERRY.addConflicts(MOONGLOW_LEAF); // Can't fling them up in the air and backwards at the same time
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_rose_thorns");
  public static SpellRoseThorns instance = new SpellRoseThorns(spellName);

  public SpellRoseThorns(ResourceLocation name) {
    super(name, TextFormatting.RED, 255f / 255f, 32f / 255f, 64f / 255f, 32f / 255f, 255f / 255f, 96f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_SLOWNESS_AMPLIFIER, PROP_SLOWNESS_DURATION, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_DURATION);
    acceptsModifiers(PERESKIA, WILDEWHEET, WILDROOT, MOONGLOW_LEAF, SPIRIT_HERB, BAFFLE_CAP, CLOUD_BERRY, INFERNAL_BULB, STALICRIPE, DEWGONIA);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("blockCactus"),
        new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.ROSE.getMeta()),
        new OreIngredient("bone"),
        new OreIngredient("dyeRed"),
        new ItemStack(ModItems.terra_moss)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks) {
    if (!player.world.isRemote) {
      EntityThornTrap trap = new EntityThornTrap(player.world, ampFloat(damage), ampInt(duration), ampInt(slownessDuration), slownessAmplifier, poisonDuration, poisonAmplifier);
      trap.setPlayer(player.getUniqueID());
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
    damage = properties.get(PROP_DAMAGE);
    poisonAmplifier = properties.get(PROP_POISON_AMPLIFIER);
    poisonDuration = properties.get(PROP_POISON_DURATION);
    slownessAmplifier = properties.get(PROP_SLOWNESS_AMPLIFIER);
    slownessDuration = properties.get(PROP_SLOWNESS_DURATION);
    duration = properties.get(PROP_DURATION);
  }
}
