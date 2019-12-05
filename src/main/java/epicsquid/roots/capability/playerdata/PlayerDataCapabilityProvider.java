package epicsquid.roots.capability.playerdata;

import javax.annotation.Nonnull;

import epicsquid.roots.Roots;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerDataCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
  public static ResourceLocation IDENTIFIER = new ResourceLocation(Roots.MODID, "player_data_capability");

  @CapabilityInject(IPlayerDataCapability.class)
  public static final Capability<PlayerDataCapability> PLAYER_DATA_CAPABILITY = injected();

  private PlayerDataCapability instance = PLAYER_DATA_CAPABILITY.getDefaultInstance();

  @Override
  public CompoundNBT serializeNBT() {
    return (CompoundNBT) PLAYER_DATA_CAPABILITY.getStorage().writeNBT(PLAYER_DATA_CAPABILITY, this.instance, null);
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    PLAYER_DATA_CAPABILITY.getStorage().readNBT(PLAYER_DATA_CAPABILITY, this.instance, null, nbt);
  }

  @Override
  public boolean hasCapability(Capability<?> capability, Direction facing) {
    return capability == PLAYER_DATA_CAPABILITY;
  }

  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, Direction facing) {
    return capability == PLAYER_DATA_CAPABILITY ? PLAYER_DATA_CAPABILITY.cast(this.instance) : null;
  }

  //This is here to get rid of all the ugly PLAYER_GROVE might be null
  private static final Object NULL = null;

  @SuppressWarnings("unchecked")
  private static <T> T injected() {
    return (T) NULL;
  }

}