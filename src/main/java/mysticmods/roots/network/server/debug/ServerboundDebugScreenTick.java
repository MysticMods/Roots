package mysticmods.roots.network.server.debug;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Optional;

public record ServerboundDebugScreenTick(@Nullable InteractionHand hand, int inventorySlot) implements IRootsPacket {
  public static final Type<ServerboundDebugScreenTick> TYPE = new Type<>(RootsAPI.rl("server_bound_debug_screen_tick"));
  public static final StreamCodec<ByteBuf, ServerboundDebugScreenTick> CODEC = StreamCodec.composite(
      ByteBufCodecs.optional(ExtraStreamCodecs.INTERACTION_HAND_CODEC), o -> Optional.ofNullable(o.hand),
      ByteBufCodecs.VAR_INT, ServerboundDebugScreenTick::inventorySlot,
      ServerboundDebugScreenTick::new
  );

  public ServerboundDebugScreenTick (Optional<InteractionHand> hand, int inventorySlot) {
    this(hand.orElse(null), inventorySlot);
  }

  @Override
  public void handle(IPayloadContext context) {
    ServerDebugHooks.tryDroppingStaff(context.player(), hand, inventorySlot);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
