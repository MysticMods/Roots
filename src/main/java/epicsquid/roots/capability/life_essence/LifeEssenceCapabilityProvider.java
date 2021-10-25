package epicsquid.roots.capability.life_essence;

import epicsquid.roots.Roots;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;

public class LifeEssenceCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
  public static final ResourceLocation IDENTIFIER = new ResourceLocation(Roots.MODID, "life_essence_capability");

  @CapabilityInject(LifeEssenceCapability.class)
  public static final Capability<LifeEssenceCapability> LIFE_ESSENCE_CAPABILITY = injected();

  private final LifeEssenceCapability instance = LIFE_ESSENCE_CAPABILITY.getDefaultInstance();

  @Override
  public CompoundNBT serializeNBT() {
    return (CompoundNBT) LIFE_ESSENCE_CAPABILITY.getStorage().writeNBT(LIFE_ESSENCE_CAPABILITY, this.instance, null);
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    LIFE_ESSENCE_CAPABILITY.getStorage().readNBT(LIFE_ESSENCE_CAPABILITY, this.instance, null, nbt);
  }

  @Override
  public boolean hasCapability(Capability<?> capability, Direction facing) {
    return capability == LIFE_ESSENCE_CAPABILITY;
  }

  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, Direction facing) {
    return capability == LIFE_ESSENCE_CAPABILITY ? LIFE_ESSENCE_CAPABILITY.cast(this.instance) : null;
  }

  //This is here to get rid of all the ugly PLAYER_GROVE might be null
  private static final Object NULL = null;

  @SuppressWarnings("unchecked")
  private static <T> T injected() {
    return (T) NULL;
  }
}
