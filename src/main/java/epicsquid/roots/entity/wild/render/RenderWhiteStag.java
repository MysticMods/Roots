package epicsquid.roots.entity.wild.render;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.wild.EntityWhiteStag;
import epicsquid.roots.model.entity.ModelHolder;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;


public class RenderWhiteStag extends MobRenderer<EntityWhiteStag> {
  public static final ResourceLocation TEXTURE = new ResourceLocation(Roots.MODID, "textures/entity/white_stag.png");

  protected RenderWhiteStag(@Nonnull EntityRendererManager renderManager, @Nonnull ModelBase modelBase, float shadowSize) {
    super(renderManager, modelBase, shadowSize);
  }

  @Override
  @Nonnull
  protected ResourceLocation getEntityTexture(@Nonnull EntityWhiteStag entity) {
    return TEXTURE;
  }

  @Override
  protected void preRenderCallback(EntityWhiteStag entitylivingbaseIn, float partialTickTime) {
    super.preRenderCallback(entitylivingbaseIn, partialTickTime);

    GlStateManager.scale(1.5, 1.5, 1.5);
  }


  public static class Factory implements IRenderFactory<EntityWhiteStag> {

    @Override
    public EntityRenderer<EntityWhiteStag> createRenderFor(EntityRendererManager manager) {
      return new RenderWhiteStag(manager, ModelHolder.models.get("white_stag"), 0.5f);
    }
  }
}

