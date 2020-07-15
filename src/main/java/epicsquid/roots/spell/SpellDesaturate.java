package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageDesaturationFX;
import epicsquid.roots.properties.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class SpellDesaturate extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(500);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("spirit_herb", 0.7));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("terra_moss", 0.5));
  public static Property<Double> PROP_MULTIPLIER = new Property<>("multiplier", 0.5).setDescription("amount of health points restored by each food point");

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "amplified_saturation"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier WILDEWHEET = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_heal"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_growth"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_absorb"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_damage"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier CLOUD_BERRY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_levitation"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier INFERNAL_BULB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_fire_resistance"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_stone_skin"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "excess_breath"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_desaturate");
  public static SpellDesaturate instance = new SpellDesaturate(spellName);

  private double multiplier;

  public SpellDesaturate(ResourceLocation name) {
    super(name, TextFormatting.DARK_PURPLE, 184F / 255F, 232F / 255F, 42F / 255F, 109F / 255F, 32F / 255F, 168F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_MULTIPLIER);
    acceptsModifiers(PERESKIA, WILDEWHEET, WILDROOT, MOONGLOW_LEAF, BAFFLE_CAP, CLOUD_BERRY, INFERNAL_BULB, STALICRIPE, DEWGONIA);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.BOWL),
        new ItemStack(ModItems.fey_leather),
        new ItemStack(Items.ROTTEN_FLESH),
        new OreIngredient("bone"),
        new ItemStack(ModItems.spirit_herb)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
    if (!caster.shouldHeal()) {
      return false;
    }
    FoodStats stats = caster.getFoodStats();
    int food = stats.getFoodLevel();
    if (food <= 1) {
      return false;
    }
    float required = (float) ((caster.getMaxHealth() - caster.getHealth()) / (float) multiplier - multiplier * amplifier);
    float healed = 0;

    for (int i = 0; i <= required; i++) {
      if (food > 1) {
        food--;
        healed += 1 * multiplier - multiplier * amplifier;
      } else {
        break;
      }
    }

    World world = caster.world;

    if (!world.isRemote) {
      caster.heal(healed);
      stats.setFoodLevel(food);
      stats.foodSaturationLevel = Math.min(stats.foodSaturationLevel, food);

      ((EntityPlayerMP) caster).connection.sendPacket(new SPacketUpdateHealth(caster.getHealth(), stats.getFoodLevel(), stats.getSaturationLevel()));

      MessageDesaturationFX message = new MessageDesaturationFX(caster);
      PacketHandler.sendToAllTracking(message, caster);
    }

    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.multiplier = properties.get(PROP_MULTIPLIER);
  }
}
