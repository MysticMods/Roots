package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageDandelionCastFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellDandelionWinds extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("cloud_berry", 0.125));
  public static Property<Float> PROP_DISTANCE = new Property<>("distance", 0.75f).setDescription("the vertical component of the vector that determines the direction of the entity");
  public static Property<Double> PROP_RANGE_1 = new Property<>("range_1", 6.0).setDescription("the first range increment for calculating the bounding box");
  public static Property<Double> PROP_RANGE_2 = new Property<>("range_2", 4.0).setDescription("the first range increment for calculating the bounding box");
  public static Property<Float> PROP_ADDITIONAL_DISTANCE = new Property<>("additional_distance", 0.25f).setDescription("the additional vertical component of the vector that determines the direction of the entity");
  public static Property<Float> PROP_ADDITIONAL_FALL = new Property<>("additional_fall", 1.75f).setDescription("the fall distance modifier");
  public static Property<Integer> PROP_SLOW_DURATION = new Property<>("slow_duration", 60).setDescription("the duration of the slow fall effect");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("posion_duration", 5 * 20).setDescription("the duration of the poison effect to apply");
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the amplifier to apply to the poison effect");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 4).setDescription("the duration (in seconds) that entities should be set on fire for");

  public static Modifier STRONGER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "strong_gusts"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.45)));
  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_winds"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier GROUNDED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "grounded_wind"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.345)));
  public static Modifier SLOW_FALL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slow_falling"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.125)));
  public static Modifier CIRCLE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "circle_of_winds"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.475)));
  public static Modifier ITEMS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "item_updraft"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.125)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poisonous_breeze"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.375)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "flaming_wind"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.275)));
  public static Modifier SUCTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "suction"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.375)));
  public static Modifier EXTINGUISH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "extinguisher"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.275)));

  // Conflicts
  static {
    // Grounded Wind <-> Slow Falling
    GROUNDED.addConflict(SLOW_FALL);
    // Circle of Winds <-> Billowing Sails
    CIRCLE.addConflict(EXTINGUISH);
    EXTINGUISH.addConflict(FIRE);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_dandelion_winds");
  public static SpellDandelionWinds instance = new SpellDandelionWinds(spellName);

  private int slow_duration, poison_amplifier, poison_duration, fire_duration;
  private double r1, r2;
  private float distance, additional_distance, additional_fall;

  public SpellDandelionWinds(ResourceLocation name) {
    super(name, TextFormatting.YELLOW, 255f / 255f, 255f / 255f, 32f / 255f, 255f / 255f, 176f / 255f, 32f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DISTANCE, PROP_RANGE_1, PROP_RANGE_2, PROP_ADDITIONAL_DISTANCE, PROP_ADDITIONAL_DISTANCE, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION);
    acceptsModifiers(STRONGER, PEACEFUL, GROUNDED, SLOW_FALL, CIRCLE, ITEMS, POISON, FIRE, SUCTION, EXTINGUISH);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Blocks.YELLOW_FLOWER),
        new OreIngredient("treeLeaves"),
        new ItemStack(ModItems.runic_dust),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.terra_spores)
    );
    setCastSound(ModSounds.Spells.DANDELION_WINDS);
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    Vec3d lookVec = player.getLookVec();
    float d = info.has(STRONGER) ? distance + additional_distance : distance;
    float motion = d * d + d;
    Vec3d playVec = player.getPositionVector();

    int count = 0;
    int extinguish = 0;

    if (!info.has(CIRCLE)) {
      AxisAlignedBB bounding = new AxisAlignedBB(player.posX + lookVec.x * r1 - r1, player.posY + lookVec.y * r1 - r1, player.posZ + lookVec.z * r1 - r2, player.posX + lookVec.x * r1 + r1, player.posY + lookVec.y * r1 + r1, player.posZ + lookVec.z * r1 + r1);
      List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, bounding);
      count = entities.size() - 1;
      if (!player.world.isRemote) {
        entities.forEach(o -> {
          if (o != player) {
            BlockPos pos = o.getPosition();
            Vec3d vec = lookVec;
            if (info.has(SUCTION)) {
              vec = playVec.subtract(o.getPositionVector()).normalize();
            }
            flingLivingEntity(o, vec, motion, info);
            if (info.has(FIRE)) {
              trySetFire(player.world, pos);
            }
          }
        });
      }

      if (info.has(ITEMS)) {
        List<EntityItem> items = player.world.getEntitiesWithinAABB(EntityItem.class, bounding);
        count += items.size();
        if (!player.world.isRemote) {
          items.forEach(o -> {
            BlockPos pos = o.getPosition();
            Vec3d vec = lookVec;
            if (info.has(SUCTION)) {
              vec = playVec.subtract(o.getPositionVector()).normalize();
            }
            flingEntity(o, vec, motion, info);
            if (info.has(FIRE)) {
              trySetFire(player.world, pos);
            }
          });
        }
      }
      if (info.has(EXTINGUISH)) {
        for (BlockPos pos : AABBUtil.unique(bounding)) {
          IBlockState state = player.world.getBlockState(pos);
          if (state.getBlock() == Blocks.FIRE) {
            extinguish++;
            if (!player.world.isRemote) {
              player.world.setBlockToAir(pos);
            }
          }
        }
      }
    } else {
      double val = (r1 + r2) / 2;
      AxisAlignedBB bounding = new AxisAlignedBB(val, val, val, -(val) - 0.5, -(val) - 0.5, -(val) - 0.5).offset(player.getPosition());
      List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, bounding);
      if (!player.world.isRemote) {
        entities.forEach(o -> {
          if (o != player) {
            BlockPos pos = o.getPosition();
            Vec3d vec;
            if (info.has(SUCTION)) {
              vec = playVec.subtract(o.getPositionVector()).normalize();
            } else {
              vec = o.getPositionVector().subtract(playVec).normalize();
            }
            flingLivingEntity(o, vec, motion, info);
            if (info.has(FIRE)) {
              trySetFire(player.world, pos);
            }
          }
        });
        if (info.has(ITEMS)) {
          List<EntityItem> items = player.world.getEntitiesWithinAABB(EntityItem.class, bounding);
          count += items.size();
          if (!player.world.isRemote) {
            items.forEach(o -> {
              BlockPos pos = o.getPosition();
              Vec3d vec;
              if (info.has(SUCTION)) {
                vec = playVec.subtract(o.getPositionVector()).normalize();
              } else {
                vec = o.getPositionVector().subtract(playVec).normalize();
              }
              flingEntity(o, vec, motion, info);
              if (info.has(FIRE)) {
                trySetFire(player.world, pos);
              }
            });
          }
        }
      }
      if (info.has(EXTINGUISH)) {
        for (BlockPos pos : AABBUtil.unique(bounding)) {
          IBlockState state = player.world.getBlockState(pos);
          if (state.getBlock() == Blocks.FIRE) {
            extinguish++;
            if (!player.world.isRemote) {
              player.world.setBlockToAir(pos);
            }
          }
        }
      }
    }

    if (count > 0 || extinguish > 0) {
      if (!player.world.isRemote) {
        PacketHandler.sendToAllTracking(new MessageDandelionCastFX(player.getUniqueID(), player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
      }
      return true;
    } else {
      return false;
    }
  }

  private void trySetFire(World world, BlockPos pos) {
    if (world.isRemote) {
      return;
    }

    if (world.isAirBlock(pos) && Blocks.FIRE.canPlaceBlockAt(world, pos)) {
      world.setBlockState(pos, Blocks.FIRE.getDefaultState());
    }
  }

  private void flingEntity(Entity e, Vec3d lookVec, double motion, StaffModifierInstanceList info) {
    e.motionX += lookVec.x;
    // TODO: Improve suction
    e.motionY += (motion * 0.7);
    e.motionZ += lookVec.z;
    if (info.has(GROUNDED)) {
      e.fallDistance *= additional_fall;
    }
    e.velocityChanged = true;
  }

  private void flingLivingEntity(EntityLivingBase e, Vec3d lookVec, double motion, StaffModifierInstanceList info) {
    if (info.has(PEACEFUL) && EntityUtil.isFriendly(e, SpellDandelionWinds.instance)) {
      return;
    }
    flingEntity(e, lookVec, motion, info);
    if (info.has(SLOW_FALL)) {
      e.addPotionEffect(new PotionEffect(ModPotions.slow_fall, slow_duration, 0, false, false));
    }
    if (info.has(POISON)) {
      e.addPotionEffect(new PotionEffect(MobEffects.POISON, poison_duration, poison_amplifier));
    }
    if (info.has(FIRE)) {
      e.setFire(fire_duration);
    }
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.distance = properties.get(PROP_DISTANCE);
    this.additional_distance = properties.get(PROP_ADDITIONAL_DISTANCE);
    this.additional_fall = properties.get(PROP_ADDITIONAL_FALL);
    this.slow_duration = properties.get(PROP_SLOW_DURATION);
    this.poison_amplifier = properties.get(PROP_POISON_AMPLIFIER);
    this.poison_duration = properties.get(PROP_POISON_DURATION);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
    this.r1 = properties.get(PROP_RANGE_1);
    this.r2 = properties.get(PROP_RANGE_2);
  }
}
