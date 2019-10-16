package epicsquid.roots.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class ModelHolder implements IResourceManagerReloadListener {

  public static Map<String, ModelBase> models = new HashMap<>();

  public static void init() {
    models.put("fairy", new ModelFairy());
    models.put("white_stag", new ModelWhiteStag());
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
    models.clear();
    init();
  }
}

