package mysticmods.roots.network;

import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundModifierCostsPacket {
  public ClientBoundModifierCostsPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      ResourceLocation rl = buffer.readResourceLocation();
      Modifier prop = Registries.MODIFIER_REGISTRY.get().getValue(rl);
      if (prop != null) {
        prop.setCosts(Cost.fromNetworkArray(buffer));
      } else {
        throw new IllegalStateException();
      }
    }
  }

  public ClientBoundModifierCostsPacket() {
  }

  public void encode(FriendlyByteBuf buffer) {
    Collection<Modifier> props = Registries.MODIFIER_REGISTRY.get().getValues();
    buffer.writeVarInt(props.size());
    for (Modifier spell : props) {
      ResourceLocation rl = Registries.MODIFIER_REGISTRY.get().getKey(spell);
      if (rl == null) {
      } else {
        buffer.writeResourceLocation(rl);
        buffer.writeVarInt(spell.getCosts().size());
        for (Cost cost : spell.getCosts()) {
          cost.toNetwork(buffer);
        }
      }
    }
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
  }

  private static void handle(ClientBoundModifierCostsPacket message, Supplier<NetworkEvent.Context> context) {
    context.get().setPacketHandled(true);
  }
}
