package mysticmods.roots.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import noobanidus.libs.noobutil.config.BiomeConfig;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

  public static ForgeConfigSpec COMMON_CONFIG;

  static {
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static void loadConfig(ForgeConfigSpec spec, Path path) {
    CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
    configData.load();
    spec.setConfig(configData);
  }
}
