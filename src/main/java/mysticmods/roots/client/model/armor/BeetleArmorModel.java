package mysticmods.mysticalworld.client.model.armor;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.EquipmentSlot;

public class BeetleArmorModel extends ArmorModel {
  private final ModelPart headAnchor;
  private final ModelPart bodyAnchor;
  public final ModelPart armR;
  public final ModelPart armL;
  private final ModelPart legR;
  private final ModelPart bootR;
  private final ModelPart legL;
  private final ModelPart bootL;

  public BeetleArmorModel(ModelPart root, EquipmentSlot slot) {
    super(root, slot);

    this.headAnchor = head.getChild("head_anchor");
    this.bodyAnchor = body.getChild("body_anchor");
    this.armL = leftArm.getChild("left_arm_anchor");
    this.armR = rightArm.getChild("right_arm_anchor");
    this.legR = rightLeg.getChild("right_leg_anchor");
    this.legL = leftLeg.getChild("left_leg_anchor");
    this.bootR = rightLeg.getChild("right_boot");
    this.bootL = leftLeg.getChild("left_boot");

    setAllVisible(true);
    headAnchor.visible = slot == EquipmentSlot.HEAD;
    bodyAnchor.visible = slot == EquipmentSlot.CHEST;
    armL.visible = slot == EquipmentSlot.CHEST;
    armR.visible = slot == EquipmentSlot.CHEST;
    legR.visible = slot == EquipmentSlot.LEGS;
    legL.visible = slot == EquipmentSlot.LEGS;
    bootL.visible = slot == EquipmentSlot.FEET;
    bootR.visible = slot == EquipmentSlot.FEET;
    hat.visible = false;
  }

  protected void setPartVisibility(EquipmentSlot slot) {
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

    PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
    PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
    PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
    PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
    PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-1.9F, 12.0F, 0.0F));
    PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(1.9F, 12.0F, 0.0F));

    PartDefinition head_anchor = head.addOrReplaceChild("head_anchor", CubeListBuilder.create(), PartPose.offset(-1.5f, -6.5f, -5.0f));
    head_anchor.addOrReplaceChild("head1", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, 7.0F, 0.0F, 5.0F, 4.0F, 1.0F), PartPose.ZERO);
    head_anchor.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(6, 11).addBox(-2.75F, 2.0F, 2.0F, 1.0F, 2.0F, 7.0F), PartPose.ZERO);
    head_anchor.addOrReplaceChild("head3", CubeListBuilder.create().texOffs(0, 30).addBox(-2.75F, 2.0F, 8.25F, 8.0F, 2.0F, 1.0F), PartPose.ZERO);
    head_anchor.addOrReplaceChild("head4", CubeListBuilder.create().texOffs(6, 11).addBox(4.75F, 2.0F, 2.0F, 1.0F, 2.0F, 7.0F), PartPose.ZERO);
    head_anchor.addOrReplaceChild("head5", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, 0.0F, 9.0F, 9.0F, 2.0F), PartPose.ZERO);
    PartDefinition bottom_horn_piece_r1 = head_anchor.addOrReplaceChild("bottom_horn_piece_r1", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5f, -5.25f, 3.25f, -0.3927F, 0.0F, 0.0F));
    bottom_horn_piece_r1.addOrReplaceChild("bottom_horn_piece_r1_1", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -2.75F, -1.75F, 2.0F, 7.0F, 2.0F), PartPose.ZERO);
    bottom_horn_piece_r1.addOrReplaceChild("bottom_horn_piece_r1_2", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -2.75F, 0.25F, 2.0F, 2.0F, 3.0F), PartPose.ZERO);

    PartDefinition body_anchor = body.addOrReplaceChild("body_anchor", CubeListBuilder.create(), PartPose.offset(-4.0f, 1.5f, -3.0f));
    body_anchor.addOrReplaceChild("body_anchor1", CubeListBuilder.create().texOffs(0, 46).addBox(-0.25F, 0.0F, 0.75F, 8.0F, 2.0F, 4.0F), PartPose.ZERO);
    body_anchor.addOrReplaceChild("body_anchor2", CubeListBuilder.create().texOffs(0, 46).addBox(0F, 6.0F, 0.75F, 8.0F, 2.0F, 4.0F), PartPose.ZERO);
    body_anchor.addOrReplaceChild("body_anchor3", CubeListBuilder.create().texOffs(8, 20).addBox(0.25F, -1.75F, 0.75F, 2.0F, 1.0F, 4.0F), PartPose.ZERO);
    body_anchor.addOrReplaceChild("body_anchor4", CubeListBuilder.create().texOffs(8, 20).addBox(5.75F, -1.75F, 0.75F, 2.0F, 1.0F, 4.0F), PartPose.ZERO);
    body_anchor.addOrReplaceChild("body_anchor5", CubeListBuilder.create().texOffs(0, 53).addBox(-0.5F, -1.0F, 5.0F, 9.0F, 10.0F, 1.0F), PartPose.ZERO);
    body_anchor.addOrReplaceChild("body_anchor6", CubeListBuilder.create().texOffs(0, 40).addBox(1.0F, 5.0F, 0.0F, 6.0F, 5.0F, 1.0F), PartPose.ZERO);
    body_anchor.addOrReplaceChild("body_anchor7", CubeListBuilder.create().texOffs(0, 33).addBox(-0.5F, -1.0F, -0.5F, 9.0F, 6.0F, 1.0F), PartPose.ZERO);

    PartDefinition right_arm_anchor = right_arm.addOrReplaceChild("right_arm_anchor", CubeListBuilder.create(), PartPose.offset(7.75f, -5.5f, -2.0f));
    right_arm_anchor.addOrReplaceChild("right_arm_anchor1", CubeListBuilder.create().texOffs(18, 18).addBox(-10.25F, 13.5F, -0.25F, 3.0F, 1.0F, 4.0F), PartPose.ZERO);
    right_arm_anchor.addOrReplaceChild("right_arm_anchor2", CubeListBuilder.create().texOffs(18, 18).addBox(-10.25F, 12.0F, -0.25F, 3.0F, 1.0F, 4.0F), PartPose.ZERO);
    right_arm_anchor.addOrReplaceChild("right_arm_anchor3", CubeListBuilder.create().texOffs(14, 40).addBox(-11.25F, 15.0F, -0.5F, 3.0F, 1.0F, 5.0F), PartPose.ZERO);
    right_arm_anchor.addOrReplaceChild("right_arm_anchor4", CubeListBuilder.create().texOffs(38, 0).addBox(-11.25F, 11.0F, -0.5F, 2.0F, 4.0F, 5.0F), PartPose.ZERO);
    right_arm_anchor.addOrReplaceChild("right_arm_anchor5", CubeListBuilder.create().texOffs(12, 22).addBox(-11.0F, 9.25F, 1.0F, 1.0F, 1.0F, 2.0F), PartPose.ZERO);
    right_arm_anchor.addOrReplaceChild("right_arm_anchor6", CubeListBuilder.create().texOffs(22, 0).addBox(-11.25F, 3.25F, -0.5F, 3.0F, 6.0F, 5.0F), PartPose.ZERO);
    right_arm_anchor.addOrReplaceChild("right_arm_anchor7", CubeListBuilder.create().texOffs(15, 11).addBox(-7.75F, 3.25F, 0.75F, 1.0F, 1.0F, 2.0F), PartPose.ZERO);

    PartDefinition right_bottom_horn_r1 = right_arm_anchor.addOrReplaceChild("right_bottom_horn_r1", CubeListBuilder.create(), PartPose.offsetAndRotation(-11.25f, 3.0f, 3.5f, 0.0f, 0.0f, -0.3927f));
    right_bottom_horn_r1.addOrReplaceChild("right_bottom_horn_r1_1", CubeListBuilder.create().texOffs(22, 11).addBox(-1.0F, -2.0F, -2.0F, 1.0F, 5.0F, 2.0F), PartPose.ZERO);
    right_bottom_horn_r1.addOrReplaceChild("right_bottom_horn_r1_2", CubeListBuilder.create().texOffs(10, 27).addBox(0.0F, -2.0F, -2.0F, 2.0F, 1.0F, 2.0F), PartPose.ZERO);

    PartDefinition left_arm_anchor = left_arm.addOrReplaceChild("left_arm_anchor", CubeListBuilder.create(), PartPose.offset(-7.75f, -5.5f, -2.0f));
    left_arm_anchor.addOrReplaceChild("left_arm_anchor1", CubeListBuilder.create().texOffs(18, 18).addBox(6.5F, 13.5F, -0.25F, 3.0F, 1.0F, 4.0F), PartPose.ZERO);
    left_arm_anchor.addOrReplaceChild("left_arm_anchor2", CubeListBuilder.create().texOffs(18, 18).addBox(6.5F, 12.0F, -0.25F, 3.0F, 1.0F, 4.0F), PartPose.ZERO);
    left_arm_anchor.addOrReplaceChild("left_arm_anchor3", CubeListBuilder.create().texOffs(14, 40).addBox(8.25F, 15.0F, -0.5F, 3.0F, 1.0F, 5.0F), PartPose.ZERO);
    left_arm_anchor.addOrReplaceChild("left_arm_anchor4", CubeListBuilder.create().texOffs(38, 0).addBox(9.25F, 11.0F, -0.5F, 2.0F, 4.0F, 5.0F), PartPose.ZERO);
    left_arm_anchor.addOrReplaceChild("left_arm_anchor5", CubeListBuilder.create().texOffs(12, 22).addBox(10.0F, 9.25F, 1.0F, 1.0F, 1.0F, 2.0F), PartPose.ZERO);
    left_arm_anchor.addOrReplaceChild("left_arm_anchor6", CubeListBuilder.create().texOffs(22, 0).addBox(7.75F, 3.25F, -0.5F, 3.0F, 6.0F, 5.0F), PartPose.ZERO);
    left_arm_anchor.addOrReplaceChild("left_arm_anchor7", CubeListBuilder.create().texOffs(15, 11).addBox(6.5F, 3.25F, 0.75F, 1.0F, 1.0F, 2.0F), PartPose.ZERO);

    PartDefinition left_bottom_horn_r1 = left_arm_anchor.addOrReplaceChild("left_bottom_horn_r1", CubeListBuilder.create(), PartPose.offsetAndRotation(11.25f, 3.0f, 3.5f, 0.0f, 0.0f, 0.3927f));
    left_bottom_horn_r1.addOrReplaceChild("left_bottom_horn_r1_1", CubeListBuilder.create().texOffs(22, 11).addBox(0.0F, -2.0F, -2.0F, 1.0F, 5.0F, 2.0F), PartPose.ZERO);
    left_bottom_horn_r1.addOrReplaceChild("left_bottom_horn_r1_2", CubeListBuilder.create().texOffs(10, 27).addBox(-2.0F, -2.0F, -2.0F, 2.0F, 1.0F, 2.0F), PartPose.ZERO);

    PartDefinition right_leg_anchor = right_leg.addOrReplaceChild("right_leg_anchor", CubeListBuilder.create(), PartPose.offset(1.6284F, 6.652F, 0.0F));
    right_leg_anchor.addOrReplaceChild("right_leg_anchor1", CubeListBuilder.create().texOffs(28, 11).addBox(-4F, -6.402F, -2.5F, 3.0F, 4.0F, 5.0F), PartPose.ZERO);
    right_leg_anchor.addOrReplaceChild("right_leg_anchor2", CubeListBuilder.create().texOffs(19, 29).addBox(-3.9784F, 1.098F, -2.25F, 4.0F, 1.0F, 4.0F), PartPose.ZERO);
    right_leg_anchor.addOrReplaceChild("right_leg_anchor3", CubeListBuilder.create().texOffs(19, 29).addBox(-3.9784F, -0.402F, -2.25F, 4.0F, 1.0F, 4.0F), PartPose.ZERO);
    right_leg_anchor.addOrReplaceChild("right_leg_anchor4", CubeListBuilder.create().texOffs(16, 23).addBox(-1F, -5.152F, -2.25F, 1.0F, 2.0F, 4.0F), PartPose.ZERO);
    right_leg_anchor.addOrReplaceChild("right_leg_anchor5", CubeListBuilder.create().texOffs(39, 11).addBox(-3.4784F, -0.902F, -2.5F, 3.0F, 4.0F, 1.0F), PartPose.ZERO);

    PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create(), PartPose.offset(0.025F, 11.5F, -2.75F));
    right_boot.addOrReplaceChild("right_boot1", CubeListBuilder.create().texOffs(20, 57).addBox(-2.125F, -0.9F, 0.0F, 4.0F, 2.0F, 5.0F), PartPose.ZERO);
    right_boot.addOrReplaceChild("right_boot2", CubeListBuilder.create().texOffs(20, 46).addBox(-2.125F, -1.9F, 3.0F, 4.0F, 1.0F, 2.0F), PartPose.ZERO);

    PartDefinition right_boot_plate_r1 = right_boot.addOrReplaceChild("right_boot_plate_r1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -1.9635F, 0.0F, 0.0F));
    right_boot_plate_r1.addOrReplaceChild("right_boot_plate_r1_1", CubeListBuilder.create().texOffs(20, 34).addBox(-1.875F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F), PartPose.ZERO);

    PartDefinition left_leg_anchor = left_leg.addOrReplaceChild("left_leg_anchor", CubeListBuilder.create(), PartPose.offset(-1.6284F, 0.152F, 0.0F));
    left_leg_anchor.addOrReplaceChild("left_leg_anchor1", CubeListBuilder.create().texOffs(19, 29).addBox(-0F, 7.598F, -2.25F, 4.0F, 1.0F, 4.0F), PartPose.ZERO);
    left_leg_anchor.addOrReplaceChild("left_leg_anchor2", CubeListBuilder.create().texOffs(19, 29).addBox(-0F, 6.098F, -2.25F, 4.0F, 1.0F, 4.0F), PartPose.ZERO);
    left_leg_anchor.addOrReplaceChild("left_leg_anchor3", CubeListBuilder.create().texOffs(16, 23).addBox(-0.0216F, 1.348F, -2.25F, 1.0F, 2.0F, 4.0F), PartPose.ZERO);
    left_leg_anchor.addOrReplaceChild("left_leg_anchor4", CubeListBuilder.create().texOffs(39, 11).addBox(0.5F, 5.598F, -2.5F, 3.0F, 4.0F, 1.0F), PartPose.ZERO);
    left_leg_anchor.addOrReplaceChild("left_leg_anchor5", CubeListBuilder.create().texOffs(28, 11).addBox(0.9784F, 0.098F, -2.5F, 3.0F, 4.0F, 5.0F), PartPose.ZERO);

    PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create(), PartPose.offset(-0.025F, 11.5F, -2.75F));
    left_boot.addOrReplaceChild("left_boot1", CubeListBuilder.create().texOffs(20, 57).addBox(-1.75F, -0.9F, 0.0F, 4.0F, 2.0F, 5.0F), PartPose.ZERO);
    left_boot.addOrReplaceChild("left_boot2", CubeListBuilder.create().texOffs(20, 46).addBox(-1.75F, -1.9F, 3.0F, 4.0F, 1.0F, 2.0F), PartPose.ZERO);

    PartDefinition left_boot_plate_r1 = left_boot.addOrReplaceChild("left_boot_plate_r1", CubeListBuilder.create(), PartPose.offsetAndRotation(0f, 0f, 0f, -1.9635F, 0.0F, 0.0F));
    left_boot_plate_r1.addOrReplaceChild("left_boot_plate_r1_1", CubeListBuilder.create().texOffs(20, 34).addBox(-1.125F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F), PartPose.ZERO);

    return LayerDefinition.create(meshdefinition, 64, 64);
  }
}
