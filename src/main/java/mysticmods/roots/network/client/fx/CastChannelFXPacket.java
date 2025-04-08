package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CastChannelFXPacket(Spell spell, int casterId, Vec3 casterPosition, Vec3 targetPosition, int ticks) implements IRootsPacket {
  public static final CustomPacketPayload.Type<CastChannelFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/channel"));
  public static final StreamCodec<RegistryFriendlyByteBuf, CastChannelFXPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), CastChannelFXPacket::spell,
      ByteBufCodecs.VAR_INT, CastChannelFXPacket::casterId,
      ExtraStreamCodecs.VEC3, CastChannelFXPacket::casterPosition,
      ExtraStreamCodecs.VEC3, CastChannelFXPacket::targetPosition,
      ByteBufCodecs.VAR_INT, CastChannelFXPacket::ticks,
      CastChannelFXPacket::new
  );

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.castChannel(spell, casterId, casterPosition, targetPosition, ticks);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
