package mysticmods.roots.event.neoforge;

import mysticmods.roots.recipe.AnimalHarvestRecipe;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class LootTableHandler extends SimplePreparableReloadListener<Void> {
  public static AnimalHarvestRecipe.Cache cached = null;
  public static final LootTableHandler INSTANCE = new LootTableHandler();

  @Override
  protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
    return null;
  }

  @Override
  protected void apply(Void object, ResourceManager resourceManager, ProfilerFiller profiler) {
    cached = AnimalHarvestRecipe.getServerRecipes(getRegistryLookup().asGetterLookup());
  }
}
