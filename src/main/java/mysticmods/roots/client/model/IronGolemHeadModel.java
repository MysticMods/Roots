package mysticmods.roots.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class IronGolemHeadModel extends SkullModelBase {
  private final ModelPart head;

  public IronGolemHeadModel(ModelPart root) {
    this.head = root.getChild("head");
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

  public static LayerDefinition createIronGolemHead() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
        "head",
        CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO);
    partdefinition1.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0)
            .addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F),
        PartPose.offset(0.0F, -2.0F, -0.0F)
    );
    return LayerDefinition.create(meshdefinition, 128, 128);
  }
}
