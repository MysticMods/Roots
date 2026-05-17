package mysticmods.roots.impl;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.IRootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.attachment.*;
import mysticmods.roots.api.datamap.AugmentationInfo;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModAttributes;
import mysticmods.roots.integration.curios.CuriosIntegration;
import mysticmods.roots.mixin.accessor.AccessorMixinEntity;
import mysticmods.roots.network.client.*;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RootsAPIImpl implements IRootsAPI {
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
      int change = unique ? reputationStorage.apply(grove, id, reputation) : reputationStorage.adjust(grove, reputation);
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

  @Override
  public List<ItemStack> getCurios(Player player, TagKey<Item> tag) {
    return CuriosIntegration.getTagged(player, tag);
  }

  @Override
  public List<ItemStack> getPouches(Player player) {
    List<ItemStack> pouches = new ArrayList<>();
    pouches.addAll(getCurios(player, RootsTags.Items.ALL_POUCHES));

    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
    if (stack.is(RootsTags.Items.POUCHES)) {
      pouches.add(stack);
    }
    stack = player.getItemInHand(InteractionHand.OFF_HAND);
    if (stack.is(RootsTags.Items.POUCHES)) {
      pouches.add(stack);
    }

    Inventory inv = player.getInventory();
    for (int i = 0; i < inv.getContainerSize(); i++) {
      stack = inv.getItem(i);
      if (stack.is(RootsTags.Items.POUCHES)) {
        pouches.add(stack);
      }
    }

    return pouches;
  }

  @Override
  public ItemStack getTome(Player player) {
    List<ItemStack> tomes = CuriosIntegration.getTagged(player, RootsTags.Items.CURIOS_TOMES);
    if (!tomes.isEmpty()) {
      return tomes.getFirst();
    }

    if (player.getOffhandItem().is(RootsTags.Items.CURIOS_TOMES)) {
      return player.getOffhandItem();
    }

    if (player.getMainHandItem().is(RootsTags.Items.CURIOS_TOMES)) {
      return player.getMainHandItem();
    }

    return ItemStack.EMPTY;
  }

  @Override
  public double getCostReduction(Player player) {
    return player.getAttributeValue(ModAttributes.COST_REDUCTION);
  }

  @Override
  public double getCooldownReduction(Player player) {
    return player.getAttributeValue(ModAttributes.COOLDOWN_REDUCTION);
  }

  @Override
  public IRootsPacket getEntityDiscardPacket(ResourceKey<AttachmentType<?>> attachmentType, Entity entity) {
    return new ClientboundDiscardEntityAttachmentPacket(attachmentType.location().toString(), entity.getId());
  }

  @Override
  public IRootsPacket getBlockEntityDiscardPacket(ResourceKey<AttachmentType<?>> attachmentType, BlockEntity entity) {
    return new ClientboundDiscardBlockEntityAttachmentPacket(attachmentType.location().toString(), entity.getBlockPos()
        .asLong());
  }

  @Override
  public boolean logGroveActions() {
    return ConfigManager.DEBUG_GROVE_ACTIONS.getAsBoolean();
  }

  @Override
  public AttachmentType<CooldownStorage> getCooldownStorageType() {
    return ModAttachments.COOLDOWN_STORAGE.value();
  }

  @Override
  public AttachmentType<GrantStorage> getGrantStorageType() {
    return ModAttachments.GRANT_STORAGE.value();
  }

  @Override
  public AttachmentType<Map<Holder<Attribute>, AugmentationInfo>> getAugmentationInfoType() {
    return ModAttachments.AUGMENTATION_INFO.value();
  }

  @Override
  public DataComponentType<Unit> getDeletableType() {
    return ModAttachments.DELETABLE.value();
  }

  @Override
  public DataComponentType<Unit> getModifiableType () {
    return ModAttachments.MODIFIABLE.value();
  }

  @Override
  public void readAdditionalSavedData(Entity entity, @NotNull CompoundTag tag) {
    ((AccessorMixinEntity) entity).roots$ReadAdditionalSaveData(tag);
  }

  // TODO: Neither of these break the API namespacing, thus this method could simply be an abstract implementation for Modifier.
  @SuppressWarnings("unchecked")
  @Override
  @Nullable
  public <T extends Modifier<?, ?>> TagKey<T> getRestrictedTagFor(ResourceKey<T> key) {
    if (key.isFor(RootsRegistries.Keys.SPELL_MODIFIERS)) {
      return (TagKey<T>) RootsTags.SpellModifiers.RESTRICTED;
    } else if (key.isFor(RootsRegistries.Keys.RITUAL_MODIFIERS)) {
      return (TagKey<T>) RootsTags.RitualModifiers.RESTRICTED;
    }
    return null;
  }

  // TODO: That said, Rituals will not necessarily require unlocks.
  @SuppressWarnings("unchecked")
  @Override
  @Nullable
  public <T extends Modifier<?, ?>> TagKey<T> getRequiresUnlockTagFor(ResourceKey<T> key) {
    if (key.isFor(RootsRegistries.Keys.SPELL_MODIFIERS)) {
      return (TagKey<T>) RootsTags.SpellModifiers.REQUIRES_UNLOCK;
    }
    return null;
  }
}
