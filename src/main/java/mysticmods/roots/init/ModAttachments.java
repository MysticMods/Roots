package mysticmods.roots.init;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.attachment.SnapshotStorage;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
  private static final DeferredRegister<AttachmentType<?>> CAPABILITIES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RootsAPI.MODID);

  public static final DeferredHolder<AttachmentType<?>, AttachmentType<HerbStorage>> HERB_STORAGE = CAPABILITIES.register("herb_storage", () -> AttachmentType.builder(() -> new HerbStorage()).serialize(HerbStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<ReputationStorage>> REPUTATION_STORAGE = CAPABILITIES.register("reputation_storage", () -> AttachmentType.builder(ReputationStorage::new).serialize(ReputationStorage.CODEC).copyOnDeath().build());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<SnapshotStorage>> SNAPSHOT_STORAGE = CAPABILITIES.register("snapshot_storage", () -> AttachmentType.builder(() -> new SnapshotStorage()).serialize(SnapshotStorage.CODEC).copyOnDeath().build());
  public static DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RUNIC_SHEARS_ENTITY_COOLDOWN = CAPABILITIES.register("runic_shears_entity_cooldown", ModAttachments::createIntegerAttachmentType);
  public static DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RUNIC_SHEARS_TOKEN_COOLDOWN = CAPABILITIES.register("runic_shears_token_cooldown", ModAttachments::createIntegerAttachmentType);
  public static DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SQUID_MILKING_COOLDOWN = CAPABILITIES.register("squid_milking_cooldown", ModAttachments::createIntegerAttachmentType);

  private static AttachmentType<Integer> createIntegerAttachmentType() {
    return AttachmentType.builder(() -> -1).serialize(Codec.INT).build();
  }

  public static void register(IEventBus bus) {
    CAPABILITIES.register(bus);
  }
}
