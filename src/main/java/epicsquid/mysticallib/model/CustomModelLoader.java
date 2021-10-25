package epicsquid.mysticallib.model;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.event.RegisterCustomModelsEvent;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.MinecraftForge;

public class CustomModelLoader implements ICustomModelLoader {

  public static Map<ResourceLocation, IModel> blockmodels = new HashMap<>();
  public static Map<ResourceLocation, IModel> itemmodels = new HashMap<>();

  @Override
  public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
    blockmodels.clear();
    itemmodels.clear();
    MinecraftForge.EVENT_BUS.post(new RegisterCustomModelsEvent());
  }

  @Override
  public boolean accepts(@Nonnull ResourceLocation modelLocation) {
    return blockmodels.containsKey(modelLocation) || itemmodels.containsKey(modelLocation);
  }

  @Override
  @Nullable
  public IModel loadModel(@Nonnull ResourceLocation modelLocation) {
    if (blockmodels.containsKey(modelLocation)) {
      return blockmodels.get(modelLocation);
    } else if (itemmodels.containsKey(modelLocation)) {
      return itemmodels.get(modelLocation);
    }
    return null;
  }

}
