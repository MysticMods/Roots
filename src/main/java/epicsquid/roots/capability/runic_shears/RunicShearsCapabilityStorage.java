package epicsquid.roots.capability.runic_shears;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class RunicShearsCapabilityStorage implements Capability.IStorage<RunicShearsCapability> {
  @Nullable
  @Override
  public NBTBase writeNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, EnumFacing side) {
    return new NBTTagLong(instance.getCooldown());
  }

  @Override
  public void readNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, EnumFacing side, NBTBase nbt) {
    if (nbt.getId() == Constants.NBT.TAG_LONG) {
      instance.setCooldown(((NBTTagLong) nbt).getLong());
    } else {
      instance.setCooldown(0);
    }
  }
}
