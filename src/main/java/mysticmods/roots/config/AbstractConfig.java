package mysticmods.mysticalworld.config;

import net.minecraftforge.common.ForgeConfigSpec;
import noobanidus.libs.noobutil.config.IBaseConfig;

public abstract class AbstractConfig implements IBaseConfig {
  public AbstractConfig() {
    ConfigManager.CONFIGS.add(this);
  }

  public abstract void apply(ForgeConfigSpec.Builder builder);

  public abstract void reset();
}
