package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageFallBladesFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.util.OreDictCache;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.OreIngredient;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SpellNaturesScythe extends SpellBase {
  private static final MethodHandle GET_SILK_DROP;
  private static final ItemStack SHEARS = new ItemStack(Items.SHEARS);

  static {
    try {
      Method getSilkDrop = ObfuscationReflectionHelper.findMethod(Block.class, "func_180643_i", ItemStack.class, IBlockState.class);
      getSilkDrop.setAccessible(true);
      GET_SILK_DROP = MethodHandles.lookup().unreflect(getSilkDrop).asType(MethodType.methodType(ItemStack.class, Block.class, IBlockState.class));
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Unable to properly handle silk touch drops", e);
    }
  }

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(160);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.CONTINUOUS);
  public static Property.PropertyCost PROP_COST = new Property.PropertyCost(0, new SpellCost("stalicripe", 0.1));
  public static Property<Integer> PROP_RADIUS = new Property<>("radius", 12).setDescription("horizontal radius of the area in which the spell takes effect");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("radius on the Y axis of the area in which the spell takes effect");
  public static Property<Integer> PROP_INTERVAL = new Property<>("interval", 2).setDescription("interval in ticks between each harvested block segment");
  public static Property<Integer> PROP_MAX_AFFECTED = new Property<>("max_affected", 3).setDescription("maximum number of blocks affected each tick per enabled type");
  public static Property<String> PROP_TREE_DICT = new Property<>("tree_dictionary", "treeLeaves").setDescription("the ore dictionary entry that should be used to identify leaves");
  public static Property<String> PROP_WEB_DICT = new Property<>("web_dictionary", "webs").setDescription("the ore dictionary entry that should be used to identify cobwebs in addition to the standard vanilla block");
  public static Property<String> PROP_GRASS_DICT = new Property<>("grass_dictionary", "tallgrass").setDescription("the ore dictionary entry that should be used to identify grass in addition to the standard vanilla blocks");
  public static Property<String> PROP_MUSHROOM_DICT = new Property<>("mushroom_dictionary", "mushrooms").setDescription("the ore dictionary entry that should be used to identify mushroom blocks (not huge mushrooms) in addition to those that derive from the default mushroom");
  public static Property<String> PROP_FLOWER_DICT = new Property<>("flower_dictionary", "flowers").setDescription("the ore dictionary entry that should be used to identify flower blocks in addition to those that derive from the default flowers");
  public static Property<String> PROP_VINES_DICT = new Property<>("vine_dictionary", "vines").setDescription("the ore dictionary entry that should be used to identify vines in addition to those that by default derive from vanilla vines");

  public static Modifier WEBS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "dewebbing"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier LEAVES = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "leaf_harvester"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier MAGNETISM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_harvester"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier FORTUNE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fortunate_scythe"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier VOID = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "voiding_scythe"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier GRASS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "grass_harvester"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier MUSHROOM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "mushroom_harvester"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier FLOWER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "flower_harvester"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier SPEED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "blazing_speed"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier SILK_TOUCH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "silk_scythe"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_natures_scythe");
  public static SpellNaturesScythe instance = new SpellNaturesScythe(spellName);

  private int radius, radius_y, interval, max_affected;

  private String tree, web, grass, mushroom, flower, vines;

  public SpellNaturesScythe(ResourceLocation name) {
    super(name, TextFormatting.DARK_GREEN, 64 / 255F, 240 / 255F, 24 / 255F, 26 / 255F, 110 / 255F, 13 / 255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST, PROP_RADIUS, PROP_RADIUS_Y, PROP_INTERVAL, PROP_MAX_AFFECTED, PROP_TREE_DICT, PROP_WEB_DICT, PROP_GRASS_DICT, PROP_MUSHROOM_DICT, PROP_FLOWER_DICT, PROP_VINES_DICT);
    acceptsModifiers(WEBS, LEAVES, MAGNETISM, FORTUNE, VOID, GRASS, MUSHROOM, FLOWER, SPEED, SILK_TOUCH);
  }

  @Override
  public void init() {
    addIngredients(
        new OreIngredient("wildroot"),
        new OreIngredient("wildroot"),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(Items.GOLDEN_SWORD),
        new OreIngredient("tallgrass")
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks) {
    int x = max_affected;
    if (info.has(SPEED)) {
      x *= 2;
    }

    int interval = speedSubInt(this.interval);
    if (interval != 0 && ticks % interval == 0) {
      return false;
    }

    World world = caster.world;
    List<BlockPos> blocks = Util.getBlocksWithinRadius(caster.world, caster.getPosition(), radius, radius_y, radius, pos -> ifAffectedByNaturesScythe(world, pos, info));

    if (blocks.isEmpty()) {
      return false;
    }

    for (int i = 0; i <= x; i++) {
      BlockPos pos = blocks.get(Util.rand.nextInt(blocks.size()));

      if (!world.isRemote) {
        if (info.has(VOID)) {
          world.destroyBlock(pos, false);
        } else {
          breakBlock(world, pos, info, caster);
        }
        world.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.5f, 1f);
        PacketHandler.sendToAllTracking(new MessageFallBladesFX(pos.getX(), pos.getY(), pos.getZ(), false), world, pos);
      }
    }
    return true;
  }

  public void breakBlock(World world, BlockPos pos, StaffModifierInstanceList info, EntityPlayer player) {
    // TODO: HANDLE MAGNETISM
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    int fortune = (info.has(SpellHarvest.FORTUNE) || info.has(FORTUNE)) ? 2 : 0;
    boolean skip = false;
    List<ItemStack> drops = new ArrayList<>();
    if (info.has(SILK_TOUCH) || info.has(SpellHarvest.SILK_TOUCH)) {
      if (block instanceof IShearable && ((IShearable) block).isShearable(SHEARS, world, pos)) {
        IShearable shearable = (IShearable) block;
        drops.addAll(shearable.onSheared(SHEARS, player.world, pos, fortune));
        world.destroyBlock(pos, false);
      } else if (block.canSilkHarvest(world, pos, state, player)) {
        ItemStack silked;
        try {
          silked = (ItemStack) GET_SILK_DROP.invokeExact(block, state);
        } catch (Throwable throwable) {
          throw new RuntimeException("Unable to get silk touch drop!?", throwable);
        }
        if (silked.isEmpty()) {
          drops.add(silked);
        }
        ForgeEventFactory.fireBlockHarvesting(drops, world, pos, state, fortune, 1.0f, true, player);
      } else {
        skip = true;
      }
    } else {
      skip = true;
    }
    if (skip) {
      NonNullList<ItemStack> dropped = NonNullList.create();
      block.getDrops(dropped, world, pos, state, fortune);
      drops.addAll(dropped);
      ForgeEventFactory.fireBlockHarvesting(dropped, world, pos, state, fortune, 1.0f, true, player);
      world.destroyBlock(pos, false);
    }
    boolean magnet = info.has(MAGNETISM);
    for (ItemStack item : drops) {
      if (magnet) {
        ItemUtil.spawnItem(world, player.getPosition(), item);
      } else {
        ItemUtil.spawnItem(world, pos, item);
      }
    }
  }

  private boolean ifAffectedByNaturesScythe(World world, BlockPos pos, StaffModifierInstanceList info) {
    if (world.isAirBlock(pos)) {
      return false;
    }

    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    if (info.has(WEBS)) {
      if (block instanceof BlockWeb || OreDictCache.matches(this.web, state)) {
        return true;
      }
    }
    if (info.has(LEAVES)) {
      if (block instanceof BlockLeaves || OreDictCache.matches(this.tree, state)) {
        return true;
      }
    }
    if (info.has(GRASS)) {
      if (block == Blocks.TALLGRASS || (block == Blocks.DOUBLE_PLANT) && (state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.FERN || state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS) || OreDictCache.matches(this.grass, state) || block instanceof BlockVine || OreDictCache.matches(this.vines, state)) {
        return true;
      }
    }
    if (info.has(FLOWER)) {
      if (block instanceof BlockFlower || (block instanceof BlockDoublePlant && state.getValue(BlockDoublePlant.VARIANT) != BlockDoublePlant.EnumPlantType.GRASS && state.getValue(BlockDoublePlant.VARIANT) != BlockDoublePlant.EnumPlantType.FERN) || OreDictCache.matches(this.flower, state)) {
        return true;
      }
    }
    if (info.has(MUSHROOM)) {
      if (block instanceof BlockMushroom || OreDictCache.matches(this.mushroom, state)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius = properties.get(PROP_RADIUS);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.interval = properties.get(PROP_INTERVAL);
    this.flower = properties.get(PROP_FLOWER_DICT);
    this.grass = properties.get(PROP_GRASS_DICT);
    this.mushroom = properties.get(PROP_MUSHROOM_DICT);
    this.tree = properties.get(PROP_TREE_DICT);
    this.vines = properties.get(PROP_VINES_DICT);
    this.web = properties.get(PROP_WEB_DICT);
    this.max_affected = properties.get(PROP_MAX_AFFECTED);
  }
}
