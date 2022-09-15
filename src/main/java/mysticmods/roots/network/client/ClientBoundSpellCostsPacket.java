package mysticmods.roots.network.client;

import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundSpellCostsPacket {
  public ClientBoundSpellCostsPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      int id = buffer.readVarInt();
      Spell prop = Registries.SPELL_REGISTRY.get().getValue(id);
      if (prop != null) {
        prop.setCosts(Cost.fromNetworkArray(buffer));
      } else {
        throw new IllegalStateException();
      }
    }
  }

  public ClientBoundSpellCostsPacket() {
  }

  public void encode(FriendlyByteBuf buffer) {
    Collection<Spell> props = Registries.SPELL_REGISTRY.get().getValues();
    buffer.writeVarInt(props.size());
    for (Spell spell : props) {
      int id = Registries.SPELL_REGISTRY.get().getID(spell);
      if (id == -1) {
        throw new IllegalStateException("tried to serialize a spell cost that doesn't exist: " + spell);
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
