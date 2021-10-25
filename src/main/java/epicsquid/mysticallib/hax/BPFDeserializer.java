package epicsquid.mysticallib.hax;

import java.lang.reflect.Type;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;

public class BPFDeserializer implements JsonDeserializer<BlockPartFace> {
  @Override
  public BlockPartFace deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
    JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
    EnumFacing enumfacing = this.parseCullFace(jsonobject);
    int i = this.parseTintIndex(jsonobject);
    String s = this.parseTexture(jsonobject);
    BlockFaceUV blockfaceuv = p_deserialize_3_.deserialize(jsonobject, BlockFaceUV.class);
    return new BlockPartFace(enumfacing, i, s, blockfaceuv);
  }

  protected int parseTintIndex(JsonObject object) {
    return JsonUtils.getInt(object, "tintindex", -1);
  }

  private String parseTexture(JsonObject object) {
    return JsonUtils.getString(object, "texture");
  }

  @Nullable
  private EnumFacing parseCullFace(JsonObject object) {
    String s = JsonUtils.getString(object, "cullface", "");
    return EnumFacing.byName(s);
  }
}
