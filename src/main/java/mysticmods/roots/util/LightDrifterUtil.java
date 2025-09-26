package mysticmods.roots.util;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.LightDrifterStorage;
import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SuppressWarnings("resource")
public class LightDrifterUtil {
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
      } else {
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
      } else {
        RootsAPI.LOG.error("Light Drifter entity with ID {} not a LightDrifterEntity, is instead {}", storage.entityId(), e);
        return null;
      }
    }
  }
}
