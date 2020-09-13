package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockWildFire;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageSenseFX;
import epicsquid.roots.network.fx.MessageSenseFX.SensePosition;
import epicsquid.roots.properties.Property;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpellExtension extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(350);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 1.0));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 50).setDescription("radius on the X axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 25).setDescription("radius on the Y axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 50).setDescription("radius on the Z axis within which entities are affected by the spell");
  public static Property<Integer> PROP_ANIMAL_DURATION = new Property<>("animal_glow_duration", 40 * 20).setDescription("the duration of the glow effect when applied to passive entities");
  public static Property<Integer> PROP_ENEMY_DURATION = new Property<>("enemy_glow_duration", 40 * 20).setDescription("the duration of the glow effect when applied to hostile entities");
  public static Property<Integer> PROP_NIGHT_VISION = new Property<>("night_vision", 40 * 20).setDescription("how long the danger sense effect is applied to the player");
  public static Property<Float> PROP_SUMMON_ANIMAL_CHANCE = new Property<>("animal_summon_chance", 0.15f).setDescription("the percentage chance per entity affected that they will be summoned to the player instead");
  public static Property<Float> PROP_SUMMON_ENEMY_CHANCE = new Property<>("summon_enemy_chance", 0.05f).setDescription("the percentage chance per entity affected that they will be summoned to the player instead");

  public static Modifier SUMMON_ANIMALS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "summon_animals"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier SENSE_ANIMALS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_animals"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier NONDETECTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "nondetection"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SUMMON_DANGER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "summon_hostiles"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier SENSE_DANGER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_hostiles"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier ATTRACTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "unhealthy_attraction"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier SENSE_CONTAINERS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_containers"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier SENSE_FIRE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_fire"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier SENSE_ORES = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_ores"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier SENSE_LIQUIDS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_liquids"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    NONDETECTION.addConflict(ATTRACTION); //
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "extension");
  public static SpellExtension instance = new SpellExtension(spellName);

  private AxisAlignedBB box;
  private int radius_x, radius_y, radius_z, animal_duration, enemy_duration, night_vision;
  public float summon_animal, summon_enemy;

  private SpellExtension(ResourceLocation name) {
    super(name, TextFormatting.WHITE, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_ANIMAL_DURATION, PROP_ENEMY_DURATION, PROP_NIGHT_VISION, PROP_SUMMON_ANIMAL_CHANCE, PROP_SUMMON_ENEMY_CHANCE);
    acceptsModifiers(SUMMON_ANIMALS, SENSE_ANIMALS, NONDETECTION, SUMMON_DANGER, SENSE_DANGER, ATTRACTION, SENSE_CONTAINERS, SENSE_FIRE, SENSE_ORES, SENSE_LIQUIDS);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("cropCarrot"),
        new ItemStack(Items.LEAD),
        new ItemStack(ModItems.glass_eye),
        new ItemStack(Items.COMPASS),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine)
    );
  }

  private IntArraySet ores = new IntArraySet();

  public enum SenseType {
    CONTAINER, FIRE, LIQUID, ORE;

    @Nullable
    public static SenseType fromOrdinal (int ordinal) {
      for (SenseType type : values()) {
        if (type.ordinal() == ordinal) {
          return type;
        }
      }

      return null;
    }
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
    caster.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, ampInt(night_vision), 0, false, false));
    if (info.has(SENSE_ANIMALS)) {
      caster.addPotionEffect(new PotionEffect(ModPotions.animal_sense, ampInt(animal_duration), 0, false, false));
      caster.getEntityData().setIntArray(getCachedName(), info.snapshot());
    }
    if (info.has(SENSE_DANGER)) {
      caster.addPotionEffect(new PotionEffect(ModPotions.danger_sense, ampInt(enemy_duration), 0, false, false));
      caster.getEntityData().setIntArray(getCachedName(), info.snapshot());
    }
    if (info.has(SENSE_CONTAINERS) || info.has(SENSE_FIRE) || info.has(SENSE_LIQUIDS) || info.has(SENSE_ORES)) {
      for (BlockPos.MutableBlockPos pos : AABBUtil.uniqueMutable(box.offset(caster.getPosition()))) {
        if (caster.world.isAirBlock(pos)) {
          continue;
        }
        List<SensePosition> positions = new ArrayList<>();
        IBlockState state = caster.world.getBlockState(pos);
        Block block = state.getBlock();
        if (info.has(SENSE_CONTAINERS)) {
          if (block instanceof BlockContainer) {
            positions.add(new SensePosition(SenseType.CONTAINER, pos.toImmutable(), caster.world.provider.getDimension()));
          }
        }
        if (info.has(SENSE_FIRE)) {
          if (block instanceof BlockFire) {
            positions.add(new SensePosition(SenseType.FIRE, pos.toImmutable(), caster.world.provider.getDimension()));
          }
        }
        if (info.has(SENSE_LIQUIDS)) {
          if (block instanceof BlockLiquid || block instanceof IFluidHandler) {
            positions.add(new SensePosition(SenseType.LIQUID, pos.toImmutable(), caster.world.provider.getDimension()));
          }
        }
        if (info.has(SENSE_ORES)) {
          ItemStack stack = ItemUtil.stackFromState(state);
          int[] ids = OreDictionary.getOreIDs(stack);
          boolean ore = false;
          for (int id : ids) {
            if (ores.contains(id)) {
              ore = true;
              break;
            }
            if (OreDictionary.getOreName(id).startsWith("ore")) {
              ores.add(id);
              ore = true;
              break;
            }
          }
          if (ore) {
            positions.add(new SensePosition(SenseType.ORE, pos.toImmutable(), caster.world.provider.getDimension()));
          }
        }
        if (!caster.world.isRemote && !positions.isEmpty()) {
          MessageSenseFX message = new MessageSenseFX(positions);
          PacketHandler.INSTANCE.sendToAllTracking(message, caster);
        }
      }
    }

    return true;
  }

  public int[] getRadius() {
    return new int[]{radius_x, radius_y, radius_z};
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.animal_duration = properties.get(PROP_ANIMAL_DURATION);
    this.enemy_duration = properties.get(PROP_ENEMY_DURATION);
    this.night_vision = properties.get(PROP_NIGHT_VISION);
    this.summon_animal = properties.get(PROP_SUMMON_ANIMAL_CHANCE);
    this.summon_enemy = properties.get(PROP_SUMMON_ENEMY_CHANCE);
    this.box = new AxisAlignedBB(-radius_x, -radius_y, -radius_z, (radius_x+1), (radius_y+1), (radius_z+1));
  }
}
