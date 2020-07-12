package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityTimeStop;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageTimeStopStartFX;
import epicsquid.roots.properties.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellTimeStop extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(320);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("pereskia", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("moonglow_leaf", 0.5));
  public static Property<Integer> PROP_DURATION = new Property<>("duration", 200).setDescription("the duration of the time stop effect on entities");

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_time_stop");
  public static SpellTimeStop instance = new SpellTimeStop(spellName);

  public static Modifier WILDEWHEET = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_passage"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "freed_familiars"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "overtime"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier TERRA_MOSS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "time_slip"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "draining_stop"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier CLOUD_BERRY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "hasty_escape"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier INFERNAL_BULB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "burn_in_hell"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "minor_halt"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "ice_age"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  public static int duration;

  static {
    // Conflicts
    STALICRIPE.addConflicts(SPIRIT_HERB);
  }

  public SpellTimeStop(ResourceLocation name) {
    super(name, TextFormatting.DARK_BLUE, 64f / 255f, 64f / 255f, 64f / 255f, 192f / 255f, 32f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DURATION);
    acceptsModifiers(WILDEWHEET, WILDROOT, SPIRIT_HERB, TERRA_MOSS, BAFFLE_CAP, CLOUD_BERRY, INFERNAL_BULB, STALICRIPE, DEWGONIA);
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

  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    if (!player.world.isRemote) {
      EntityTimeStop timeStop = new EntityTimeStop(player.world, (int) (duration + duration * amplifier));
      timeStop.setPlayer(player.getUniqueID());
      timeStop.setPosition(player.posX, player.posY, player.posZ);
      player.world.spawnEntity(timeStop);
      PacketHandler.sendToAllTracking(new MessageTimeStopStartFX(player.posX, player.posY + 1.0f, player.posZ), player);
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    duration = properties.get(PROP_DURATION);
  }
}
