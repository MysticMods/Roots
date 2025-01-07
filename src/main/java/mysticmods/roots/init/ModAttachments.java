package mysticmods.roots.init;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.GrantCapability;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
  private static final DeferredRegister<AttachmentType<?>> CAPABILITIES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RootsAPI.MODID);

/*  public static DeferredHolder<AttachmentType<?>, AttachmentType<GrantCapability>> GRANT_CAPABILITY = CAPABILITIES.register("grant_capability", () -> AttachmentType.builder(GrantCapability::new).serialize(GrantCapability.CODEC).build());*/

  public static DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RUNIC_SHEARS_ENTITY_COOLDOWN = CAPABILITIES.register("runic_shears_entity_cooldown", ModAttachments::createIntegerAttachmentType);
  public static DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> RUNIC_SHEARS_TOKEN_COOLDOWN = CAPABILITIES.register("runic_shears_token_cooldown", ModAttachments::createIntegerAttachmentType);
  public static DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SQUID_MILKING_COOLDOWN = CAPABILITIES.register("squid_milking_cooldown", ModAttachments::createIntegerAttachmentType);

  private static AttachmentType<Integer> createIntegerAttachmentType() {
    return AttachmentType.builder(() -> 0).serialize(Codec.INT).build();
  }
}
