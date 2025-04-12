package mysticmods.roots.impl;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.attachment.AttachmentUtil;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.RitualInformation;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.ClientboundGrantSyncPacket;
import mysticmods.roots.network.client.ClientboundHerbCountSyncPacket;
import mysticmods.roots.network.client.ClientboundReputationMessagePacket;
import mysticmods.roots.network.client.ClientboundReputationSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class RootsAPIImpl extends RootsAPI {
  @Override
  public void unlock(ServerPlayer player, Unlock<?> unlock) {
    AttachmentUtil.monitorAndSync(player, ModAttachments.GRANT_STORAGE, (sPlayer, storage) -> {
      storage.unlock(sPlayer, unlock);
    }, ClientboundGrantSyncPacket::new);
  }

  @Override
  public boolean canUnlock(ServerPlayer player, Unlock<?> unlock) {
    GrantStorage storage = player.getData(ModAttachments.GRANT_STORAGE);
    if (storage == null) {
      return false;
    }

    return storage.canUnlock(unlock);
  }

  @Override
  public void syncHerbs(Player player, Object2DoubleMap<Herb> herbs) {
    PacketDistributor.sendToPlayer((ServerPlayer) player, new ClientboundHerbCountSyncPacket(herbs));
  }

  @Override
  public void grant(ServerPlayer player, Grove grove, ResourceLocation id, GroveReputation reputation, boolean unique) {
    AttachmentUtil.monitorAndSync(player, ModAttachments.REPUTATION_STORAGE, (serverPlayer, reputationStorage) -> {
      int change = reputationStorage.adjust(grove, reputation);
      // TODO: When a rank changes etc
      if (change != 0 && ConfigManager.DEBUG_REPUTATION.get()) {
        PacketDistributor.sendToPlayer(serverPlayer, new ClientboundReputationMessagePacket(grove, change));
      }
    }, ClientboundReputationSyncPacket::new);
  }

  @Override
  public RitualInformation.RitualResolutionType getRitualResolutionType() {
    return ConfigManager.RITUAL_RESOLUTION_TYPE.get();
  }
}
