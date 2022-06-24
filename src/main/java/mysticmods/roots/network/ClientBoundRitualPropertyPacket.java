package mysticmods.roots.network;

import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundRitualPropertyPacket {
  public ClientBoundRitualPropertyPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      ResourceLocation rl = buffer.readResourceLocation();
      Property.RitualProperty<?> prop = ModRegistries.RITUAL_PROPERTY_REGISTRY.get().getValue(rl);
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
    Collection<Property.RitualProperty<?>> props = ModRegistries.RITUAL_PROPERTY_REGISTRY.get().getValues().stream().filter(Property::shouldSerialize).toList();
    buffer.writeVarInt(props.size());
    for (Property.RitualProperty<?> prop : props) {
      ResourceLocation rl = ModRegistries.RITUAL_PROPERTY_REGISTRY.get().getKey(prop);
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
    for (Ritual ritual : ModRegistries.RITUAL_REGISTRY.get().getValues()) {
      ritual.initialize();
    }
    context.get().setPacketHandled(true);
  }
}
