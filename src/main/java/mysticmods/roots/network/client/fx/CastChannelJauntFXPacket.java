package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CastChannelJauntFXPacket(Spell spell, int casterId, int ticks) implements IRootsPacket {
  public static final Type<CastChannelJauntFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/channel_jaunt"));
  public static final StreamCodec<RegistryFriendlyByteBuf, CastChannelJauntFXPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), CastChannelJauntFXPacket::spell,
      ByteBufCodecs.VAR_INT, CastChannelJauntFXPacket::casterId,
      ByteBufCodecs.VAR_INT, CastChannelJauntFXPacket::ticks,
      CastChannelJauntFXPacket::new
  );

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.castChannelJaunt(spell, casterId, ticks);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
