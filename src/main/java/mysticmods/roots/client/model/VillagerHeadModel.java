package mysticmods.roots.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class VillagerHeadModel extends SkullModelBase {
  private final ModelPart head;
  private final ModelPart hat;
  private final ModelPart hatRim;
  protected final ModelPart nose;

  public VillagerHeadModel(ModelPart root) {
    this.head = root.getChild("head");
    this.hat = this.head.getChild("hat");
    this.hatRim = this.hat.getChild("hat_rim");
    this.nose = this.head.getChild("nose");
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

  public static LayerDefinition createVillagerHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    float f = 0.5F;
    PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
        "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO
    );
    PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild(
        "hat", CubeListBuilder.create().texOffs(32, 0)
            .addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.51F)), PartPose.ZERO
    );
    partdefinition2.addOrReplaceChild(
        "hat_rim",
        CubeListBuilder.create().texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F),
        PartPose.rotation((float) (-Math.PI / 2), 0.0F, 0.0F)
    );
    partdefinition1.addOrReplaceChild(
        "nose", CubeListBuilder.create().texOffs(24, 0)
            .addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -2.0F, 0.0F)
    );
    return LayerDefinition.create(meshdefinition, 64, 64);
  }
}
