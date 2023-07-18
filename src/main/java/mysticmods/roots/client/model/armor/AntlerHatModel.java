package mysticmods.mysticalworld.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EquipmentSlot;

public class AntlerHatModel extends ArmorModel {
  private final ModelPart hat;
  private final ModelPart horn1;
  private final ModelPart horn1_1;
  private final ModelPart strap;
  private final ModelPart horn3;
  private final ModelPart horn6;
  private final ModelPart horn8;
  private final ModelPart horn3_1;
  private final ModelPart horn6_1;
  private final ModelPart horn8_1;

  public AntlerHatModel(ModelPart pRoot) {
    super(pRoot, EquipmentSlot.HEAD);
    this.hat = pRoot.getChild("head");
    this.horn1 = this.hat.getChild("horn1");
    this.horn1_1 = this.hat.getChild("horn1_1");
    this.strap = this.hat.getChild("strap");
    this.horn3 = this.horn1.getChild("horn3");
    this.horn3_1 = this.horn1_1.getChild("horn3_1");
    this.horn6 = this.horn3.getChild("horn6");
    this.horn6_1 = this.horn3_1.getChild("horn6_1");
    this.horn8 = this.horn6.getChild("horn8");
    this.horn8_1 = this.horn6_1.getChild("horn8_1");
  }

  @Override
  public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
    partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
    partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
    partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
    partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
    partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

    PartDefinition hat = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 0).addBox(-3.0F, -10.0F, -3.0F, 6f, 3f, 6f), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
    PartDefinition horn1_1 = hat.addOrReplaceChild("horn1_1", CubeListBuilder.create().texOffs(66, 32).addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1), PartPose.offsetAndRotation(-2.0F, -9.0F, 0.0F, 0.0F, 0.0F, -0.08726646259971647F));
    PartDefinition horn3_1 = horn1_1.addOrReplaceChild("horn3_1", CubeListBuilder.create().texOffs(66, 32).addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1), PartPose.offsetAndRotation(-0.15F, -2.5F, 0.0F, 0.0F, -0.08726646259971647F, -1.0471975511965976F));
    PartDefinition horn6_1 = horn3_1.addOrReplaceChild("horn6_1", CubeListBuilder.create().texOffs(66, 32).addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1), PartPose.offsetAndRotation(0.3F, -3.2F, -0.2F, 0.0F, 0.08726646259971647F, 1.0471975511965976F));
    horn6_1.addOrReplaceChild("horn8_1", CubeListBuilder.create().texOffs(70, 32).addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, -0.17453292519943295F, -0.7853981633974483F));
    PartDefinition horn1 = hat.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(66, 32).addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1), PartPose.offsetAndRotation(2.0F, -9.0F, 0.0F, 0.0F, 0.0F, 0.08726646259971647F));
    PartDefinition horn3 = horn1.addOrReplaceChild("horn3", CubeListBuilder.create().texOffs(66, 32).addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1), PartPose.offsetAndRotation(0.15F, -2.5F, 0.0F, 0.0F, 0.08726646259971647F, 1.0471975511965976F));
    PartDefinition horn6 = horn3.addOrReplaceChild("horn6", CubeListBuilder.create().texOffs(66, 32).addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1), PartPose.offsetAndRotation(-0.3F, -3.2F, -0.2F, 0.0F, -0.08726646259971647F, -1.0471975511965976F));
    horn6.addOrReplaceChild("horn8", CubeListBuilder.create().texOffs(70, 32).addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.17453292519943295F, 0.7853981633974483F));
    hat.addOrReplaceChild("strap", CubeListBuilder.create().texOffs(64, 10).addBox(-4.5F, 0.0F, -0.5F, 9, 9, 1), PartPose.offsetAndRotation(0.0F, -8.1F, -0.5F, -0.25132741228718347F, 0.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 128, 64);
  }
}
