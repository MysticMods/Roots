package mysticmods.roots.util;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.LightDrifterStorage;
import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.LightDrifterSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("resource")
public class LightDrifterUtil {
  @Nullable
  public static LightDrifterSnapshot getSnapshot(Player player) {
    if (!player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      return null;
    }
    LightDrifterEntity entity = getLightDrifterEntity(player);
    if (entity == null) {
      return null;
    }

    return SnapshotHelper.getSnapshot(entity, ModSerializers.LIGHT_DRIFTER.get());
  }

  @Nullable
  public static LightDrifterEntity getLightDrifterEntity(Player player) {
    if (!player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      return null;
    }

    Level level = player.level();

    if (level.isClientSide()) {
      LightDrifterStorage storage = player.getData(ModAttachments.DRIFTER_CLIENT_STORAGE);
      Entity e = level.getEntity(storage.entityId());
      if (e instanceof LightDrifterEntity drifter) {
        return drifter;
      } else if (storage.entityId() != -1) {
        RootsAPI.LOG.error("Light Drifter entity with ID {} not a LightDrifterEntity, is instead {}", storage.entityId(), e);
        return null;
      }
    } else {
      ServerLevel serverLevel = (ServerLevel) level;

      LightDrifterStorage storage = player.getData(ModAttachments.DRIFTER_SERVER_STORAGE);
      if (storage.id() == null) {
        RootsAPI.LOG.error("Light Drifter storage for player {} has null UUID", player);
        return null;
      }
      Entity e = serverLevel.getEntity(storage.id());
      if (e instanceof LightDrifterEntity drifter) {
        return drifter;
      } else if (storage.entityId() != -1) {
        RootsAPI.LOG.error("Light Drifter entity with ID {} not a LightDrifterEntity, is instead {}", storage.entityId(), e);
        return null;
      }
    }
    return null;
  }
}
