package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageDisarmFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellDisarm extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(350);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("moonglow_leaf", 1.0));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 2).setDescription("radius on the X axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 2).setDescription("radius on the Y axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 2).setDescription("radius on the Z axis within which entities are affected by the spell");
  public static Property<Float> PROP_DROP_CHANCE = new Property<>("drop_chance", 0.35f).setDescription("percentage chance for disarmed mobs to actually drop their weapons (default 0.35 = 35%)");
  public static Property<Integer> PROP_REARM_DURATION = new Property<>("rearm_duration", 25).setDescription("the duration of strength applied to peaceful creatures when cows with guns is enabled");
  public static Property<Float> PROP_CHANCE_INCREASE = new Property<>("chance_increase", 0.35f).setDescription("percentage chance to be applied when increased drop chance is active (default 0.35 = 35%)");
  public static Property<Float> PROP_ARMOR = new Property<>("armor_chance", 0.30f).setDescription("percent chance when armor is enabled to drop armor (percent doubled when both applied)");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("posion_duration", 120).setDescription("duration of the poison effect when applied");
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the amplifier to be applied to the poison effect");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4).setDescription("duration that disarmed creatures should be set on fire for in seconds");
  public static Property<Float> PROP_KNOCKBACK = new Property<>("knockback_strength", 0.5f).setDescription("the strength of the knockback effect");
  public static Property<Integer> PROP_PARALYSIS_DURATION = new Property<>("paralysis_duration", 120).setDescription("how long a creature should be paralysed for");

  public static Modifier DROP_CHANCE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "boost_drops"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "cows_with_guns"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier ARMOR1 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "armor_i"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier DUO = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "disarm_duumvirate"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier ARMOR2 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "armor_ii"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poison_hands"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier FLOWERS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "flower_child"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "blaze"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier PARALYSIS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "paralysis"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier KNOCKBACK = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "liquid_lurch"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    // Conflicts
    // Poison Hands < -> Flower Child
    POISON.addConflict(FLOWERS);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_disarm");
  public static SpellDisarm instance = new SpellDisarm(spellName);

  private int radius_x, radius_y, radius_z, rearm_duration, poison_duration, poison_amplifier, fire_duration, paralysis_duration;
  private float drop_chance, armor_chance, chance_increase, knockback;

  private SpellDisarm(ResourceLocation name) {
    super(name, TextFormatting.DARK_RED, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_REARM_DURATION, PROP_CHANCE_INCREASE, PROP_ARMOR, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_FIRE_DURATION, PROP_KNOCKBACK, PROP_PARALYSIS_DURATION);
    acceptsModifiers(DROP_CHANCE, PEACEFUL, ARMOR1, DUO, ARMOR2, POISON, FLOWERS, FIRE, PARALYSIS, KNOCKBACK);
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
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
    BlockPos playerPos = caster.getPosition();
    World world = caster.world;

    List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(playerPos.getX() - radius_x, playerPos.getY() - radius_y, playerPos.getZ() - radius_z, playerPos.getX() + radius_x, playerPos.getY() + radius_y, playerPos.getZ() + radius_z));
    entities.remove(caster);

    if (entities.isEmpty()) {
      return false;
    }

    for (EntityLivingBase entity : entities) {
      if (EntityUtil.isHostile(entity)) {
        boolean disarmed = false;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
          ItemStack stack = entity.getItemStackFromSlot(slot);
          if (stack.isEmpty()) {
            continue;
          }
          disarmed = true;
          if (!world.isRemote) {
            entity.setItemStackToSlot(slot, ItemStack.EMPTY);
            if (drop_chance == 1 || drop_chance > 1 && Util.rand.nextInt(ampSubInt(drop_chance)) == 0) {
              ItemUtil.spawnItem(world, entity.getPosition(), stack);
            }
          }
        }

        if (disarmed) {
          if (!world.isRemote) {
            PacketHandler.sendToAllTracking(new MessageDisarmFX(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ()), caster);
          }
          return true;
        }
      } else if (info.has(PEACEFUL) && EntityUtil.isFriendlyTo(entity, caster)) {
        entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, this.rearm_duration, 2));
      }
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
    this.chance_increase = properties.get(PROP_CHANCE_INCREASE);
    this.armor_chance = properties.get(PROP_ARMOR);
    this.knockback = properties.get(PROP_KNOCKBACK);
    this.poison_amplifier = properties.get(PROP_POISON_AMPLIFIER);
    this.poison_duration = properties.get(PROP_POISON_DURATION);
    this.paralysis_duration = properties.get(PROP_PARALYSIS_DURATION);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
    this.rearm_duration = properties.get(PROP_REARM_DURATION);
  }
}
