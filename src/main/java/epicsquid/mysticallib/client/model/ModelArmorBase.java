package epicsquid.mysticallib.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHandSide;

public class ModelArmorBase extends ModelBiped {


  public EntityEquipmentSlot slot;
  public float armorScale = 1.05f;
  protected ModelRenderer head;
  protected ModelRenderer chest;
  protected ModelRenderer armR;
  protected ModelRenderer armL;
  protected ModelRenderer legR;
  protected ModelRenderer legL;
  protected ModelRenderer bootR;
  protected ModelRenderer bootL;

  public ModelArmorBase(EntityEquipmentSlot slot) {
    super(0.0f, 1.0f, 64, 64);
    this.textureHeight = 64;
    this.textureWidth = 64;
    this.slot = slot;
    this.isChild = false;
    head = new ModelRenderer(this);
    chest = new ModelRenderer(this);
    armR = new ModelRenderer(this);
    armL = new ModelRenderer(this);
    legR = new ModelRenderer(this);
    legL = new ModelRenderer(this);
    bootR = new ModelRenderer(this);
    bootL = new ModelRenderer(this);
    chest.offsetY -= 0.125;
  }

  /**
   * borrowed from https://github.com/Shinoow/AbyssalCraft/blob/master/src/main/java/com/shinoow/abyssalcraft/client/model/item/ModelDreadiumSamuraiArmor.java
   */
  @Override
  public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
    if (entityIn instanceof EntityArmorStand) {
      EntityArmorStand entityarmorstand = (EntityArmorStand) entityIn;
      bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
      bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
      bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
      bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
      bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
      bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
      bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
      bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
      bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
      bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
      bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
      bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
      bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
      bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
      bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
      bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
      bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
      bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
      bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
      bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
      bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
      copyModelAngles(bipedHead, bipedHeadwear);
    } else super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
  }

  @Override
  public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float netHeadPitch, float scale) {
    prepareForRender(entity);
    this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, netHeadPitch, scale, entity);
    this.setHeadRotation();
    this.setChestRotation(entity);
    this.setLegsRotation();
    this.setBootRotation();
    GlStateManager.pushMatrix();
    head.showModel = slot == EntityEquipmentSlot.HEAD;
    chest.showModel = slot == EntityEquipmentSlot.CHEST;
    armR.showModel = slot == EntityEquipmentSlot.CHEST;
    armL.showModel = slot == EntityEquipmentSlot.CHEST;
    legR.showModel = slot == EntityEquipmentSlot.LEGS;
    legL.showModel = slot == EntityEquipmentSlot.LEGS;
    bootR.showModel = slot == EntityEquipmentSlot.FEET;
    bootL.showModel = slot == EntityEquipmentSlot.FEET;
    if (this.isChild) {
      float f = 2.0F;
      GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
      GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
      this.renderHead(scale * armorScale);
      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
      GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
      this.renderPartsMinusHead(scale * armorScale);
      GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
      this.renderLegs(scale * armorScale);
    } else {
      if (entity.isSneaking()) {
        GlStateManager.translate(0.0F, 0.2F, 0.0F);
      }
      this.renderParts(scale * armorScale);
      this.renderArms(scale * armorScale);
      GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
      this.renderLegs(scale * armorScale);
    }
    GlStateManager.popMatrix();
  }

  public void renderHead(float scale) {
    head.render(scale);
  }

  public void renderParts(float scale) {
    head.render(scale);
    chest.render(scale);
  }

  public void renderArms(float scale) {
    armR.render(scale);
    armL.render(scale);
  }

  public void renderPartsMinusHead(float scale) {
    chest.render(scale);
  }

  public void renderLegs(float scale) {
    legR.render(scale);
    legL.render(scale);
    bootR.render(scale);
    bootL.render(scale);
  }

  public void setHeadRotation() {
    head.rotationPointX = bipedHead.rotationPointX;
    head.rotationPointY = bipedHead.rotationPointY;
    head.rotationPointZ = bipedHead.rotationPointZ;
    setRotation(head, bipedHead.rotateAngleX, bipedHead.rotateAngleY, bipedHead.rotateAngleZ);
  }

  public void setChestRotation(Entity e) {
    /* if (e instanceof EntityPlayer){ ((EntityPlayer)e).get } */
    chest.rotationPointX = bipedBody.rotationPointX;
    chest.rotationPointY = bipedBody.rotationPointY - 1;
    chest.rotationPointZ = bipedBody.rotationPointZ;
    armR.rotationPointX = bipedRightArm.rotationPointX + 5;
    armR.rotationPointY = bipedRightArm.rotationPointY - 1;
    armR.rotationPointZ = bipedRightArm.rotationPointZ;
    armL.rotationPointX = bipedLeftArm.rotationPointX - 5;
    armL.rotationPointY = bipedLeftArm.rotationPointY - 1;
    armL.rotationPointZ = bipedLeftArm.rotationPointZ;
    setRotation(chest, bipedBody.rotateAngleX, bipedBody.rotateAngleY, bipedBody.rotateAngleZ);
    setRotation(armR, bipedRightArm.rotateAngleX, bipedRightArm.rotateAngleY, bipedRightArm.rotateAngleZ);
    setRotation(armL, bipedLeftArm.rotateAngleX, bipedLeftArm.rotateAngleY, bipedLeftArm.rotateAngleZ);
  }

  public void setLegsRotation() {
    legR.rotationPointX = bipedRightLeg.rotationPointX + 2;
    legR.rotationPointY = bipedRightLeg.rotationPointY - 22;
    legR.rotationPointZ = bipedRightLeg.rotationPointZ;
    legL.rotationPointX = bipedLeftLeg.rotationPointX - 2;
    legL.rotationPointY = bipedLeftLeg.rotationPointY - 22;
    legL.rotationPointZ = bipedLeftLeg.rotationPointZ;
    setRotation(legR, bipedRightLeg.rotateAngleX, bipedRightLeg.rotateAngleY, bipedRightLeg.rotateAngleZ);
    setRotation(legL, bipedLeftLeg.rotateAngleX, bipedLeftLeg.rotateAngleY, bipedLeftLeg.rotateAngleZ);
  }

  public void setBootRotation() {
    bootR.rotationPointX = bipedRightLeg.rotationPointX + 2;
    bootR.rotationPointY = bipedRightLeg.rotationPointY - 22;
    bootR.rotationPointZ = bipedRightLeg.rotationPointZ;
    bootL.rotationPointX = bipedLeftLeg.rotationPointX - 2;
    bootL.rotationPointY = bipedLeftLeg.rotationPointY - 22;
    bootL.rotationPointZ = bipedLeftLeg.rotationPointZ;
    setRotation(bootR, bipedRightLeg.rotateAngleX, bipedRightLeg.rotateAngleY, bipedRightLeg.rotateAngleZ);
    setRotation(bootL, bipedLeftLeg.rotateAngleX, bipedLeftLeg.rotateAngleY, bipedLeftLeg.rotateAngleZ);
  }

  /**
   * borrowed from: https://github.com/williewillus/Botania/blob/MC19/src/main/java/vazkii/botania/client/model/armor/ModelArmorManasteel.java
   */
  public void prepareForRender(Entity entity) {
    EntityLivingBase living = (EntityLivingBase) entity;
    isSneak = living != null && living.isSneaking();
    isChild = living != null && living.isChild();
    isRiding = living.isRiding();
    this.swingProgress = living.getSwingProgress(0);
    if (living != null) {
      ModelBiped.ArmPose mainPose = ModelBiped.ArmPose.EMPTY;
      ModelBiped.ArmPose offPose = ModelBiped.ArmPose.EMPTY;
      if (living.getHeldItemMainhand() != null) {
        mainPose = ModelBiped.ArmPose.ITEM;
        if (living.getItemInUseCount() > 0) {
          EnumAction enumaction = living.getHeldItemMainhand().getItemUseAction();
          if (enumaction == EnumAction.BLOCK) {
            mainPose = ModelBiped.ArmPose.BLOCK;
          } else if (enumaction == EnumAction.BOW) {
            mainPose = ModelBiped.ArmPose.BOW_AND_ARROW;
          }
        }
      }
      if (living.getHeldItemOffhand() != null) {
        offPose = ModelBiped.ArmPose.ITEM;
        if (living.getItemInUseCount() > 0) {
          EnumAction enumaction1 = living.getHeldItemOffhand().getItemUseAction();
          if (enumaction1 == EnumAction.BLOCK) {
            offPose = ModelBiped.ArmPose.BLOCK;
          }
        }
      }
      if (living.getPrimaryHand() == EnumHandSide.RIGHT) {
        rightArmPose = mainPose;
        leftArmPose = offPose;
      } else {
        rightArmPose = offPose;
        leftArmPose = mainPose;
      }
    }
  }

  public static void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
