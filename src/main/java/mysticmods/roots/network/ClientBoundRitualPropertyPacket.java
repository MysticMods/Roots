package mysticmods.roots.network;

import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundRitualPropertyPacket {
  public ClientBoundRitualPropertyPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      ResourceLocation rl = buffer.readResourceLocation();
      RitualProperty<?> prop = Registries.RITUAL_PROPERTY_REGISTRY.get().getValue(rl);
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
      ResourceLocation rl = Registries.RITUAL_PROPERTY_REGISTRY.get().getKey(prop);
      if (rl == null) {
      } else {
        buffer.writeResourceLocation(rl);
        prop.serializeNetwork(buffer);
      }
    }
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
  }

  private static void handle(ClientBoundRitualPropertyPacket message, Supplier<NetworkEvent.Context> context) {
    for (Ritual ritual : Registries.RITUAL_REGISTRY.get().getValues()) {
      ritual.init();
    }
    context.get().setPacketHandled(true);
  }
}
