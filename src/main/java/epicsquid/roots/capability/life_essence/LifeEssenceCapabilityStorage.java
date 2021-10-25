package epicsquid.roots.capability.life_essence;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class LifeEssenceCapabilityStorage implements Capability.IStorage<LifeEssenceCapability> {
  @Nullable
  @Override
  public NBTBase writeNBT(Capability<LifeEssenceCapability> capability, LifeEssenceCapability instance, Direction side) {
    return instance.writeNBT();
  }

  @Override
  public void readNBT(Capability<LifeEssenceCapability> capability, LifeEssenceCapability instance, Direction side, NBTBase nbt) {
    instance.readNBT(nbt);
  }
}
