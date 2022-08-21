package mysticmods.roots.api.capability;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class Capabilities {
  public static final Capability<HerbCapability> HERB_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
  public static final Capability<GrantCapability> GRANT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
}
