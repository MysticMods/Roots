package epicsquid.roots.capability.playerdata;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerDataCapabilityStorage implements IStorage<IPlayerDataCapability> {


  @Nullable
  @Override
  public NBTBase writeNBT(Capability<IPlayerDataCapability> capability, IPlayerDataCapability instance, Direction side) {
    return instance.getData();
  }

  @Override
  public void readNBT(Capability<IPlayerDataCapability> capability, IPlayerDataCapability instance, Direction side, NBTBase nbt) {
    instance.setData((CompoundNBT) nbt);
  }
}