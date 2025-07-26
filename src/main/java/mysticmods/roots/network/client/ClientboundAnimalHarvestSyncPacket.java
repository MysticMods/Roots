package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ClientboundAnimalHarvestSyncPacket(List<AnimalHarvestRecipe> recipes) implements IRootsPacket {
  public static final Type<ClientboundAnimalHarvestSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_animal_harvest_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAnimalHarvestSyncPacket> CODEC = StreamCodec.composite(AnimalHarvestRecipe.LIST_STREAM_CODEC, ClientboundAnimalHarvestSyncPacket::recipes, ClientboundAnimalHarvestSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.setAnimalHarvestRecipes(this.recipes);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
