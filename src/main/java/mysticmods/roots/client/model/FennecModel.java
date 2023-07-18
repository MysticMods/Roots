package mysticmods.mysticalworld.client.model;

import com.google.common.collect.ImmutableSet;
import mysticmods.mysticalworld.entity.FennecEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import javax.annotation.Nonnull;

public class FennecModel extends AgeableListModel<FennecEntity> {
  private final ModelPart body1;
  private final ModelPart frontL;
  private final ModelPart frontR;
  private final ModelPart body2;
  private final ModelPart neck;
  private final ModelPart backL;
  private final ModelPart backR;
  private final ModelPart tail1;
  private final ModelPart tail2;
  private final ModelPart tail3;
  private final ModelPart tail4;
  private final ModelPart head;
  private final ModelPart snout;
  private final ModelPart earR;
  private final ModelPart earL;

  public FennecModel(ModelPart pRoot) {
    super(true, 5.0f, 2.0f);

    this.body1 = pRoot.getChild("body1");
    this.neck = body1.getChild("neck");
    this.head = neck.getChild("head");
    this.earR = head.getChild("ear_right");
    this.earL = head.getChild("ear_left");
    this.snout = head.getChild("snout");
    this.frontL = body1.getChild("front_left_leg");
    this.frontR = body1.getChild("front_right_leg");
    this.body2 = body1.getChild("body2");
    this.backL = body2.getChild("back_left_leg");
    this.backR = body2.getChild("back_right_leg");
    this.tail1 = body2.getChild("tail1");
    this.tail2 = tail1.getChild("tail2");
    this.tail3 = tail2.getChild("tail3");
    this.tail4 = tail3.getChild("tail4");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body1 = partdefinition.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 23).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.5F, -2.0F));

    PartDefinition neck = body1.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 2.0F, -0.2618F, 0.0F, 0.0F));

    PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 7).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));

    PartDefinition earR = head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(0, 0).addBox(-1.4071F, -5.2021F, -0.3909F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -1.0F, -1.0F, 0.0F, -0.2618F, 0.5988F));

    PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(10, 0).addBox(-1.5F, -0.0872F, -2.0038F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.1745F, 0.0F, 0.0F));

    PartDefinition earL = head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.6004F, -5.2075F, -0.3929F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, -1.0F, -1.0F, 0.0F, 0.2618F, -0.6086F));

    PartDefinition frontL = body1.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(24, 26).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 4.0F, 1.0F));

    PartDefinition body2 = body1.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(18, 8).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

    PartDefinition backL = body2.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(24, 26).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 4.0F, 1.5F));

    PartDefinition backR = body2.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(24, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 4.0F, 1.5F));

    PartDefinition tail1 = body2.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(12, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.5F, 1.1781F, 0.0F, -0.0617F));

    PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, -0.3927F, 0.0F, -0.0873F));

    PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(16, 15).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, -0.0617F));

    PartDefinition tail4 = tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(15, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5F, 0.0F, 0.3927F, 0.0F, 0.0F));

    PartDefinition frontR = body1.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(24, 26).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 4.0F, 1.0F));

    return LayerDefinition.create(meshdefinition, 32, 32);
  }

  @Nonnull
  @Override
  protected Iterable<ModelPart> headParts() {
    return ImmutableSet.of();
  }

  @Nonnull
  @Override
  protected Iterable<ModelPart> bodyParts() {
    return ImmutableSet.of(body1);
  }

  private float getBobble(float deg, float ageInTicks) {
    return (float) Math.sin(ageInTicks * 0.03125f * (Math.PI * 2.0f) + Math.toRadians(deg));
  }

  @Override
  public void setupAnim(@Nonnull FennecEntity fennec, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    float sin = (float) Math.sin(ageInTicks * 0.125f * (Math.PI * 2.0f));
    float cos = (float) Math.cos(ageInTicks * 0.0625f * (Math.PI * 2.0f));
    if (!fennec.isInSittingPose()) {
      this.setRotateAngle(body1, 0F, 0F, 0F);
      this.setRotateAngle(body2, 0F, 0F, 0F);
      this.setRotateAngle(backL, 0f, 0f, 0f);
      this.setRotateAngle(backR, 0f, 0f, 0f);
      this.setRotateAngle(neck, -0.2618F, 0.0F, 0.0F);
      this.earL.zRot = 0.5235987755982988F + getBobble(60, ageInTicks) * 0.0981747703F;
      this.earR.zRot = -0.5235987755982988F - getBobble(130, ageInTicks) * 0.0981747703F;
      this.backL.xRot = limbSwingAmount * sin;
      this.frontR.xRot = limbSwingAmount * sin;
      this.backR.xRot = -limbSwingAmount * sin;
      this.frontL.xRot = -limbSwingAmount * sin;
      this.head.xRot = headPitch * 0.017453292F;
      this.head.yRot = netHeadYaw * 0.017453292F;
      this.setRotateAngle(tail1, 1.1780972450961724F + limbSwingAmount, 0f, limbSwingAmount * 0.375f * cos + 0.0872664626F * getBobble(45, ageInTicks));
      this.setRotateAngle(tail2, -0.3927f, 0f, limbSwingAmount * 0.375f * cos + 0.0872664626F * getBobble(90, ageInTicks));
      this.setRotateAngle(tail3, 0f, 0f, limbSwingAmount * 0.375f * cos + 0.0872664626F * getBobble(135, ageInTicks));
      this.setRotateAngle(tail4, 0.3927f, 0f, limbSwingAmount * 0.375f * cos + 0.0872664626F * getBobble(180, ageInTicks));
    } else {
      this.setRotateAngle(backL, -0.8197f, -0.3187f, 0.0F); // DONE
      this.setRotateAngle(backR, -0.8197f, 0.3643f, 0.0F); // DONE
      this.setRotateAngle(body1, -0.6830f, 0.0F, 0.0F); // DONE
      this.setRotateAngle(body2, -0.0456f, 0.0F, 0.0F); // DONE
      this.setRotateAngle(earL, 0.0F, 0.2618f, 0.6086f); // Z changed but reset by animation
      this.setRotateAngle(earR, 0.0F, -0.2618f, -0.5988f); // Z changed but reset by animation
      this.setRotateAngle(frontL, 0.3187f, 0.0F, 0.0F); // X changed by animation
      this.setRotateAngle(frontR, 0.3187f, 0.0F, 0.0F); // X changed by animation
      this.setRotateAngle(neck, 0.3643f, 0.0F, 0.0F); // DONE
      this.setRotateAngle(tail1, 2.5498f, 0.0F, 0.0617f);
      this.setRotateAngle(tail2, -0.3927f, 0.0F, 0.08723f);
      this.setRotateAngle(tail3, 0.0F, 0.0F, 0.06179f);
      this.setRotateAngle(tail4, 0.3927f, 0.0F, 0.0F);
    }
  }

  @Override
  public void prepareMobModel(@Nonnull FennecEntity fennec, float limbSwing, float limbSwingAmount, float partialTickTime) {
    super.prepareMobModel(fennec, limbSwing, limbSwingAmount, partialTickTime);
  }

  /**
   * This is a helper function from Tabula to set the rotation of model parts
   */
  private void setRotateAngle(@Nonnull ModelPart ModelRenderer, float x, float y, float z) {
    ModelRenderer.xRot = x;
    ModelRenderer.yRot = y;
    ModelRenderer.zRot = z;
  }
}