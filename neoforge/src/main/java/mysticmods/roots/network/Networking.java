package mysticmods.roots.network;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.network.client.*;
import mysticmods.roots.network.server.ServerBoundLibraryToStaffPacket;
import mysticmods.roots.network.server.ServerBoundSwapStaffSlotsPacket;
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
    registerMessage(ClientBoundHerbSyncPacket.class, ClientBoundHerbSyncPacket::encode, ClientBoundHerbSyncPacket::new, ClientBoundHerbSyncPacket::handle);
    registerMessage(ClientBoundGrantSyncPacket.class, ClientBoundGrantSyncPacket::encode, ClientBoundGrantSyncPacket::new, ClientBoundGrantSyncPacket::handle);
    registerMessage(ClientBoundShoulderRidePacket.class, ClientBoundShoulderRidePacket::encode, ClientBoundShoulderRidePacket::new, ClientBoundShoulderRidePacket::handle);
    registerMessage(ClientBoundOpenLibraryPacket.class, ClientBoundOpenLibraryPacket::encode, ClientBoundOpenLibraryPacket::new, ClientBoundOpenLibraryPacket::handle);
    registerMessage(ClientBoundUpdateStaffStackPacket.class, ClientBoundUpdateStaffStackPacket::encode, ClientBoundUpdateStaffStackPacket::new, ClientBoundUpdateStaffStackPacket::handle);
    registerMessage(ServerBoundSwapStaffSlotsPacket.class, ServerBoundSwapStaffSlotsPacket::encode, ServerBoundSwapStaffSlotsPacket::new, ServerBoundSwapStaffSlotsPacket::handle);
    registerMessage(ServerBoundLibraryToStaffPacket.class, ServerBoundLibraryToStaffPacket::encode, ServerBoundLibraryToStaffPacket::new, ServerBoundLibraryToStaffPacket::handle);
    registerMessage(ClientBoundReputationSyncPacket.class, ClientBoundReputationSyncPacket::encode, ClientBoundReputationSyncPacket::new, ClientBoundReputationSyncPacket::handle);
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
