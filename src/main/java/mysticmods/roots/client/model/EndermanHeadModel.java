package mysticmods.roots.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class EndermanHeadModel extends SkullModel {
  protected ModelPart hat;

  public EndermanHeadModel(ModelPart root) {
    super(root);
    this.hat = root.getChild("hat");
  }

  @Override
  public void setupAnim(float mouthAnimation, float yRot, float xRot) {
    this.head.resetPose();
    this.hat.resetPose();
    super.setupAnim(mouthAnimation, yRot, xRot);

    this.hat.xRot = this.head.xRot;
    this.hat.yRot = this.head.yRot;
    this.hat.zRot = this.head.zRot;

    if (mouthAnimation != 0.0f) {
      this.head.y -= 5.0F;
    }
  }

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
    super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
  }

  public static LayerDefinition createEndermanHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    partdefinition.addOrReplaceChild(
        "hat", CubeListBuilder.create().texOffs(0, 16)
            .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.2f)), PartPose.offset(0, 0.2f, 0));
    partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0, 0f, 0));
    return LayerDefinition.create(meshdefinition, 64, 32);
  }
}
