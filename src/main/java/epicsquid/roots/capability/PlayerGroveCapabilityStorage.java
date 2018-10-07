package epicsquid.roots.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerGroveCapabilityStorage implements Capability.IStorage<IPlayerGroveCapability> {

  @Nullable
  @Override
  public NBTBase writeNBT(Capability<IPlayerGroveCapability> capability, IPlayerGroveCapability instance, EnumFacing side) {
    return instance.getData();
  }

  @Override
  public void readNBT(Capability<IPlayerGroveCapability> capability, IPlayerGroveCapability instance, EnumFacing side, NBTBase nbt) {
    instance.setData((NBTTagCompound) nbt);
  }
}