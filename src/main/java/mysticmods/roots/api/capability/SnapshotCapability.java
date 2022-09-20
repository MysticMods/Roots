package mysticmods.roots.api.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SnapshotCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {



  public void setDirty(boolean dirty) {

  }

  public boolean isDirty() {
    return false;
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return null;
  }

  @Override
  public CompoundTag serializeNBT() {
    return null;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {

  }
}
