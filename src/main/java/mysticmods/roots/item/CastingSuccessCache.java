package mysticmods.roots.item;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mysticmods.roots.util.BooleanRingBuffer40;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class CastingSuccessCache {
  private static final Object2ObjectLinkedOpenHashMap<UUID, CacheEntry> CACHE_MAP = new Object2ObjectLinkedOpenHashMap<>();
  private static final Object2ObjectLinkedOpenHashMap.FastSortedEntrySet<UUID, CacheEntry> CACHE_SET = CACHE_MAP.object2ObjectEntrySet();
  private static final int SAMPLE_INTERVAL = 5;

  public static void tick() {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    final long currentTick = server.overworld().getGameTime();
    CACHE_SET.removeIf(entry -> currentTick - entry.getValue().lastUpdated > 5 * 20);
  }

  public static void clear(UUID uuid) {
    CACHE_MAP.remove(uuid);
  }

  public static void clear(ItemStack stack) {
    UUID uuid = CastingItem.getUUID(stack);
    clear(uuid);
  }

  public static void clear() {
    CACHE_MAP.clear();
  }

  public static void note(ItemStack stack, boolean value) {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    final long updateTime = server.overworld().getGameTime();
    UUID uuid = CastingItem.getUUID(stack);
    note(uuid, value, updateTime);
  }

  public static void note(UUID uuid, boolean value, long updateTime) {
    CacheEntry entry = CACHE_MAP.computeIfAbsent(uuid, k -> new CacheEntry());
    entry.note(value, updateTime);
  }

  public static boolean isASuccess(ItemStack stack) {
    UUID uuid = CastingItem.getUUID(stack);
    return isASuccess(uuid);
  }

  public static boolean isASuccess(UUID uuid) {
    CacheEntry entry = CACHE_MAP.get(uuid);
    if (entry != null) {
      return entry.isASuccess();
    }
    return false;
  }

  private static class CacheEntry {
    public long lastUpdated = -1;
    public long lastRecordedTick = -1;
    public final BooleanRingBuffer40 buffer = new BooleanRingBuffer40();

    public void note(boolean value, long updateTime) {
      // Only record once every SAMPLE_INTERVAL ticks
      if (lastRecordedTick == -1 || updateTime - lastRecordedTick >= SAMPLE_INTERVAL) {
        buffer.add(value);
        lastRecordedTick = updateTime;
      } else if (value) {
        // Optional: upgrade existing 'false' sample to 'true' if a success occurs before sample interval ends
        buffer.replaceLast(true);
      }

      this.lastUpdated = updateTime;
    }

    public boolean isASuccess() {
      if (buffer.countTrue() >= 8) return true;
      for (int i = buffer.size() - 1; i >= Math.max(0, buffer.size() - 5); i--) {
        if (buffer.get(i)) return true;
      }
      return false;
    }

    public int count() {
      return buffer.countTrue();
    }
  }
}
