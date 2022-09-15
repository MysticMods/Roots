package mysticmods.roots.api.capability;

import net.minecraft.network.FriendlyByteBuf;

public interface INetworkedCapability<T extends INetworkedCapability.SerializedCapability> {
  void fromRecord (T record);
  T toRecord ();

  void setDirty(boolean dirty);

  boolean isDirty();

  interface SerializedCapability {
    void fromNetwork (FriendlyByteBuf buf);
    void toNetwork (FriendlyByteBuf buf);
  }
}
