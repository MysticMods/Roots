package epicsquid.roots.recipe.conditions;

import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;

public interface Condition {

  boolean checkCondition(TileEntityBonfire tile, EntityPlayer player);
}
