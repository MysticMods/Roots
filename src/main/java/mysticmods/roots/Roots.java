package mysticmods.roots;

import mysticmods.roots.init.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.libs.noobutil.data.RecipeGenerator;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.libs.particleslib.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("roots")
public class Roots {
  public static final String MODID = "roots";
  public static CustomRegistrate REGISTRATE;
  public static Logger LOG = LogManager.getLogger();
  public static final RecipeGenerator RECIPES = new RecipeGenerator(MODID);

  public static final ItemGroup ITEM_GROUP = new ItemGroup(MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.Herbs.WILDROOT.get());
    }
  };

  public Roots() {
    REGISTRATE = CustomRegistrate.create(MODID);
    REGISTRATE.itemGroup(() -> ITEM_GROUP);

    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);

    ModBlocks.load();
    ModBlockEntities.load();
    ModItems.load();
    ModHerbs.load();
    ModRegistries.load();
    ModTags.load();
  }
}
