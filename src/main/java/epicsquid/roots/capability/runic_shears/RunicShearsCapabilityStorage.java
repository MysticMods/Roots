package epicsquid.roots.capability.runic_shears;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class RunicShearsCapabilityStorage implements Capability.IStorage<RunicShearsCapability> {
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, EnumFacing side) {
		return instance.writeNBT();
	}
	
	@Override
	public void readNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, EnumFacing side, NBTBase nbt) {
		instance.readNBT(nbt);
	}
}
