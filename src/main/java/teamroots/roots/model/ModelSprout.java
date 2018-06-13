package teamroots.roots.model;

import teamroots.roots.entity.EntityDeer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ModelSprout extends ModelBase
{
  //fields
    ModelRenderer head;
    ModelRenderer legL;
    ModelRenderer legR;
    ModelRenderer leafTop;
    ModelRenderer leafBottom;
  
  public ModelSprout()
  {
	    textureWidth = 32;
	    textureHeight = 32;
	    
	      head = new ModelRenderer(this, 12, 0);
	      head.addBox(-2.5F, 0F, -2.5F, 5, 5, 5);
	      head.setRotationPoint(0F, 11F, 0F);
	      head.setTextureSize(32, 32);
	      head.mirror = true;
	      setRotation(head, 0F, 0F, 0F);
	      legL = new ModelRenderer(this, 0, 0);
	      legL.addBox(-1F, 0F, -1F, 2, 8, 2);
	      legL.setRotationPoint(1.5F, 16F, 0F);
	      legL.setTextureSize(32, 32);
	      legL.mirror = true;
	      setRotation(legL, 0F, 0F, 0F);
	      legR = new ModelRenderer(this, 0, 0);
	      legR.addBox(-1F, 0F, -1F, 2, 8, 2);
	      legR.setRotationPoint(-1.5F, 16F, 0F);
	      legR.setTextureSize(32, 32);
	      legR.mirror = true;
	      setRotation(legR, 0F, 0F, 0F);
	      leafTop = new ModelRenderer(this, 8, 0);
	      leafTop.addBox(-0.5F, 0F, -0.5F, 1, 2, 1);
	      leafTop.setRotationPoint(0F, 9F, -1F);
	      leafTop.setTextureSize(32, 32);
	      leafTop.mirror = true;
	      setRotation(leafTop, 0F, 0F, 0F);
	      leafBottom = new ModelRenderer(this, 0, 10);
	      leafBottom.addBox(-1.5F, -0.5F, -0.5F, 3, 4, 1);
	      leafBottom.setRotationPoint(0F, 9F, -1F);
	      leafBottom.setTextureSize(32, 32);
	      leafBottom.mirror = true;
	      setRotation(leafBottom, 1.963495F, 0.5235988F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float age, float f3, float f4, float f5)
  {
	GlStateManager.pushMatrix();
	float speed = (float)Math.min(0.25f, ((new Vec3d(entity.motionX,0,entity.motionZ)).lengthVector() * 4.0f));
    super.render(entity, f, f1, age, f3, f4, f5);
    setRotationAngles(f, f1, age, f3, f4, f5);
    head.render(f5);
    legL.rotateAngleX = -(float)Math.toRadians(speed*240f*(float) Math.sin(Math.toRadians(age % 360)*24F));
    legL.render(f5);
    legR.rotateAngleX = (float)Math.toRadians(speed*240f*(float) Math.sin(Math.toRadians(age % 360)*24F));
    legR.render(f5);
    leafTop.render(f5);
    leafBottom.render(f5);
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
