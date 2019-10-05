package epicsquid.roots.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;

import javax.annotation.Nonnull;

public class ModelWhiteStag extends ModelBase {

  //fields
  private ModelRenderer head;
  private ModelRenderer ear1;
  private ModelRenderer ear2;
  private ModelRenderer neck;
  private ModelRenderer body;
  private ModelRenderer tail;
  private ModelRenderer legRF;
  private ModelRenderer legLF;
  private ModelRenderer legLB;
  private ModelRenderer legRB;
  private ModelRenderer horn1;
  private ModelRenderer horn2;
  private ModelRenderer horn3;
  private ModelRenderer horn4;
  private ModelRenderer horn5;
  private ModelRenderer horn6;
  private ModelRenderer horn7;
  private ModelRenderer horn8;

  public ModelWhiteStag() {
    textureWidth = 64;
    textureHeight = 64;

    textureWidth = 64;
    textureHeight = 64;

    head = new ModelRenderer(this, 0, 16);
    head.addBox(-2F, 0F, -2.5F, 4, 7, 4);
    head.setRotationPoint(0F, 7F, -6.953333F);
    head.setTextureSize(64, 64);
    head.mirror = true;
    setRotation(head, 1.047198F, 0F, 0F);
    ear1 = new ModelRenderer(this, 17, 0);
    ear1.addBox(-1.5F, -4F, -0.5F, 3, 4, 1);
    ear1.setRotationPoint(-2F, 5.5F, -6.5F);
    ear1.setTextureSize(64, 64);
    ear1.mirror = true;
    setRotation(ear1, -0.174532925F, -0.174532925F, -1.178097F);
    ear2 = new ModelRenderer(this, 17, 0);
    ear2.mirror = true;
    ear2.addBox(-1.5F, -4F, -0.5F, 3, 4, 1);
    ear2.setRotationPoint(2F, 5.5F, -6.5F);
    ear2.setTextureSize(64, 64);
    setRotation(ear2, 0.174532925F, 0.174532925F, 1.178097F);
    neck = new ModelRenderer(this, 0, 0);
    neck.addBox(-2.5F, -2.5F, -7F, 5, 5, 7);
    neck.setRotationPoint(0F, 6F, -5F);
    neck.setTextureSize(64, 64);
    neck.mirror = true;
    setRotation(neck, 0.3926991F, 0F, 0F);
    body = new ModelRenderer(this, 16, 16);
    body.addBox(-2.5F, 0F, 0F, 5, 7, 9);
    body.setRotationPoint(0F, 8F, -3.953333F);
    body.setTextureSize(64, 64);
    body.mirror = true;
    setRotation(body, 0F, 0F, 0F);
    tail = new ModelRenderer(this, 32, 0);
    tail.addBox(-1.5F, -1.5F, 0F, 3, 3, 4);
    tail.setRotationPoint(0F, 9F, 4F);
    tail.setTextureSize(64, 64);
    tail.mirror = true;
    setRotation(tail, 0.7853982F, 0F, 0F);
    legRF = new ModelRenderer(this, 0, 32);
    legRF.addBox(-1F, 0F, -1F, 2, 9, 2);
    legRF.setRotationPoint(-1.5F, 15F, -2.953333F);
    legRF.setTextureSize(64, 64);
    legRF.mirror = true;
    setRotation(legRF, 0F, 0F, 0F);
    legLF = new ModelRenderer(this, 0, 32);
    legLF.addBox(-1F, 0F, -1F, 2, 9, 2);
    legLF.setRotationPoint(1.5F, 15F, -2.953333F);
    legLF.setTextureSize(64, 64);
    legLF.mirror = true;
    setRotation(legLF, 0F, 0F, 0F);
    legLB = new ModelRenderer(this, 0, 32);
    legLB.addBox(-1F, 0F, -1F, 2, 9, 2);
    legLB.setRotationPoint(-1.5F, 15F, 4F);
    legLB.setTextureSize(64, 64);
    legLB.mirror = true;
    setRotation(legLB, 0F, 0F, 0F);
    legRB = new ModelRenderer(this, 0, 32);
    legRB.addBox(-1F, 0F, -1F, 2, 9, 2);
    legRB.setRotationPoint(1.5F, 15F, 4F);
    legRB.setTextureSize(64, 64);
    legRB.mirror = true;
    setRotation(legRB, 0F, 0F, 0F);
    horn1 = new ModelRenderer(this, 16, 32);
    horn1.addBox(-0.5F, -5F, -0.5F, 1, 5, 1);
    horn1.setRotationPoint(1F, 4F, -6F);
    horn1.setTextureSize(64, 64);
    horn1.mirror = true;
    setRotation(horn1, 0F, 0F, 0.2617994F);
    horn2 = new ModelRenderer(this, 16, 32);
    horn2.addBox(-0.5F, -5F, -0.5F, 1, 5, 1);
    horn2.setRotationPoint(-1F, 4F, -6F);
    horn2.setTextureSize(64, 64);
    horn2.mirror = true;
    setRotation(horn2, 0F, 0F, -0.2617994F);
    horn3 = new ModelRenderer(this, 16, 32);
    horn3.addBox(-0.5F, -5F, -0.5F, 1, 5, 1);
    horn3.setRotationPoint(1.75F, 2F, -6F);
    horn3.setTextureSize(64, 64);
    horn3.mirror = true;
    setRotation(horn3, 0F, 0.0872665F, 1.047198F);
    horn4 = new ModelRenderer(this, 16, 32);
    horn4.addBox(-0.5F, -5F, -0.5F, 1, 5, 1);
    horn4.setRotationPoint(-1.8F, 2F, -6F);
    horn4.setTextureSize(64, 64);
    horn4.mirror = true;
    setRotation(horn4, 0F, -0.0872665F, -1.047198F);
    horn5 = new ModelRenderer(this, 16, 32);
    horn5.addBox(-0.5F, -5F, -0.5F, 1, 5, 1);
    horn5.setRotationPoint(-4.36F, 0.2F, -6.2F);
    horn5.setTextureSize(64, 64);
    horn5.mirror = true;
    setRotation(horn5, 0F, -0.0872665F, 0.2617994F);
    horn6 = new ModelRenderer(this, 16, 32);
    horn6.addBox(-0.5F, -5F, -0.5F, 1, 5, 1);
    horn6.setRotationPoint(4.4F, 0.2F, -6.2F);
    horn6.setTextureSize(64, 64);
    horn6.mirror = true;
    setRotation(horn6, 0F, 0.0872665F, -0.2617994F);
    horn7 = new ModelRenderer(this, 20, 32);
    horn7.addBox(-0.5F, -3F, -0.5F, 1, 3, 1);
    horn7.setRotationPoint(-3.8F, -2F, -6.2F);
    horn7.setTextureSize(64, 64);
    horn7.mirror = true;
    setRotation(horn7, 0F, -0.1745329F, -0.7853982F);
    horn8 = new ModelRenderer(this, 20, 32);
    horn8.addBox(-0.5F, -3F, -0.5F, 1, 3, 1);
    horn8.setRotationPoint(3.8F, -2F, -6.2F);
    horn8.setTextureSize(64, 64);
    horn8.mirror = true;
    setRotation(horn8, 0F, 0.1745329F, 0.7853982F);
  }

  @Override
  public void render(@Nonnull Entity entity, float f, float limbSwingAmount, float age, float f3, float f4, float f5) {
    float sin = (float) Math.sin(age * 0.125f * (Math.PI * 2.0f));
    GlStateManager.pushMatrix();
    super.render(entity, f, limbSwingAmount, age, f3, f4, f5);
    setRotationAngles(f, limbSwingAmount, age, f3, f4, f5);
    if (((EntityAnimal) entity).getGrowingAge() < 0) {
      GlStateManager.scale(0.5, 0.5, 0.5);
      GlStateManager.translate(0, 1.5, 0);
    }
    head.render(f5);
    ear1.render(f5);
    ear2.render(f5);
    neck.render(f5);
    body.render(f5);
    tail.render(f5);
    legRF.rotateAngleX = limbSwingAmount * sin;
    legRF.render(f5);
    legLF.rotateAngleX = -limbSwingAmount * sin;
    legLF.render(f5);
    legLB.rotateAngleX = limbSwingAmount * sin;
    legLB.render(f5);
    legRB.rotateAngleX = -limbSwingAmount * sin;
    legRB.render(f5);
    horn1.render(f5);
    horn2.render(f5);
    horn3.render(f5);
    horn4.render(f5);
    horn5.render(f5);
    horn6.render(f5);
    horn7.render(f5);
    horn8.render(f5);
    GlStateManager.popMatrix();
  }

  private void setRotation(@Nonnull ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

  private void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }

}
