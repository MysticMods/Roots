package epicsquid.roots.util;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelScaleUtil {
  public static void copyRenderer(ModelRenderer renderer, ModelRenderer thisRenderer) {
    thisRenderer.textureWidth = renderer.textureWidth;
    thisRenderer.textureHeight = renderer.textureHeight;
    thisRenderer.textureOffsetX = renderer.textureOffsetX;
    thisRenderer.textureOffsetY = renderer.textureOffsetY;
    thisRenderer.rotationPointX = renderer.rotationPointX;
    thisRenderer.rotationPointY = renderer.rotationPointY;
    thisRenderer.rotationPointZ = renderer.rotationPointZ;
    thisRenderer.rotateAngleX = renderer.rotateAngleX;
    thisRenderer.rotateAngleY = renderer.rotateAngleY;
    thisRenderer.rotateAngleZ = renderer.rotateAngleZ;
    thisRenderer.mirror = renderer.mirror;
    thisRenderer.showModel = renderer.showModel;
    thisRenderer.isHidden = renderer.isHidden;
    thisRenderer.offsetX = renderer.offsetX;
    thisRenderer.offsetY = renderer.offsetY;
    thisRenderer.offsetZ = renderer.offsetZ;
  }

  public static class ModelScaled extends ModelBase {
    public ModelScaled(ModelBase parent, float scale) {
      for (ModelRenderer renderer : parent.boxList) {
        ModelRenderer thisRenderer = new ModelRenderer(this, renderer.boxName);
        copyRenderer(renderer, thisRenderer);
      }
      for (ModelRenderer renderer : this.boxList) {
        if (renderer.childModels != null) {
          for (ModelRenderer childRenderer : renderer.childModels) {
            if (!this.boxList.contains(childRenderer)) {

            }
          }
        }
      }
    }
  }
}
