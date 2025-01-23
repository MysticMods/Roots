package mysticmods.roots.network.server;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Optional;

public record ServerboundSetSpellPacket(@Nullable InteractionHand hand, int inventorySlot, int staffSlot,
                                        Spell spell) implements IRootsPacket {
  public static final Type<ServerboundSetSpellPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_set_spell"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetSpellPacket> CODEC =
      StreamCodec.composite(
          ByteBufCodecs.optional(ExtraStreamCodecs.INTERACTION_HAND_CODEC), o -> Optional.ofNullable(o.hand),
          ByteBufCodecs.VAR_INT, o -> o.inventorySlot,
          ByteBufCodecs.VAR_INT, o -> o.staffSlot,
          Spell.STREAM_CODEC, o -> o.spell,
          ServerboundSetSpellPacket::new);


  private ServerboundSetSpellPacket(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<InteractionHand> hand, int inventorySlot, int staffSlot, Spell spell) {
    this(hand.orElse(null), inventorySlot, staffSlot, spell);
  }

  public ServerboundSetSpellPacket (InteractionHand hand, int staffSlot, Spell spell) {
    this(hand, -1, staffSlot, spell);
  }

  public ServerboundSetSpellPacket (int inventorySlot, int staffSlot, Spell spell) {
    this((InteractionHand)null, inventorySlot, staffSlot, spell);
  }

  @Override

  public void handle(IPayloadContext context) {
    ServerNetworkHooks.setSpellSlot(context.player(), this.hand, this.inventorySlot, this.staffSlot, this.spell);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
