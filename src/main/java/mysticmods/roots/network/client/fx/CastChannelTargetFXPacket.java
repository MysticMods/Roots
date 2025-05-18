package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CastChannelTargetFXPacket(Spell spell, int casterId, Vec3 casterPosition, Vec3 targetPosition,
                                        int ticks) implements IRootsPacket {
  public static final CustomPacketPayload.Type<CastChannelTargetFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/channel_target"));
  public static final StreamCodec<RegistryFriendlyByteBuf, CastChannelTargetFXPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), CastChannelTargetFXPacket::spell,
      ByteBufCodecs.VAR_INT, CastChannelTargetFXPacket::casterId,
      ExtraStreamCodecs.VEC3, CastChannelTargetFXPacket::casterPosition,
      ExtraStreamCodecs.VEC3, CastChannelTargetFXPacket::targetPosition,
      ByteBufCodecs.VAR_INT, CastChannelTargetFXPacket::ticks,
      CastChannelTargetFXPacket::new
  );

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.castChannelTarget(spell, casterId, casterPosition, targetPosition, ticks);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
