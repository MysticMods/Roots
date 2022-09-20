package mysticmods.roots.network.client;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundModifierCostsPacket {
  public ClientBoundModifierCostsPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      int id = buffer.readVarInt();
      Modifier prop = Registries.MODIFIER_REGISTRY.get().getValue(id);
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
      int id = Registries.MODIFIER_REGISTRY.get().getID(spell);
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
