package mysticmods.roots.network.client;

import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundSpellPropertyPacket {
  public ClientBoundSpellPropertyPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      int id = buffer.readVarInt();
      SpellProperty<?> prop = Registries.SPELL_PROPERTY_REGISTRY.get().getValue(id);
      if (prop != null) {
        prop.updateFromNetwork(buffer);
      } else {
        throw new IllegalStateException();
      }
    }
  }

  public ClientBoundSpellPropertyPacket() {
  }

  public void encode(FriendlyByteBuf buffer) {
    Collection<SpellProperty<?>> props = Registries.SPELL_PROPERTY_REGISTRY.get().getValues().stream().filter(Property::shouldSerialize).toList();
    buffer.writeVarInt(props.size());
    for (SpellProperty<?> prop : props) {
      int id = Registries.SPELL_PROPERTY_REGISTRY.get().getID(prop);
      if (id == -1) {
        throw new IllegalStateException("tried to serialize a spell property that doesn't exist: " + prop);
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

  private static void handle(ClientBoundSpellPropertyPacket message, Supplier<NetworkEvent.Context> context) {
    for (Spell spell : Registries.SPELL_REGISTRY.get().getValues()) {
      spell.init();
    }
  }
}
