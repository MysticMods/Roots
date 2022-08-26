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
  public static Property<Integer> PROP_DROP_CHANCE = new Property<>("drop_chance", 4).setDescription("chance for mobs to drop their equipment and weapons (the higher the number is the lower the chance is: 1/x) [default: 1/4]");
  public static Property<Integer> PROP_REARM_DURATION = new Property<>("rearm_duration", 25).setDescription("the duration of strength applied to peaceful creatures when cows with guns is enabled");

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

  private int radius_x, radius_y, radius_z, drop_chance, rearm_duration;

  private SpellDisarm(ResourceLocation name) {
    super(name, TextFormatting.DARK_RED, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_REARM_DURATION);
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
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList modifiers, int ticks) {
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
      } else if (has(PEACEFUL) && EntityUtil.isFriendlyTo(entity, caster)) {
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
    this.rearm_duration = properties.get(PROP_REARM_DURATION);
  }
}
