package epicsquid.roots.spell;

import epicsquid.roots.entity.spell.EntityThornTrap;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

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

    addIngredients(
        new OreIngredient("blockCactus"),
        new ItemStack(Blocks.DOUBLE_PLANT, 1, 4),
        new OreIngredient("bone"),
        new ItemStack(Items.FERMENTED_SPIDER_EYE),
        new ItemStack(ModItems.terra_moss)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      EntityThornTrap trap = new EntityThornTrap(player.world, damage, duration, slownessDuration, slownessAmplifier, poisonDuration, poisonAmplifier);
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
  public void finalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);

    SpellCost cost = properties.getProperty(PROP_COST_1);
    addCost(cost.getHerb(), cost.getCost());

    damage = properties.getProperty(PROP_DAMAGE);
    poisonAmplifier = properties.getProperty(PROP_POISON_AMPLIFIER);
    poisonDuration = properties.getProperty(PROP_POISON_DURATION);
    slownessAmplifier = properties.getProperty(PROP_SLOWNESS_AMPLIFIER);
    slownessDuration = properties.getProperty(PROP_SLOWNESS_DURATION);
    duration = properties.getProperty(PROP_DURATION);
  }
}
