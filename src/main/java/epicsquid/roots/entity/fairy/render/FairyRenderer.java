package epicsquid.roots.entity.fairy.render;

import epicsquid.roots.entity.fairy.FairyEntity;
import epicsquid.roots.model.entity.FairyModel;
import epicsquid.roots.model.entity.ModelHolder;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class FairyRenderer extends MobRenderer<FairyEntity, FairyModel> {
  public FairyRenderer(EntityRendererManager renderManager, FairyModel modelBase, float shadowSize) {
    super(renderManager, modelBase, shadowSize);
  }

  @Override
  protected ResourceLocation getEntityTexture(FairyEntity entity) {
    switch (entity.getVariant()) {
      case 0: {
        return new ResourceLocation("roots:textures/entity/green_fairy.png");
      }
      case 1: {
        return new ResourceLocation("roots:textures/entity/purple_fairy.png");
      }
      case 2: {
        return new ResourceLocation("roots:textures/entity/pink_fairy.png");
      }
      case 3: {
        return new ResourceLocation("roots:textures/entity/orange_fairy.png");
      }
      default: {
        return new ResourceLocation("roots:textures/entity/green_fairy.png");
      }
    }
  }

  public static class Factory implements IRenderFactory<FairyEntity> {
    @Override
    public EntityRenderer<FairyEntity> createRenderFor(EntityRendererManager manager) {
      return new FairyRenderer(manager, ModelHolder.fairyModel, 0f);
    }
  }
}
