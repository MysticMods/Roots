package mysticmods.roots.mixin;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.capability.PlayerShoulderCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: Make this so much better
@Mixin(Player.class)
public class MixinPlayer {
  @Inject(method = "removeEntitiesOnShoulder", at = @At("HEAD"), cancellable = true)
  protected void RootsAvoidRemovingBeetles(CallbackInfo info) {
    Player player = (Player) (Object) this;

    if (player.timeEntitySatOnShoulder + 20L < player.level.getGameTime()) {
      CompoundTag leftNBT = player.getShoulderEntityLeft();
      CompoundTag rightNBT = player.getShoulderEntityRight();
      if (!leftNBT.isEmpty() && !leftNBT.getString("id").equals("roots:beetle")) {
        player.respawnEntityOnShoulder(leftNBT);
        try {
          PlayerShoulderCapability.setLeftShoulder.invokeExact(player, new CompoundTag());
        } catch (Throwable e) {
          RootsAPI.LOG.error("Unable to unset left shoulder entity data for player " + player, e);
        }
      }
      if (!rightNBT.isEmpty() && !rightNBT.getString("id").equals("roots:beetle")) {
        player.respawnEntityOnShoulder(rightNBT);
        try {
          PlayerShoulderCapability.setRightShoulder.invokeExact(player, new CompoundTag());
        } catch (Throwable e) {
          RootsAPI.LOG.error("Unable to unset right shoulder entity data for player " + player, e);
        }
      }
    }
    info.cancel();
  }
}
