package epicsquid.roots.capability;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerGroveCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

  @CapabilityInject(IPlayerGroveCapability.class) public static final Capability<PlayerGroveCapability> PLAYER_GROVE_CAPABILITY = injected();

  private PlayerGroveCapability instance = PLAYER_GROVE_CAPABILITY.getDefaultInstance();

  @Override
  public NBTTagCompound serializeNBT() {
    return (NBTTagCompound) PLAYER_GROVE_CAPABILITY.getStorage().writeNBT(PLAYER_GROVE_CAPABILITY, this.instance, null);
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    PLAYER_GROVE_CAPABILITY.getStorage().readNBT(PLAYER_GROVE_CAPABILITY, this.instance, null, nbt);
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return capability == PLAYER_GROVE_CAPABILITY;
  }

  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
    return capability == PLAYER_GROVE_CAPABILITY ? PLAYER_GROVE_CAPABILITY.cast(this.instance) : null;
  }

  //This is here to get rid of all the ugly PLAYER_GROVE might be null
  private static final Object NULL = null;

  @SuppressWarnings("unchecked")
  private static <T> T injected() {
    return (T) NULL;
  }

}