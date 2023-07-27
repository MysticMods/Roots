package mysticmods.roots.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.DuckModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.entity.DuckEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class DuckRenderer extends MobRenderer<DuckEntity, DuckModel> {
  public DuckRenderer(@Nonnull EntityRendererProvider.Context context) {
    super(context, new DuckModel(context.bakeLayer(ModelHolder.DUCK)), 0.05F);
  }

  @Override
  protected void scale(DuckEntity entity, PoseStack matrix, float partialTickTime) {
  }

  @Override
  @Nonnull
  public ResourceLocation getTextureLocation(@Nonnull DuckEntity entity) {
    return RootsAPI.rl("textures/entity/duck.png");
  }

  @Override
  protected float getBob(DuckEntity pLivingBase, float pPartialTicks) {
    if (pLivingBase.isInWater()) {
      return 0f;
    }
    float f = Mth.lerp(pPartialTicks, pLivingBase.oFlap, pLivingBase.flap);
    float f1 = Mth.lerp(pPartialTicks, pLivingBase.oFlapSpeed, pLivingBase.flapSpeed);
    return (Mth.sin(f) + 1.0F) * f1;
  }
}
