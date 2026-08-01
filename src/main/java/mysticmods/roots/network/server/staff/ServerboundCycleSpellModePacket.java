package mysticmods.roots.network.server.staff;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.network.client.staff.ClientboundRefreshStaffScreenPacket;
import mysticmods.roots.network.server.ServerNetworkHooks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundCycleSpellModePacket(InteractionHand hand, DataComponentType<?> component) implements IRootsPacket {
  public static final Type<ServerboundCycleSpellModePacket> TYPE = new Type<>(RootsAPI.rl("server_bound_cycle_spell_mode"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCycleSpellModePacket> CODEC =
      StreamCodec.composite(
          ExtraStreamCodecs.INTERACTION_HAND_CODEC, ServerboundCycleSpellModePacket::hand,
          ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE), ServerboundCycleSpellModePacket::component,
          ServerboundCycleSpellModePacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.cycleSpellMode(context.player(), this.hand, this.component);
    context.player().inventoryMenu.sendAllDataToRemote();
    PacketDistributor.sendToPlayer((ServerPlayer) context.player(), ClientboundRefreshStaffScreenPacket.getInstance());
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
