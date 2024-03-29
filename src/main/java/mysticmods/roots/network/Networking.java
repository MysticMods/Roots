package mysticmods.roots.network;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.network.client.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import noobanidus.libs.noobutil.network.PacketHandler;

public class Networking extends PacketHandler {
  public static Networking INSTANCE = new Networking();

  public Networking() {
    super(RootsAPI.MODID);
  }

  @Override
  public void registerMessages() {
    registerMessage(ClientBoundRitualPropertyPacket.class, ClientBoundRitualPropertyPacket::encode, ClientBoundRitualPropertyPacket::new, ClientBoundRitualPropertyPacket::handle);
    registerMessage(ClientBoundSpellPropertyPacket.class, ClientBoundSpellPropertyPacket::encode, ClientBoundSpellPropertyPacket::new, ClientBoundSpellPropertyPacket::handle);
    registerMessage(ClientBoundSpellCostsPacket.class, ClientBoundSpellCostsPacket::encode, ClientBoundSpellCostsPacket::new, ClientBoundSpellCostsPacket::handle);
    registerMessage(ClientBoundModifierCostsPacket.class, ClientBoundModifierCostsPacket::encode, ClientBoundModifierCostsPacket::new, ClientBoundModifierCostsPacket::handle);
    registerMessage(ClientBoundCapabilitySynchronization.class, ClientBoundCapabilitySynchronization::encode, ClientBoundCapabilitySynchronization::new, ClientBoundCapabilitySynchronization::handle);
  }

  public static void sendTo(Object msg, ServerPlayer player) {
    INSTANCE.sendToInternal(msg, player);
  }

  public static void sendToServer(Object msg) {
    INSTANCE.sendToServerInternal(msg);
  }

  public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
    INSTANCE.sendInternal(target, message);
  }
}
