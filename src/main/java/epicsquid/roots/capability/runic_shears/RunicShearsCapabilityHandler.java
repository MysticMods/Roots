package epicsquid.roots.capability.runic_shears;

import epicsquid.roots.Roots;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RunicShearsCapabilityHandler implements ICapabilitySerializable<NBTTagLong> {
  public static final String CAPABILITY_NAME = "runic_shears_capability";

  public static final ResourceLocation IDENTIFIER = new ResourceLocation(Roots.MODID, CAPABILITY_NAME);

  @CapabilityInject(RunicShearsCapability.class)
  public static Capability<RunicShearsCapability> INSTANCE = null;

  private final RunicShearsCapability capability;

  public RunicShearsCapabilityHandler() {
    this.capability = new RunicShearsCapability();
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == INSTANCE;
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    return (capability == INSTANCE) ? (T) this :null;
  }

  @Override
  public NBTTagLong serializeNBT() {
    return capability.serializeNBT();
  }

  @Override
  public void deserializeNBT(NBTTagLong nbt) {
    capability.deserializeNBT(nbt);
  }

  public static class RunicShearsCapabilityStorage implements Capability.IStorage<RunicShearsCapability> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, EnumFacing side) {
      return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<RunicShearsCapability> capability, RunicShearsCapability instance, EnumFacing side, NBTBase nbt) {
      if (nbt.getId() == Constants.NBT.TAG_LONG) {
        instance.deserializeNBT((NBTTagLong) nbt);
      } else {
        instance.setCooldown(0);
      }
    }
  }
}
