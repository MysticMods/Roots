package epicsquid.roots.capability.runic_shears;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class RunicShearsCapabilityStorage implements Capability.IStorage<RunicShearsCapability> {
  @Nullable
  @Override
  public INBT writeNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, Direction side) {
    return new LongNBT(instance.getCooldown());
  }

  @Override
  public void readNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, Direction side, INBT nbt) {
    if (nbt.getId() == Constants.NBT.TAG_LONG) {
      instance.setActualCooldown(((LongNBT) nbt).getLong());
    } else {
      instance.setActualCooldown(0);
    }
  }
}
