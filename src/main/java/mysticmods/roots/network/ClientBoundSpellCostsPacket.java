package mysticmods.roots.network;

import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spells.Spell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

public class ClientBoundSpellCostsPacket {
  public ClientBoundSpellCostsPacket(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    for (int i = 0; i < count; i++) {
      ResourceLocation rl = buffer.readResourceLocation();
      Spell prop = Registries.SPELL_REGISTRY.get().getValue(rl);
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
      ResourceLocation rl = Registries.SPELL_REGISTRY.get().getKey(spell);
      if (rl == null) {
      } else {
        buffer.writeResourceLocation(rl);
        buffer.writeVarInt(spell.getCosts().size());
        for (Cost cost : spell.getCosts()) {
          cost.toNetwork(buffer);
        }
      }
    }
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
  }

  private static void handle(ClientBoundSpellCostsPacket message, Supplier<NetworkEvent.Context> context) {
    context.get().setPacketHandled(true);
  }
}
