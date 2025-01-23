package mysticmods.roots.network.server;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Optional;

public record ServerboundSwapSpellsPacket(@Nullable InteractionHand hand, int inventorySlot, int slot1, int slot2) implements IRootsPacket {
  public static final Type<ServerboundSwapSpellsPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_swap_spells"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSwapSpellsPacket> CODEC =
      StreamCodec.composite(
          ByteBufCodecs.optional(ExtraStreamCodecs.INTERACTION_HAND_CODEC), o -> Optional.ofNullable(o.hand),
          ByteBufCodecs.VAR_INT, o -> o.inventorySlot,
          ByteBufCodecs.VAR_INT, o -> o.slot1,
          ByteBufCodecs.VAR_INT, o -> o.slot2,
          ServerboundSwapSpellsPacket::new);


  private ServerboundSwapSpellsPacket(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<InteractionHand> hand, int inventorySlot, int slot1, int slot2) {
    this(hand.orElse(null), inventorySlot, slot1, slot2);
  }

  public ServerboundSwapSpellsPacket(InteractionHand hand, int slot1, int slot2) {
    this(hand, -1, slot1, slot2);
  }

  public ServerboundSwapSpellsPacket(int inventorySlot, int slot1, int slot2)  {
    this((InteractionHand)null, inventorySlot, slot1, slot2);
  }

  @Override

  public void handle(IPayloadContext context) {
    ServerNetworkHooks.swapSpellSlots(context.player(), this.hand, this.inventorySlot, this.slot1, this.slot2);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
