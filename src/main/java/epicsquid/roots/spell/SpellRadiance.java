package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageRadianceBeamFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.recipe.ingredient.GoldOrSilverIngotIngredient;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.List;

public class SpellRadiance extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(10);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("cloud_berry", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("pereskia", 0.25));
  public static Property<Float> PROP_DISTANCE = new Property<>("distance", 32f).setDescription("maximum reach of radiance beam");
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(5f).setDescription("damage dealt each time by radiance beam");
  public static Property<Float> PROP_UNDEAD_DAMAGE = new Property<>("undead_damage", 3f).setDescription("damage dealt each time by radiance beam on undead mobs");
  public static Property<Integer> PROP_WITHER_DURATION = new Property<>("wither_duration", 5 * 20).setDescription("the duration of the wither effect to apply to affected entities");
  public static Property<Integer> PROP_GLOW_DURATION = new Property<>("glow_duration", 10 * 20).setDescription("the duration of the glow effect to apply to affected entities");
  public static Property<Integer> PROP_POISON_DURATION = new Property<>("poison_duration", 5 * 20).setDescription("the duration of the poison effect to apply to affected entities");
  public static Property<Integer> PROP_POISON_AMPLIFIER = new Property<>("poison_amplifier", 0).setDescription("the amplifier to use for the poison effect");
  public static Property<Integer> PROP_SLOW_DURATION = new Property<>("slow_duration", 5 * 20).setDescription("the duration of the slow effect to apply to affected entities");
  public static Property<Integer> PROP_SLOW_AMPLIFIER = new Property<>("slow_amplifier", 0).setDescription("the amplifier to use for the slow effect");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 5 * 20).setDescription("the duration of the fire effect to apply to affected entities");
  public static Property<Float> PROP_WIDTH = new Property<>("width", 0.1f).setDescription("default width of the radiance beam");
  public static Property<Float> PROP_ADDED_WIDTH = new Property<>("added_width", 0.3f).setDescription("width added to the default radiance beam");
  public static Property<Float> PROP_HEALING = new Property<>("healing", 2f).setDescription("the amount of healing each strike of the beam should do");

  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_radiance"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier MAGNETISM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_radiance"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.375)));
  public static Modifier WITHER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "withering_ray"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.275)));
  public static Modifier GLOWING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "glowing_ray"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.125)));
  public static Modifier HEALING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "healing_ray"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.475)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sickly_ray"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.345)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "burning_ray"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.345)));
  public static Modifier BIGGER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "wider_ray"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.275)));
  public static Modifier SLOW = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slowing_ray"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.345)));

  static {
    HEALING.addConflicts(WITHER, PEACEFUL, POISON, FIRE, SLOW); // Can't heal and do those
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_radiance");
  public static SpellRadiance instance = new SpellRadiance(spellName);

  private int wither_duration, glow_duration, poison_duration, poison_amplifier, fire_duration, slowness_duration, slowness_amplifier;
  public float distance;
  private float damage, undeadDamage, width, added_width, healing;

  public SpellRadiance(ResourceLocation name) {
    super(name, TextFormatting.WHITE, 255f / 255f, 255f / 255f, 64f / 255f, 255f / 255f, 255f / 255f, 192f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DISTANCE, PROP_DAMAGE, PROP_UNDEAD_DAMAGE, PROP_WITHER_DURATION, PROP_POISON_AMPLIFIER, PROP_POISON_DURATION, PROP_SLOW_AMPLIFIER, PROP_SLOW_DURATION, PROP_FIRE_DURATION, PROP_GLOW_DURATION, PROP_WIDTH, PROP_ADDED_WIDTH, PROP_HEALING);
    acceptsModifiers(PEACEFUL, MAGNETISM, WITHER, GLOWING, HEALING, POISON, FIRE, BIGGER, SLOW);
  }

  @Override
  public void init() {
    addIngredients(
        new GoldOrSilverIngotIngredient(),
        new OreIngredient("torch"),
        new OreIngredient("dyeYellow"),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.pereskia)
    );
    setCastSound(ModSounds.Spells.RADIANCE);
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    if (!player.world.isRemote && player.ticksExisted % 2 == 0) {
      float w = width;
      if (info.has(BIGGER)) {
        w += added_width;
      }
      float distance = this.distance;
      RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().add(0, player.getEyeHeight(), 0), player.getPositionVector().add(0, player.getEyeHeight(), 0).add(player.getLookVec().scale(distance)), false, true, true);
      Vec3d direction = player.getLookVec();
      ArrayList<Vec3d> positions = new ArrayList<>();
      float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
      float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
      positions.add(new Vec3d(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ));
      if (result != null) {
        positions.add(result.hitVec);
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
          Vec3i hitSide = result.sideHit.getDirectionVec();
          float xCoeff = 1f;
          if (hitSide.getX() != 0) {
            xCoeff = -1f;
          }
          float yCoeff = 1f;
          if (hitSide.getY() != 0) {
            yCoeff = -1f;
          }
          float zCoeff = 1f;
          if (hitSide.getZ() != 0) {
            zCoeff = -1f;
          }
          direction = new Vec3d(direction.x * xCoeff, direction.y * yCoeff, direction.z * zCoeff);
          distance -= result.hitVec.subtract(player.getPositionVector()).length();
          if (distance > 0) {
            RayTraceResult result2 = player.world.rayTraceBlocks(result.hitVec, result.hitVec.add(direction.scale(distance)));
            if (result2 != null) {
              positions.add(result2.hitVec);
              if (result2.typeOfHit == RayTraceResult.Type.BLOCK) {
                hitSide = result2.sideHit.getDirectionVec();
                xCoeff = 1f;
                if (hitSide.getX() != 0) {
                  xCoeff = -1f;
                }
                yCoeff = 1f;
                if (hitSide.getY() != 0) {
                  yCoeff = -1f;
                }
                zCoeff = 1f;
                if (hitSide.getZ() != 0) {
                  zCoeff = -1f;
                }
                direction = new Vec3d(direction.x * xCoeff, direction.y * yCoeff, direction.z * zCoeff);
                distance -= result2.hitVec.subtract(player.getPositionVector()).length();
                if (distance > 0) {
                  RayTraceResult result3 = player.world.rayTraceBlocks(result2.hitVec, result2.hitVec.add(direction.scale(distance)));
                  if (result3 != null) {
                    positions.add(result3.hitVec);
                  } else {
                    positions.add(result2.hitVec.add(direction.scale(distance)));
                  }
                }
              }
            } else {
              positions.add(result.hitVec.add(direction.scale(distance)));
            }
          }
        }
      } else {
        positions.add(player.getPositionVector().add(0, player.getEyeHeight(), 0).add(player.getLookVec().scale(distance)));
      }
      int count = 0;
      if (positions.size() > 1) {
        for (int i = 0; i < positions.size() - 1; i++) {
          double bx = Math.abs(positions.get(i + 1).x - positions.get(i).x) * w;
          double by = Math.abs(positions.get(i + 1).y - positions.get(i).y) * w;
          double bz = Math.abs(positions.get(i + 1).z - positions.get(i).z) * w;
          for (float j = 0; j < 1; j += 0.1f) {
            double x = positions.get(i).x * (1.0f - j) + positions.get(i + 1).x * j;
            double y = positions.get(i).y * (1.0f - j) + positions.get(i + 1).y * j;
            double z = positions.get(i).z * (1.0f - j) + positions.get(i + 1).z * j;
            List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - bx, y - by, z - bz, x + bx, y + by, z + bz));
            for (EntityLivingBase e : entities) {
              if (e != player && !(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
                if (info.has(HEALING)) {
                  if (!EntityUtil.isFriendly(e, SpellRadiance.instance)) {
                    continue;
                  }

                  e.heal(healing);
                  count++;
                } else {
                  if (info.has(PEACEFUL) && EntityUtil.isFriendly(e, SpellRadiance.instance)) {
                    continue;
                  }
                  if (e.hurtTime <= 0 && !e.isDead) {
                    // TODO: NOT THE RIGHT WAY TO DO THIS
                    if (info.has(MAGNETISM)) {
                      e.getEntityData().setUniqueId("magnetic", player.getUniqueID());
                      e.getEntityData().setInteger("magnetic_ticks", e.ticksExisted);
                    }
                    e.attackEntityFrom(ModDamage.radiantDamageFrom(player), damage);
                    if (e.isEntityUndead()) {
                      e.attackEntityFrom(ModDamage.radiantDamageFrom(player), undeadDamage);
                    }
                    e.setRevengeTarget(player);
                    player.setLastAttackedEntity(e);
                    if (info.has(WITHER)) {
                      e.addPotionEffect(new PotionEffect(MobEffects.WITHER, wither_duration));
                    }
                    if (info.has(GLOWING)) {
                      e.addPotionEffect(new PotionEffect(MobEffects.GLOWING, glow_duration));
                    }
                    if (info.has(POISON)) {
                      e.addPotionEffect(new PotionEffect(MobEffects.POISON, poison_duration, poison_amplifier));
                    }
                    if (info.has(FIRE)) {
                      e.setFire(fire_duration);
                    }
                    if (info.has(SLOW)) {
                      e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, slowness_duration, slowness_amplifier));
                    }

                    count++;
                  }
                }
              }
            }
          }
        }
      }
      PacketHandler.sendToAllTracking(new MessageRadianceBeamFX(player.getUniqueID(), player.posX, player.posY + 1.0f, player.posZ, count > 0), player);
      return count > 0;
    }
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.distance = properties.get(PROP_DISTANCE);
    this.damage = properties.get(PROP_DAMAGE);
    this.undeadDamage = properties.get(PROP_UNDEAD_DAMAGE);
    this.wither_duration = properties.get(PROP_WITHER_DURATION);
    this.glow_duration = properties.get(PROP_GLOW_DURATION);
    this.poison_duration = properties.get(PROP_POISON_DURATION);
    this.poison_amplifier = properties.get(PROP_POISON_AMPLIFIER);
    this.fire_duration = properties.get(PROP_FIRE_DURATION);
    this.slowness_duration = properties.get(PROP_SLOW_DURATION);
    this.slowness_amplifier = properties.get(PROP_SLOW_AMPLIFIER);
    this.width = properties.get(PROP_WIDTH);
    this.added_width = properties.get(PROP_ADDED_WIDTH);
    this.healing = properties.get(PROP_HEALING);
  }
}
