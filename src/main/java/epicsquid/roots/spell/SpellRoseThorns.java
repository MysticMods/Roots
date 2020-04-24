package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityThornTrap;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.util.types.Property;
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

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_rose_thorns");
  public static SpellRoseThorns instance = new SpellRoseThorns(spellName);

  public static float damage;
  public static int slownessDuration, slownessAmplifier, poisonDuration, poisonAmplifier, duration;

  public SpellRoseThorns(ResourceLocation name) {
    super(name, TextFormatting.RED, 255f / 255f, 32f / 255f, 64f / 255f, 32f / 255f, 255f / 255f, 96f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE, PROP_SLOWNESS_AMPLIFIER, PROP_SLOWNESS_DURATION, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_DURATION);
  }

  @Override
  public void init () {
    addIngredients(
        new OreIngredient("blockCactus"),
        new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.ROSE.getMeta()),
        new OreIngredient("bone"),
        new OreIngredient("dyeRed"),
        new ItemStack(ModItems.terra_moss)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, ModifierInstanceList modifiers, int ticks) {
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
