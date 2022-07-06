package mysticmods.roots;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.Grant;
import mysticmods.roots.api.recipe.IRecipeManagerAccessor;
import mysticmods.roots.client.impl.ClientRecipeAccessor;
import mysticmods.roots.impl.ServerRecipeAccessor;
import mysticmods.roots.init.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.libs.noobutil.data.generator.RecipeGenerator;
import noobanidus.libs.noobutil.reference.ModData;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.libs.particleslib.config.ConfigManager;

@Mod(RootsAPI.MODID)
public class Roots {
  public static CustomRegistrate REGISTRATE;
  public static final RecipeGenerator RECIPES = new RecipeGenerator(RootsAPI.MODID);

  public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(RootsAPI.MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.Herbs.WILDROOT.get());
    }
  };

  public Roots() {
    REGISTRATE = CustomRegistrate.create(RootsAPI.MODID);
    REGISTRATE.creativeModeTab(() -> ITEM_GROUP);
    ModData.setIdAndIdentifier(RootsAPI.MODID, RootsAPI.MOD_IDENTIFIERS);

    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(RootsAPI.MODID + "-common.toml"));
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

    RootsAPI.INSTANCE = new RootsAPI() {
      private final IRecipeManagerAccessor accessor = DistExecutor.safeRunForDist(() -> ClientRecipeAccessor::new, () -> ServerRecipeAccessor::new);

      @Override
      public IRecipeManagerAccessor getRecipeAccessor() {
        return accessor;
      }

      // TODO:
      @Override
      public void grant(ServerPlayer player, Grant grant) {
      }
    };

    ModBlocks.load();
    ModBlockEntities.load();
    ModItems.load();
    ModHerbs.load();
    ModRegistries.load();
    ModTags.load();
    ModRecipes.load();
    ModRituals.load();
    ModSpells.load();
    ModRegistries.register(bus);
    ModRecipes.Types.register(bus);
  }
}
