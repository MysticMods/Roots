package mysticmods.roots.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import mysticmods.roots.api.RootsAPI;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid= RootsAPI.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ConfigManager {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

  public static List<AbstractConfig> CONFIGS = new ArrayList<>();

  public static HatConfig HAT_CONFIG = new HatConfig();

  public static ForgeConfigSpec.BooleanValue EXPERIENCE_ORBS;
  public static ForgeConfigSpec.BooleanValue PACIFIST_DISABLED;
  public static ForgeConfigSpec.IntValue REPUTATION_LOSS_PACIFIST;
  public static ForgeConfigSpec COMMON_CONFIG;

  static {
    COMMON_BUILDER.comment("magnetism-related configuration").push("magnetism");
    EXPERIENCE_ORBS = COMMON_BUILDER.comment("whether or not experience orbs should be teleported when using magnetism").define("move_experience_orbs", true);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.comment("Hat configuration").push("hat_config");
    HAT_CONFIG.apply(COMMON_BUILDER);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.comment("Reputation-related configurations").push("reputation_config");
    REPUTATION_LOSS_PACIFIST = COMMON_BUILDER.comment("how much reputation is lost when killing a pacifist mob [if 0, no reputation is lost]").defineInRange("reputation_loss_pacifist", 10, 0, Integer.MAX_VALUE);
    PACIFIST_DISABLED = COMMON_BUILDER.comment("whether or not the Untrue Pacifist advancement is granted or utilized").define("pacifist_disabled", false);
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static void loadConfig(ForgeConfigSpec spec, Path path) {
    CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
    configData.load();
    spec.setConfig(configData);
  }

  @SubscribeEvent
  public static void onConfigReload (ModConfigEvent.Reloading event) {
    configReload(event);
  }

  @SubscribeEvent
  public static void onConfigLoaded (ModConfigEvent.Loading event) {
    configReload(event);
  }

  public static void configReload (ModConfigEvent event) {
    CONFIGS.forEach(AbstractConfig::reset);
  }
}
