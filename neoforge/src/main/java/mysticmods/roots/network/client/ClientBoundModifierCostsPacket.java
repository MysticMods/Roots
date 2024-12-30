package mysticmods.roots.network.client;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.FriendlyByteBuf;


import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundModifierCostsPacket {
  public ClientBoundModifierCostsPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      int id = buffer.readVarInt();
      SpellModifier prop = RootsRegistries.MODIFIER_REGISTRY.get().getValue(id);
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
    Collection<SpellModifier> props = RootsRegistries.MODIFIER_REGISTRY.get().getValues();
    buffer.writeVarInt(props.size());
    for (SpellModifier spell : props) {
      int id = RootsRegistries.MODIFIER_REGISTRY.get().getID(spell);
      if (id == -1) {
        throw new IllegalStateException("tried to serialize a modifier that doesn't exist: " + spell);
      } else {
        buffer.writeVarInt(id);
        buffer.writeVarInt(spell.getCosts().size());
        for (Cost cost : spell.getCosts()) {
          cost.toNetwork(buffer);
        }
      }
    }
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().setPacketHandled(true);
  }
}
