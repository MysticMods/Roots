package mysticmods.mysticalworld.client.model;

import com.google.common.collect.ImmutableList;
import mysticmods.mysticalworld.entity.SproutEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.phys.Vec3;

public class SproutModel extends AgeableListModel<SproutEntity> {
  private final ModelPart head;
  private final ModelPart legL;
  private final ModelPart legR;
  private final ModelPart leafTop;
  private final ModelPart leafBottom;

  public SproutModel(ModelPart pRoot) {
    super(true, 5.0f, 2.0f);
    this.head = pRoot.getChild("head");
    this.legL = pRoot.getChild("left_leg");
    this.legR = pRoot.getChild("right_leg");
    this.leafTop = pRoot.getChild("leaf_top");
    this.leafBottom = pRoot.getChild("leaf_bottom");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(12, 0).addBox(-2.5F, 0F, -2.5F, 5, 5, 5).mirror(),
        PartPose.offset(0F, 11F, 0F));

    partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-1F, 0F, -1F, 2, 8, 2).mirror(),
        PartPose.offset(1.5F, 16F, 0F));

    partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-1F, 0F, -1F, 2, 8, 2).mirror(),
        PartPose.offset(-1.5F, 16F, 0F));

    partdefinition.addOrReplaceChild("leaf_top", CubeListBuilder.create()
            .texOffs(8, 0).addBox(-0.5F, 0F, -0.5F, 1, 2, 1).mirror(),
        PartPose.offset(0F, 9F, -1F));

    partdefinition.addOrReplaceChild("leaf_bottom", CubeListBuilder.create()
            .texOffs(0, 10).addBox(-1.5F, -0.5F, -0.5F, 3, 4, 1).mirror(),
        PartPose.offsetAndRotation(0F, 9F, -1F, 1.963495F, 0.5235988F, 0F));

    return LayerDefinition.create(meshdefinition, 32, 32);
  }

  @Override
  protected Iterable<ModelPart> headParts() {
    return ImmutableList.of();
  }

  @Override
  protected Iterable<ModelPart> bodyParts() {
    return ImmutableList.of(head, leafTop, leafBottom, legL, legR);
  }

  @Override
  public void setupAnim(SproutEntity entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    float speed = (float) Math.min(0.25f, ((new Vec3(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z)).length() * 4.0f));
    legL.xRot = -(float) Math.toRadians(speed * 240f * (float) Math.sin(Math.toRadians(pAgeInTicks % 360) * 24F));
    legR.xRot = (float) Math.toRadians(speed * 240f * (float) Math.sin(Math.toRadians(pAgeInTicks % 360) * 24F));
  }
}