package epicsquid.roots.capability.grove;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerGroveCapabilityStorage implements Capability.IStorage<IPlayerGroveCapability> {

  @Nullable
  @Override
  public NBTBase writeNBT(Capability<IPlayerGroveCapability> capability, IPlayerGroveCapability instance, Direction side) {
    return instance.getData();
  }

  @Override
  public void readNBT(Capability<IPlayerGroveCapability> capability, IPlayerGroveCapability instance, Direction side, NBTBase nbt) {
    instance.setData((CompoundNBT) nbt);
  }
}