package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.ThornTrapEntity;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellRoseThorns extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(24);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("terra_moss", 0.25));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(8f);
  public static Property<Integer> PROP_SLOWNESS_DURATION = new Property<>("slowness_duration", 80);
  public static Property<Integer> PROP_SLOWNESS_AMPLIFIER = new Property<>("slowness_amplifier", 0);
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 80);
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0);
  public static Property<Integer> PROP_DURATION = new Property<>("trap_duration", 600);

  public static String spellName = "spell_rose_thorns";
  public static SpellRoseThorns instance = new SpellRoseThorns(spellName);

  public static float damage;
  public static int slownessDuration, slownessAmplifier, poisonDuration, poisonAmplifier, duration;

  public SpellRoseThorns(String name) {
    super(name, TextFormatting.RED, 255f / 255f, 32f / 255f, 64f / 255f, 32f / 255f, 255f / 255f, 96f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_SLOWNESS_AMPLIFIER, PROP_SLOWNESS_DURATION, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_DURATION);
  }

  @Override
  public void init() {
    addIngredients(
/*        new OreIngredient("blockCactus"),
        new ItemStack(Blocks.DOUBLE_PLANT, 1, DoublePlantBlock.EnumPlantType.ROSE.getMeta()),
        new OreIngredient("bone"),
        new ItemStack(Items.DYE, 1, DyeColor.RED.getDyeDamage()),
        new ItemStack(ModItems.terra_moss)*/
    );
  }

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      ThornTrapEntity trap = new ThornTrapEntity(player.world, damage, duration, slownessDuration, slownessAmplifier, poisonDuration, poisonAmplifier);
      trap.setPlayer(player.getUniqueID());
      trap.setPosition(player.posX + player.getLookVec().x, player.posY + player.getEyeHeight() + player.getLookVec().y, player.posZ + player.getLookVec().z);
      trap.setMotion(player.getLookVec().x * 0.75f, player.getLookVec().y * 0.75f, player.getLookVec().z * 0.75);
      player.world.addEntity(trap);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
    damage = properties.getProperty(PROP_DAMAGE);
    poisonAmplifier = properties.getProperty(PROP_POISON_AMPLIFIER);
    poisonDuration = properties.getProperty(PROP_POISON_DURATION);
    slownessAmplifier = properties.getProperty(PROP_SLOWNESS_AMPLIFIER);
    slownessDuration = properties.getProperty(PROP_SLOWNESS_DURATION);
    duration = properties.getProperty(PROP_DURATION);
  }
}
