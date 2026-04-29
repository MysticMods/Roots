package mysticmods.roots.util;

import net.minecraft.world.entity.Entity;

public interface IVehicleMoveAccessor {
  void roots$setVehicleFirstGood(double x, double y, double z);

  void roots$setVehicleLastGood(double x, double y, double z);

  void roots$setClientVehicleIsFloating(boolean value);

  void roots$setLastVehicle(Entity vehicle);

  void roots$ignoreVehiclePackets(int count);

  default void roots$handleTeleportVehicle(double x, double y, double z, Entity entity) {
    roots$setVehicleFirstGood(x, y, z);
    roots$setVehicleLastGood(x, y, z);
    roots$setClientVehicleIsFloating(false);
    roots$setLastVehicle(entity);
    roots$ignoreVehiclePackets(2);
  }
}
