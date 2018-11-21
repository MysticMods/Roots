package epicsquid.roots.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerDataCapabilityStorage implements IStorage<IPlayerDataCapability>{


	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IPlayerDataCapability> capability, IPlayerDataCapability instance, EnumFacing side) {
		return instance.getData();
	}

	@Override
	public void readNBT(Capability<IPlayerDataCapability> capability, IPlayerDataCapability instance, EnumFacing side, NBTBase nbt) {
		instance.setData((NBTTagCompound) nbt);
	}
}