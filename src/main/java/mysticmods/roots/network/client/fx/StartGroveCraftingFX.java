package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.recipe.TaggedPedestalCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record StartGroveCraftingFX(BlockPos groveCrafter,
                                   List<TaggedPedestalCrafting.ItemPosition> positions) implements IRootsPacket {
  public static final Type<StartGroveCraftingFX> TYPE = new Type<>(RootsAPI.rl("client_fx/start_grove_crafting"));
  public static final StreamCodec<RegistryFriendlyByteBuf, StartGroveCraftingFX> CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC, StartGroveCraftingFX::groveCrafter,
      TaggedPedestalCrafting.ItemPosition.STREAM_CODEC.apply(ByteBufCodecs.list()),
      StartGroveCraftingFX::positions,
      StartGroveCraftingFX::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.startGroveCrafting(groveCrafter, positions);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
