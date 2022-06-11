package mysticmods.roots.event.forge;

import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import mysticmods.roots.api.MonitoringBlockEntity;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
public class BlockHandler {
  // TODO: clear when dimensions unload, server restart
  private static final Map<ResourceKey<Level>, Object2ObjectLinkedOpenHashMap<BoundingBox, BlockPos>> bounds = Collections.synchronizedMap(new Object2ObjectLinkedOpenHashMap<>());

  // TODO: convert to mixin
  public static void onBlockChanged(Level pLevel, BlockPos pPos, BlockState newState) {
    if (pLevel instanceof ServerLevel level) {
      MinecraftServer server = level.getServer();
      if (!server.isSameThread()) {
        return;
      }
      ResourceKey<Level> dimension = level.dimension();
      Object2ObjectMap<BoundingBox, BlockPos> boxes = bounds.get(dimension);
      if (boxes != null) {
        Set<BlockPos> toMark = new HashSet<>();
        for (Object2ObjectMap.Entry<BoundingBox, BlockPos> entry : boxes.object2ObjectEntrySet()) {
          if (entry.getKey().isInside(pPos) && !entry.getValue().equals(pPos)) {
            toMark.add(entry.getValue());
          }
        }
        for (BlockPos pos : toMark) {
          if (level.hasChunkAt(pos) && level.getBlockEntity(pos) instanceof MonitoringBlockEntity monitor) {
            monitor.notify(level, pPos);
          }
        }
      }
    }
  }

  public static void register(Level level, BoundingBox box, BlockPos pos) {
    if (level == null || level.isClientSide()) {
      return;
    }
    MinecraftServer server = level.getServer();
    if (server != null && !server.isSameThread()) {
      return;
    }
    Object2ObjectMap<BoundingBox, BlockPos> boxes = bounds.computeIfAbsent(level.dimension(), (k) -> new Object2ObjectLinkedOpenHashMap<>());
    boxes.put(box, pos);
  }

  public static void unregister(Level level, BoundingBox box) {
    if (level == null || level.isClientSide()) {
      return;
    }
    MinecraftServer server = level.getServer();
    if (server != null && !server.isSameThread()) {
      return;
    }
    Object2ObjectMap<BoundingBox, BlockPos> boxes = bounds.get(level.dimension());
    if (boxes != null) {
      boxes.remove(box);
      if (boxes.isEmpty()) {
        bounds.remove(level.dimension());
      }
    }
  }
}
*/
