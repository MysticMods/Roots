package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityTimeStop;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageTimeStopStartFX;
import epicsquid.roots.properties.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellTimeStop extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(320);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("pereskia", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("moonglow_leaf", 0.5));
  public static Property.PropertyCost PROP_COST_3 = new Property.PropertyCost(new SpellCost("dewgonia", 0.325));
  public static Property<Integer> PROP_DURATION = new Property<>("duration", 200).setDescription("the duration of the time stop effect on thaumcraft.entities");
  public static Property<Integer> PROP_OVERTIME = new Property<>("overtime", 200).setDescription("the extended duration that should apply when overtime is active");
  public static Property<Integer> PROP_UNDERTIME = new Property<>("undertime", 100).setDescription("the shortened duration that is applied instead of the normal duration");
  public static Property<Integer> PROP_SLOWNESS_DURATION = new Property<>("slowness_duration", 80).setDescription("duration in ticks of the slowness effect applied when the traps are triggered");
  public static Property<Integer> PROP_SLOWNESS_AMPLIFIER = new Property<>("slowness_amplifier", 0).setDescription("the level of the slowness effect (0 is the first level)");
  public static Property<Integer> PROP_WEAKNESS_DURATION = new Property<>("weakness_duration", 80).setDescription("duration in ticks of the weakness effect applied when the traps are triggered");
  public static Property<Integer> PROP_WEAKNESS_AMPLIFIER = new Property<>("weakness_amplifier", 0).setDescription("the level of the weakness effect (0 is the first level)");
  public static Property<Integer> PROP_SPEED_DURATION = new Property<>("speed_duration", 80).setDescription("duration in ticks of the speed effect applied when the traps are triggered");
  public static Property<Integer> PROP_SPEED_AMPLIFIER = new Property<>("speed_amplifier", 0).setDescription("the level of the speed effect (0 is the first level)");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 5).setDescription("the duration that thaumcraft.entities should be set on fire for");

  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_passage"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier FAMILIARS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "freed_familiars"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.125)));
  public static Modifier LONGER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "overtime"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.375)));
  public static Modifier SLOW = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "time_slip"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.375)));
  public static Modifier WEAKNESS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "draining_stop"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.45)));
  public static Modifier SPEED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "hasty_escape"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.45)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "burn_in_hell"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.45)));
  public static Modifier SHORTER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "minor_halt"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.125)));
  public static Modifier FREEZE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "ice_age"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.345)));

  static {
    // Conflicts
    SHORTER.addConflicts(LONGER);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_time_stop");
  public static SpellTimeStop instance = new SpellTimeStop(spellName);

  public int duration, slow_duration, slow_amplifier, overtime, weakness_duration, weakness_amplifier, speed_duration, speed_amplifier, fire_duration, undertime;

  public SpellTimeStop(ResourceLocation name) {
    super(name, TextFormatting.DARK_BLUE, 64f / 255f, 64f / 255f, 64f / 255f, 192f / 255f, 32f / 255f, 255f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_COST_3, PROP_DURATION, PROP_SLOWNESS_AMPLIFIER, PROP_SLOWNESS_DURATION, PROP_OVERTIME, PROP_WEAKNESS_AMPLIFIER, PROP_WEAKNESS_DURATION, PROP_SPEED_AMPLIFIER, PROP_SPEED_DURATION, PROP_FIRE_DURATION, PROP_UNDERTIME);
    acceptsModifiers(PEACEFUL, FAMILIARS, LONGER, SLOW, WEAKNESS, SPEED, FIRE, SHORTER, FREEZE);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("enderpearl"),
        new ItemStack(ModItems.moonglow_leaf),
        PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.SLOWNESS),
        new ItemStack(ModItems.pereskia),
        new ItemStack(Items.CLOCK)
    );
    setCastSound(ModSounds.Spells.TIME_STOP); // DONE, TY KITZAH
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    if (!player.world.isRemote) {
      int dur = duration;
      if (info.has(LONGER)) {
        dur += overtime;
      }
      if (info.has(SHORTER)) {
        dur = undertime;
      }
      EntityTimeStop timeStop = new EntityTimeStop(player.world, dur);
      timeStop.setPlayer(player.getUniqueID());
      timeStop.setPosition(player.posX, player.posY, player.posZ);
      timeStop.setModifiers(info);
      player.world.spawnEntity(timeStop);
      PacketHandler.sendToAllTracking(new MessageTimeStopStartFX(player.posX, player.posY + 1.0f, player.posZ), player);
      if (info.has(SPEED)) {
        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, speed_duration, speed_amplifier));
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.duration = properties.get(PROP_DURATION);
    this.slow_duration = properties.get(PROP_SLOWNESS_DURATION);
    this.slow_amplifier = properties.get(PROP_SLOWNESS_AMPLIFIER);
    this.overtime = properties.get(PROP_OVERTIME);
    this.weakness_amplifier = properties.get(PROP_WEAKNESS_AMPLIFIER);
    this.weakness_duration = properties.get(PROP_WEAKNESS_DURATION);
    this.slow_amplifier = properties.get(PROP_SLOWNESS_AMPLIFIER);
    this.slow_duration = properties.get(PROP_SLOWNESS_DURATION);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
    this.undertime = properties.get(PROP_UNDERTIME);
    this.speed_duration = properties.get(PROP_SPEED_DURATION);
    this.speed_amplifier = properties.get(PROP_SPEED_AMPLIFIER);
  }
}
