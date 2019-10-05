package epicsquid.roots.entity.wild.render;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.wild.EntityWhiteStag;
import epicsquid.roots.model.entity.ModelHolder;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;


public class RenderWhiteStag extends RenderLiving<EntityWhiteStag> {
  public static final ResourceLocation TEXTURE = new ResourceLocation(Roots.MODID, "textures/entity/white_stag.png");

  protected RenderWhiteStag(@Nonnull RenderManager renderManager, @Nonnull ModelBase modelBase, float shadowSize) {
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
    public Render<EntityWhiteStag> createRenderFor(RenderManager manager) {
      return new RenderWhiteStag(manager, ModelHolder.models.get("white_stag"), 0.5f);
    }
  }
}

