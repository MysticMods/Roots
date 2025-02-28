package mysticmods.roots.init;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class ModAttachments {
  private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RootsAPI.MODID);
  private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> HAS_GEAS = ATTACHMENTS.register("has_geas", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<GrantStorage>> GRANT_STORAGE = ATTACHMENTS.register("grant_storage", () -> AttachmentType.builder(GrantStorage::new)
      .serialize(GrantStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<HerbStorage>> HERB_STORAGE = ATTACHMENTS.register("herb_storage", () -> AttachmentType.builder(() -> new HerbStorage())
      .serialize(HerbStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<ReputationStorage>> REPUTATION_STORAGE = ATTACHMENTS.register("reputation_storage", () -> AttachmentType.builder(ReputationStorage::new)
      .serialize(ReputationStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<SnapshotStorage>> SNAPSHOT_STORAGE = ATTACHMENTS.register("snapshot_storage", () -> AttachmentType.builder(() -> new SnapshotStorage())
      .serialize(SnapshotStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RUNIC_SHEARS_ENTITY_COOLDOWN = ATTACHMENTS.register("runic_shears_entity_cooldown", ModAttachments::createIntegerAttachmentType);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RUNIC_SHEARS_TOKEN_COOLDOWN = ATTACHMENTS.register("runic_shears_token_cooldown", ModAttachments::createIntegerAttachmentType);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SQUID_MILKING_COOLDOWN = ATTACHMENTS.register("squid_milking_cooldown", ModAttachments::createIntegerAttachmentType);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellStorage>> SPELL_STORAGE = COMPONENTS.register("spell_storage", () -> new DataComponentType.Builder<SpellStorage>().persistent(SpellStorage.CODEC)
      .networkSynchronized(SpellStorage.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> HERB_POUCH_CONTENTS = COMPONENTS.register("herb_pouch_contents", () -> new DataComponentType.Builder<ItemContainerContents>().persistent(ItemContainerContents.CODEC)
      .networkSynchronized(ItemContainerContents.STREAM_CODEC).build());
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FORAGING = COMPONENTS.register("foraging", () -> new DataComponentType.Builder<Integer>().persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());

  private static AttachmentType<Integer> createIntegerAttachmentType() {
    return AttachmentType.builder(() -> -1).serialize(Codec.INT).build();
  }

  public static void register(IEventBus bus) {
    ATTACHMENTS.register(bus);
    COMPONENTS.register(bus);
  }
}
