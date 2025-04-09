package mysticmods.roots.network.client.fx;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AnimalHarvestFXPacket (int entityId) implements IRootsPacket {
  public static final CustomPacketPayload.Type<AnimalHarvestFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/animal_harvest"));
  public static final StreamCodec<FriendlyByteBuf, AnimalHarvestFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, AnimalHarvestFXPacket::entityId, AnimalHarvestFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.animalHarvest(entityId);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
