package mysticmods.roots.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class IllagerHeadModel extends SkullModelBase {
  private final ModelPart head;
  private final ModelPart hat;

  public IllagerHeadModel(ModelPart root) {
    this.head = root.getChild("head");
    this.hat = this.head.getChild("hat");
    this.hat.visible = false;
  }

  @Override
  public void setupAnim(float mouthAnimation, float yRot, float xRot) {
    this.head.yRot = yRot * (float) (Math.PI / 180.0);
    this.head.xRot = xRot * (float) (Math.PI / 180.0);
  }

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
    this.head.render(poseStack, buffer, packedLight, packedOverlay, color);
  }

  public static LayerDefinition createPillagerHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
        "head", CubeListBuilder.create().texOffs(0, 0)
            .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
    );
    partdefinition1.addOrReplaceChild(
        "hat", CubeListBuilder.create().texOffs(32, 0)
            .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.45F)), PartPose.ZERO
    );
    partdefinition1.addOrReplaceChild(
        "nose", CubeListBuilder.create().texOffs(24, 0)
            .addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -2.0F, 0.0F)
    );
    return LayerDefinition.create(meshdefinition, 64, 64);
  }
}
