package epicsquid.roots.integration.baubles.pouch;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class BaublesHook {
  public static ICapabilityProvider getInstance () {
    return BaubleBeltCapabilityHandler.instance;
  }
}
