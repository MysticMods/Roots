package epicsquid.mysticallib.item.tool;

import net.minecraft.util.EnumFacing;

import java.util.Set;

public interface ILimitAxis {
  Set<EnumFacing.Axis> getLimits ();
}
