package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
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

import java.util.Arrays;
import java.util.List;

public class SpellDisarm extends SpellBase {

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(350);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("moonglow_leaf", 0.425));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("wildewheet", 0.125));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 5).setDescription("radius on the X axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("radius on the Y axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 5).setDescription("radius on the Z axis within which entities are affected by the spell");
  public static Property<Float> PROP_DROP_CHANCE = new Property<>("drop_chance", 0.35f).setDescription("percentage chance for disarmed mobs to actually drop their weapons (default 0.35 = 35%)");
  public static Property<Float> PROP_CHANCE_INCREASE = new Property<>("chance_increase", 0.35f).setDescription("percentage chance to be applied when increased drop chance is active (default 0.35 = 35%)");
  public static Property<Float> PROP_ARMOR = new Property<>("armor_chance", 0.30f).setDescription("percent chance when armor is enabled to drop armor (percent doubled when both applied)");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("posion_duration", 120).setDescription("duration of the poison effect when applied");
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the amplifier to be applied to the poison effect");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4).setDescription("duration that disarmed creatures should be set on fire for in seconds");
  public static Property<Float> PROP_KNOCKBACK = new Property<>("knockback_strength", 0.5f).setDescription("the strength of the knockback effect");
  public static Property<Integer> PROP_WEAKNESS_DURATION = new Property<>("weakness_duration", 4 * 20).setDescription("how long enemies should be weakened in place for");
  public static Property<Integer> PROP_WEAKNESS_AMPLIFIER = new Property<>("weakness_amplifier", 0).setDescription("the amplifier to be applied to the weakness effect");

  public static Modifier DROP_CHANCE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "boost_drops"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.45)));
  public static Modifier ARMOR1 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "armor_i"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.45)));
  public static Modifier DUO = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "disarm_duumvirate"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.45)));
  public static Modifier ARMOR2 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "armor_ii"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.45)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poison_hands"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.45)));
  public static Modifier FLOWERS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "flower_child"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.125)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "blaze"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.275)));
  public static Modifier WEAKNESS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "weakness"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.375)));
  public static Modifier KNOCKBACK = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "liquid_lurch"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.45)));

  static {
    // Conflicts
    // Poison Hands < -> Flower Child
    POISON.addConflict(FLOWERS);
  }

  public static List<EntityEquipmentSlot> HANDS;
  public static List<EntityEquipmentSlot> ARMOR;

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_disarm");
  public static SpellDisarm instance = new SpellDisarm(spellName);

  private int radius_x;
  private int radius_y;
  private int radius_z;
  private int poison_duration;
  private int poison_amplifier;
  private int fire_duration;
  private int weakness_duration;
  private int weakness_amplifier;
  private float drop_chance, armor_chance, chance_increase, knockback;

  private SpellDisarm(ResourceLocation name) {
    super(name, TextFormatting.DARK_RED, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_CHANCE_INCREASE, PROP_ARMOR, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_FIRE_DURATION, PROP_KNOCKBACK, PROP_WEAKNESS_AMPLIFIER, PROP_WEAKNESS_DURATION);
    acceptsModifiers(DROP_CHANCE, ARMOR1, DUO, ARMOR2, POISON, FLOWERS, FIRE, WEAKNESS, KNOCKBACK);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(ModItems.moonglow_leaf),
        new OreIngredient("gemDiamond"),
        new OreIngredient("bone"),
        new ItemStack(Item.getItemFromBlock(Blocks.TRIPWIRE_HOOK)),
        new ItemStack(ModItems.moonglow_leaf)
    );
    setCastSound(ModSounds.Spells.DISARM);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
    if (ARMOR == null) {
      ARMOR = Arrays.asList(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET);
    }
    if (HANDS == null) {
      HANDS = Arrays.asList(EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND);
    }
    BlockPos playerPos = caster.getPosition();
    World world = caster.world;

    List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(playerPos.getX() - radius_x, playerPos.getY() - radius_y, playerPos.getZ() - radius_z, playerPos.getX() + radius_x, playerPos.getY() + radius_y, playerPos.getZ() + radius_z));
    entities.remove(caster);

    if (entities.isEmpty()) {
      return false;
    }

    int count = 0;

    for (EntityLivingBase entity : entities) {
      if (EntityUtil.isHostile(entity, SpellDisarm.instance)) {
        boolean disarmed = false;
        for (EntityEquipmentSlot handSlot : HANDS) {
          ItemStack stack = entity.getItemStackFromSlot(handSlot);
          if (stack.isEmpty() || stack.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER)) {
            continue;
          }
          disarmed = true;
          if (!world.isRemote) {
            if (info.has(FLOWERS)) {
              entity.setItemStackToSlot(handSlot, new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER)));
            } else {
              entity.setItemStackToSlot(handSlot, ItemStack.EMPTY);
            }
            if (Util.rand.nextFloat() < drop_chance + chance_increase) {
              ItemUtil.spawnItem(world, entity.getPosition(), stack);
            }
          }
        }

        float armorChance = info.has(ARMOR1) ? armor_chance : 0;
        if (info.has(ARMOR2)) {
          armorChance += armor_chance;
        }
        for (EntityEquipmentSlot slot : ARMOR) {
          ItemStack stack = entity.getItemStackFromSlot(slot);
          if (stack.isEmpty()) {
            continue;
          }
          disarmed = true;
          if (!world.isRemote) {
            entity.setItemStackToSlot(slot, ItemStack.EMPTY);
            if (Util.rand.nextFloat() < armorChance) {
              ItemUtil.spawnItem(world, entity.getPosition(), stack);
            }
          }
        }

        if (disarmed) {
          if (!world.isRemote) {
            if (info.has(POISON)) {
              entity.addPotionEffect(new PotionEffect(MobEffects.POISON, poison_duration, poison_amplifier));
            }
            if (info.has(FIRE)) {
              entity.setFire(fire_duration);
            }
            if (info.has(WEAKNESS)) {
              entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, weakness_duration, weakness_amplifier));
            }
            if (info.has(KNOCKBACK)) {
              entity.knockBack(caster, knockback, caster.posX - entity.posX, caster.posZ - entity.posZ);
            }

            PacketHandler.sendToAllTracking(new MessageDisarmFX(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ()), caster);
          }
          if (info.has(DUO) && count < 2) {
            count++;
          } else {
            return true;
          }
        }
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
    this.weakness_duration = properties.get(PROP_WEAKNESS_DURATION);
    this.weakness_amplifier = properties.get(PROP_WEAKNESS_AMPLIFIER);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
  }
}
