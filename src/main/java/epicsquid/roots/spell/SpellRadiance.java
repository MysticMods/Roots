package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageRadianceBeamFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.recipe.ingredient.GoldOrSilverIngotIngredient;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("cloud_berry", 0.5));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(1, new SpellCost("pereskia", 0.25));
  public static Property<Float> PROP_DISTANCE = new Property<>("distance", 32f).setDescription("maximum reach of radiance beam");
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(5f).setDescription("damage dealt each time by radiance beam");
  public static Property<Float> PROP_UNDEAD_DAMAGE = new Property<>("undead_damage", 3f).setDescription("damage dealt each time by radiance beam on undead mobs");

  public static Modifier PEACEFUL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "peaceful_radiance"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_radiance"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "withering_ray"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "glowing_ray"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier TERRA_MOSS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "healing_ray"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sickly_ray"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "burning_ray"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "wider_ray"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slowing_ray"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    TERRA_MOSS.addConflicts(MOONGLOW_LEAF, PEACEFUL, BAFFLE_CAP, FIRE, DEWGONIA); // Can't heal and do those
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_radiance");
  public static SpellRadiance instance = new SpellRadiance(spellName);

  private float distance;
  private float damage;
  private float undeadDamage;

  public SpellRadiance(ResourceLocation name) {
    super(name, TextFormatting.WHITE, 255f / 255f, 255f / 255f, 64f / 255f, 255f / 255f, 255f / 255f, 192f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_COST_2, PROP_DISTANCE, PROP_DAMAGE, PROP_UNDEAD_DAMAGE);
    acceptsModifiers(PEACEFUL, WILDROOT, MOONGLOW_LEAF, SPIRIT_HERB, TERRA_MOSS, BAFFLE_CAP, FIRE, STALICRIPE, DEWGONIA);
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
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks) {
    if (!player.world.isRemote && player.ticksExisted % 2 == 0) {
      float distance = 32;
      RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().add(0, player.getEyeHeight(), 0), player.getPositionVector().add(0, player.getEyeHeight(), 0).add(player.getLookVec().scale(distance)), false, true, true);
      Vec3d direction = player.getLookVec();
      ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
      float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
      float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
      positions.add(new Vec3d(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ));
      PacketHandler.sendToAllTracking(new MessageRadianceBeamFX(player.getUniqueID(), player.posX, player.posY + 1.0f, player.posZ), player);
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
          double bx = Math.abs(positions.get(i + 1).x - positions.get(i).x) * 0.1f;
          double by = Math.abs(positions.get(i + 1).y - positions.get(i).y) * 0.1f;
          double bz = Math.abs(positions.get(i + 1).z - positions.get(i).z) * 0.1f;
          for (float j = 0; j < 1; j += 0.1f) {
            double x = positions.get(i).x * (1.0f - j) + positions.get(i + 1).x * j;
            double y = positions.get(i).y * (1.0f - j) + positions.get(i + 1).y * j;
            double z = positions.get(i).z * (1.0f - j) + positions.get(i + 1).z * j;
            List<EntityLivingBase> entities = player.world
                .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - bx, y - by, z - bz, x + bx, y + by, z + bz));
            for (EntityLivingBase e : entities) {
              if (e != player && !(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
                if (has(PEACEFUL) && EntityUtil.isFriendly(e)) {
                  continue;
                }
                if (e.hurtTime <= 0 && !e.isDead) {
                  e.attackEntityFrom(ModDamage.radiantDamageFrom(player), ampFloat(damage));
                  if (e.isEntityUndead()) {
                    e.attackEntityFrom(ModDamage.radiantDamageFrom(player), ampFloat(undeadDamage));
                  }
                  e.setRevengeTarget(player);
                  e.setLastAttackedEntity(player);
                  count++;
                }
              }
            }
          }
        }
      }
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
  }
}
