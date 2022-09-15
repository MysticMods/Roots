package mysticmods.roots.network.client;

import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundRitualPropertyPacket {
  public ClientBoundRitualPropertyPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      int id = buffer.readVarInt();
      RitualProperty<?> prop = Registries.RITUAL_PROPERTY_REGISTRY.get().getValue(id);
      if (prop != null) {
        prop.updateFromNetwork(buffer);
      } else {
        throw new IllegalStateException();
      }
    }
  }

  public ClientBoundRitualPropertyPacket() {
  }

  public void encode(FriendlyByteBuf buffer) {
    Collection<RitualProperty<?>> props = Registries.RITUAL_PROPERTY_REGISTRY.get().getValues().stream().filter(Property::shouldSerialize).toList();
    buffer.writeVarInt(props.size());
    for (RitualProperty<?> prop : props) {
      int id = Registries.RITUAL_PROPERTY_REGISTRY.get().getID(prop);
      if (id == -1) {
        throw new IllegalStateException("tried to serialize a ritual property that doesn't exist: " + prop);
      } else {
        buffer.writeVarInt(id);
        prop.serializeNetwork(buffer);
      }
    }
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ClientBoundRitualPropertyPacket message, Supplier<NetworkEvent.Context> context) {
    for (Ritual ritual : Registries.RITUAL_REGISTRY.get().getValues()) {
      ritual.init();
    }
  }
}
