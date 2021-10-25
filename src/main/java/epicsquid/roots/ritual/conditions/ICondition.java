package epicsquid.roots.ritual.conditions;

import epicsquid.roots.tileentity.TileEntityPyre;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface ICondition {

  default boolean check(TileEntityPyre tile, @Nullable PlayerEntity player) {
    if (!checkCondition(tile, player)) {
      if (player != null) {
        player.sendMessage(failMessage());
      }
      return false;
    }
    return true;
  }

  boolean checkCondition(TileEntityPyre tile, @Nullable PlayerEntity player);

  @Nullable
  ITextComponent failMessage();
}
