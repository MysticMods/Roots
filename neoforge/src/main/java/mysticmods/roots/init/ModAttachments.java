package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.GrantCapability;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
  private static final DeferredRegister<AttachmentType<?>> CAPABILITIES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, RootsAPI.MODID);

  public static DeferredHolder<AttachmentType<?>, AttachmentType<GrantCapability>> GRANT_CAPABILITY = CAPABILITIES.register("grant_capability", () -> AttachmentType.builder(GrantCapability::new).serialize(GrantCapability.CODEC).build());
}
