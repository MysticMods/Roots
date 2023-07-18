package mysticmods.roots.client.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.IPlayerShoulderCapability;
import mysticmods.roots.client.model.BeetleModel;
import mysticmods.roots.client.model.ModelState;
import mysticmods.roots.client.model.ShoulderRidingModel;
import mysticmods.roots.init.ModEntities;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class ShoulderRenderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {

  public static BeetleModel beetleModel = null;

  public ShoulderRenderLayer(RenderLayerParent<T, PlayerModel<T>> p_i50929_1_) {
    super(p_i50929_1_);
  }

  @Nullable
  public ShoulderRidingModel<?> getModelFor(EntityType<?> type) {
    if (type == ModEntities.BEETLE.get()) {
      return beetleModel;
    }
    return null;
  }

  @Override
  public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    LazyOptional<IPlayerShoulderCapability> lazyCap = pLivingEntity.getCapability(Capabilities.PLAYER_SHOULDER_CAPABILITY);
    if (!lazyCap.isPresent()) {
      return;
    }

    IPlayerShoulderCapability cap = lazyCap.orElseThrow(IllegalStateException::new);
    EntityType<?> type = cap.getEntityType();
    ShoulderRidingModel<?> model = getModelFor(type);
    if (model != null) {
      pMatrixStack.pushPose();
      pMatrixStack.translate(0.375, pLivingEntity.isCrouching() ? -0.7 : -0.89, 0);
      pMatrixStack.scale(0.6f, 0.6f, 0.6f);
      VertexConsumer vertex = pBuffer.getBuffer(model.renderType(model.getTexture(ModelState.SHOULDER)));
      model.renderOnShoulder(pMatrixStack, vertex, pPackedLight, OverlayTexture.NO_OVERLAY, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch, pLivingEntity.tickCount);
      pMatrixStack.popPose();
    }
  }
}
