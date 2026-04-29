package mysticmods.roots.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class TeleportUtil {
  public static void teleportWithVehicle(ServerPlayer player, ServerLevel level, double x, double y, double z, Set<RelativeMovement> relativeMovements, float yRot, float xRot) {
    ChunkPos chunkpos = new ChunkPos(BlockPos.containing(x, y, z));
    level.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.getId());

    var vehicle = player.getVehicle();
    if (vehicle != null) {
      if (vehicle.level() == level) {
        vehicle.teleportTo(level, x, y, z, relativeMovements, yRot, xRot);
        ((IVehicleMoveAccessor) player.connection).roots$handleTeleportVehicle(x, y, z, vehicle);
        player.connection.send(new ClientboundMoveVehiclePacket(vehicle));
      } else {
        vehicle.changeDimension(
            new DimensionTransition(level, new Vec3(x, y, z), Vec3.ZERO, yRot, xRot, DimensionTransition.DO_NOTHING)
        );
      }
    } else {
      player.teleportTo(level, x, y, z, relativeMovements, yRot, xRot);
    }
  }
}
