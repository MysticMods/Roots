package mysticmods.roots.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.entity.other.RoseThornsEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class RoseThornsModel extends EntityModel<RoseThornsEntity> {
  private final ModelPart main;

  public RoseThornsModel(ModelPart root) {
    this.main = root.getChild("bb_main");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

    PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-9, 7)
        .addBox(-2.5F, 0.0F, -4.5F, 5.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.0F, -2.0F, 0.0F, 2.6616F, 0.0F));

    PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-9, 7)
        .addBox(-2.5F, 0.0F, -4.5F, 5.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.0F, -2.5F, 0.0F, 0.5236F, 0.0F));

    PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-5, 0)
        .addBox(-6.0F, 0.0F, -2.5F, 12.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0608F, -1.0F, 4.9447F, 0.0F, -0.2182F, 0.0F));

    return LayerDefinition.create(meshdefinition, 16, 16);
  }

  @Override
  public void setupAnim(RoseThornsEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

  }

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
    main.render(poseStack, buffer, packedLight, packedOverlay, color);
  }
}