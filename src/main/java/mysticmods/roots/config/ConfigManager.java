package mysticmods.roots.config;

import mysticmods.roots.api.RootsAPI;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigManager {

  private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

  public static List<AbstractConfig> CONFIGS = new ArrayList<>();

  public static HatConfig HAT_CONFIG = new HatConfig();

  public static ModConfigSpec.BooleanValue EXPERIENCE_ORBS;
  public static ModConfigSpec.BooleanValue PACIFIST_DISABLED;
  public static ModConfigSpec.IntValue REPUTATION_LOSS_PACIFIST;
  public static ModConfigSpec.IntValue AOE_BOUNDING_BOX_X;
  public static ModConfigSpec.IntValue AOE_BOUNDING_BOX_Y;
  public static ModConfigSpec.IntValue AOE_BOUNDING_BOX_Z;

  public static ModConfigSpec.IntValue PYRE_BOUNDS_X;
  public static ModConfigSpec.IntValue PYRE_BOUNDS_Y;
  public static ModConfigSpec.IntValue PYRE_BOUNDS_Z;
  public static ModConfigSpec COMMON_CONFIG;

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
    COMMON_BUILDER.comment("Runic Shears configuration options").push("runic_shears");
    AOE_BOUNDING_BOX_X = COMMON_BUILDER.comment("the X half value for the size of the Runic Shears aoe bounding box").defineInRange("aoe_bounding_box_x", 3, 1, Integer.MAX_VALUE);
    AOE_BOUNDING_BOX_Y = COMMON_BUILDER.comment("the Y half value for the size of the Runic Shears aoe bounding box").defineInRange("aoe_bounding_box_y", 3, 1, Integer.MAX_VALUE);
    AOE_BOUNDING_BOX_Z = COMMON_BUILDER.comment("the Z half value for the size of the Runic Shears aoe bounding box").defineInRange("aoe_bounding_box_z", 3, 1, Integer.MAX_VALUE);
    PYRE_BOUNDS_X = COMMON_BUILDER.comment("the X half value for the size of the Pyre aoe bounding box").defineInRange("pyre_bounds_x", 10, 1, Integer.MAX_VALUE);
    PYRE_BOUNDS_Y = COMMON_BUILDER.comment("the Y half value for the size of the Pyre aoe bounding box").defineInRange("pyre_bounds_y", 10, 1, Integer.MAX_VALUE);
    PYRE_BOUNDS_Z = COMMON_BUILDER.comment("the Z half value for the size of the Pyre aoe bounding box").defineInRange("pyre_bounds_z", 10, 1, Integer.MAX_VALUE);
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  @SubscribeEvent
  public static void onConfigReload(ModConfigEvent.Reloading event) {
    configReload(event);
  }

  @SubscribeEvent
  public static void onConfigLoaded(ModConfigEvent.Loading event) {
    configReload(event);
  }

  public static void configReload(ModConfigEvent event) {
    CONFIGS.forEach(AbstractConfig::reset);
  }
}
