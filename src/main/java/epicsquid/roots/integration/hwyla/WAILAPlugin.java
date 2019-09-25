package epicsquid.roots.integration.hwyla;

import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.integration.hwyla.providers.ProviderBonfire;
import epicsquid.roots.integration.hwyla.providers.ProviderGroveStone;
import epicsquid.roots.tileentity.TileEntityBonfire;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
@SuppressWarnings("unused")
public class WAILAPlugin implements IWailaPlugin {
  @SuppressWarnings("unused")
  public WAILAPlugin() {
  }

  @Override
  public void register(IWailaRegistrar registrar) {
    registrar.registerBodyProvider(new ProviderGroveStone(), BlockGroveStone.class);
    registrar.registerBodyProvider(new ProviderBonfire(), TileEntityBonfire.class);
  }
}
