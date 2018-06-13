package teamroots.roots.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import teamroots.roots.Roots;

public class PlayerDataProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound>{
	public DefaultPlayerDataCapability capability;
	public static final ResourceLocation PLAYER_DATA_CAPABILITY_LOC = new ResourceLocation(Roots.MODID+":player_data_capability");
	
	public PlayerDataProvider(){
		
	}
	
	public PlayerDataProvider(DefaultPlayerDataCapability c){
		this.capability = c;
	}
	
	@CapabilityInject(IPlayerDataCapability.class)
    public static final Capability<IPlayerDataCapability> playerDataCapability = null;
	
	@Override
	public NBTTagCompound serializeNBT() {
		return capability.getData();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		capability.setData(nbt);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == this.playerDataCapability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == this.playerDataCapability ? (T)this.capability : null;
	}

}
