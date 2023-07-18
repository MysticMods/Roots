package mysticmods.mysticalworld.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;

import java.util.function.Function;

public abstract class ShoulderRidingModel<T extends Entity> extends AgeableListModel<T> {
  public ShoulderRidingModel(boolean scaleHead, float yHeadOffset, float zHeadOfset) {
    super(scaleHead, yHeadOffset, zHeadOfset);
  }

  public ShoulderRidingModel(boolean scaleHead, float yHeadOffset, float zHeadOffset, float babyHeadScale, float babyBodyScale, float bodyYOffset) {
    super(scaleHead, yHeadOffset, zHeadOffset, babyHeadScale, babyBodyScale, bodyYOffset);
  }

  public ShoulderRidingModel(Function<ResourceLocation, RenderType> renderType, boolean scaleHead, float yHeadOffset, float zHeadOffset, float babyHeadScale, float babyBodyScale, float bodyYOffset) {
    super(renderType, scaleHead, yHeadOffset, zHeadOffset, babyHeadScale, babyBodyScale, bodyYOffset);
  }

  public ShoulderRidingModel() {
  }

  public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    this.setupAnim(getStateFor(pEntity), pEntity.tickCount, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
  }

  public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
    this.prepare(getStateFor(pEntity));
  }

  public void renderOnShoulder(PoseStack pMatrixStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pLimbSwing, float pLimbSwingAmount, float pNetHeadYaw, float pHeadPitch, int ticks) {
    this.prepare(ModelState.SHOULDER);
    this.setupAnim(ModelState.SHOULDER, ticks, pLimbSwing, pLimbSwingAmount, 0.0F, pNetHeadYaw, pHeadPitch);
    if (this.young) {
      pMatrixStack.pushPose();
      if (this.scaleHead) {
        float f = 1.5F / this.babyHeadScale;
        pMatrixStack.scale(f, f, f);
      }

      this.headParts().forEach((part) -> part.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay));
      pMatrixStack.popPose();
      pMatrixStack.pushPose();
      float f1 = 1.0F / this.babyBodyScale;
      pMatrixStack.scale(f1, f1, f1);
      pMatrixStack.translate(0.0D, this.bodyYOffset / 16.0F, 0.0D);
      this.bodyParts().forEach((part) -> part.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay));
      pMatrixStack.popPose();
    } else {
      this.headParts().forEach((part) -> part.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay));
      this.bodyParts().forEach((part) -> part.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay));
    }
  }

  protected ModelState getStateFor(T entity) {
    if (entity instanceof TamableAnimal) {
      TamableAnimal tameable = (TamableAnimal) entity;
      if (tameable.isInSittingPose()) {
        return ModelState.SITTING;
      } else {
        return ModelState.NORMAL;
      }
    }
    return ModelState.NORMAL;
  }

  protected abstract void setupAnim(ModelState state, int ticks, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

  protected abstract void prepare(ModelState state);

  public abstract ResourceLocation getTexture(ModelState state);
}
