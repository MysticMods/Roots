package mysticmods.roots.impl;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.ClientboundHerbCountSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.network.PacketDistributor;

public class RootsAPIImpl extends RootsAPI {
  @Override
  public void unlock(ServerPlayer player, Unlock<?> unlock) {
    GrantStorage storage = player.getData(ModAttachments.GRANT_STORAGE);
    if (storage == null) {
      return;
    }

    storage.unlock(player, unlock);
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
}
