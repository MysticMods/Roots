package mysticmods.roots.api.capability;

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
