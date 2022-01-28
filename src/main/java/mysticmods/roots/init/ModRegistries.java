package mysticmods.roots.init;

import mysticmods.roots.api.herbs.IHerb;
import net.minecraftforge.registries.IForgeRegistry;
import noobanidus.libs.noobutil.processor.IProcessor;

public class ModRegistries {
  public static IForgeRegistry<IHerb> HERB_REGISTRY;
  public static IForgeRegistry<IProcessor<?>> PROCESSOR_REGISTRY;

  public static void load() {
  }
}

