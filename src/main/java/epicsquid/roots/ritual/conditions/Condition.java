package epicsquid.roots.ritual.conditions;

import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface Condition {

  default boolean check (TileEntityBonfire tile, @Nullable EntityPlayer player) {
    if (!checkCondition(tile, player)) {
      if (player != null) {
        player.sendMessage(failMessage());
      }
      return false;
    }
    return true;
  }

  boolean checkCondition(TileEntityBonfire tile, @Nullable EntityPlayer player);

  @Nullable
  ITextComponent failMessage ();
}
