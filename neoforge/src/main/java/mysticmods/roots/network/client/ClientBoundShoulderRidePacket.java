package mysticmods.roots.network.client;


import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.IPlayerShoulderCapability;
import mysticmods.roots.capability.PlayerShoulderCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientBoundShoulderRidePacket {
  private final CompoundTag tag;
  private final UUID id;

  public ClientBoundShoulderRidePacket(FriendlyByteBuf buffer) {
    long uuid1 = buffer.readLong();
    long uuid2 = buffer.readLong();
    id = new UUID(uuid1, uuid2);
    tag = buffer.readNbt();
  }

  public ClientBoundShoulderRidePacket(Player player, IPlayerShoulderCapability cap) {
    this.tag = cap.serializeNBT();
    this.id = player.getUUID();
  }

  public CompoundTag getTag() {
    return tag;
  }

  public UUID getId() {
    return id;
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeLong(id.getMostSignificantBits());
    buf.writeLong(id.getLeastSignificantBits());
    buf.writeNbt(tag);
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  @OnlyIn(Dist.CLIENT)
  private static void handle(ClientBoundShoulderRidePacket message, Supplier<NetworkEvent.Context> context) {
    Player target = Minecraft.getInstance().player;
    if (target == null) {
      return;
    }
    Level world = target.level;
    if (!target.getUUID().equals(message.getId())) {
      target = world.getPlayerByUUID(message.getId());
    }

    if (target == null) {
      return;
    }

    final Player player = target;

    target.getCapability(Capabilities.PLAYER_SHOULDER_CAPABILITY).ifPresent((cap) -> {
      cap.deserializeNBT(message.getTag());
      try {
        PlayerShoulderCapability.setLeftShoulder.invokeExact(player, cap.generateShoulderNBT());
      } catch (Throwable throwable) {
        RootsAPI.LOG.error("Unable to fake player having a shoulder entity", throwable);
      }
    });

    context.get().setPacketHandled(true);
  }
}

