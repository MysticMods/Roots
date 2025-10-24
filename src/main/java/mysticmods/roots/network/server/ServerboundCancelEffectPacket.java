package mysticmods.roots.network.server;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundCancelEffectPacket(Holder<MobEffect> effect) implements IRootsPacket {
  public static final Type<ServerboundCancelEffectPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_cancel_effect_packet"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCancelEffectPacket> CODEC = ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT)
      .map(ServerboundCancelEffectPacket::new, ServerboundCancelEffectPacket::effect);

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.cancelEffect(context.player(), effect);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
