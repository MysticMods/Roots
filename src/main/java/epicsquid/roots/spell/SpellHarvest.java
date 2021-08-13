package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.mechanics.Harvest;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageHarvestCompleteFX;
import epicsquid.roots.network.fx.MessageRampantLifeInfusionFX;
import epicsquid.roots.properties.Property;
import epicsquid.roots.recipe.FoodPoisoning;
import epicsquid.roots.recipe.MortarRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SpellHarvest extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(25);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("wildewheet", 0.55));
  public static Property.PropertyCost PROP_COST_2 = new Property.PropertyCost(new SpellCost("terra_moss", 0.1));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 6).setDescription("radius on the X axis of the area the spell has effect on");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("radius on the Y axis of the area the spell has effect on");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 6).setDescription("radius on the Z axis of the area the spell has effect on");
  public static Property<Integer> PROP_RADIUS_BOOST = new Property<>("radius_boost", 4).setDescription("how much the radius should be increased by");
  public static Property<Integer> PROP_RADIUS_UNBOOST = new Property<>("radius_unboost", 3).setDescription("how much the radius should be decreased by");
  public static Property<Float> PROP_UNDEAD_CHANCE = new Property<>("undead_chance", 0.3f).setDescription("the chance per cast of getting an undead cache");
  public static Property<Integer> PROP_UNDEAD_COUNT = new Property<>("undead_count", 1).setDescription("the number of guaranteed undead caches when granted");
  public static Property<Integer> PROP_UNDEAD_ADDITIONAL = new Property<>("undead_additional", 3).setDescription("the number of (0-(x-1)) additional caches");
  public static Property<Float> PROP_UNDEAD_RARITY = new Property<>("undead_rare", 0.3f).setDescription("the frequency at which caches will be upgraded from common to rare");
  public static Property<Integer> PROP_GROWTH_COUNT = new Property<>("growth_count", 3).setDescription("how many additional ticks of growth will be applied");
  public static Property<Integer> PROP_GROWTH_ADDITIONAL = new Property<>("growth_additional", 6).setDescription("how many additional randomised ticks of growth will be applied");

  public static Modifier RADIUS1 = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "extended_harvest"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.275)));
  public static Modifier MAGNETISM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_harvest"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.125)));
  public static Modifier UNDEAD = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "gifts_of_undeath"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.75)));
  public static Modifier SMALL_RADIUS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "targeted_harvest"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.275)));
  public static Modifier POISON = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "poisoned_harvest"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.125)));
  public static Modifier GROWTH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "regrowth"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.345)));
  public static Modifier COOKING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fiery_harvest"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.125)));
  public static Modifier CRUSHING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "yield"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.125)));
  public static Modifier SILK_TOUCH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "harvest_silk_touch"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.475)));

  static {
    // Conflcits
    RADIUS1.addConflict(SMALL_RADIUS); // Can't increase/decrease at the same time
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_harvest");
  public static SpellHarvest instance = new SpellHarvest(spellName);

  private int radius_x, radius_y, radius_z, radius_boost, radius_unboost, undead_count, undead_additional, growth_count, growth_additional;
  private float undead_chance, undead_rarity;

  public SpellHarvest(ResourceLocation name) {
    super(name, TextFormatting.GREEN, 57f / 255f, 253f / 255f, 28f / 255f, 197f / 255f, 233f / 255f, 28f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_RADIUS_BOOST, PROP_RADIUS_UNBOOST, PROP_UNDEAD_ADDITIONAL, PROP_UNDEAD_CHANCE, PROP_UNDEAD_COUNT, PROP_UNDEAD_RARITY, PROP_GROWTH_COUNT, PROP_GROWTH_ADDITIONAL);
    acceptModifiers(RADIUS1, MAGNETISM, UNDEAD, SMALL_RADIUS, POISON, GROWTH, COOKING, CRUSHING, SILK_TOUCH);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.STONE_HOE),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(ModItems.wildewheet_seed),
        new ItemStack(Items.WHEAT_SEEDS)
    );
    setCastSound(ModSounds.Spells.HARVEST);
  }

  private static final List<Block> skipBlocks = Arrays.asList(Blocks.BEDROCK, Blocks.GRASS, Blocks.DIRT, Blocks.STONE, Blocks.TALLGRASS, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.LAVA, Blocks.DOUBLE_PLANT, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);

  public ItemStack tryCook(ItemStack input) {
    ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
    if (result.isEmpty()) {
      return input;
    }
    return result.copy();
  }

  public ItemStack tryCrush(ItemStack input) {
    MortarRecipe recipe = ModRecipes.getMortarRecipe(Collections.singletonList(input));
    if (recipe == null) {
      return input;
    }
    return recipe.getResult().copy();
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    Harvest.prepare();

    List<Function<ItemStack, ItemStack>> converters = new ArrayList<>();
    if (info.has(CRUSHING)) {
      converters.add(this::tryCrush);
    }
    if (info.has(POISON)) {
      converters.add(FoodPoisoning::replacement);
    }
    if (info.has(COOKING)) {
      converters.add(this::tryCook);
    }

    int x = radius_x;
    int y = radius_y;
    int z = radius_z;
    if (info.has(RADIUS1)) {
      x += radius_boost;
      y += radius_boost;
      z += radius_boost;
    } else if (info.has(SMALL_RADIUS)) {
      x -= radius_unboost;
      y -= radius_unboost;
      z -= radius_unboost;
    }

    Predicate<IBlockState> pumpkinOrMelonTest = new Harvest.Matcher(Blocks.PUMPKIN).or(new Harvest.Matcher(Blocks.MELON_BLOCK));
    Predicate<IBlockState> reedsOrCactusTest = new Harvest.Matcher(Blocks.REEDS).or(new Harvest.Matcher(Blocks.CACTUS));

    List<BlockPos> affectedPositions = new ArrayList<>();
    List<BlockPos> pumpkinsAndMelons = new ArrayList<>();
    List<BlockPos> reedsAndCactus = new ArrayList<>();
    List<BlockPos> crops = Util.getBlocksWithinRadius(player.world, player.getPosition(),
        x, y, z, (pos) -> {
          if (player.world.isAirBlock(pos)) return false;
          IBlockState state = player.world.getBlockState(pos);
          Block block = state.getBlock();

          if (skipBlocks.contains(block)) return false;

          if (pumpkinOrMelonTest.test(state)) {
            pumpkinsAndMelons.add(pos);
            return false;
          }
          if (reedsOrCactusTest.test(state)) {
            IBlockState down = player.world.getBlockState(pos.down());
            if (reedsOrCactusTest.test(down)) {
              reedsAndCactus.add(pos);
            }
            return false;
          }
          IProperty<?> prop = Harvest.resolveStates(state);
          if (prop != null) {
            return Harvest.isGrown(state);
          }
          return false;
        });

    int count = 0;

    reedsAndCactus.sort((o1, o2) -> Integer.compare(o2.getY(), o1.getY()));
    pumpkinsAndMelons.addAll(reedsAndCactus);

    List<ItemStack> drops = new ArrayList<>();
    for (BlockPos pos : crops) {
      IBlockState state = player.world.getBlockState(pos);

      if (!player.world.isRemote) {
        List<ItemStack> origBlockDrops = Harvest.harvestReturnDrops(state, pos, player.world, player);
        List<ItemStack> blockDrops;
        if (converters.isEmpty()) {
          blockDrops = origBlockDrops;
        } else {
          blockDrops = new ArrayList<>();
          for (ItemStack orig : origBlockDrops) {
            for (Function<ItemStack, ItemStack> converter : converters) {
              orig = converter.apply(orig);
            }
            blockDrops.add(orig);
          }
        }

        if (info.has(MAGNETISM)) {
          drops.addAll(blockDrops);
        } else {
          for (ItemStack stack : blockDrops) {
            ItemUtil.spawnItem(player.world, pos, stack);
          }
        }
        affectedPositions.add(pos);
      }
      count++;
    }
    for (BlockPos pos : pumpkinsAndMelons) {
      count++;
      if (!player.world.isRemote) {
        SpellNaturesScythe.instance.breakBlock(player.world, pos, info, player, converters);
        affectedPositions.add(pos);
      }
    }

    if (info.has(UNDEAD) && !player.world.isRemote) {
      if (Util.rand.nextFloat() < undead_chance) {
        int c = undead_count + Util.rand.nextInt(undead_additional);
        for (int i = 0; i < c; i++) {
          Item it = ModItems.spirit_bag;
          if (Util.rand.nextFloat() < undead_rarity) {
            it = ModItems.reliquary;
          }
          ItemStack stack = new ItemStack(it);
          if (info.has(MAGNETISM)) {
            ItemUtil.spawnItem(player.world, player.getPosition(), stack);
          } else {
            ItemUtil.spawnItem(player.world, Util.getRandomWithinRadius(player.getPosition(), x, y, z), stack);
          }
        }
        count++;
      }
    }

    if (!affectedPositions.isEmpty() && !player.world.isRemote) {
      MessageHarvestCompleteFX message = new MessageHarvestCompleteFX(affectedPositions);
      PacketHandler.sendToAllTracking(message, player);
    }

    if (!player.world.isRemote) {
      for (ItemStack item : drops) {
        ItemUtil.spawnItem(player.world, player.getPosition(), item);
      }

      if (info.has(GROWTH)) {
        for (BlockPos pos : affectedPositions) {
          IBlockState state = player.world.getBlockState(pos);
          if (Growth.canGrow(player.world, pos, state)) {
            int c = growth_count + Util.rand.nextInt(growth_additional);
            for (int i = 0; i < c; i++) {
              state.getBlock().randomTick(player.world, pos, state, Util.rand);
            }
            PacketHandler.sendToAllTracking(new MessageRampantLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), player);
          }
        }
      }
    }

    return count != 0;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.radius_boost = properties.get(PROP_RADIUS_BOOST);
    this.radius_unboost = properties.get(PROP_RADIUS_UNBOOST);
    this.undead_additional = properties.get(PROP_UNDEAD_ADDITIONAL);
    this.undead_chance = properties.get(PROP_UNDEAD_CHANCE);
    this.undead_count = properties.get(PROP_UNDEAD_COUNT);
    this.undead_rarity = properties.get(PROP_UNDEAD_RARITY);
    this.growth_count = properties.get(PROP_GROWTH_COUNT);
    this.growth_additional = properties.get(PROP_GROWTH_ADDITIONAL);
  }

  private interface ItemStackConverter extends Function<ItemStack, ItemStack> {
  }
}
