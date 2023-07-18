package mysticmods.mysticalworld.client.model;

import com.google.common.collect.ImmutableList;
import mysticmods.mysticalworld.entity.DuckEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import java.util.Collections;

public class DuckModel extends AgeableListModel<DuckEntity> {
  private final ModelPart main;
  private final ModelPart neck;
  private final ModelPart head;
  private final ModelPart tail;
  private final ModelPart leg_L;
  private final ModelPart leg_R;
  private final ModelPart wing_L;
  private final ModelPart wing_R;

  public DuckModel(ModelPart pRoot) {
    this.main = pRoot.getChild("main");
    this.neck = main.getChild("neck");
    this.head = neck.getChild("head");

    this.tail = main.getChild("tail");
    this.leg_L = main.getChild("leg_left");
    this.leg_R = main.getChild("leg_right");
    this.wing_L = main.getChild("wing_left");
    this.wing_R = main.getChild("wing_right");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 6.0F, 6.0F)
            .texOffs(0, 12).addBox(-3.0F, -2.0F, 2.0F, 6.0F, 5.0F, 3.0F),
        PartPose.offset(0.0F, 17.0F, 0.0F));

    PartDefinition neck = main.addOrReplaceChild("neck", CubeListBuilder.create()
            .texOffs(23, 23).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 5.0F, 3.0F),
        PartPose.offset(0.0F, 1.0F, -3.5F));

    PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(20, 8).addBox(-2.0F, -4.0F, -2.5F, 4.0F, 4.0F, 4.0F)
            .texOffs(20, 16).addBox(-1.5F, -2.0F, -4.5F, 3.0F, 2.0F, 2.0F),
        PartPose.offset(0.0F, -5.0F, 0.0F));

    PartDefinition tail = main.addOrReplaceChild("tail", CubeListBuilder.create()
            .texOffs(18, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 2.0F, 2.0F),
        PartPose.offsetAndRotation(0.0F, -2.0F, 5.0F, 0.4363F, 0.0F, 0.0F));

    PartDefinition leg_L = main.addOrReplaceChild("leg_left", CubeListBuilder.create()
            .texOffs(30, 0).addBox(-3.25F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F),
        PartPose.offset(0.25F, 4.0F, 1.0F));

    PartDefinition leg_R = main.addOrReplaceChild("leg_right", CubeListBuilder.create()
            .texOffs(30, 0).addBox(-3.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F),
        PartPose.offset(3.0F, 4.0F, 1.0F));

    PartDefinition wing_L = main.addOrReplaceChild("wing_left", CubeListBuilder.create()
            .texOffs(12, 14).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
        PartPose.offset(-3.0F, -2.0F, 0.0F));

    PartDefinition wing_R = main.addOrReplaceChild("wing_right", CubeListBuilder.create()
            .texOffs(0, 20).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F),
        PartPose.offset(3.0F, -2.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  @Override
  protected Iterable<ModelPart> headParts() {
    return Collections.emptyList();
  }

  @Override
  protected Iterable<ModelPart> bodyParts() {
    return ImmutableList.of(main);
  }

  @Override
  public void setupAnim(DuckEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    boolean vallen = pEntity.hasCustomName() && pEntity.getCustomName() != null && (pEntity.getCustomName().getString().equals("Vallen") || pEntity.getCustomName().getString().equals("VallenFrostweavr"));

    if (pEntity.isInWater()) {
      this.leg_L.visible = false;
      this.leg_R.visible = false;
    } else {
      this.leg_L.visible = true;
      this.leg_R.visible = true;
    }
    this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
    this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
    this.leg_L.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
    this.leg_R.xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount;
    this.wing_L.zRot = pAgeInTicks;
    this.wing_R.zRot = -pAgeInTicks;

    if (vallen) {
      this.main.xRot = ((float) Math.PI / 2F);
    } else {
      this.main.xRot = 0;
    }
  }

  public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
    modelRenderer.xRot = x;
    modelRenderer.yRot = y;
    modelRenderer.zRot = z;
  }
}
