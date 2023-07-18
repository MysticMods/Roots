package mysticmods.mysticalworld.client.model;

import com.google.common.collect.ImmutableSet;
import mysticmods.mysticalworld.entity.OwlEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class OwlModel extends AgeableListModel<OwlEntity> {
  private final ModelPart body;
  private final ModelPart footR;
  private final ModelPart footL;
  private final ModelPart wingR1;
  private final ModelPart wingR2;
  private final ModelPart wingL1;
  private final ModelPart wingL2;
  private final ModelPart tail;
  private final ModelPart head;
  private final ModelPart tuftR;
  private final ModelPart tuftL;
  private State state = State.STANDING;

  public OwlModel(ModelPart pRoot) {
    super(true, 5.0f, 2.0f);
    this.body = pRoot.getChild("body");
    this.footR = body.getChild("foot_right");
    this.footL = body.getChild("foot_left");
    this.wingR1 = body.getChild("wing_right1");
    this.wingR2 = wingR1.getChild("wing_right2");
    this.wingL1 = body.getChild("wing_left1");
    this.wingL2 = wingL1.getChild("wing_left2");
    this.tail = body.getChild("tail");
    this.head = body.getChild("head");
    this.tuftR = head.getChild("tuft_right");
    this.tuftL = head.getChild("tuft_left");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 0).addBox(-4.0F, -13.0F, -4.0F, 8.0F, 9.0F, 8.0F), PartPose.offset(0.0F, 24.0F, 0.0F));

    PartDefinition footR = body.addOrReplaceChild("foot_right", CubeListBuilder.create()
            .texOffs(0, 43).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F)
            .texOffs(26, 48).addBox(-1.0F, 2.0F, 0.5F, 2.0F, 2.0F, 0.0F)
            .texOffs(0, 48).addBox(-1.0F, 4.0F, -1.5F, 2.0F, 0.0F, 2.0F),
        PartPose.offset(-1.5F, -4.0F, -0.5F));

    PartDefinition footL = body.addOrReplaceChild("foot_left", CubeListBuilder.create()
            .texOffs(12, 43).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F)
            .texOffs(22, 48).addBox(-1.0F, 2.0F, 0.5F, 2.0F, 2.0F, 0.0F)
            .texOffs(8, 48).addBox(-1.0F, 4.0F, -1.5F, 2.0F, 0.0F, 2.0F),
        PartPose.offset(1.5F, -4.0F, -0.5F));

    PartDefinition wingR1 = body.addOrReplaceChild("wing_right1", CubeListBuilder.create()
            .texOffs(16, 17).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 7.0F, 7.0F),
        PartPose.offset(-4.0F, -13.0F, -1.0F));

    PartDefinition wingR2 = wingR1.addOrReplaceChild("wing_right2", CubeListBuilder.create()
            .texOffs(14, 31).addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 6.0F),
        PartPose.offsetAndRotation(-1.0F, 7.0F, -2.0F, 0.1745F, 0.0349F, -0.1745F));

    PartDefinition wingL1 = body.addOrReplaceChild("wing_left1", CubeListBuilder.create()
            .texOffs(0, 17).addBox(0.0F, 0.0F, -2.0F, 1.0F, 7.0F, 7.0F),
        PartPose.offset(4.0F, -13.0F, -1.0F));

    PartDefinition wingL2 = wingL1.addOrReplaceChild("wing_left2", CubeListBuilder.create()
            .texOffs(0, 31).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 6.0F),
        PartPose.offsetAndRotation(1.0F, 7.0F, -2.0F, 0.1745F, -0.0349F, 0.1745F));

    PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create()
            .texOffs(28, 31).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 4.0F, 2.0F),
        PartPose.offsetAndRotation(0.0F, -5.0F, 4.0F, 0.5236F, 0.0F, 0.0F));

    PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(32, 0).addBox(-3.5F, -6.0F, -3.5F, 7.0F, 6.0F, 7.0F)
            .texOffs(16, 48).addBox(-1.0F, -3.0F, -4.5F, 2.0F, 2.0F, 1.0F),
        PartPose.offset(0.0F, -13.0F, 0.0F));

    PartDefinition tuftR = head.addOrReplaceChild("tuft_right", CubeListBuilder.create()
            .texOffs(24, 43).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F),
        PartPose.offsetAndRotation(-2.0F, -6.0F, -3.0F, 0.4363F, -0.3491F, 0.0F));

    PartDefinition tuftL = head.addOrReplaceChild("tuft_left", CubeListBuilder.create()
            .texOffs(36, 43).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 4.0F),
        PartPose.offsetAndRotation(2.0F, -6.0F, -3.0F, 0.4363F, 0.3491F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  @Nonnull
  @Override
  protected Iterable<ModelPart> headParts() {
    return ImmutableSet.of();
  }

  @Nonnull
  @Override
  protected Iterable<ModelPart> bodyParts() {
    return ImmutableSet.of(body);
  }

  @Override
  public void setupAnim(OwlEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.head.xRot = headPitch * 0.017453292F;
    this.head.yRot = netHeadYaw * 0.017453292F;
    this.head.zRot = 0.0F;

    boolean hootfire = entityIn.hasCustomName() && entityIn.getCustomName() != null && entityIn.getCustomName().getString().equals("Hootfire");

    if (this.state == State.FLYING && !hootfire) {
      setRotationAngle(this.body, 0.45f, 0, 0);
      setRotationAngle(this.footR, 0.25f, 0, 0);
      setRotationAngle(this.footL, 0.25f, 0, 0);
      setRotationAngle(this.tail, 0.15f, 0, 0);
      setRotationAngle(this.head, -0.3f, 0, 0);
      float wingR_rotation = -(0.65f * (float) Math.sin(ageInTicks) - 2.5f * 0.65f);
      float wingL_rotation = 0.65f * (float) Math.sin(ageInTicks) - 2.5f * 0.65f;
      setRotationAngle(this.wingR1, 0.45f, 0, wingR_rotation);
      setRotationAngle(this.wingL1, 0.45f, 0, wingL_rotation);
      setRotationAngle(this.wingR2, 0.45f, 0, 0.05f * wingR_rotation);
      setRotationAngle(this.wingL2, 0.45f, 0, 0.05f * wingL_rotation);
    } else {
      setRotationAngle(this.body, 0, 0, 0);
      setRotationAngle(this.footR, 0, 0, 0);
      setRotationAngle(this.footL, 0, 0, 0);
      setRotationAngle(tail, 0.5236F, 0.0F, 0.0F);
      setRotationAngle(this.head, 0, 0, 0);
      setRotationAngle(wingR1, 0, 0, 0);
      setRotationAngle(wingR2, 0.1745F, 0.0349F, -0.1745F);
      setRotationAngle(wingL1, 0, 0, 0);
      setRotationAngle(wingL2, 0.1745F, -0.0349F, 0.1745F);
    }
  }

  @Override
  public void prepareMobModel(OwlEntity owl, float limbSwing, float limbSwingAmount, float partialTickTime) {
    if (owl.isFlying()) {
      this.state = State.FLYING;
    } else {
      this.state = State.STANDING;
    }
  }

  public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
    modelRenderer.xRot = x;
    modelRenderer.yRot = y;
    modelRenderer.zRot = z;
  }

  @OnlyIn(Dist.CLIENT)
  public enum State {
    FLYING,
    STANDING,
    SITTING
  }
}