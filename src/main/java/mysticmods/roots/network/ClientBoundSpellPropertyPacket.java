package mysticmods.roots.network;

import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundSpellPropertyPacket {
  public ClientBoundSpellPropertyPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      ResourceLocation rl = buffer.readResourceLocation();
      Property.SpellProperty<?> prop = ModRegistries.SPELL_PROPERTY_REGISTRY.get().getValue(rl);
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
    Collection<Property.SpellProperty<?>> props = ModRegistries.SPELL_PROPERTY_REGISTRY.get().getValues().stream().filter(Property::shouldSerialize).toList();
    buffer.writeVarInt(props.size());
    for (Property.SpellProperty<?> prop : props) {
      ResourceLocation rl = ModRegistries.SPELL_PROPERTY_REGISTRY.get().getKey(prop);
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

  private static void handle(ClientBoundSpellPropertyPacket message, Supplier<NetworkEvent.Context> context) {
    for (Spell spell : ModRegistries.SPELL_REGISTRY.get().getValues()) {
      spell.initialize();
    }
    context.get().setPacketHandled(true);
  }
}
