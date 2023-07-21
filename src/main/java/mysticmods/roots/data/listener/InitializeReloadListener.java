package mysticmods.roots.data.listener;

import mysticmods.roots.event.forge.DataHandler;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class InitializeReloadListener extends SimplePreparableReloadListener<Void> {
  private boolean fired = false;
  @Override
  protected Void prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    return null;
  }

  @Override
  protected void apply(Void pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    if (fired) {
      return;
    }
    DataHandler.init();
    fired = true;
  }
}
