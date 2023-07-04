package mysticmods.roots.api.capability;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/*public class ReputationCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>, INetworkedCapability<ReputationCapability.SerializedReputationRecord> {
  private boolean dirty = false;

  public ReputationCapability() {
  }

  @Override
  public SerializedReputationRecord toRecord() {
    return null;
*//*    return new SerializedReputationRecord();*//*
  }

  @Override
  public void fromRecord (SerializedReputationRecord record) {
  }

  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag result = new CompoundTag();
    return result;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    setDirty(true);
  }

  @NotNull
  @Override
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return Capabilities.REPUTATION_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
  }

  public static SerializedReputationRecord fromNetwork(FriendlyByteBuf buf) {
    SerializedReputationRecord result = new SerializedReputationRecord();
    result.fromNetwork(buf);
    return result;
  }

  public static class SerializedReputationRecord implements SerializedCapability {
    public SerializedReputationRecord() {
    }

    @Override
    public void fromNetwork(FriendlyByteBuf buf) {
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
    }
  }
}*/
