package mysticmods.roots.client.model;

import com.google.common.collect.ImmutableSet;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.entity.BeetleEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class BeetleModel extends ShoulderRidingModel<BeetleEntity> {
  private final ModelPart body;
  private final ModelPart head;
  private final ModelPart antennaR1;
  private final ModelPart antennaL1;
  private final ModelPart antennaR2;
  private final ModelPart antennaL2;
  private final ModelPart wingL;
  private final ModelPart wingR;
  private final ModelPart legL1;
  private final ModelPart legL2;
  private final ModelPart legL3;
  private final ModelPart legR1;
  private final ModelPart legR2;
  private final ModelPart legR3;

  public BeetleModel(ModelPart pRoot) {
    super(true, 5.0f, 2.0f);
    this.body = pRoot.getChild("body");
    this.head = body.getChild("head");
    this.antennaL1 = head.getChild("antenna_left");
    this.antennaL2 = antennaL1.getChild("antenna_left2");
    this.antennaR1 = head.getChild("antenna_right");
    this.antennaR2 = antennaR1.getChild("antenna_right2");
    this.legL1 = body.getChild("leg_left1");
    this.legL2 = body.getChild("leg_left2");
    this.legL3 = body.getChild("leg_left3");
    this.legR1 = body.getChild("leg_right1");
    this.legR2 = body.getChild("leg_right2");
    this.legR3 = body.getChild("leg_right3");
    this.wingL = body.getChild("wing_left");
    this.wingR = body.getChild("wing_right");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
        .texOffs(0, 11).addBox(-2.5F, -2.0F, 0.0F, 5, 4, 8),
      PartPose.offset(0.0F, 16.0F, -4.0F));

    PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
        .texOffs(16, 0).addBox(-2.0F, -1.25F, -3.0F, 4, 3, 3),
      PartPose.rotation(0.17453292519943295F, 0.0F, 0.0F));

    CubeListBuilder antennaCubeList = CubeListBuilder.create()
      .texOffs(24, 6).addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1);
    PartDefinition antennaL1 = head.addOrReplaceChild("antenna_left", antennaCubeList,
      PartPose.offsetAndRotation(1.0F, 0.0F, -0.5F, 0.1308996938995747F, 0.0F, 0.2617993877991494F));
    PartDefinition antennaL2 = antennaL1.addOrReplaceChild("antenna_left2", antennaCubeList,
      PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.39269908169872414F, 0.0F, 0.0F));
    PartDefinition antennaR1 = head.addOrReplaceChild("antenna_right", antennaCubeList,
      PartPose.offsetAndRotation(-1.0F, 0.0F, -0.5F, 0.1308996938995747F, 0.0F, -0.2617993877991494F));
    PartDefinition antennaR2 = antennaR1.addOrReplaceChild("antenna_right2", antennaCubeList,
      PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.39269908169872414F, 0.0F, 0.0F));

    CubeListBuilder legCubeList = CubeListBuilder.create()
      .texOffs(18, 6).addBox(-0.5F, 0.0F, -0.5F, 1, 7, 1);
    PartDefinition legL1 = body.addOrReplaceChild("leg_left1", legCubeList,
      PartPose.offsetAndRotation(1.5F, 1.0F, 1.0F, -0.2617993877991494F, 0.0F, -0.2617993877991494F));
    PartDefinition legR1 = body.addOrReplaceChild("leg_right1", legCubeList,
      PartPose.offsetAndRotation(-1.5F, 1.0F, 1.0F, -0.2617993877991494F, 0.0F, 0.2617993877991494F));
    PartDefinition legL2 = body.addOrReplaceChild("leg_left2", legCubeList,
      PartPose.offsetAndRotation(2.0F, 1.0F, 3.5F, 0.0F, 0.0F, -0.2617993877991494F));
    PartDefinition legR2 = body.addOrReplaceChild("leg_right2", legCubeList,
      PartPose.offsetAndRotation(-2.0F, 1.0F, 3.5F, 0.0F, 0.0F, 0.2617993877991494F));
    PartDefinition legL3 = body.addOrReplaceChild("leg_left3", legCubeList,
      PartPose.offsetAndRotation(1.5F, 1.0F, 6.0F, 0.2617993877991494F, 0.0F, -0.2617993877991494F));
    PartDefinition legR3 = body.addOrReplaceChild("leg_right3", legCubeList,
      PartPose.offsetAndRotation(-1.5F, 1.0F, 6.0F, 0.2617993877991494F, 0.0F, 0.2617993877991494F));

    CubeListBuilder wingCubeList = CubeListBuilder.create()
      .texOffs(0, 0).addBox(-2.5F, 0.0F, -1.0F, 5, 8, 3);
    PartDefinition wingL = body.addOrReplaceChild("wing_left", wingCubeList,
      PartPose.offsetAndRotation(1.5F, -0.5F, 1.0F, 1.7453292519943295F, 0.17453292519943295F, 0.2617993877991494F));
    PartDefinition wingR = body.addOrReplaceChild("wing_right", wingCubeList.mirror(),
      PartPose.offsetAndRotation(-1.5F, -0.5F, 1.0F, 1.7453292519943295F, -0.17453292519943295F, -0.2617993877991494F));

    return LayerDefinition.create(meshdefinition, 32, 32);
  }

  @Nonnull
  @Override
  protected Iterable<ModelPart> headParts() {
    return ImmutableSet.of();
  }

  @Nonnull
  @Override
  protected Iterable<ModelPart> bodyParts() {
    return ImmutableSet.of(body);
  }

  private float getSwing(float deg, float ageInTicks) {
    return (float) Math.sin(ageInTicks * 0.125f * (Math.PI * 2.0f) + Math.toRadians(deg));
  }

  private float getBobble(float deg, float ageInTicks) {
    return (float) Math.sin(ageInTicks * 0.03125f * (Math.PI * 2.0f) + Math.toRadians(deg));
  }

  @Override
  protected void setupAnim(ModelState state, int ticks, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    this.head.xRot = headPitch * 0.017453292F;
    this.head.yRot = netHeadYaw * 0.017453292F;
    this.antennaR1.xRot = 0.1308996938995747F + getBobble(30, ageInTicks) * 0.2617993877991494F;
    this.antennaL1.xRot = 0.1308996938995747F + getBobble(100, ageInTicks) * 0.2617993877991494F;
    this.wingL.yRot = 0.17453292519943295F + 0.0872664626F * getBobble(45, ageInTicks);
    this.wingR.yRot = -0.17453292519943295F - 0.0872664626F * getBobble(160, ageInTicks);

    if (state == ModelState.NORMAL) {
      this.legL1.zRot = limbSwingAmount * getSwing(0, ageInTicks) - 0.2617993877991494F;
      this.legL2.zRot = limbSwingAmount * getSwing(120, ageInTicks) - 0.2617993877991494F;
      this.legL3.zRot = limbSwingAmount * getSwing(240, ageInTicks) - 0.2617993877991494F;
      this.legR1.zRot = limbSwingAmount * getSwing(180, ageInTicks) + 0.2617993877991494F;
      this.legR2.zRot = limbSwingAmount * getSwing(300, ageInTicks) + 0.2617993877991494F;
      this.legR3.zRot = limbSwingAmount * getSwing(60, ageInTicks) + 0.2617993877991494F;
    }
  }

  @Override
  protected void prepare(ModelState state) {
    if (state == ModelState.SITTING) {
      this.body.setPos(0.0F, 20.0F, -4.0F);
      this.setRotateAngle(legR1, -0.4619008920774175F, -0.12228424816241118F, 1.2226123587776043F);
      this.setRotateAngle(legR2, 0.0F, 0.0F, 1.1609087739532686F);
      this.setRotateAngle(legR3, 0.2617993950843811F, 0.0F, 1.1727415173224531F);
      this.setRotateAngle(legL1, -0.5508348907409892F, 0.12228424816241122F, -1.213797560945557F);
      this.setRotateAngle(legL2, 0.0F, 0.0F, -1.1873531674494129F);
      this.setRotateAngle(legL3, 0.2617993950843811F, 0.0F, -1.231427156609652F);
    } else {
      this.body.setPos(0.0F, 16.0F, -4.0F);
      this.setRotateAngle(legR1, -0.2617993877991494F, 0.0F, 0.2617993877991494F);
      this.setRotateAngle(legR2, 0.0F, 0.0F, 0.2617993877991494F);
      this.setRotateAngle(legR3, 0.2617993877991494F, 0.0F, 0.2617993877991494F);
      this.setRotateAngle(legL1, -0.2617993877991494F, 0.0F, -0.2617993877991494F);
      this.setRotateAngle(legL2, 0.0F, 0.0F, -0.2617993877991494F);
      this.setRotateAngle(legL3, 0.2617993877991494F, 0.0F, -0.2617993877991494F);
    }
  }

  private static final ResourceLocation texture = RootsAPI.rl("textures/entity/beetle_blue.png");

  @Override
  public ResourceLocation getTexture(ModelState state) {
    return texture;
  }

  private void setRotateAngle(@Nonnull ModelPart modelRenderer, float x, float y, float z) {
    modelRenderer.xRot = x;
    modelRenderer.yRot = y;
    modelRenderer.zRot = z;
  }
}