package mysticmods.roots.api;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.attachment.CooldownStorage;
import mysticmods.roots.api.attachment.RitualInformation;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.datamap.AugmentationInfo;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.item.GramaryItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.List;
import java.util.Map;

public interface IRootsAPI {
  void unlock(ServerPlayer player, Unlock<?> unlock);

  boolean canUnlock(ServerPlayer player, Unlock<?> unlock);

  void syncHerbs(Player player, Object2DoubleMap<Herb> herbs);

  void grant(ServerPlayer player, Grove grove, ResourceLocation id, GroveReputation reputation, boolean unique);

  RitualInformation.RitualResolutionType getRitualResolutionType();

  List<ItemStack> getCurios(Player player, TagKey<Item> tag);

  ItemStack getTome(Player player);

  default GramaryItem.GramaryMode getTomeMode(Player player) {
    ItemStack tome = getTome(player);
    if (tome.isEmpty()) {
      return GramaryItem.GramaryMode.NONE;
    }

    return GramaryItem.getMode(tome);
  }

  double getCostReduction(Player player);

  double getCooldownReduction(Player player);

  IRootsPacket getEntityDiscardPacket(ResourceKey<AttachmentType<?>> attachmentType, Entity entity);

  IRootsPacket getBlockEntityDiscardPacket(ResourceKey<AttachmentType<?>> attachmentType, BlockEntity entity);

  boolean logGroveActions();

  AttachmentType<CooldownStorage> getCooldownStorageType();
  AttachmentType<Map<Holder<Attribute>, AugmentationInfo>> getAugmentationInfoType();
  DataComponentType<Unit> getDeletableType ();
}
