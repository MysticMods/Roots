package epicsquid.roots.util;

import epicsquid.roots.Roots;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.*;

@Mod.EventBusSubscriber(modid= Roots.MODID)
public class HarvestUtil {
  private static Method getSeed;
  public static Deque<HarvestEntry> queue = new ArrayDeque<>();
  public static HashMap<Block, ItemStack> seedCache = new HashMap<>();
  public static HashMap<Block, IProperty<Integer>> propertyMap = new HashMap<>();

  static {
    getSeed = ReflectionHelper.findMethod(BlockCrops.class, "getSeed", "func_149866_i");
  }

  public static void prepare () {
    seedCache.clear();
    seedCache.put(Blocks.NETHER_WART, new ItemStack(Items.NETHER_WART));
    propertyMap.clear();
    propertyMap.put(Blocks.BEETROOTS, BlockBeetroot.BEETROOT_AGE);
    propertyMap.put(Blocks.COCOA, BlockCocoa.AGE);
    propertyMap.put(Blocks.NETHER_WART, BlockNetherWart.AGE);
  }

  public static void add (ItemStack seed, int dimension, BlockPos position, IBlockState block) {
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
    if (seed != null) return seed;
    try {
      seed = new ItemStack((Item) getSeed.invoke(state.getBlock()));
    } catch (Exception e) {
      seed = ItemStack.EMPTY;
    }
    seedCache.put(state.getBlock(), seed);
    return seed;
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
