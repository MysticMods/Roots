package epicsquid.mysticallib.hax;

import java.lang.reflect.Type;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.model.BlockFaceUV;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;

public class BPFDeserializer implements JsonDeserializer<net.minecraft.client.renderer.model.BlockPartFace> {
  @Override
  public BlockPartFace deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
    JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
    Direction enumfacing = this.parseCullFace(jsonobject);
    int i = this.parseTintIndex(jsonobject);
    String s = this.parseTexture(jsonobject);
    net.minecraft.client.renderer.model.BlockFaceUV blockfaceuv = p_deserialize_3_.deserialize(jsonobject, BlockFaceUV.class);
    return new BlockPartFace(enumfacing, i, s, blockfaceuv);
  }

  protected int parseTintIndex(JsonObject object) {
    return JSONUtils.getInt(object, "tintindex", -1);
  }

  private String parseTexture(JsonObject object) {
    return JSONUtils.getString(object, "texture");
  }

  @Nullable
  private Direction parseCullFace(JsonObject object) {
    String s = JSONUtils.getString(object, "cullface", "");
    return Direction.byName(s);
  }
}
