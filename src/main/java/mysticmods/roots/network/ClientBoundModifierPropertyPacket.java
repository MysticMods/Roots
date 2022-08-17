package mysticmods.roots.network;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.ModifierProperty;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundModifierPropertyPacket {
  public ClientBoundModifierPropertyPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      int id = buffer.readVarInt();
      ModifierProperty<?> prop = Registries.MODIFIER_PROPERTY_REGISTRY.get().getValue(id);
      if (prop != null) {
        prop.updateFromNetwork(buffer);
      } else {
        throw new IllegalStateException();
      }
    }
  }

  public ClientBoundModifierPropertyPacket() {
  }

  public void encode(FriendlyByteBuf buffer) {
    Collection<ModifierProperty<?>> props = Registries.MODIFIER_PROPERTY_REGISTRY.get().getValues().stream().filter(Property::shouldSerialize).toList();
    buffer.writeVarInt(props.size());
    for (ModifierProperty<?> prop : props) {
      int id = Registries.MODIFIER_PROPERTY_REGISTRY.get().getID(prop);
      if (id == -1) {
        throw new IllegalStateException("tried to serialize a property that doesn't exist: " + prop);
      } else {
        buffer.writeVarInt(id);
        prop.serializeNetwork(buffer);
      }
    }
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
  }

  private static void handle(ClientBoundModifierPropertyPacket message, Supplier<NetworkEvent.Context> context) {
    for (Modifier modifier : Registries.MODIFIER_REGISTRY.get().getValues()) {
      modifier.initialize();
    }
    context.get().setPacketHandled(true);
  }
}
