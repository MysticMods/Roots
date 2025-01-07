package mysticmods.roots.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public abstract class AbstractConfig {
  public AbstractConfig() {
    ConfigManager.CONFIGS.add(this);
  }

  public abstract void apply(ModConfigSpec.Builder builder);

  public abstract void reset();
}
