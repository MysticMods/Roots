package mysticmods.roots.event.forge;

import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class BlockHandler {
  private static final Map<ResourceKey<Level>, Object2BooleanMap<BoundingBox>> bounds = Collections.synchronizedMap(new LinkedHashMap<>());

  @SubscribeEvent
  public static void onBlockNeighbor(BlockEvent.NeighborNotifyEvent event) {
    if (event.getWorld() instanceof ServerLevel level) {
      MinecraftServer server = level.getServer();
      if (!server.isSameThread()) {
        return;
      }
      ResourceKey<Level> dimension = level.dimension();
      Object2BooleanMap<BoundingBox> boxes = bounds.get(dimension);
      if (boxes != null) {
        Set<BoundingBox> toMark = new HashSet<>();
        for (BoundingBox box : boxes.keySet()) {
          if (box.isInside(event.getPos())) {
            toMark.add(box);
          }
        }
        for (BoundingBox marked : toMark) {
          boxes.put(marked, false);
        }
      }
    }
  }

  public static boolean isDirty(Level level, BoundingBox box) {
    if (level == null || level.isClientSide()) {
      return false;
    }

    MinecraftServer server = level.getServer();
    if (server == null || !server.isSameThread()) {
      return false;
    }
    Object2BooleanMap<BoundingBox> boxes = bounds.get(level.dimension());
    if (boxes != null) {
      return boxes.getBoolean(box);
    }

    return false;
  }

  public static void clean(Level level, BoundingBox box) {
    if (level == null || level.isClientSide()) {
      return;
    }

    MinecraftServer server = level.getServer();
    if (server == null || server.isSameThread()) {
      return;
    }

    Object2BooleanMap<BoundingBox> boxes = bounds.get(level.dimension());
    if (boxes != null) {
      boxes.put(box, true);
    }
  }

  public static void register(Level level, BoundingBox box) {
    if (level == null || level.isClientSide()) {
      return;
    }
    MinecraftServer server = level.getServer();
    if (server != null && !server.isSameThread()) {
      return;
    }
    Object2BooleanMap<BoundingBox> boxes = bounds.computeIfAbsent(level.dimension(), (k) -> new Object2BooleanLinkedOpenHashMap<>());
    boxes.put(box, false);
  }

  public static void unregister(Level level, BoundingBox box) {
    if (level == null || level.isClientSide()) {
      return;
    }
    MinecraftServer server = level.getServer();
    if (server != null && !server.isSameThread()) {
      return;
    }
    Object2BooleanMap<BoundingBox> boxes = bounds.get(level.dimension());
    if (boxes != null) {
      boxes.removeBoolean(box);
      if (boxes.isEmpty()) {
        bounds.remove(level.dimension());
      }
    }
  }
}
