package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageHarvestCompleteFX;
import epicsquid.roots.network.fx.MessageTreeCompleteFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.ItemSpawnUtil;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.*;

public class SpellHarvest extends SpellBase {
  public static String spellName = "spell_harvest";
  public static SpellHarvest instance = new SpellHarvest(spellName);

  private static Method getSeed;

  {
    getSeed = ReflectionHelper.findMethod(BlockCrops.class, "getSeed", "func_149866_i");
  }

  public Deque<HarvestEntry> queue = new ArrayDeque<>();
  public static HashMap<Block, ItemStack> seedCache = new HashMap<>();

  static {
    seedCache.put(Blocks.NETHER_WART, new ItemStack(Items.NETHER_WART));
  }
  public HashMap<Block, IProperty<Integer>> propertyMap = new HashMap<>();

  public SpellHarvest(String name) {
    super(name, TextFormatting.GREEN, 57f / 255f, 253f / 255f, 28f / 255f, 197f / 255f, 233f / 255f, 28f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 25;

    addCost(HerbRegistry.getHerbByName("wildewheet"), 0.55f);
    addIngredients(
            new ItemStack(Items.GOLDEN_HOE),
            new ItemStack(ModItems.spirit_herb),
            new ItemStack(ModItems.wildewheet),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Items.WHEAT_SEEDS)
    );

    // This is so harvest drops can be monitored.
    MinecraftForge.EVENT_BUS.register(this);

    // For sanity
    propertyMap.put(Blocks.BEETROOTS, BlockBeetroot.BEETROOT_AGE);
    propertyMap.put(Blocks.COCOA, BlockCocoa.AGE);
    propertyMap.put(Blocks.NETHER_WART, BlockNetherWart.AGE);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
    if (queue.peek() == null) return;

    HarvestEntry ref = queue.pop();
    HarvestEntry me = new HarvestEntry(null, event.getWorld().provider.getDimension(), event.getPos(), event.getState());
    if (!ref.equals(me)) return;

    ItemStack seed = ref.getSeed();
    if (seed.isEmpty()) return;

    List<ItemStack> drops = event.getDrops();
    Iterator<ItemStack> dropIter = drops.listIterator();
    while (dropIter.hasNext()) {
      ItemStack copy = dropIter.next().copy();
      copy.setCount(1);
      if (ItemStack.areItemStacksEqual(copy, seed)) {
        dropIter.remove();
        break;
      }
    }
  }

  private static List<Block> skipBlocks = Arrays.asList(Blocks.BEDROCK, Blocks.GRASS, Blocks.DIRT);
  private static Map<IProperty<Integer>, Integer> stateMax = new Object2IntOpenHashMap<>();

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    List<BlockPos> affectedPositions = new ArrayList<>();
    List<BlockPos> pumpkinsAndMelons = new ArrayList<>();
    List<BlockPos> reedsAndCactus = new ArrayList<>();
    List<BlockPos> crops = Util.getBlocksWithinRadius(player.world, player.getPosition(), 6, 5, 6, (pos) -> {
      if (player.world.isAirBlock(pos)) return false;
      IBlockState state = player.world.getBlockState(pos);
      if (skipBlocks.contains(state.getBlock())) return false;

      if (state.getBlock() == Blocks.PUMPKIN || state.getBlock() == Blocks.MELON_BLOCK) {
        pumpkinsAndMelons.add(pos);
        return false;
      }
      if (state.getBlock() == Blocks.REEDS || state.getBlock() == Blocks.CACTUS) {
        reedsAndCactus.add(pos);
        return false;
      }
      for (IProperty<?> prop : state.getPropertyKeys()) {
        if (prop.getName().equals("age") && prop.getValueClass() == Integer.class) {
          int max = stateMax.getOrDefault(prop, -1);
          if (max == -1) {
            max = Collections.max((Collection<Integer>) prop.getAllowedValues());
            stateMax.put((IProperty<Integer>) prop, max);
          }
          if (state.getValue((IProperty<Integer>) prop) == max) {
            return true;
          }
        }
      }
      return false;
    });

    int count = 0;

    for (BlockPos pos : crops) {
      IBlockState state = player.world.getBlockState(pos);
      ItemStack seed = getSeed(state);
      if (!seed.isEmpty()) {
        // Do do do the harvest!
        IProperty<Integer> prop = null;
        for (IProperty<Integer> entry : stateMax.keySet()) {
          if (state.getPropertyKeys().contains(entry)) {
            prop = entry;
          }
        }

        assert prop != null;

        if (!player.world.isRemote) {
          IBlockState newState = state.withProperty(prop, 0);
          NonNullList<ItemStack> drops = NonNullList.create();
          queue.push(new HarvestEntry(seed, player.dimension, pos, state));
          state.getBlock().getDrops(drops, player.world, pos, state, 0);
          ForgeEventFactory.fireBlockHarvesting(drops, player.world, pos, state, 0, 1.0f, false, player);
          player.world.setBlockState(pos, newState);
          for (ItemStack stack : drops) {
            if (stack.isEmpty()) continue;
            ItemSpawnUtil.spawnItem(player.world, pos, stack);
          }
          affectedPositions.add(pos);
        }
        count++;
      }
    }
    for (BlockPos pos : pumpkinsAndMelons) {
      count++;
      if (!player.world.isRemote) {
        IBlockState state = player.world.getBlockState(pos);
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        state.getBlock().harvestBlock(player.world, player, pos, state, null, player.getHeldItemMainhand());
        affectedPositions.add(pos);
      }
    }
    Set<BlockPos> done = new HashSet<>();
    List<BlockPos> lowest = new ArrayList<>();
    for (BlockPos pos : reedsAndCactus) {
      if (done.contains(pos)) continue;

      BlockPos down = pos.down();
      IBlockState downState = player.world.getBlockState(down);
      while (downState.getBlock() == Blocks.CACTUS || downState.getBlock() == Blocks.REEDS) {
        done.add(down);
        down = down.down();
        downState = player.world.getBlockState(down);
      }
      lowest.add(down.up());
      done.add(pos);
    }
    for (BlockPos pos : lowest) {
      IBlockState state = player.world.getBlockState(pos.up());
      if (state.getBlock() == Blocks.CACTUS || state.getBlock() == Blocks.REEDS) {
        count++;
        if (!player.world.isRemote) {
          state.getBlock().harvestBlock(player.world, player, pos.up(), state, null, player.getHeldItemMainhand());
          player.world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
          affectedPositions.add(pos);
        }
      }
    }

    if (!affectedPositions.isEmpty() && !player.world.isRemote) {
      MessageHarvestCompleteFX message = new MessageHarvestCompleteFX(affectedPositions);
      PacketHandler.sendToAllTracking(message, player);
    }

    return count != 0;
  }

  public ItemStack getSeed(IBlockState state) {
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

  private class HarvestEntry {
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
