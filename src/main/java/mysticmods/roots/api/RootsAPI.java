package mysticmods.roots.api;

import mysticmods.roots.api.recipe.Grant;
import mysticmods.roots.api.recipe.IRecipeManagerAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RootsAPI {
  public static final String MODID = "roots";
  public static final String MOD_IDENTIFIERS = "Roots";
  public static Logger LOG = LogManager.getLogger();

  public static RootsAPI INSTANCE;

  public static RootsAPI getInstance() {
    return INSTANCE;
  }

  public static boolean isPresent() {
    return getInstance() != null;
  }

  public abstract IRecipeManagerAccessor getRecipeAccessor();

  public abstract void grant (ServerPlayer player, Grant grant);

  public RecipeManager getRecipeManager() {
    return getRecipeAccessor().getManager();
  }
}
