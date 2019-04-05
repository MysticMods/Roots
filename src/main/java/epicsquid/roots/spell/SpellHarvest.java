package epicsquid.roots.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
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
  public HashMap<Block, ItemStack> seedCache = new HashMap<>();
  public HashMap<Block, IProperty<Integer>> propertyMap = new HashMap<>();

  public SpellHarvest(String name) {
    super(name, TextFormatting.GREEN, 57f / 255f, 253f / 255f, 28f / 255f, 197f / 255f, 233f / 255f, 28f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 25;

    addCost(HerbRegistry.getHerbByName("spirit_herb"), 0.55f);
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

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      List<BlockPos> pumpkinsAndMelons = new ArrayList<>();
      List<BlockPos> reedsAndCactus = new ArrayList<>();
      List<BlockPos> crops = Util.getBlocksWithinRadius(player.world, player.getPosition(), 6, 5, 6, (pos) -> {
        IBlockState state = player.world.getBlockState(pos);
        if (state.getBlock() == Blocks.PUMPKIN || state.getBlock() == Blocks.MELON_BLOCK) {
          pumpkinsAndMelons.add(pos);
          return false;
        }
        if (state.getBlock() == Blocks.REEDS || state.getBlock() == Blocks.CACTUS) {
          reedsAndCactus.add(pos);
          return false;
        }
        if (state.getProperties().get(BlockCrops.AGE) != null && state.getValue(BlockCrops.AGE) == 7) {
          return true;
        }
        for (IProperty<Integer> prop : Arrays.asList(BlockBeetroot.AGE, BlockNetherWart.AGE, BlockCocoa.AGE)) {
          if (state.getProperties().get(prop) != null && state.getValue(prop).equals(Collections.max(prop.getAllowedValues()))) {
            return true;
          }
        }
        return false;
      });

      for (BlockPos pos : crops) {
        IBlockState state = player.world.getBlockState(pos);
        ItemStack seed = getSeed(state);
        if (!seed.isEmpty()) {
          // Do do do the harvest!
          IProperty<Integer> age = propertyMap.getOrDefault(state.getBlock(), BlockCrops.AGE);
          IBlockState newState = state.withProperty(age, Collections.min(age.getAllowedValues()));
          NonNullList<ItemStack> drops = NonNullList.create();
          queue.push(new HarvestEntry(seed, player.dimension, pos, state));
          state.getBlock().getDrops(drops, player.world, pos, state, 0);
          ForgeEventFactory.fireBlockHarvesting(drops, player.world, pos, state, 0, 1.0f, false, player);
          player.world.setBlockState(pos, newState);
          for (ItemStack stack : drops) {
            if (stack.isEmpty()) continue;
            EntityItem spawn = new EntityItem(player.world, pos.getX(), pos.getY(), pos.getZ(), stack);
            player.world.spawnEntity(spawn);
          }
        }
      }
      for (BlockPos pos : pumpkinsAndMelons) {
        IBlockState state = player.world.getBlockState(pos);
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        state.getBlock().harvestBlock(player.world, player, pos, state, null, player.getHeldItemMainhand());
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
          state.getBlock().harvestBlock(player.world, player, pos.up(), state, null, player.getHeldItemMainhand());
          player.world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
        }
      }
    }
    return true;
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
