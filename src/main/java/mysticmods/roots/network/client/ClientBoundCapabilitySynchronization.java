package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.api.capability.HerbCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundCapabilitySynchronization {
  private Player player;
  private ResourceLocation capabilityId;

  public ClientBoundCapabilitySynchronization(FriendlyByteBuf buffer) {
    Player player = RootsAPI.getInstance().getPlayer();
    if (player == null) {
      throw new IllegalStateException("Tried to read a client-bound capability synchronization packet but the player is null");
    }
    ResourceLocation cap = buffer.readResourceLocation();
    boolean present = buffer.readBoolean();
    if (present) {
      if (cap.equals(RootsAPI.GRANT_CAPABILITY_ID)) {
        player.getCapability(Capabilities.GRANT_CAPABILITY).ifPresent(capability -> {
          capability.fromNetwork(buffer);
        });
      } else if (cap.equals(RootsAPI.HERB_CAPABILITY_ID)) {
        player.getCapability(Capabilities.HERB_CAPABILITY).ifPresent(capability -> {
          capability.fromNetwork(buffer);
        });
      }
    }
  }

  public ClientBoundCapabilitySynchronization(Player player, ResourceLocation capabilityId) {
    this.player = player;
    this.capabilityId = capabilityId;
  }

  public void encode(FriendlyByteBuf buffer) {
    buffer.writeResourceLocation(capabilityId);
    if (capabilityId.equals(RootsAPI.HERB_CAPABILITY_ID)) {
      LazyOptional<HerbCapability> capability = player.getCapability(Capabilities.HERB_CAPABILITY);
      buffer.writeBoolean(capability.isPresent());
      if (capability.isPresent()) {
        HerbCapability cap = capability.orElseThrow(() -> new IllegalStateException("Herb capability is not present for '" + player));
        cap.toNetwork(buffer);
      }
    } else if (capabilityId.equals(RootsAPI.GRANT_CAPABILITY_ID)) {
      LazyOptional<GrantCapability> capability = player.getCapability(Capabilities.GRANT_CAPABILITY);
      buffer.writeBoolean(capability.isPresent());
      if (capability.isPresent()) {
        GrantCapability cap = capability.orElseThrow(() -> new IllegalStateException("Grant capability is not present for '" + player));
        cap.toNetwork(buffer);
      }
    } else {
      throw new IllegalArgumentException("Unknown capability id: " + capabilityId);
    }
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
  }

  private static void handle(ClientBoundCapabilitySynchronization message, Supplier<NetworkEvent.Context> context) {
    context.get().setPacketHandled(true);
  }
}
