package mysticmods.roots.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.growth.HarvestRecord;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@EventBusSubscriber
public class HarvestUtil {
  private static boolean capturingDrops = false;
  private static final Long2ObjectMap<List<DropStuff>> adjustmentMap = new Long2ObjectOpenHashMap<>();
  private static final List<ItemEntity> capturedDrops = new ArrayList<>();

  public static HarvestRecord getRecord(Level level, BlockPos pos, @Nullable BlockState state, @Nullable Player player) {
    if (state == null) {
      state = level.getBlockState(pos);
    }

    HarvestRecord record = state.getBlockHolder().getData(DataMaps.HARVEST_RECORDS);
    if (record == null && state.getBlock() instanceof CropBlock crop) {
      record = HarvestRecord.of(crop, ModTests.HARVEST_SINGLE_CROP_BLOCK.get());
      RootsAPI.LOG.error("We're guessing a harvest record for block '{}'. This should be added as a harvest record.", state.getBlock());
    }

    return record;
  }

  public static void beginCapture() {
    if (capturingDrops) {
      throw new IllegalStateException("We're already capturing drops");
    }
    if (!capturedDrops.isEmpty()) {
      throw new IllegalStateException("Unresolved captured drops");
    }
    capturingDrops = true;
  }

  public static List<ItemEntity> endCapture() {
    List<ItemEntity> copy = new ArrayList<>(capturedDrops);
    capturedDrops.clear();
    capturingDrops = false;
    return copy;
  }

  public static void adjustOrCapture(DropStuff entry) {
    if (!adjustmentMap.containsKey(entry.pos.asLong())) {
      adjustmentMap.put(entry.pos.asLong(), new ArrayList<>());
    }
    List<DropStuff> list = adjustmentMap.get(entry.pos.asLong());
    list.add(entry);
  }

  public static boolean capture (ItemEntity entity) {
    if (capturingDrops) {
      capturedDrops.add(entity);
      return true;
    }

    return false;
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onBlockDrops(BlockDropsEvent event) {
    BlockPos pos = event.getPos();
    ServerLevel level = event.getLevel();
    ResourceKey<Level> dimension = level.dimension();
    List<DropStuff> dropStuff = adjustmentMap.getOrDefault(pos.asLong(), null);
    if (dropStuff == null) {
      return;
    }

    List<ItemEntity> drops = event.getDrops();

    Iterator<DropStuff> iterator = dropStuff.iterator();
    while (iterator.hasNext()) {
      DropStuff stuff = iterator.next();

      if (!stuff.dimension().equals(dimension)) {
        continue;
      }

      if (stuff.test(drops)) {
        iterator.remove();
      } else {
        RootsAPI.LOG.error("Unable to modify loot at {} in dimension {}. Item was {} with count {}.", pos, dimension, stuff.seedItem, stuff.count);
      }
    }

    if (capturingDrops) {
      capturedDrops.addAll(drops);
      event.setCanceled(true);
    }
  }

  public record DropStuff(BlockPos pos, ResourceKey<Level> dimension, @Nullable Item seedItem,
                          int count) implements Predicate<List<ItemEntity>> {

    public DropStuff(BlockPos pos, ResourceKey<Level> dimension) {
      this(pos, dimension, null, 0);
    }

    public DropStuff(BlockPos pos, ResourceKey<Level> dimension, Item item) {
      this(pos, dimension, item, 1);
    }

    @Override
    public boolean test(List<ItemEntity> itemEntities) {
      if (seedItem == null) {
        return true;
      }

      int toRemove = count;

      for (ItemEntity entity : itemEntities) {
        ItemStack item = entity.getItem();
        if (item.is(seedItem)) {
          if (item.getCount() < toRemove) {
            toRemove -= item.getCount();
            item.setCount(0);
          } else {
            item.shrink(toRemove);
            toRemove = 0;
          }
        }
      }

      itemEntities.removeIf(o -> o.getItem().isEmpty());

      return toRemove == 0;
    }
  }
}
