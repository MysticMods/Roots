package mysticmods.roots.api;

import mysticmods.roots.api.herbs.HerbCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class Capabilities {
  public static final Capability<HerbCapability> HERB_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
}
