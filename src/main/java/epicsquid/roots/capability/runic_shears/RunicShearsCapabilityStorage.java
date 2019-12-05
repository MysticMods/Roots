package epicsquid.roots.capability.runic_shears;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class RunicShearsCapabilityStorage implements Capability.IStorage<RunicShearsCapability> {
  @Nullable
  @Override
  public NBTBase writeNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, Direction side) {
    return instance.writeNBT();
  }

  @Override
  public void readNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, Direction side, NBTBase nbt) {
    instance.readNBT(nbt);
  }
}
