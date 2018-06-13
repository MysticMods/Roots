package teamroots.roots.model;

import teamroots.roots.entity.EntityDeer;
import teamroots.roots.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ModelFairy extends ModelBase
{
  //fields
    ModelRenderer head;
    ModelRenderer chest;
    ModelRenderer armR;
    ModelRenderer armL;
    ModelRenderer legR;
    ModelRenderer legL;
    ModelRenderer wingR1;
    ModelRenderer wingR2;
    ModelRenderer wingL1;
    ModelRenderer wingL2;
  
  public ModelFairy()
  {
	    textureWidth = 32;
	    textureHeight = 32;
	    
	      head = new ModelRenderer(this, 0, 0);
	      head.addBox(-2F, -4F, -2F, 4, 4, 4);
	      head.setRotationPoint(0F, 18F, 0F);
	      head.setTextureSize(32, 32);
	      head.mirror = true;
	      setRotation(head, 0F, 0F, 0F);
	      chest = new ModelRenderer(this, 4, 10);
	      chest.addBox(-1.5F, 0F, -1F, 3, 4, 2);
	      chest.setRotationPoint(0F, 17.5F, 0.5F);
	      chest.setTextureSize(32, 32);
	      chest.mirror = true;
	      setRotation(chest, 0.2617994F, 0F, 0F);
	      armR = new ModelRenderer(this, 0, 10);
	      armR.addBox(-0.5F, 0F, -0.5F, 1, 3, 1);
	      armR.setRotationPoint(-1.5F, 18F, 0.5F);
	      armR.setTextureSize(32, 32);
	      armR.mirror = true;
	      setRotation(armR, 0.1745329F, 0F, 0.3926991F);
	      armL = new ModelRenderer(this, 0, 10);
	      armL.addBox(-0.5F, 0F, -0.5F, 1, 3, 1);
	      armL.setRotationPoint(1.5F, 18F, 0.5F);
	      armL.setTextureSize(32, 32);
	      armL.mirror = true;
	      setRotation(armL, 0.1745329F, 0F, -0.3926991F);
	      legR = new ModelRenderer(this, 0, 10);
	      legR.addBox(-0.5F, 0F, -0.5F, 1, 3, 1);
	      legR.setRotationPoint(-0.75F, 21F, 1.5F);
	      legR.setTextureSize(32, 32);
	      legR.mirror = true;
	      setRotation(legR, 0.1745329F, 0F, 0.1308997F);
	      legL = new ModelRenderer(this, 0, 10);
	      legL.addBox(-0.5F, 0F, -0.5F, 1, 3, 1);
	      legL.setRotationPoint(0.8F, 21F, 1.5F);
	      legL.setTextureSize(32, 32);
	      legL.mirror = true;
	      setRotation(legL, 0.1745329F, 0F, -0.1308997F);
	      wingR1 = new ModelRenderer(this, 0, 16);
	      wingR1.addBox(-0.5F, -1.5F, 0F, 1, 3, 4);
	      wingR1.setRotationPoint(-1F, 18F, 1.5F);
	      wingR1.setTextureSize(32, 32);
	      wingR1.mirror = true;
	      setRotation(wingR1, 0.5235988F, -0.3926991F, 0F);
	      wingR2 = new ModelRenderer(this, 0, 24);
	      wingR2.addBox(-0.5F, -1F, 0F, 1, 2, 3);
	      wingR2.setRotationPoint(-0.8F, 19.5F, 1.5F);
	      wingR2.setTextureSize(32, 32);
	      wingR2.mirror = true;
	      setRotation(wingR2, 0.1745329F, -0.2617994F, 0F);
	      wingL1 = new ModelRenderer(this, 0, 16);
	      wingL1.addBox(-0.5F, -1.5F, 0F, 1, 3, 4);
	      wingL1.setRotationPoint(1F, 18F, 1.5F);
	      wingL1.setTextureSize(32, 32);
	      wingL1.mirror = true;
	      setRotation(wingL1, 0.5235988F, 0.3926991F, 0F);
	      wingL2 = new ModelRenderer(this, 0, 24);
	      wingL2.addBox(-0.5F, -1F, 0F, 1, 2, 3);
	      wingL2.setRotationPoint(0.8F, 19.5F, 1.5F);
	      wingL2.setTextureSize(32, 32);
	      wingL2.mirror = true;
	      setRotation(wingL2, 0.1745329F, 0.2617994F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float age, float f3, float f4, float f5)
  {
	OpenGlHelper.setLightmapTextureCoords(1, 240f, 240f);
	GlStateManager.pushMatrix();
	float speed = (float)Math.min(0.25f, ((new Vec3d(entity.motionX,0,entity.motionZ)).lengthVector() * 4.0f));
    super.render(entity, f, f1, age, f3, f4, f5);
    setRotationAngles(f, f1, age, f3, f4, f5);
    wingL1.rotateAngleY = 0.3926991F + Math.abs((float)Math.toRadians(45f*(float) Math.sin(Math.toRadians(age % 360)*24F)));
    wingL2.rotateAngleY = 0.3926991F + Math.abs((float)Math.toRadians(45f*(float) Math.sin(Math.toRadians(age % 360)*24F)));
    wingR1.rotateAngleY = -(0.3926991F + Math.abs((float)Math.toRadians(45f*(float) Math.sin(Math.toRadians(age % 360)*24F))));
    wingR2.rotateAngleY = -(0.3926991F + Math.abs((float)Math.toRadians(45f*(float) Math.sin(Math.toRadians(age % 360)*24F))));
    //head.rotateAngleX = (float)Math.toRadians(entity.rotationPitch);
    //head.rotateAngleY = (float)Math.toRadians((entity.getRotationYawHead()));
    head.render(f5);
    chest.render(f5);
    armR.render(f5);
    armL.render(f5);
    legR.render(f5);
    legL.render(f5);
    wingR1.render(f5);
    wingR2.render(f5);
    wingL1.render(f5);
    wingL2.render(f5);
	GlStateManager.popMatrix();
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }

}
