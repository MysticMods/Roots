package epicsquid.roots.entity.render;

import epicsquid.roots.entity.EntityFairy;
import epicsquid.roots.model.entity.ModelFairy;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFairy extends RenderLiving<EntityFairy> {
  public static ModelFairy instance = null;

  public RenderFairy(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
    super(renderManager, modelBase, shadowSize);
  }

  @Override
  protected ResourceLocation getEntityTexture(EntityFairy entity) {
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

  public static class Factory implements IRenderFactory<EntityFairy> {
    @Override
    public Render<EntityFairy> createRenderFor(RenderManager manager) {
      if (instance == null) {
        instance = new ModelFairy();
      }
      return new RenderFairy(manager, instance, 0);
    }
  }
}
