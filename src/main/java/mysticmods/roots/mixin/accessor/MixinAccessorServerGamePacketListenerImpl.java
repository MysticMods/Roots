package mysticmods.roots.mixin.accessor;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.util.IVehicleMoveAccessor;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinAccessorServerGamePacketListenerImpl implements IVehicleMoveAccessor {
  @Shadow
  private double vehicleFirstGoodX;
  @Shadow
  private double vehicleFirstGoodY;
  @Shadow
  private double vehicleFirstGoodZ;
  @Shadow
  private double vehicleLastGoodX;
  @Shadow
  private double vehicleLastGoodY;
  @Shadow
  private double vehicleLastGoodZ;
  @Shadow
  private boolean clientVehicleIsFloating;
  @Shadow
  private Entity lastVehicle;

  @Unique
  private int roots$ignoreVehiclePackets;

  @Override
  public void roots$setVehicleFirstGood(double x, double y, double z) {
    this.vehicleFirstGoodX = x;
    this.vehicleFirstGoodY = y;
    this.vehicleFirstGoodZ = z;
  }

  @Override
  public void roots$setVehicleLastGood(double x, double y, double z) {
    this.vehicleLastGoodX = x;
    this.vehicleLastGoodY = y;
    this.vehicleLastGoodZ = z;
  }

  @Override
  public void roots$setClientVehicleIsFloating(boolean value) {
    this.clientVehicleIsFloating = value;
  }

  @Override
  public void roots$setLastVehicle(Entity vehicle) {
    this.lastVehicle = vehicle;
  }

  @Override
  public void roots$ignoreVehiclePackets(int count) {
    this.roots$ignoreVehiclePackets = Math.max(this.roots$ignoreVehiclePackets, count);
  }

  @WrapMethod(method = "handleMoveVehicle")
  private void roots$wrapHandleMoveVehicle(ServerboundMoveVehiclePacket packet, Operation<Void> original) {
    if (this.roots$ignoreVehiclePackets > 0) {
      this.roots$ignoreVehiclePackets--;
      return;
    }
    original.call(packet);
  }
}
