package epicsquid.mysticallib.hax;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;

@SuppressWarnings("deprecation")
public class ICTDeserializer implements JsonDeserializer<ItemCameraTransforms> {
  @Override
  public net.minecraft.client.renderer.model.ItemCameraTransforms deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
      throws JsonParseException {
    JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
    net.minecraft.client.renderer.model.ItemTransformVec3f itemtransformvec3f = this.getTransform(p_deserialize_3_, jsonobject, "thirdperson_righthand");
    net.minecraft.client.renderer.model.ItemTransformVec3f itemtransformvec3f1 = this.getTransform(p_deserialize_3_, jsonobject, "thirdperson_lefthand");

    if (itemtransformvec3f1 == net.minecraft.client.renderer.model.ItemTransformVec3f.DEFAULT) {
      itemtransformvec3f1 = itemtransformvec3f;
    }

    net.minecraft.client.renderer.model.ItemTransformVec3f itemtransformvec3f2 = this.getTransform(p_deserialize_3_, jsonobject, "firstperson_righthand");
    net.minecraft.client.renderer.model.ItemTransformVec3f itemtransformvec3f3 = this.getTransform(p_deserialize_3_, jsonobject, "firstperson_lefthand");

    if (itemtransformvec3f3 == net.minecraft.client.renderer.model.ItemTransformVec3f.DEFAULT) {
      itemtransformvec3f3 = itemtransformvec3f2;
    }

    net.minecraft.client.renderer.model.ItemTransformVec3f itemtransformvec3f4 = this.getTransform(p_deserialize_3_, jsonobject, "head");
    ItemTransformVec3f itemtransformvec3f5 = this.getTransform(p_deserialize_3_, jsonobject, "gui");
    net.minecraft.client.renderer.model.ItemTransformVec3f itemtransformvec3f6 = this.getTransform(p_deserialize_3_, jsonobject, "ground");
    net.minecraft.client.renderer.model.ItemTransformVec3f itemtransformvec3f7 = this.getTransform(p_deserialize_3_, jsonobject, "fixed");
    return new ItemCameraTransforms(itemtransformvec3f1, itemtransformvec3f, itemtransformvec3f3, itemtransformvec3f2, itemtransformvec3f4, itemtransformvec3f5,
        itemtransformvec3f6, itemtransformvec3f7);
  }

  private ItemTransformVec3f getTransform(JsonDeserializationContext p_181683_1_, JsonObject p_181683_2_, String p_181683_3_) {
    return p_181683_2_.has(p_181683_3_) ?
        (net.minecraft.client.renderer.model.ItemTransformVec3f) p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), net.minecraft.client.renderer.model.ItemTransformVec3f.class) :
        net.minecraft.client.renderer.model.ItemTransformVec3f.DEFAULT;
  }
}
