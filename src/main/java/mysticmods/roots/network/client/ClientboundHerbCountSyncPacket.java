package mysticmods.roots.network.client;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundHerbCountSyncPacket(Object2DoubleMap<Herb> data) implements IRootsPacket {
  public static final CustomPacketPayload.Type<ClientboundHerbCountSyncPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_bound_herb_count_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundHerbCountSyncPacket> CODEC = StreamCodec.composite(ByteBufCodecs.map(Object2DoubleOpenHashMap::new, ByteBufCodecs.registry(RootsRegistries.Keys.HERBS), ByteBufCodecs.DOUBLE), ClientboundHerbCountSyncPacket::data, ClientboundHerbCountSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.setHerbCount(this.data);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
