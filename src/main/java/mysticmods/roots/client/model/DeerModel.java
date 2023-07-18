package mysticmods.mysticalworld.client.model;

import com.google.common.collect.ImmutableList;
import mysticmods.mysticalworld.entity.DeerEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import java.util.Collections;

public class DeerModel extends AgeableListModel<DeerEntity> {

  //fields
  private final ModelPart head;
  private final ModelPart neck;
  private final ModelPart body;
  private final ModelPart tail;
  private final ModelPart legRF;
  private final ModelPart legLF;
  private final ModelPart legLB;
  private final ModelPart legRB;
  private final ModelPart horn1;
  private final ModelPart horn2;
  private final ModelPart horn3;
  private final ModelPart horn4;
  private final ModelPart horn5;
  private final ModelPart horn6;
  private final ModelPart horn7;
  private final ModelPart horn8;

  public DeerModel(ModelPart pRoot) {
    super(true, 5.0f, 2.0f);
    this.head = pRoot.getChild("head");
    this.horn1 = head.getChild("horn1");
    this.horn2 = head.getChild("horn2");
    this.horn3 = head.getChild("horn3");
    this.horn4 = head.getChild("horn4");
    this.horn5 = head.getChild("horn5");
    this.horn6 = head.getChild("horn6");
    this.horn7 = head.getChild("horn7");
    this.horn8 = head.getChild("horn8");
    this.neck = pRoot.getChild("neck");
    this.body = pRoot.getChild("body");
    this.legRF = pRoot.getChild("legRF");
    this.legLF = pRoot.getChild("legLF");
    this.legLB = pRoot.getChild("legLB");
    this.legRB = pRoot.getChild("legRB");
    this.tail = pRoot.getChild("tail");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(),
        PartPose.offset(0.0F, 6.0F, -5.0F));

    PartDefinition main = head.addOrReplaceChild("main", CubeListBuilder.create()
            .texOffs(0, 0).mirror().addBox(-2.5F, -2.5F, -7.0F, 5.0F, 5.0F, 7.0F).mirror(false),
        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

    PartDefinition ear1 = head.addOrReplaceChild("ear1", CubeListBuilder.create()
            .texOffs(17, 0).addBox(-1.5F, -4.0F, -0.5F, 3.0F, 4.0F, 1.0F),
        PartPose.offsetAndRotation(-2.0F, -0.5F, -1.5F, -0.1745F, -0.1745F, -1.1781F));

    PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create()
            .texOffs(17, 0).mirror().addBox(-1.5F, -4.0F, -0.5F, 3.0F, 4.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(2.0F, -0.5F, -1.5F, 0.1745F, 0.1745F, 1.1781F));

    PartDefinition horn1 = head.addOrReplaceChild("horn1", CubeListBuilder.create()
            .texOffs(16, 32).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(1.0F, -2.0F, -1.0F, 0.0F, 0.0F, 0.2618F));

    PartDefinition horn2 = head.addOrReplaceChild("horn2", CubeListBuilder.create()
            .texOffs(16, 32).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(-1.0F, -2.0F, -1.0F, 0.0F, 0.0F, -0.2618F));

    PartDefinition horn3 = head.addOrReplaceChild("horn3", CubeListBuilder.create()
            .texOffs(16, 32).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(1.75F, -4.0F, -1.0F, 0.0F, 0.0873F, 1.0472F));

    PartDefinition horn4 = head.addOrReplaceChild("horn4", CubeListBuilder.create()
            .texOffs(16, 32).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(-1.8F, -4.0F, -1.0F, 0.0F, -0.0873F, -1.0472F));

    PartDefinition horn5 = head.addOrReplaceChild("horn5", CubeListBuilder.create()
            .texOffs(16, 32).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(-4.36F, -5.8F, -1.2F, 0.0F, -0.0873F, 0.2618F));

    PartDefinition horn6 = head.addOrReplaceChild("horn6", CubeListBuilder.create()
            .texOffs(16, 32).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(4.4F, -5.8F, -1.2F, 0.0F, 0.0873F, -0.2618F));

    PartDefinition horn7 = head.addOrReplaceChild("horn7", CubeListBuilder.create()
            .texOffs(20, 32).mirror().addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(-3.8F, -8.0F, -1.2F, 0.0F, -0.1745F, -0.7854F));

    PartDefinition horn8 = head.addOrReplaceChild("horn8", CubeListBuilder.create()
            .texOffs(20, 32).mirror().addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F).mirror(false),
        PartPose.offsetAndRotation(3.8F, -8.0F, -1.2F, 0.0F, 0.1745F, 0.7854F));

    PartDefinition neck = partdefinition.addOrReplaceChild("neck", CubeListBuilder.create()
            .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.5F, 4.0F, 7.0F, 4.0F).mirror(false),
        PartPose.offsetAndRotation(0.0F, 7.0F, -6.9533F, 1.0472F, 0.0F, 0.0F));

    PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
            .texOffs(16, 16).mirror().addBox(-2.5F, 0.0F, 0.0F, 5.0F, 7.0F, 9.0F).mirror(false),
        PartPose.offset(0.0F, 8.0F, -3.9533F));

    PartDefinition legRF = partdefinition.addOrReplaceChild("legRF", CubeListBuilder.create()
            .texOffs(0, 32).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F).mirror(false),
        PartPose.offset(-1.5F, 15.0F, -2.9533F));

    PartDefinition legLF = partdefinition.addOrReplaceChild("legLF", CubeListBuilder.create()
            .texOffs(0, 32).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F).mirror(false),
        PartPose.offset(1.5F, 15.0F, -2.9533F));

    PartDefinition legLB = partdefinition.addOrReplaceChild("legLB", CubeListBuilder.create()
            .texOffs(0, 32).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F).mirror(false),
        PartPose.offset(-1.5F, 15.0F, 4.0F));

    PartDefinition legRB = partdefinition.addOrReplaceChild("legRB", CubeListBuilder.create()
            .texOffs(0, 32).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F).mirror(false),
        PartPose.offset(1.5F, 15.0F, 4.0F));

    PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create()
            .texOffs(32, 0).mirror().addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 4.0F).mirror(false),
        PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.7854F, 0.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  @Override
  protected Iterable<ModelPart> headParts() {
    return Collections.emptyList();
  }

  @Override
  protected Iterable<ModelPart> bodyParts() {
    return ImmutableList.of(neck, body, tail, legRF, legLF, legLB, legRB, head);
  }

  @Override
  public void setupAnim(DeerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    head.xRot = headPitch * ((float) Math.PI / 180F);
    head.yRot = netHeadYaw * ((float) Math.PI / 180F);
    float sin = (float) Math.sin(ageInTicks * 0.125f * (Math.PI * 2.0f));
    legRF.xRot = limbSwingAmount * sin;
    legLF.xRot = -limbSwingAmount * sin;
    legLB.xRot = limbSwingAmount * sin;
    legRB.xRot = -limbSwingAmount * sin;
    if (!entityIn.getEntityData().get(DeerEntity.hasHorns)) {
      horn1.visible = false;
      horn2.visible = false;
      horn3.visible = false;
      horn4.visible = false;
      horn5.visible = false;
      horn6.visible = false;
      horn7.visible = false;
      horn8.visible = false;
    } else {
      horn1.visible = true;
      horn2.visible = true;
      horn3.visible = true;
      horn4.visible = true;
      horn5.visible = true;
      horn6.visible = true;
      horn7.visible = true;
      horn8.visible = true;
    }
  }
}