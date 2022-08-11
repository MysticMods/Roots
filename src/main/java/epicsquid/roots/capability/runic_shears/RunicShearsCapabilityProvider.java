package epicsquid.roots.capability.runic_shears;

import epicsquid.roots.Roots;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;

public class RunicShearsCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation IDENTIFIER = new ResourceLocation(Roots.MODID, "new_runic_shears_capability");
	
	@CapabilityInject(RunicShearsCapability.class)
	public static final Capability<RunicShearsCapability> RUNIC_SHEARS_CAPABILITY = injected();
	
	private final RunicShearsCapability instance = RUNIC_SHEARS_CAPABILITY.getDefaultInstance();
	
	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) RUNIC_SHEARS_CAPABILITY.getStorage().writeNBT(RUNIC_SHEARS_CAPABILITY, this.instance, null);
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		RUNIC_SHEARS_CAPABILITY.getStorage().readNBT(RUNIC_SHEARS_CAPABILITY, this.instance, null, nbt);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == RUNIC_SHEARS_CAPABILITY;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
		return capability == RUNIC_SHEARS_CAPABILITY ? RUNIC_SHEARS_CAPABILITY.cast(this.instance) : null;
	}
	
	//This is here to get rid of all the ugly PLAYER_GROVE might be null
	private static final Object NULL = null;
	
	@SuppressWarnings("unchecked")
	private static <T> T injected() {
		return (T) NULL;
	}
}
