package mysticmods.roots.network.server;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Optional;

public record ServerboundToggleSpellModifierPacket (@Nullable InteractionHand hand, int inventorySlot, int staffSlot, Spell spell, SpellModifier modifier) implements IRootsPacket {
  public static final Type<ServerboundToggleSpellModifierPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_toggle_spell_modifier"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundToggleSpellModifierPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.optional(ExtraStreamCodecs.INTERACTION_HAND_CODEC), o -> Optional.ofNullable(o.hand),
      ByteBufCodecs.VAR_INT, ServerboundToggleSpellModifierPacket::inventorySlot,
      ByteBufCodecs.VAR_INT, ServerboundToggleSpellModifierPacket::staffSlot,
      Spell.STREAM_CODEC, ServerboundToggleSpellModifierPacket::spell,
      SpellModifier.STREAM_CODEC, ServerboundToggleSpellModifierPacket::modifier,
      ServerboundToggleSpellModifierPacket::new
  );

  private ServerboundToggleSpellModifierPacket(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<InteractionHand> hand, int inventorySlot, int staffSlot, Spell spell, SpellModifier modifier) {
    this(hand.orElse(null), inventorySlot, staffSlot, spell, modifier);
  }

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.toggleSpellModifier(context.player(), this.hand, this.inventorySlot, this.staffSlot, this.spell, this.modifier);
    context.player().inventoryMenu.sendAllDataToRemote();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
