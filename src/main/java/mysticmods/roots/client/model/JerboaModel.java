package mysticmods.roots.client.model;

import com.google.common.collect.ImmutableList;
import mysticmods.roots.entity.JerboaEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class JerboaModel extends AgeableListModel<JerboaEntity> {
  private final ModelPart body;
  private final ModelPart tail;
  private final ModelPart leftArm;
  private final ModelPart rightFoot;
  private final ModelPart leftFoot;
  private final ModelPart nose;
  private final ModelPart rightArm;
  private final ModelPart rightEar;
  private final ModelPart leftEar;

  public JerboaModel(ModelPart root) {
    this.body = root.getChild("body");
    this.tail = this.body.getChild("tail");
    this.leftArm = this.body.getChild("leftArm");
    this.rightFoot = this.body.getChild("rightFoot");
    this.leftFoot = this.body.getChild("leftFoot");
    this.nose = this.body.getChild("nose");
    this.rightArm = this.body.getChild("rightArm");
    this.rightEar = this.body.getChild("rightEar");
    this.leftEar = this.body.getChild("leftEar");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
        .addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.5F, 0.0F));

    PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 8)
        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 2.5F));

    PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 11)
        .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.5F, 0.0F));

    PartDefinition rightFoot = body.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(0, 8)
        .addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.5F, 2.0F, 0.0F, 0.6981F, 0.0F));

    PartDefinition leftFoot = body.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(0, 8)
        .addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.5F, 2.0F, 0.0F, -0.6981F, 0.0F));

    PartDefinition nose = body.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 0)
        .addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -2.5F));

    PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 11)
        .addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 1.5F, 0.0F));

    PartDefinition rightEar = body.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(0, 0)
        .addBox(0.0F, -3.0F, -0.5F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, -1.0F, -0.3491F, -0.1745F, -0.2618F));

    PartDefinition leftEar = body.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(0, 0).mirror()
        .addBox(0.0F, -3.0F, -0.5F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
        .mirror(false), PartPose.offsetAndRotation(1.5F, 0.0F, -1.0F, -0.3491F, 0.1745F, 0.2618F));

    return LayerDefinition.create(meshdefinition, 32, 32);
  }

  @Override
  public void setupAnim(JerboaEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    float sin = (float) Math.sin(ageInTicks * 0.125f * (Math.PI * 2.0f));
    leftFoot.xRot = limbSwingAmount * sin;
    rightFoot.xRot = -limbSwingAmount * sin;
  }


  @Override
  protected Iterable<ModelPart> headParts() {
    return ImmutableList.of();
  }

  @Override
  protected Iterable<ModelPart> bodyParts() {
    return ImmutableList.of(body);
  }
}