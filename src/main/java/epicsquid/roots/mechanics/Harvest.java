package epicsquid.roots.mechanics;

import epicsquid.roots.Roots;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.config.CropConfig;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber(modid = Roots.MODID)
public class Harvest {
  private static Method getSeed;
  private static Map<IProperty<?>, Integer> stateMax = new Object2IntOpenHashMap<>();
  private static Deque<HarvestEntry> queue = new ArrayDeque<>();
  private static HashMap<Block, ItemStack> seedCache = new HashMap<>();

  public static int getMaxState(IProperty<?> prop) {
    return stateMax.getOrDefault(prop, -1);
  }

  public static void setMaxState(IProperty<?> prop, int max) {
    stateMax.put(prop, max);
  }

  public static Set<IProperty<?>> getStateKeys() {
    return stateMax.keySet();
  }

  public static IProperty<?> resolveStates(IBlockState state) {
    Block block = state.getBlock();
    ResourceLocation rl = block.getRegistryName();
    if (CropConfig.getHarvestModBlacklist().contains(Objects.requireNonNull(rl).getNamespace()) || CropConfig.getHarvestBlacklist().contains(rl)) {
      return null;
    }

    for (IProperty<?> prop : state.getPropertyKeys()) {
      if ((prop.getName().equals("age") || prop.getName().equals("growth")) && prop.getValueClass() == Integer.class) {
        int max = Harvest.getMaxState(prop);
        if (max == -1) {
          max = Collections.max((Collection<Integer>) prop.getAllowedValues());
          Harvest.setMaxState(prop, max);
        }
        return prop;
      }
    }
    return null;
  }

  static {
    getSeed = ReflectionHelper.findMethod(BlockCrops.class, "getSeed", "func_149866_i");
  }

  public static void prepare() {
    seedCache.clear();
    seedCache.put(Blocks.NETHER_WART, new ItemStack(Items.NETHER_WART));
  }

  public static void add(ItemStack seed, int dimension, BlockPos position, IBlockState block) {
    queue.add(new HarvestEntry(seed, dimension, position, block));
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
    if (queue.peek() == null) return;

    HarvestEntry ref = queue.pop();
    HarvestEntry me = new HarvestEntry(null, event.getWorld().provider.getDimension(), event.getPos(), event.getState());
    if (!ref.equals(me)) return;

    ItemStack seed = ref.getSeed();

    List<ItemStack> drops = event.getDrops();
    Iterator<ItemStack> dropIter = drops.listIterator();
    while (dropIter.hasNext()) {
      ItemStack copy = dropIter.next().copy();
      copy.setCount(1);
      if (ItemStack.areItemStacksEqual(copy, seed)) {
        dropIter.remove();
        break;
      } else if (copy.getItem() instanceof IPlantable) {
        dropIter.remove();
        break;
      }
    }
  }

  public static ItemStack getSeed(IBlockState state) {
    Block block = state.getBlock();
    ItemStack seed = seedCache.get(block);
    if (seed != null && !seed.isEmpty()) return seed;
    try {
      seed = new ItemStack((Item) getSeed.invoke(state.getBlock()));
    } catch (Exception e) {
      seed = ItemStack.EMPTY;
    }
    seedCache.put(state.getBlock(), seed);
    return seed;
  }

  public static List<ItemStack> harvestReturnDrops (IBlockState state, IProperty<?> prop, ItemStack seed, BlockPos pos, World world, @Nullable EntityPlayer player) {
    IBlockState newState = state.withProperty((IProperty<Integer>) prop, 0);
    NonNullList<ItemStack> drops = NonNullList.create();
    Harvest.add(seed, world.provider.getDimension(), pos, state);
    state.getBlock().getDrops(drops, world, pos, state, 0);
    ForgeEventFactory.fireBlockHarvesting(drops, world, pos, state, 0, 1.0f, false, player);
    world.setBlockState(pos, newState);
    return drops;
  }

  public static void doHarvest (IBlockState state, IProperty<?> prop, ItemStack seed, BlockPos pos, World world, @Nullable EntityPlayer player) {
    List<ItemStack> drops = harvestReturnDrops(state, prop, seed, pos, world, player);
    for (ItemStack stack : drops) {
      if (stack.isEmpty()) continue;
      ItemUtil.spawnItem(world, pos, stack);
    }
  }

  public static List<ItemStack> harvestReturnDrops (IBlockState state, BlockPos pos, World world, @Nullable EntityPlayer player) {
    ItemStack seed = Harvest.getSeed(state);
    IProperty<?> prop = Harvest.resolveStates(state);
    return harvestReturnDrops(state, prop, seed, pos, world, player);
  }

  public static void doHarvest (IBlockState state, BlockPos pos, World world, @Nullable EntityPlayer player) {
    List<ItemStack> drops = harvestReturnDrops(state, pos, world, player);
    for (ItemStack stack : drops) {
      if (stack.isEmpty()) continue;
      ItemUtil.spawnItem(world, pos, stack);
    }
  }

  public static boolean isGrown (IBlockState state) {
    IProperty<?> prop = resolveStates(state);
    if (prop == null) return false;

    return state.getValue((IProperty<Integer>) prop) == getMaxState(prop);
  }

  public static class HarvestEntry {
    private ItemStack seed;
    private int dimension;
    private BlockPos position;
    private IBlockState block;

    public HarvestEntry(ItemStack seed, int dimension, BlockPos position, IBlockState block) {
      this.seed = seed;
      this.dimension = dimension;
      this.position = position;
      this.block = block;
    }

    public ItemStack getSeed() {
      return seed;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      HarvestEntry that = (HarvestEntry) o;
      return dimension == that.dimension &&
          Objects.equals(position, that.position) &&
          Objects.equals(block, that.block);
    }

    @Override
    public int hashCode() {
      return Objects.hash(seed, dimension, position, block);
    }
  }
}
