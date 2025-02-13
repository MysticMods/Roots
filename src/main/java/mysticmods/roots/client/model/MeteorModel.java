package mysticmods.roots.client.model;// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.entity.projectile.MeteorEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class MeteorModel extends EntityModel<MeteorEntity> {
  private final ModelPart bone;

  public MeteorModel(ModelPart root) {
    this.bone = root.getChild("main");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition bone = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0)
        .addBox(-2.625F, -2.125F, -2.75F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
        .texOffs(12, 10).addBox(-0.625F, -0.125F, -3.75F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(0, 14).addBox(-0.625F, -0.125F, 2.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
        .texOffs(0, 10)
        .addBox(-2.625F, -3.125F, -0.75F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0f, 0f, 0f));

    return LayerDefinition.create(meshdefinition, 32, 32);
  }

  @Override
  public void setupAnim(MeteorEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.bone.xRot = (ageInTicks * 0.2f) % Mth.TWO_PI;
    this.bone.yRot = (ageInTicks * 0.15f) % Mth.TWO_PI;
    this.bone.zRot = (ageInTicks * 0.04f) % Mth.TWO_PI;
  }

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
    bone.render(poseStack, buffer, packedLight, packedOverlay, color);
  }
}