package epicsquid.roots.capability.life_essence;

import epicsquid.roots.Roots;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;

public class LifeEssenceCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	public static final ResourceLocation IDENTIFIER = new ResourceLocation(Roots.MODID, "life_essence_capability");
	
	@CapabilityInject(LifeEssenceCapability.class)
	public static final Capability<LifeEssenceCapability> LIFE_ESSENCE_CAPABILITY = injected();
	
	private final LifeEssenceCapability instance = LIFE_ESSENCE_CAPABILITY.getDefaultInstance();
	
	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) LIFE_ESSENCE_CAPABILITY.getStorage().writeNBT(LIFE_ESSENCE_CAPABILITY, this.instance, null);
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		LIFE_ESSENCE_CAPABILITY.getStorage().readNBT(LIFE_ESSENCE_CAPABILITY, this.instance, null, nbt);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == LIFE_ESSENCE_CAPABILITY;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
		return capability == LIFE_ESSENCE_CAPABILITY ? LIFE_ESSENCE_CAPABILITY.cast(this.instance) : null;
	}
	
	//This is here to get rid of all the ugly PLAYER_GROVE might be null
	private static final Object NULL = null;
	
	@SuppressWarnings("unchecked")
	private static <T> T injected() {
		return (T) NULL;
	}
}
