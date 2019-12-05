package epicsquid.roots.entity.wild.render;

import com.mojang.blaze3d.platform.GlStateManager;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.wild.WhiteStagEntity;
import epicsquid.roots.model.entity.ModelHolder;
import epicsquid.roots.model.entity.WhiteStagModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;


public class WhiteStagRenderer extends MobRenderer<WhiteStagEntity, WhiteStagModel> {
  public static final ResourceLocation TEXTURE = new ResourceLocation(Roots.MODID, "textures/entity/white_stag.png");

  protected WhiteStagRenderer(@Nonnull EntityRendererManager renderManager, @Nonnull WhiteStagModel modelBase, float shadowSize) {
    super(renderManager, modelBase, shadowSize);
  }

  @Override
  @Nonnull
  protected ResourceLocation getEntityTexture(@Nonnull WhiteStagEntity entity) {
    return TEXTURE;
  }

  @Override
  protected void preRenderCallback(WhiteStagEntity entitylivingbaseIn, float partialTickTime) {
    super.preRenderCallback(entitylivingbaseIn, partialTickTime);

    GlStateManager.scaled(1.5, 1.5, 1.5);
  }


  public static class Factory implements IRenderFactory<WhiteStagEntity> {

    @Override
    public EntityRenderer<WhiteStagEntity> createRenderFor(EntityRendererManager manager) {
      return new WhiteStagRenderer(manager, ModelHolder.whiteStagModel, 0.5f);
    }
  }
}

