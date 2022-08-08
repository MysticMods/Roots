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
      ResourceLocation rl = buffer.readResourceLocation();
      ModifierProperty<?> prop = Registries.MODIFIER_PROPERTY_REGISTRY.get().getValue(rl);
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
      ResourceLocation rl = Registries.MODIFIER_PROPERTY_REGISTRY.get().getKey(prop);
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

  private static void handle(ClientBoundModifierPropertyPacket message, Supplier<NetworkEvent.Context> context) {
    for (Modifier modifier : Registries.MODIFIER_REGISTRY.get().getValues()) {
      modifier.initialize();
    }
    context.get().setPacketHandled(true);
  }
}
