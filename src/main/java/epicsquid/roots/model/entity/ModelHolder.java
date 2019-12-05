package epicsquid.roots.model.entity;

import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class ModelHolder implements ISelectiveResourceReloadListener {

  public static FairyModel fairyModel;
  public static WhiteStagModel whiteStagModel;

  public static void init() {
    fairyModel = new FairyModel();
    whiteStagModel = new WhiteStagModel();

  }

  @Override
  public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
  }

  @Override
  public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
    // TODO make this work selectively
    init();
  }
}

