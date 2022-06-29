package mysticmods.roots.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.entity.RitualEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RitualEntityRenderer extends EntityRenderer<RitualEntity> {
  public RitualEntityRenderer(EntityRendererProvider.Context p_174008_) {
    super(p_174008_);
  }

  @Override
  public boolean shouldRender(RitualEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
    return false;
  }

  @Override
  public void render(RitualEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
  }

  @Override
  public ResourceLocation getTextureLocation(RitualEntity pEntity) {
    return null;
  }
}
