package mysticmods.roots;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.IRecipeManagerAccessor;
import mysticmods.roots.client.impl.ClientRecipeAccessor;
import mysticmods.roots.impl.ServerRecipeAccessor;
import mysticmods.roots.init.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.libs.noobutil.data.generator.RecipeGenerator;
import noobanidus.libs.noobutil.reference.ModData;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.libs.particleslib.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("roots")
public class Roots {
  public static CustomRegistrate REGISTRATE;
  public static final RecipeGenerator RECIPES = new RecipeGenerator(RootsAPI.MODID);

  public static final ItemGroup ITEM_GROUP = new ItemGroup(RootsAPI.MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.Herbs.WILDROOT.get());
    }
  };

  public Roots() {
    REGISTRATE = CustomRegistrate.create(RootsAPI.MODID);
    REGISTRATE.itemGroup(() -> ITEM_GROUP);
    ModData.setIdAndIdentifier(RootsAPI.MODID, RootsAPI.MOD_IDENTIFIERS);

    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(RootsAPI.MODID + "-common.toml"));
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);

    RootsAPI.INSTANCE = new RootsAPI() {
      private final IRecipeManagerAccessor accessor = DistExecutor.safeRunForDist(() -> ClientRecipeAccessor::new, () -> ServerRecipeAccessor::new);

      @Override
      public IRecipeManagerAccessor getRecipeAccessor() {
        return accessor;
      }
    };

    ModBlocks.load();
    ModBlockEntities.load();
    ModItems.load();
    ModHerbs.load();
    ModRegistries.load();
    ModTags.load();
    ModRecipes.load();
  }
}
