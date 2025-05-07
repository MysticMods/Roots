package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CastChannelFailFXPacket(Spell spell, int casterId, int ticks) implements IRootsPacket {
  public static final Type<CastChannelFailFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/channel_fail"));
  public static final StreamCodec<RegistryFriendlyByteBuf, CastChannelFailFXPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), CastChannelFailFXPacket::spell,
      ByteBufCodecs.VAR_INT, CastChannelFailFXPacket::casterId,
      ByteBufCodecs.VAR_INT, CastChannelFailFXPacket::ticks,
      CastChannelFailFXPacket::new
  );

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.castChannelFail(spell, casterId, ticks);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
