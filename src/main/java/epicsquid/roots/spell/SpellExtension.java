package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageSenseFX;
import epicsquid.roots.network.fx.MessageSenseFX.SensePosition;
import epicsquid.roots.properties.Property;
import it.unimi.dsi.fastutil.ints.Int2CharOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.*;

public class SpellExtension extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(350);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("wildroot", 1.0));
  public static Property<Integer> PROP_RADIUS_ANIMALS_X = new Property<>("radius_animals_x", 50).setDescription("radius on the X axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_ANIMALS_Y = new Property<>("radius_animals_y", 25).setDescription("radius on the Y axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_ANIMALS_Z = new Property<>("radius_animals_z", 50).setDescription("radius on the Z axis within which entities are affected by the spell");

  public static Property<Integer> PROP_RADIUS_HOSTILES_X = new Property<>("radius_hostiles_x", 50).setDescription("radius on the X axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_HOSTILES_Y = new Property<>("radius_hostiles_y", 25).setDescription("radius on the Y axis within which entities are affected by the spell");
  public static Property<Integer> PROP_RADIUS_HOSTILES_Z = new Property<>("radius_hostiles_z", 50).setDescription("radius on the Z axis within which entities are affected by the spell");

  public static Property<Integer> PROP_ANIMAL_DURATION = new Property<>("animal_glow_duration", 40 * 20).setDescription("the duration of the glow effect when applied to passive entities");
  public static Property<Integer> PROP_ENEMY_DURATION = new Property<>("enemy_glow_duration", 40 * 20).setDescription("the duration of the glow effect when applied to hostile entities");
  public static Property<Integer> PROP_NIGHT_VISION = new Property<>("night_vision", 40 * 20).setDescription("how long the danger sense effect is applied to the player");
  public static Property<Float> PROP_SUMMON_ANIMAL_CHANCE = new Property<>("animal_summon_chance", 0.15f).setDescription("the percentage chance per entity affected that they will be summoned to the player instead");
  public static Property<Float> PROP_SUMMON_ENEMY_CHANCE = new Property<>("summon_enemy_chance", 0.05f).setDescription("the percentage chance per entity affected that they will be summoned to the player instead");

  public static Property<Integer> PROP_RADIUS_ORE_X = new Property<>("radius_ore_x", 15).setDescription("radius on the X axis within which ores are searched for");
  public static Property<Integer> PROP_RADIUS_ORE_Y = new Property<>("radius_ore_y", 15).setDescription("radius on the Y axis within which ores are searched for");
  public static Property<Integer> PROP_RADIUS_ORE_Z = new Property<>("radius_ore_z", 15).setDescription("radius on the Z axis within which ores are searched for");

  public static Property<Integer> PROP_RADIUS_ORE_SPECIFIC_X = new Property<>("radius_ore_specific_x", 30).setDescription("radius on the X axis within which specific ores are searched for");
  public static Property<Integer> PROP_RADIUS_ORE_SPECIFIC_Y = new Property<>("radius_ore_specific_y", 30).setDescription("radius on the Y axis within which specific ores are searched for");
  public static Property<Integer> PROP_RADIUS_ORE_SPECIFIC_Z = new Property<>("radius_ore_specific_z", 30).setDescription("radius on the Z axis within which specific ores are searched for");

  public static Property<Integer> PROP_RADIUS_LIQUID_X = new Property<>("radius_liquid_x", 30).setDescription("radius on the X axis within which liquids are searched for");
  public static Property<Integer> PROP_RADIUS_LIQUID_Y = new Property<>("radius_liquid_y", 30).setDescription("radius on the Y axis within which liquids are searched for");
  public static Property<Integer> PROP_RADIUS_LIQUID_Z = new Property<>("radius_liquid_z", 30).setDescription("radius on the Z axis within which liquids are searched for");

  public static Property<Integer> PROP_RADIUS_CONTAINER_X = new Property<>("radius_container_x", 30).setDescription("radius on the X axis within which containers are searched for");
  public static Property<Integer> PROP_RADIUS_CONTAINER_Y = new Property<>("radius_container_y", 30).setDescription("radius on the Y axis within which containers are searched for");
  public static Property<Integer> PROP_RADIUS_CONTAINER_Z = new Property<>("radius_container_z", 30).setDescription("radius on the Z axis within which containers are searched for");

  public static Property<Integer> PROP_RADIUS_SPAWNER_X = new Property<>("radius_spawner_x", 30).setDescription("radius on the X axis within which spawners are searched for");
  public static Property<Integer> PROP_RADIUS_SPAWNER_Y = new Property<>("radius_spawner_y", 30).setDescription("radius on the Y axis within which spawners are searched for");
  public static Property<Integer> PROP_RADIUS_SPAWNER_Z = new Property<>("radius_spawner_z", 30).setDescription("radius on the Z axis within which spawners are searched for");

  public static Modifier SUMMON_ANIMALS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "summon_animals"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier SENSE_ANIMALS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_animals"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier NONDETECTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "nondetection"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SUMMON_DANGER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "summon_hostiles"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier SENSE_DANGER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_hostiles"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier ATTRACTION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "unhealthy_attraction"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier SENSE_CONTAINERS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_containers"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier SENSE_SPAWNERS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_spanwers"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier SENSE_ORES = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_ores"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier SENSE_LIQUIDS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "sense_liquids"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    NONDETECTION.addConflict(ATTRACTION); //
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_extension");
  public static SpellExtension instance = new SpellExtension(spellName);

  private AxisAlignedBB ore, ore_specific, liquid, container, spawner;
  private int radius_animals_x, radius_animals_y, radius_animals_z, animal_duration, enemy_duration, night_vision, radius_ore_x, radius_ore_y, radius_ore_z, radius_ore_specific_x, radius_ore_specific_y, radius_ore_specific_z, radius_liquid_x, radius_liquid_y, radius_liquid_z, radius_container_x, radius_container_y, radius_container_z, radius_spawner_x, radius_spawner_y, radius_spawner_z, radius_hostiles_x, radius_hostiles_y, radius_hostiles_z;
  public float summon_animal, summon_enemy;

  private SpellExtension(ResourceLocation name) {
    super(name, TextFormatting.WHITE, 122F / 255F, 0F, 0F, 58F / 255F, 58F / 255F, 58F / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_ANIMAL_DURATION, PROP_ENEMY_DURATION, PROP_NIGHT_VISION, PROP_SUMMON_ANIMAL_CHANCE, PROP_SUMMON_ENEMY_CHANCE, PROP_RADIUS_ORE_X, PROP_RADIUS_ORE_Y, PROP_RADIUS_ORE_Z, PROP_RADIUS_ORE_SPECIFIC_X, PROP_RADIUS_ORE_SPECIFIC_Y, PROP_RADIUS_ORE_SPECIFIC_Z, PROP_RADIUS_LIQUID_X, PROP_RADIUS_LIQUID_Y, PROP_RADIUS_LIQUID_Z, PROP_RADIUS_CONTAINER_X, PROP_RADIUS_CONTAINER_Y, PROP_RADIUS_CONTAINER_Z, PROP_RADIUS_SPAWNER_X, PROP_RADIUS_SPAWNER_Y, PROP_RADIUS_SPAWNER_Z, PROP_RADIUS_ANIMALS_X, PROP_RADIUS_ANIMALS_Y, PROP_RADIUS_ANIMALS_Z, PROP_RADIUS_HOSTILES_X, PROP_RADIUS_HOSTILES_Y, PROP_RADIUS_HOSTILES_Z);
    acceptsModifiers(SUMMON_ANIMALS, SENSE_ANIMALS, NONDETECTION, SUMMON_DANGER, SENSE_DANGER, ATTRACTION, SENSE_CONTAINERS, SENSE_SPAWNERS, SENSE_ORES, SENSE_LIQUIDS);
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
    CONTAINER, SPAWNER, LIQUID, ORE;

    @Nullable
    public static SenseType fromOrdinal(int ordinal) {
      for (SenseType type : values()) {
        if (type.ordinal() == ordinal) {
          return type;
        }
      }

      return null;
    }
  }

  private boolean isOre(ItemStack stack) {
    if (stack.isEmpty()) {
      return false;
    }
    int[] ids = OreDictionary.getOreIDs(stack);
    for (int id : ids) {
      if (ores.contains(id)) {
        return true;
      }
      if (OreDictionary.getOreName(id).startsWith("ore")) {
        ores.add(id);
        return true;
      }
    }

    return false;
  }

  @Nullable
  private IntArraySet getHeldIds(ItemStack stack) {
    if (stack.isEmpty()) {
      return null;
    }
    IntArraySet heldIds = new IntArraySet();
    int[] ids = OreDictionary.getOreIDs(stack);
    for (int id : ids) {
      if (ores.contains(id)) {
        heldIds.add(id);
        continue;
      }

      if (OreDictionary.getOreName(id).startsWith("ore")) {
        ores.add(id);
        heldIds.add(id);
      }
    }

    return heldIds;
  }

  private boolean oresMatch(ItemStack block, IntArraySet heldIds) {
    if (block.isEmpty()) {
      return false;
    }

    IntArraySet setBlock = new IntArraySet(OreDictionary.getOreIDs(block));
    setBlock.retainAll(heldIds);
    return !setBlock.isEmpty();
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
    boolean c = info.has(SENSE_CONTAINERS);
    boolean s = info.has(SENSE_SPAWNERS);
    boolean l = info.has(SENSE_LIQUIDS);
    boolean o = info.has(SENSE_ORES);
    IntArraySet held = getHeldIds(caster.getHeldItemOffhand());
    boolean specific = held != null && !held.isEmpty();
    if (c || s || l || o) {
      if (!caster.world.isRemote) {
        List<SensePosition> positions = new ArrayList<>();
        BlockPos playerPos = caster.getPosition();
        AxisAlignedBB ores = ore.offset(playerPos);
        AxisAlignedBB ores_specific = ore_specific.offset(playerPos);
        AxisAlignedBB liquids = liquid.offset(playerPos);
        AxisAlignedBB containers = container.offset(playerPos);
        AxisAlignedBB spawners = spawner.offset(playerPos);
        AxisAlignedBB offset = getBiggestBoundingBox(o, specific, l, c, s);
        if (offset == null) {
          return true;
        }
        AxisAlignedBB box = offset.offset(playerPos);
        for (BlockPos pos : AABBUtil.unique(box)) {
          if (!caster.world.isAreaLoaded(pos, 1, false)) {
            continue;
          }
          if (caster.world.isAirBlock(pos)) {
            continue;
          }
          Vec3d vec = new Vec3d(pos);
          IBlockState state = caster.world.getBlockState(pos);
          Block block = state.getBlock();
          if (c && containers.contains(vec)) {
            if (block instanceof BlockContainer) {
              positions.add(new SensePosition(SenseType.CONTAINER, pos.toImmutable(), caster.world.provider.getDimension()));
            }
          }
          if (s && spawners.contains(vec)) {
            if (block instanceof BlockMobSpawner) {
              positions.add(new SensePosition(SenseType.SPAWNER, pos.toImmutable(), caster.world.provider.getDimension()));
            }
          }
          if (l && liquids.contains(vec)) {
            if (block instanceof BlockLiquid || block instanceof IFluidHandler) {
              positions.add(new SensePosition(SenseType.LIQUID, pos.toImmutable(), caster.world.provider.getDimension()));
            }
          }
          if ((o && ores.contains(vec)) || (specific && ores_specific.contains(vec))) {
            ItemStack stack = ItemUtil.stackFromState(state);
            if (isOre(stack)) {
              if (specific) {
                if (oresMatch(stack, held)) {
                  positions.add(new SensePosition(SenseType.ORE, pos.toImmutable(), caster.world.provider.getDimension()));
                }
              } else {
                positions.add(new SensePosition(SenseType.ORE, pos.toImmutable(), caster.world.provider.getDimension()));
              }
            }
          }
        }
        if (!positions.isEmpty()) {
          MessageSenseFX message = new MessageSenseFX(positions);
          PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) caster);
        }
      }
    }

    return true;
  }

  public int[] getRadiusHostiles() {
    return new int[]{radius_hostiles_x, radius_hostiles_y, radius_hostiles_z};
  }

  public int[] getRadiusAnimals() {
    return new int[]{radius_animals_x, radius_animals_y, radius_animals_z};
  }

  private Map<SearchType, AxisAlignedBB> typeToBox = new HashMap<>();

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_hostiles_x = properties.get(PROP_RADIUS_HOSTILES_X);
    this.radius_hostiles_y = properties.get(PROP_RADIUS_HOSTILES_Y);
    this.radius_hostiles_z = properties.get(PROP_RADIUS_HOSTILES_Z);
    this.radius_animals_x = properties.get(PROP_RADIUS_ANIMALS_X);
    this.radius_animals_y = properties.get(PROP_RADIUS_ANIMALS_Y);
    this.radius_animals_z = properties.get(PROP_RADIUS_ANIMALS_Z);
    this.animal_duration = properties.get(PROP_ANIMAL_DURATION);
    this.enemy_duration = properties.get(PROP_ENEMY_DURATION);
    this.night_vision = properties.get(PROP_NIGHT_VISION);
    this.summon_animal = properties.get(PROP_SUMMON_ANIMAL_CHANCE);
    this.summon_enemy = properties.get(PROP_SUMMON_ENEMY_CHANCE);
    this.radius_ore_x = properties.get(PROP_RADIUS_ORE_X);
    this.radius_ore_y = properties.get(PROP_RADIUS_ORE_Y);
    this.radius_ore_z = properties.get(PROP_RADIUS_ORE_Z);
    this.radius_ore_specific_x = properties.get(PROP_RADIUS_ORE_SPECIFIC_X);
    this.radius_ore_specific_y = properties.get(PROP_RADIUS_ORE_SPECIFIC_Y);
    this.radius_ore_specific_z = properties.get(PROP_RADIUS_ORE_SPECIFIC_Z);
    this.radius_liquid_x = properties.get(PROP_RADIUS_LIQUID_X);
    this.radius_liquid_y = properties.get(PROP_RADIUS_LIQUID_Y);
    this.radius_liquid_z = properties.get(PROP_RADIUS_LIQUID_Z);
    this.radius_container_x = properties.get(PROP_RADIUS_CONTAINER_X);
    this.radius_container_y = properties.get(PROP_RADIUS_CONTAINER_Y);
    this.radius_container_z = properties.get(PROP_RADIUS_CONTAINER_Z);
    this.radius_spawner_x = properties.get(PROP_RADIUS_SPAWNER_X);
    this.radius_spawner_y = properties.get(PROP_RADIUS_SPAWNER_Y);
    this.radius_spawner_z = properties.get(PROP_RADIUS_SPAWNER_Z);
    // TODO: check all AxisAlignedBB for +1
    this.ore = new AxisAlignedBB(-radius_ore_x, -radius_ore_y, -radius_ore_z, (radius_ore_x + 1), (radius_ore_y + 1), (radius_ore_z + 1));
    this.ore_specific = new AxisAlignedBB(-radius_ore_specific_x, -radius_ore_specific_y, -radius_ore_specific_z, (radius_ore_specific_x + 1), (radius_ore_specific_y + 1), (radius_ore_specific_z + 1));
    this.liquid = new AxisAlignedBB(-radius_liquid_x, -radius_liquid_y, -radius_liquid_z, (radius_liquid_x + 1), (radius_liquid_y + 1), (radius_liquid_z + 1));
    this.container = new AxisAlignedBB(-radius_ore_x, -radius_ore_y, -radius_ore_z, (radius_ore_x + 1), (radius_ore_y + 1), (radius_ore_z + 1));
    this.spawner = new AxisAlignedBB(-radius_spawner_x, -radius_spawner_y, -radius_spawner_z, (radius_spawner_x + 1), (radius_spawner_y + 1), (radius_spawner_z + 1));

    typeToBox.put(SearchType.ORE, ore);
    typeToBox.put(SearchType.ORE_SPECIFIC, ore_specific);
    typeToBox.put(SearchType.CONTAINER, container);
    typeToBox.put(SearchType.LIQUID, liquid);
    typeToBox.put(SearchType.SPAWNER, spawner);
  }

  @Nullable
  public AxisAlignedBB getBiggestBoundingBox (boolean ore, boolean ore_specific, boolean liquid, boolean container, boolean spawner) {
    List<BoxSize> boxes = new ArrayList<>();
    if (ore) {
      boxes.add(new BoxSize(SearchType.ORE, radius_ore_x * radius_ore_y * radius_ore_z));
    }
    if (ore_specific) {
      boxes.add(new BoxSize(SearchType.ORE_SPECIFIC, radius_ore_specific_x * radius_ore_specific_y * radius_ore_specific_z));
    }
    if (liquid) {
      boxes.add(new BoxSize(SearchType.LIQUID, radius_liquid_x * radius_liquid_y * radius_liquid_z));
    }
    if (container) {
      boxes.add(new BoxSize(SearchType.CONTAINER, radius_container_x * radius_container_y * radius_container_z));
    }
    if (spawner) {
      boxes.add(new BoxSize(SearchType.SPAWNER, radius_spawner_x * radius_spawner_y * radius_spawner_z));
    }
    if (boxes.isEmpty()) {
      return null;
    }
    boxes.sort((o1, o2) -> Integer.compare(o2.size, o1.size));
    return typeToBox.get(boxes.get(0).type);
  }

  private enum SearchType {
    ORE, ORE_SPECIFIC, LIQUID, CONTAINER, SPAWNER
  }

  private class BoxSize {
    private SearchType type;
    private int size;

    private BoxSize(SearchType type, int size) {
      this.type = type;
      this.size = size;
    }
  }
}
