package mysticmods.roots.api.capability;

import net.minecraft.network.FriendlyByteBuf;

public interface INetworkedCapability {
  void toNetwork(FriendlyByteBuf buf);
  void fromNetwork(FriendlyByteBuf buf);
}
