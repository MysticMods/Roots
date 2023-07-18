package mysticmods.mysticalworld.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.mysticalworld.client.model.ModelHolder;
import mysticmods.mysticalworld.client.player.event.RenderArmEventHandler;
import mysticmods.mysticalworld.init.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.EnumMap;

public class ArmorModel extends HumanoidModel<LivingEntity> {
  private static ArmorModel antlerModel = null;
  private static EnumMap<EquipmentSlot, ArmorModel> beetleModels = null;

  public static void init(EntityRendererProvider.Context context) {
    antlerModel = new AntlerHatModel(context.bakeLayer(ModelHolder.ANTLER_ARMOR));
    beetleModels = new EnumMap<>(EquipmentSlot.class);
    for (EquipmentSlot slot : EquipmentSlot.values()) {
      beetleModels.put(slot, new BeetleArmorModel(context.bakeLayer(ModelHolder.BEETLE_ARMOR), slot));
    }
    RenderArmEventHandler.init(context);
  }

  @Nullable
  public static ArmorModel getModel(ItemStack stack) {
    if (stack.is(ModItems.ANTLER_HAT.get())) {
      return antlerModel;
    } else if (stack.is(ModItems.BEETLE_BOOTS.get()) || stack.is(ModItems.BEETLE_HELMET.get()) || stack.is(ModItems.BEETLE_CHESTPLATE.get()) || stack.is(ModItems.BEETLE_LEGGINGS.get())) {
      return beetleModels.get(stack.getEquipmentSlot());
    }

    return null;
  }

  protected final EquipmentSlot slot;

  public ArmorModel(ModelPart root, EquipmentSlot slot) {
    super(root);
    this.slot = slot;
  }

  // [VanillaCopy] ArmorStandArmorModel.setupAnim because armor stands are dumb
  // This fixes the armor "breathing" and helmets always facing south on armor stands
  @Override
  public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    if (!(entity instanceof ArmorStand entityIn)) {
      super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      return;
    }

    this.head.xRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getX();
    this.head.yRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getY();
    this.head.zRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getZ();
    this.head.setPos(0.0F, 1.0F, 0.0F);
    this.body.xRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getX();
    this.body.yRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getY();
    this.body.zRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getZ();
    this.leftArm.xRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getX();
    this.leftArm.yRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getY();
    this.leftArm.zRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getZ();
    this.rightArm.xRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getX();
    this.rightArm.yRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getY();
    this.rightArm.zRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getZ();
    this.leftLeg.xRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getX();
    this.leftLeg.yRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getY();
    this.leftLeg.zRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getZ();
    this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
    this.rightLeg.xRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getX();
    this.rightLeg.yRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getY();
    this.rightLeg.zRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getZ();
    this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
    this.hat.copyFrom(this.head);
  }

  @Override
  public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
    setPartVisibility(slot);
    super.renderToBuffer(ms, buffer, light, overlay, r, g, b, a);
  }

  protected void setPartVisibility(EquipmentSlot slot) {
    setAllVisible(false);
    switch (slot) {
      case HEAD -> {
        head.visible = true;
        hat.visible = true;
      }
      case CHEST -> {
        body.visible = true;
        rightArm.visible = true;
        leftArm.visible = true;
      }
      case LEGS -> {
        body.visible = true;
        rightLeg.visible = true;
        leftLeg.visible = true;
      }
      case FEET -> {
        rightLeg.visible = true;
        leftLeg.visible = true;
      }
    }
  }
}
