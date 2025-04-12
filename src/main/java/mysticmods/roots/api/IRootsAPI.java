package mysticmods.roots.api;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.attachment.RitualInformation;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IRootsAPI {
  void unlock(ServerPlayer player, Unlock<?> unlock);

  boolean canUnlock(ServerPlayer player, Unlock<?> unlock);

  void syncHerbs(Player player, Object2DoubleMap<Herb> herbs);

  void grant(ServerPlayer player, Grove grove, ResourceLocation id, GroveReputation reputation, boolean unique);

  RitualInformation.RitualResolutionType getRitualResolutionType();

  List<ItemStack> getCurios(Player player, TagKey<Item> tag);
}
