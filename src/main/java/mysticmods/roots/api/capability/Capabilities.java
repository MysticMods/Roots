package mysticmods.roots.api.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class Capabilities {
  public static final Capability<HerbCapability> HERB_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
  public static final Capability<GrantCapability> GRANT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
  public static final Capability<SnapshotCapability> SNAPSHOT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
  public static final Capability<EntityCooldownCapability> RUNIC_SHEARS_ENTITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
  public static final Capability<EntityCooldownCapability> RUNIC_SHEARS_TOKEN_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
  public static final Capability<EntityCooldownCapability> SQUID_MILKING_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
}
